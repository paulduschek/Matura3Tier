package at.duspau.matura.server.main;

import at.duspau.matura.model.Event;
import at.duspau.matura.model.ImportEvents;
import at.duspau.matura.server.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        String wantsToImport = "";

        // open at.duspau.matura.server.database
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

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public static void stop(){
        Database.close();
    }*/
}
