package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.controller.CommService;

public class ShipApplicationController {
    @FXML
    private Label text;  //connecting info text
    @FXML
    private Label name;  //ship name label
    @FXML
    private String appName = "Remote Ship Controller App";  //app name
    @FXML
    private ComboBox<String> portComboBox;

    //initialize
    private Stage stage;
    private Scene startScene;

    public void setStage(Stage stage) {
        this.stage = stage;
        startScene = stage.getScene();
    }

    @FXML
    public void initialize() {
        name.setText(appName);
        name.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
        initializePorts();
    }

    @FXML
    private void initializePorts() {
        for (int i = 1; i <= 16; i++) {
            portComboBox.getItems().add("COM" + i);
        }
        portComboBox.getValue();
    }

    //Handling info button/Showing info about app
    @FXML
    protected void onClickInfoButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("App info");
        alert.setContentText("App: Remote Ship Controller App" +
                "\nAuthors: Julia Manikowska, Jakub Kubiak, Mateusz Wieczorek, Jan Odważny" +
                "\nProject implemented as part of our bachelor's thesis: \"Bezzałogowy pojazd podwodny UUV (SSN688 Los Angeles 1/72)\"" +
                "\nSupervisor: D.E. Mikołaj Sobczak" +
                "\nPoznan University of Technology");
        alert.showAndWait();
    }

    //Handling connect button
    @FXML
    protected void onClickConnectButton(ActionEvent event) throws Exception {
        text.setText("Connecting...");
        String selectedPort = portComboBox.getValue();
        CommService commService = new CommService();
        commService.initializePort(selectedPort);
        //TODO łączenie z okrętem i dopiero po tym może się wykonać poniższe
        FXMLLoader loader = new FXMLLoader(getClass().getResource("basic-control-view.fxml"));
        AnchorPane controlSceneRoot = loader.load();
        Scene controlScene = new Scene(controlSceneRoot);
        stage.setScene(controlScene);
    }
}
