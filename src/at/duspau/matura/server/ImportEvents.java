package at.duspau.matura.server;

import at.duspau.matura.client.model.Event;
import at.duspau.matura.server.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ImportEvents {
    public void tryImport(){
        try(BufferedReader br = new BufferedReader(new FileReader("events.txt"))){
            String currentLine = "";
            String[] in;
            while ((currentLine = br.readLine()) != null){
                in = currentLine.split(",");
                Event event = new Event(in[0], new Timestamp(formatDate(in[1]).getTime()), Integer.parseInt(in[2]));
                insert(event.getName(), event.getDate(), event.getNumOfSeats());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void insert(String name, Timestamp date, int numOfSeats) throws SQLException {
        PreparedStatement preparedStatementInsert = Database.getInstance().getPstmtInsert();
        preparedStatementInsert.setString(1, name);
        preparedStatementInsert.setTimestamp(2, date);
        preparedStatementInsert.setInt(3, numOfSeats);
        preparedStatementInsert.execute();
        System.out.println("[Server] Inserted event successfully");
        System.out.printf("Event: %s, %s, %d %n", name, date.toString(), numOfSeats);
    }

    public static Date formatDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy|HH:mm:ss");
        return new Date(dateFormat.parse(dateString).getTime());
    }
}
