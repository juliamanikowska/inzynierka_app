package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class ShipController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Label shipName;
    @FXML
    private Label state;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private Label posXLabel;
    @FXML
    private Label posTurnLabel;
    @FXML
    private ImageView shipImageView;

    private int pos_x = 0;
    private int pos_turn = 0;
    private int immersion_time;
    private int og_width = 240;
    private int og_height = 180;
    private TextField keyboardDisplay;
    private int is_underwater = 0;
    private GridPane keyboardGrid;


    @FXML
    public void initialize() {
        shipName.setText("Ship Name");
        state.setText("Emergence");
        state.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
        posXLabel.setText("Position: " + pos_x);
        posTurnLabel.setText("Turned: " + pos_turn);
        displayImage();
        createButtons();
        createKeyboard();
        rootPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    private void displayImage() {
        Image image = new Image(getClass().getResource("/images/out_view.png").toExternalForm());
        shipImageView = new ImageView(image);
        shipImageView.setFitWidth(og_width * 1.2);
        shipImageView.setFitHeight(og_height * 1.2);
        shipImageView.setPreserveRatio(true);
        shipImageView.setLayoutX(15);
        shipImageView.setLayoutY(25);
        shipImageView.getStyleClass().add("borderedImage");
        rootPane.getChildren().add(shipImageView);

        System.out.println("Image displayed in the top-left corner.");
        System.out.println("ImageView style classes: " + shipImageView.getStyleClass());
    }

    private void createKeyboard() {
        // Tworzenie GridPane dla klawiatury
        keyboardGrid = new GridPane();
        keyboardGrid.setAlignment(Pos.CENTER);

        // Tworzenie wyświetlacza tekstowego nad klawiaturą
        keyboardDisplay = new TextField();
        keyboardDisplay.setPrefWidth(200);
        keyboardDisplay.setStyle("-fx-font-size: 18px; -fx-alignment: center;");
        keyboardDisplay.setEditable(false); // Nie można edytować ręcznie

        VBox keyboardBox = new VBox(3); // VBox do układania wyświetlacza i klawiatury
        keyboardBox.setPrefWidth(5);
        keyboardBox.setAlignment(Pos.CENTER);

        String[] buttonImages = {
                "keyboard_1.png", "keyboard_2.png", "keyboard_3.png",
                "keyboard_4.png", "keyboard_5.png", "keyboard_6.png",
                "keyboard_7.png", "keyboard_8.png", "keyboard_9.png",
                "keyboard_ok.png", "keyboard_0.png", "keyboard_x.png"
        };
        int index = 0;

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                if (index < buttonImages.length) {
                    String imageFileName = buttonImages[index];
                    Button button = createImageButton(imageFileName);
                    keyboardGrid.add(button, col, row);
                    index++;
                }
            }
        }

        keyboardBox.getChildren().addAll(keyboardDisplay, keyboardGrid);
        rootPane.getChildren().add(keyboardBox);
        AnchorPane.setTopAnchor(keyboardBox, 20.0);
        AnchorPane.setRightAnchor(keyboardBox, 20.0);
    }


    private Button createImageButton(String imageFileName) {
        Button button = new Button();
        button.setPrefSize(40, 40);

        // Load the image for the button
        Image image = new Image(getClass().getResource("/images/" + imageFileName).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);

        // Extract label from file name (e.g., "0" from "0.png")
        String label = imageFileName.substring(0, imageFileName.indexOf('.'));

        // Remove padding for the button to ensure the image covers it completely
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        // Set button action
        button.setOnAction(event -> handleKeyboardButtonPress(label));

        return button;
    }

    private void handleKeyboardButtonPress(String label) {
        switch (label) {
            case "keyboard_ok":
                System.out.println("OK button pressed, value: " + keyboardDisplay.getText());
                try {
                    immersion_time = Integer.parseInt(keyboardDisplay.getText());
                    state.setText("Immersion");
                    disableControls(true);
                    changeImageDuringImmersion();
                    startImmersionTimer();
                    keyboardDisplay.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number of seconds.");
                }
                break;
            case "keyboard_x":
                System.out.println("Cancel button pressed");
                keyboardDisplay.clear(); // Clear the display
                break;
            default:
                // Extract the number from the label (e.g., "keyboard_0" -> "0")
                if (label.startsWith("keyboard_")) {
                    String number = label.substring(9); // Extract the number after "keyboard_"
                    keyboardDisplay.appendText(number);
                }
                System.out.println("Number button pressed: " + label);
                break;
        }
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
                    changeImageDuringImmersion();
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
            } else if (buttonText.equals("Turn left")) {
                try {
                    int turnAmount = Integer.parseInt(textField.getText());
                    pos_turn -= turnAmount;
                    posTurnLabel.setText("Turned: " + pos_turn);
                    textField.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number.");
                }
            } else if (buttonText.equals("Turn right")) {
                try {
                    int turnAmount = Integer.parseInt(textField.getText());
                    pos_turn += turnAmount;
                    posTurnLabel.setText("Turned: " + pos_turn);
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

    private void changeImageDuringImmersion() {
        // Zmień obraz na "in_view.png"
        shipImageView.setImage(new Image(getClass().getResource("/images/in_view.png").toExternalForm()));

        // Przywróć oryginalny obraz po zanurzeniu
        Timeline revertImageTimeline = new Timeline(
                new KeyFrame(Duration.seconds(immersion_time), event -> {
                    shipImageView.setImage(new Image(getClass().getResource("/images/out_view.png").toExternalForm()));
                })
        );
        revertImageTimeline.setCycleCount(1);
        revertImageTimeline.play();
    }

    private void disableControls(boolean disable) {
        for (var node : buttonGrid.getChildren()) {
            node.setDisable(disable);
        }
        if (keyboardGrid != null) {
            for (var node : keyboardGrid.getChildren()) {
                node.setDisable(disable);
            }
        }
        keyboardDisplay.setDisable(disable);
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
