package model;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BusinessLogic {
    public ObservableList<String> getChoicesFromDatabase() {
        ObservableList<String> choices = FXCollections.observableArrayList();
        try {
            PreparedStatement pstmt = Database.getInstance().getPstmtSelectNameDate();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String choice = rs.getString("name") + " - " + rs.getTimestamp("date").toString();
                choices.add(choice);
            }
            rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return choices;
    }
}
