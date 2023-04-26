package at.duspau.matura.client.main;

import at.duspau.matura.server.*;

import java.sql.SQLException;

public class MainCreateDb {
    public static void main(String[] args) {
        try {
            Database.open(true);

            Database.getInstance().getStatement().execute("drop table Event if exists");
            Database.getInstance().getStatement().execute("create table Event(" +
                    "id int generated by default as identity primary key" +
                    ",  name   varchar(64) not null" +
                    ",  date  timestamp  not null    " +
                    ",  numOfSeats int not null" +
                    ")");

            System.out.println("DB initialized successfully");
            // close this particular instance again otherwise there would be a problem with having 2 instances
            Database.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}