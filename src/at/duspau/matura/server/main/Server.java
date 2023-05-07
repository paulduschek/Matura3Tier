package at.duspau.matura.server.main;

import at.duspau.matura.server.ImportEvents;
import at.duspau.matura.server.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static void main(String[] args) {
        // adds new threads to the pool, if necessary reuses old interrupted ones to better performance
        ExecutorService executorService = Executors.newCachedThreadPool();
        ReentrantLock lock = new ReentrantLock();
        String wantsToImport = "";

        // open database singleton
        try {
            Database.open(true);
            System.out.println("[Server] Database connection opened");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println("[Server] started successfully");

            // console program for reading text files
            Scanner sc = new Scanner(System.in);
            System.out.println("[Server] do you want to import new events? (yes/no)");
            wantsToImport = sc.nextLine();
            if(wantsToImport.equals("yes")){
                ImportEvents importEvents = new ImportEvents();
                importEvents.tryImport();
            }
            System.out.println("[Server] waiting for client to connect");

            while (true) {
                // accept blocks until client starts connection with server socket on port 5000
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket, lock));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void stop(){
        Database.close();
    }*/
}
