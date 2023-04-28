package at.duspau.matura.server;

import at.duspau.matura.common.Booking;

import java.io.*;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// extends thread to handle multiple clients at the same time with a thread pool
public class ClientHandler extends Thread{
    private final Socket clientSocket;

    private final String UPDATE_LIST= "updateList";
    private final String BOOK_SEATS = "bookSeats";

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try(DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream())
        ){
            System.out.println("[Server] Client connected");

            boolean closing = false;
            while (!closing) {
                System.out.println("[Server] got a request, checking it");
                if(dis.readBoolean()){
                    closing = true;
                }
                else{
                    System.out.println("[Server] client not terminated");
                    String toDo = dis.readUTF();
                    if(toDo.equals(UPDATE_LIST)){
                        System.out.println("[Server] got request to update event list, sending right now");
                        dos.writeUTF(UPDATE_LIST);
                        dos.writeInt(getChoicesFromDatabase().size());
                        for(int i = 0; i < getChoicesFromDatabase().size(); i++){
                            dos.writeUTF(getChoicesFromDatabase().get(i));
                            System.out.println("[Server] sent Event" + i+1);
                        }
                    }
                    else if(toDo.equals(BOOK_SEATS)){
                        System.out.println("[Server] got request to book seats, checking right now");
                        int howMany = dis.readInt();
                        String currentEvent = dis.readUTF();
                        if(isSeatAvailable(howMany, currentEvent)){
                            dos.writeUTF(BOOK_SEATS);
                            dos.writeUTF(Booking.OK.toString());
                        }
                        else{
                            dos.writeUTF(BOOK_SEATS);
                            dos.writeUTF(Booking.TAKEN.toString());
                        }
                    }
                }
            }
            System.out.println("[Server] Client disconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> getChoicesFromDatabase() {
        ArrayList<String> choices = new ArrayList<>();
        try {
            PreparedStatement pstmt = Database.getInstance().getPstmtSelectNameDate();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String choice = rs.getString("id") + ": " + rs.getString("name") + " - " + rs.getTimestamp("date").toString();
                choices.add(choice);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return choices;
    }

    public boolean isSeatAvailable(int howMany, String object){
        boolean isAvailable = false;
        try{
            PreparedStatement pstmt = Database.getInstance().getPstmSelectAvSeats();
            int currId = Integer.parseInt(object.substring(0,1));

            pstmt.setInt(1, currId);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                if(rs.getInt("numOfSeats") >= howMany){
                    PreparedStatement updatePstmt = Database.getInstance().getPstmtUpdateSeats();
                    updatePstmt.setInt(1, rs.getInt("numOfSeats") - howMany);
                    updatePstmt.setInt(2, currId);
                    updatePstmt.execute();
                    System.out.println("[Server] saved booking changes to db");
                    System.out.printf("[Server] remaining seats: %d %n", rs.getInt("numOfSeats") - howMany);

                    isAvailable = true;
                }
            }
            rs.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return isAvailable;
    }
}
