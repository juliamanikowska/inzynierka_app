package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class ShipController {

    @FXML
    private Label shipName;
    @FXML
    private Label state;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private Label posXLabel;

    private int pos_x = 0;
    private int pos_turn;
    private int immersion_time;

    @FXML
    public void initialize() {
        shipName.setText("Ship Name");
        state.setText("Emergence");
        state.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
        posXLabel.setText("Position: " + pos_x);
        createButtons();
    }

    private void createButtons() {
        HBox button1 = createButtonWithTextField("Move forward");
        HBox button2 = createButtonWithTextField("Turn left");
        HBox button3 = createButtonWithTextField("Immersion");
        HBox button4 = createButtonWithTextField("Move back");
        HBox button5 = createButtonWithTextField("Turn right");
        Button button6 = new Button("Help");

        button6.setPrefSize(150, 50);

        buttonGrid.add(button1, 0, 0);
        buttonGrid.add(button2, 1, 0);
        buttonGrid.add(button3, 2, 0);
        buttonGrid.add(button4, 0, 1);
        buttonGrid.add(button5, 1, 1);
        buttonGrid.add(button6, 2, 1);
    }

    private HBox createButtonWithTextField(String buttonText) {
        HBox hbox = new HBox(0);
        hbox.setAlignment(Pos.CENTER);

        Button button = new Button(buttonText);
        button.setPrefSize(100, 50);

        TextField textField = new TextField();
        textField.setPrefSize(50, 50);

        button.setOnAction(event -> {
            if (buttonText.equals("Immersion")) {
                try {
                    immersion_time = Integer.parseInt(textField.getText());
                    state.setText("Immersion");
                    disableControls(true);
                    startImmersionTimer();
                    textField.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number of seconds.");
                }
            } else if (buttonText.equals("Move forward")) {
                try {
                    int moveAmount = Integer.parseInt(textField.getText());
                    pos_x += moveAmount;
                    posXLabel.setText("Position: " + pos_x);
                    textField.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number.");
                }
            } else if (buttonText.equals("Move back")) {
                try {
                    int moveAmount = Integer.parseInt(textField.getText());
                    pos_x -= moveAmount;
                    posXLabel.setText("Position: " + pos_x);
                    textField.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number.");
                }
            } else {
                textField.clear();
            }
        });

        hbox.getChildren().addAll(button, textField);

        return hbox;
    }

    private void disableControls(boolean disable) {
        for (var node : buttonGrid.getChildren()) {
            node.setDisable(disable);
        }
    }

    private void startImmersionTimer() {
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(immersion_time), event -> {
                    state.setText("Emergence");
                    disableControls(false);
                })
        );
        timer.setCycleCount(1);
        timer.play();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
