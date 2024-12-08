package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ShipController {

    @FXML
    private Label shipName;
    @FXML
    private Label state;
    @FXML
    private GridPane buttonGrid;

    private int pos_x;
    private int pos_turn;
    private int immersion_time;

    @FXML
    public void initialize() {
        shipName.setText("Ship Name");
        state.setText("Emergence");
        state.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
        createButtons();
    }

    private void createButtons() {
        HBox  button1 = createButtonWithTextField("Move forward");
        HBox  button2 = createButtonWithTextField("Turn left");
        HBox  button3 = createButtonWithTextField("Immersion");
        HBox  button4 = createButtonWithTextField("Move back");
        HBox  button5 = createButtonWithTextField("Turn right");
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

        button.setOnAction(event -> textField.clear());

        hbox.getChildren().addAll(button, textField);

        return hbox;
    }

}
