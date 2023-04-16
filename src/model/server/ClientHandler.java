package model.server;

import model.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Vector;

public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private Vector<Event> events = new Vector<>();

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
}
