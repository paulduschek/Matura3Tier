package at.duspau.matura.common;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private ArrayList<String> eventsForCb;
    private String message;
    private String status;

    public Response(ArrayList<String> eventsForCb, String message) {
        this.eventsForCb = eventsForCb;
        this.message = message;
    }

    public Response(String status, String message){
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getEventsForCb() {
        return eventsForCb;
    }

    public void setEventsForCb(ArrayList<String> eventsForCb) {
        this.eventsForCb = eventsForCb;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
