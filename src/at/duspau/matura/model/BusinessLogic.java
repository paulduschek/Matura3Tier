package at.duspau.matura.model;

import at.duspau.matura.common.Booking;
import at.duspau.matura.common.Request;
import at.duspau.matura.common.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BusinessLogic{
    private ArrayList<String> eventsForCb;
    private String bookingStatus;

    private Socket socket;
    //private ObjectInputStream ois;
    //private ObjectOutputStream oos;
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

            //oos = new ObjectOutputStream(socket.getOutputStream());
            //ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("[Client] waiting for server to make decisions");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList() throws IOException, ClassNotFoundException {
        //Request request = new Request(UPDATE_LIST, false);
        ArrayList<String> events = new ArrayList<>();
        //oos.writeObject(request);
        dos.writeBoolean(false);
        dos.writeUTF(UPDATE_LIST);

        System.out.println("[Client] sent request to update list");

        //Response response = (Response) ois.readObject();
        //if(response != null){
        //if(dis.available() > 0){
            System.out.println("[Client] got response from server for updating, checking it");
            //if(response.getMessage().equals(UPDATE_LIST)){
            if(dis.readUTF().equals(UPDATE_LIST)){
                //while (dis.read() != null){
                int length = dis.readInt();
                for(int i = 0; i < length; i++){
                    events.add(dis.readUTF());
                }
                //}
                //setEventsForCb(response.getEventsForCb());
                setEventsForCb(events);
                System.out.println("[Client] received event list");
            }
        //}
    }

    public boolean bookSeats(int howMany, String currentEvent) throws IOException, ClassNotFoundException {
        boolean isSuccess = false;
        //Request request = new Request(BOOK_SEATS, false, howMany, currentEvent);
        dos.writeBoolean(false);
        dos.writeUTF(BOOK_SEATS);
        dos.writeInt(howMany);
        dos.writeUTF(currentEvent);
        //oos.writeObject(request);
        System.out.println("[Client] sent request to book a seat");

        //Response response = (Response) ois.readObject();
        //if(response != null){
        //if(dis.available() > 0){
            System.out.println("[Client] got response from server for booking, checking it");
            //if(response.getMessage().equals(BOOK_SEATS)){
            if(dis.readUTF().equals(BOOK_SEATS)){
                //if(response.getStatus().equals(Booking.OK.toString())){
                if(dis.readUTF().equals(Booking.OK.toString())){
                    isSuccess = true;
                }
                System.out.println("[Client] received booking status");
            }
        //}
        return isSuccess;
    }

    public void terminateClientSocket() {
        //Request request = new Request(true);
        try{
            //oos.writeObject(request);
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
