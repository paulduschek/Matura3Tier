package controllerview;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import model.BusinessLogic;
import model.ImportEvents;

import java.io.IOException;

public class BookC {
    @FXML
    private ChoiceBox eventCB;
    @FXML
    private Spinner seatsSpinner;
    @FXML
    private Button bookBTN;

    private BusinessLogic model;

    public static void show(Stage stage) {
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
    public void initialize(){
        model = new BusinessLogic();
        updateChoiceBox();
    }

    @FXML
    public void importEvent(){
        ImportEvents importEvents = new ImportEvents();
        importEvents.tryImport();
        updateChoiceBox();
    }

    public void updateChoiceBox(){
        eventCB.setItems(model.getChoicesFromDatabase());
    }
}
