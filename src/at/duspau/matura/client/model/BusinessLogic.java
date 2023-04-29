package at.duspau.matura.client.model;

import at.duspau.matura.common.Booking;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BusinessLogic{
    private ArrayList<String> eventsForCb;
    private String bookingStatus;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

    // --
    private final String UPDATE_LIST= "updateList";
    private final String BOOK_SEATS = "bookSeats";

    public BusinessLogic(){
        try{
            socket = new Socket("localhost", 5000);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            System.out.println("[Client] waiting for server to make decisions");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList() throws IOException, ClassNotFoundException {
        ArrayList<String> events = new ArrayList<>();
        dos.writeBoolean(false);
        dos.writeUTF(UPDATE_LIST);

        System.out.println("[Client] sent request to update list");

        System.out.println("[Client] got response from server for updating, checking it");
        if(dis.readUTF().equals(UPDATE_LIST)){
            int length = dis.readInt();
            for(int i = 0; i < length; i++){
                events.add(dis.readUTF());
            }

            setEventsForCb(events);
            System.out.println("[Client] received event list");
        }
    }

    public boolean bookSeats(int howMany, String currentEvent) throws IOException, ClassNotFoundException {
        boolean isSuccess = false;
        dos.writeBoolean(false);
        dos.writeUTF(BOOK_SEATS);
        dos.writeInt(howMany);
        dos.writeUTF(currentEvent);
        System.out.println("[Client] sent request to book a seat");

        System.out.println("[Client] got response from server for booking, checking it");
        if(dis.readUTF().equals(BOOK_SEATS)){
            if(dis.readUTF().equals(Booking.OK.toString())){
                isSuccess = true;
            }
            System.out.println("[Client] received booking status");
        }
        return isSuccess;
    }

    public void terminateClientSocket() {
        try{
            dos.writeBoolean(true);
        }
        catch (IOException e){
            System.out.println("[Error] when writing object for terminating to server");
        }
        System.out.println("[Client] sent request to terminate client");
    }

    public ArrayList<String> getEventsForCb() {
        return eventsForCb;
    }

    public void setEventsForCb(ArrayList<String> eventsForCb) {
        this.eventsForCb = eventsForCb;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
