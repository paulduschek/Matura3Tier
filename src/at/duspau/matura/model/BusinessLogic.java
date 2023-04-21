package at.duspau.matura.model;

import at.duspau.matura.client.common.Booking;
import at.duspau.matura.client.common.Request;
import at.duspau.matura.client.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class BusinessLogic{
    private boolean doUpdate;
    private ArrayList<String> eventsForCb;
    private String bookingStatus;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    // --
    private final String UPDATE_LIST= "updateList";
    private final String BOOK_SEATS = "bookSeats";

    public BusinessLogic(){
        doUpdate = false;
        try{
            socket = new Socket("localhost", 5000);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            System.out.println("[Client] waiting for server to make decisions");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateList() throws IOException, ClassNotFoundException {
        Request request = new Request(UPDATE_LIST, false);
        oos.writeObject(request);
        System.out.println("[Client] sent request to update list");
        setDoUpdate(false);

        Response response = (Response) ois.readObject();
        if(response != null){
            System.out.println("[Client] got response from server for updating, checking it");
            if(response.getMessage().equals(UPDATE_LIST)){
                setEventsForCb(response.getEventsForCb());
                System.out.println("[Client] received event list");
            }
        }
    }

    public boolean bookSeats(int howMany, String currentEvent) throws IOException, ClassNotFoundException {
        boolean isSuccess = false;
        Request request = new Request(BOOK_SEATS, false, howMany, currentEvent);
        oos.writeObject(request);
        System.out.println("[Client] sent request to book a seat");

        Response response = (Response) ois.readObject();
        if(response != null){
            System.out.println("[Client] got response from server for booking, checking it");
            if(response.getMessage().equals(BOOK_SEATS)){
                if(response.getStatus().equals(Booking.OK.toString())){
                    isSuccess = true;
                }
                System.out.println("[Client] received booking status");
            }
        }
        return isSuccess;
    }

    public void terminateClientSocket() {
        Request request = new Request(true);
        try{
            oos.writeObject(request);
        }
        catch (IOException e){
            System.out.println("[Error] when writing object for terminating to server");
        }
        System.out.println("[Client] sent request to terminate client");
    }

    public boolean isDoUpdate() {
        return doUpdate;
    }

    public void setDoUpdate(boolean doUpdate) {
        this.doUpdate = doUpdate;
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
