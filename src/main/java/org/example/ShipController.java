package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShipController {

    @FXML
    private Label labelControl;

    @FXML
    public void initialize() {
        labelControl.setText("Control View");
    }


}
