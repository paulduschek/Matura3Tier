package at.duspau.matura.server;

import at.duspau.matura.client.common.Booking;
import at.duspau.matura.client.common.Request;
import at.duspau.matura.client.common.Response;
import at.duspau.matura.model.Event;
import at.duspau.matura.model.ImportEvents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private Vector<Event> events = new Vector<>();
    private ImportEvents importEvents = new ImportEvents();

    private final String UPDATE_LIST= "updateList";
    private final String BOOK_SEATS = "bookSeats";

    public ClientHandler(Socket clientSocket, Vector<Event> events) {
        this.clientSocket = clientSocket;
        this.events = events;
    }

    @Override
    public void run() {
        try(ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
        ){
            System.out.println("[Server] Client connected");

            boolean closing = false;
            while (!closing) {
                Request request = (Request) ois.readObject();
                System.out.println("[Server] got a request, checking it");
                if(request.isCloseConnection()){
                    closing = true;
                }
                else{
                    if(request.getMessage().equals(UPDATE_LIST)){
                        System.out.println("[Server] got request to update event list, sending right now");
                        Response response = new Response(getChoicesFromDatabase(), UPDATE_LIST);
                        oos.writeObject(response);
                    }
                    else if(request.getMessage().equals(BOOK_SEATS)){
                        System.out.println("[Server] got request to book seats, checking right now");
                        if(isSeatAvailable(request.getHowMany(), request.getCurrentEvent())){
                            Response response = new Response(Booking.OK.toString(), BOOK_SEATS);
                            oos.writeObject(response);
                        }
                        else{
                            Response response = new Response(Booking.TAKEN.toString(), BOOK_SEATS);
                            oos.writeObject(response);
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