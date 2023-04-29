package at.duspau.matura.common;

import java.io.Serializable;

// has to implement serializable otherwise it cant be written via object streams
@Deprecated
public class Request implements Serializable {
    private String message;
    private boolean closeConnection;

    private int howMany;
    private String currentEvent;

    public Request(String message, boolean closeConnection){
        this.message = message;
        this.closeConnection = closeConnection;
    }

    public Request(String message, boolean closeConnection, int howMany, String currentEvent){
        this.message = message;
        this.closeConnection = closeConnection;
        this.howMany = howMany;
        this.currentEvent = currentEvent;
    }

    public Request(boolean closeConnection){
        this.closeConnection = closeConnection;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCloseConnection() {
        return closeConnection;
    }

    public void setCloseConnection(boolean closeConnection) {
        this.closeConnection = closeConnection;
    }

    public int getHowMany() {
        return howMany;
    }

    public void setHowMany(int howMany) {
        this.howMany = howMany;
    }

    public String getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(String currentEvent) {
        this.currentEvent = currentEvent;
    }
}
