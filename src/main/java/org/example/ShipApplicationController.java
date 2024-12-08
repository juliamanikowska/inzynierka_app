package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ShipApplicationController
{
    @FXML
    private Label text;
    @FXML
    private Label name;
    @FXML
    private String appName = "Remote Ship Controller App";

    @FXML
    public void initialize() {
        name.setText(appName);
        name.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
    }

    @FXML
    protected void onClickConnectButton(){text.setText("Connecting...");}

    @FXML
    protected void onClickInfoButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("App info");
        alert.setContentText("App: Remote Ship Controller App\nAuthors: Julia Manikowska, Jakub Kubiak, Mateusz Wieczorek, Jan Odważny" +
                "\nProject implemented as part of bachelor's thesis: \"Bezzałogowy pojazd podwodny UUV (SSN688 Los Angeles 1/72)\"" +
                "\nSupervisor: D.E. Mikołaj Sobczak" +
                "\nPoznan University of Technology");

        alert.showAndWait();
    }


}
