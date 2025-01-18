package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class ShipApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    //initialize app
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("base-view.fxml"));
        AnchorPane root = loader.load();
        ShipApplicationController controller = loader.getController();
        controller.setStage(stage);

        Scene startScene = new Scene(root);
        stage.setScene(startScene);
        stage.setTitle("Remote Ship Controller");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}
