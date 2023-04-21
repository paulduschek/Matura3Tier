package at.duspau.matura.client.controllerview;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import at.duspau.matura.model.BusinessLogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class BookC {
    @FXML
    private ChoiceBox eventCB;
    @FXML
    private TextField seatsTF;
    @FXML
    private Button updateBTN;

    private static BusinessLogic model;
    private int seatsToBook;
    private String currentEvent;

    public static void show(Stage stage, BusinessLogic model) {
        BookC.model = model;
        try {

            FXMLLoader loader = new FXMLLoader(BookC.class.getResource("bookV.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Booking-3tier");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    @FXML
    public void initialize() {
        update();
    }

    public void update() {
        //model.setDoUpdate(true);
        try{
            model.updateList();
        }
        catch (IOException e){
            error("Error when sending object to server");
        }
        catch(ClassNotFoundException e){
            error("Error when reading events from database");
        }
        eventCB.setItems(FXCollections.observableArrayList(model.getEventsForCb()));
    }

    @FXML
    public void book(){
        seatsToBook = Integer.parseInt(seatsTF.getText());
        currentEvent = (String) eventCB.getValue();
        System.out.println(currentEvent);
        try{
            if(model.bookSeats(seatsToBook, currentEvent)){
                info("Seat booked!");
            }
            else{
                error("Seat taken!");
            }
        }
        catch (IOException e){
            error("Error when sending object to server");
        }
        catch(ClassNotFoundException e){
            error("Error when reading events from database");
        }
    }

    private void info(String msg) {
        Alert error = new Alert(Alert.AlertType.INFORMATION, msg);
        error.showAndWait();
    }

    private void error(String msg) {
        Alert error = new Alert(Alert.AlertType.ERROR, msg);
        error.showAndWait();
    }
}
