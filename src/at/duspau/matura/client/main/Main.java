package at.duspau.matura.client.main;

import at.duspau.matura.client.controllerview.*;
import at.duspau.matura.model.BusinessLogic;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    BusinessLogic model;
    @Override
    public void start(Stage stage){
        model = new BusinessLogic();
        BookC.show(stage, model);
    }

    @Override
    public void stop(){
        model.terminateClientSocket();
    }
}
