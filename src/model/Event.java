package model;

import java.sql.Timestamp;

public class Event {
    private String name;
    private Timestamp date;
    private int numOfSeats;

    public Event(String name, Timestamp date, int numOfSeats){
        this.name = name;
        this.date = date;
        this.numOfSeats = numOfSeats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getNumOfSeats() {
        return numOfSeats;
    }

    public void setNumOfSeats(int numOfSeats) {
        this.numOfSeats = numOfSeats;
    }
}
