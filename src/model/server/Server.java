package model.server;

import model.Event;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Vector<Event> events = new Vector<>();

        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("[Server] started successfully");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket, events));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
