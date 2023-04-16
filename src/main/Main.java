package main;

import controllerview.BookC;
import database.Database;
import javafx.application.Application;
import javafx.stage.Stage;
import model.ImportEvents;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void init() throws SQLException {
        Database.open(true);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BookC.show(stage);
    }

    @Override
    public void stop(){
        Database.close();
    }
}
