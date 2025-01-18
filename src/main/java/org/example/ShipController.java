package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
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
    private Label posXLabel; // Label do wyświetlania pozycji
    @FXML
    private Label posTurnLabel;
    @FXML
    private ImageView shipImageView;
    @FXML
    private ImageView joystickImageView; // Obrazek joysticka
    @FXML
    private ImageView joystickImageView2;
    @FXML
    private Button arrowUpButton; // Przycisk ze strzałką w górę
    @FXML
    private Button arrowDownButton; // Przycisk ze strzałką w dół
    @FXML
    private Button confirmButton; // Przycisk "Confirm"
    @FXML
    private Button arrowLeftButton; // Przycisk ze strzałką w lewo
    @FXML
    private Button arrowRightButton; // Przycisk ze strzałką w prawo
    @FXML
    private Button confirmButton2; // Przycisk "Confirm" 2
    @FXML
    private TextArea logTextArea; // Pole do wyświetlania logów


    private int pos_x = 0; // Zmienna przechowująca aktualną pozycję
    private int pos_turn = 0;
    private int immersion_time;
    private int og_width = 240;
    private int og_height = 180;
    private TextField keyboardDisplay;
    private GridPane keyboardGrid;
    private int x = 0;
    private int turn =0;

    @FXML
    public void initialize() {
        //shipName.setText("Ship Name");
        //state.setText("Emergence");
        //state.setStyle("-fx-font-size: 25px; -fx-text-fill: blue; -fx-font-weight: bold;");
        //posXLabel.setText("Position: " + pos_x); // Pokazujemy początkową wartość pozycji
        //posTurnLabel.setText("Turned: " + pos_turn);
        displayImage();  // Wyświetlenie obrazu statku
        createKeyboard();  // Tworzenie klawiatury
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();

        logTextArea = new TextArea();
        logTextArea.setEditable(false); // Logi nie powinny być edytowalne
        logTextArea.setPrefHeight(250); // Ustal wysokość logów
        logTextArea.setPrefWidth(200);
        logTextArea.setStyle("-fx-control-inner-background: black; " + // Tło tekstu
                "-fx-text-fill: white; " +                // Kolor tekstu
                "-fx-font-size: 14px; " +                // Rozmiar czcionki
                "-fx-border-color: gray; " +             // Obramowanie
                "-fx-border-width: 2px;");

        rootPane.getChildren().add(logTextArea);
        AnchorPane.setTopAnchor(logTextArea, 20.0);
        AnchorPane.setRightAnchor(logTextArea, 10.0);

        logMessage("Application initialized.");

        // Wczytanie obrazka joysticka (domyślnie base_move_center.png)
        Image joystickImage = new Image(getClass().getResource("/images/base_move_center.png").toExternalForm());
        joystickImageView = new ImageView(joystickImage);
        joystickImageView.setFitWidth(300);
        joystickImageView.setFitHeight(300);
        joystickImageView.setPreserveRatio(true);

        Image joystickImage2 = new Image(getClass().getResource("/images/base_turn_center.png").toExternalForm());
        joystickImageView2 = new ImageView(joystickImage2);
        joystickImageView2.setFitWidth(200);
        joystickImageView2.setFitHeight(200);
        joystickImageView2.setPreserveRatio(true);

        // Dodanie joysticka do rootPane
        rootPane.getChildren().add(joystickImageView);
        AnchorPane.setBottomAnchor(joystickImageView, 80.0);
        AnchorPane.setLeftAnchor(joystickImageView, 190.0);

        rootPane.getChildren().add(joystickImageView2);
        AnchorPane.setBottomAnchor(joystickImageView2, 125.0);
        AnchorPane.setRightAnchor(joystickImageView2, 15.0);

        // Tworzenie przycisków strzałek
        arrowUpButton = createArrowButton("arrow_up.png", "up");
        arrowDownButton = createArrowButton("arrow_down.png", "down");
        arrowLeftButton = createArrowButton("arrow_left.png", "left");
        arrowRightButton = createArrowButton("arrow_right.png", "right");

        // Dodanie przycisków do layoutu
        VBox arrowBox = new VBox(10);
        arrowBox.getChildren().addAll(arrowUpButton, arrowDownButton);
        rootPane.getChildren().add(arrowBox);
        AnchorPane.setBottomAnchor(arrowBox, 130.0);
        AnchorPane.setLeftAnchor(arrowBox, 390.0); // Przesunięcie przycisków obok joysticka

        HBox arrowBox2 = new HBox(60);
        arrowBox2.getChildren().addAll(arrowLeftButton, arrowRightButton);
        rootPane.getChildren().add(arrowBox2);
        AnchorPane.setBottomAnchor(arrowBox2, 70.0);
        AnchorPane.setRightAnchor(arrowBox2, 35.0);

        // Tworzenie wyświetlacza pozycji po lewej stronie joysticka
        createPositionDisplay();
        createPositionDisplay2();
    }

    private void logMessage(String message) {
        Platform.runLater(() -> {
            logTextArea.appendText(message + "\n"); // Wyświetla log w interfejsie
        });
        System.out.println(message); // Wyświetla log w stdout
    }


    private Button createArrowButton(String imageFileName, String direction) {
        Button button = new Button();
        button.setPrefSize(50, 50);

        // Załadowanie obrazka do przycisku
        Image image = new Image(getClass().getResource("/images/" + imageFileName).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);

        // Obsługa kliknięcia
        button.setOnAction(event -> handleArrowButtonPress(direction));

        // Stylizacja
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        return button;
    }

    private void handleArrowButtonPress(String direction) {
        switch (direction) {
            case "up":
                // Zmiana obrazka joysticka na strzałkę w górę
                Image upImage = new Image(getClass().getResource("/images/base_move_up.png").toExternalForm());
                joystickImageView.setImage(upImage);
                // Zwiększanie wartości pos_x
                x++;
                posXLabel.setText("Move: " + x); // Zaktualizowanie etykiety z pozycją
                break;
            case "down":
                // Zmiana obrazka joysticka na strzałkę w dół
                Image downImage = new Image(getClass().getResource("/images/base_move_down.png").toExternalForm());
                joystickImageView.setImage(downImage);
                // Zmniejszanie wartości pos_x
                x--;
                posXLabel.setText("Move: " + x); // Zaktualizowanie etykiety z pozycją
                break;
            case "left":
                // Zmiana obrazka joysticka na strzałkę w dół
                Image leftImage = new Image(getClass().getResource("/images/base_turn_left.png").toExternalForm());
                joystickImageView2.setImage(leftImage);
                // Zmniejszanie wartości pos_x
                turn--;
                posTurnLabel.setText("Turn: " + turn); // Zaktualizowanie etykiety z pozycją
                break;
            case "right":
                // Zmiana obrazka joysticka na strzałkę w dół
                Image rightImage = new Image(getClass().getResource("/images/base_turn_right.png").toExternalForm());
                joystickImageView2.setImage(rightImage);
                // Zmniejszanie wartości pos_x
                turn++;
                posTurnLabel.setText("Turn: " + turn); // Zaktualizowanie etykiety z pozycją
                break;
        }
    }

    private void createPositionDisplay() {
        // Tworzenie wyświetlacza pozycji (Label) po lewej stronie joysticka
        //Label positionLabel = new Label("Position:");
        //positionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tworzenie etykiety do wyświetlania pozycji
        posXLabel = new Label("Move: " + x);
        posXLabel.setStyle("-fx-font-size: 18px;");

        // Tworzenie przycisku "Confirm"
        confirmButton = new Button("Confirm");
        confirmButton.setPrefSize(80, 40);
        confirmButton.setStyle("-fx-font-size: 16px;");
        confirmButton.setOnAction(event -> handleConfirmButtonPress());

        // Umieszczamy wszystkie elementy w VBox
        VBox positionBox = new VBox(10);
        positionBox.getChildren().addAll(posXLabel, confirmButton);
        rootPane.getChildren().add(positionBox);

        // Ustawienie pozycji w AnchorPane
        AnchorPane.setBottomAnchor(positionBox, 140.0);
        AnchorPane.setLeftAnchor(positionBox, 180.0); // Po lewej stronie joysticka
    }

    private void createPositionDisplay2() {
        // Tworzenie wyświetlacza pozycji (Label) po lewej stronie joysticka
        //Label positionLabel = new Label("Position:");
        //positionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Tworzenie etykiety do wyświetlania pozycji
        posTurnLabel = new Label("Turn: " + x);
        posTurnLabel.setStyle("-fx-font-size: 18px;");

        // Tworzenie przycisku "Confirm"
        confirmButton2 = new Button("Confirm");
        confirmButton2.setPrefSize(80, 40);
        confirmButton2.setStyle("-fx-font-size: 16px;");
        confirmButton2.setOnAction(event -> handleConfirmButtonPress2());

        // Umieszczamy wszystkie elementy w VBox
        VBox positionBox = new VBox(10);
        positionBox.getChildren().addAll(posTurnLabel, confirmButton2);
        rootPane.getChildren().add(positionBox);

        // Ustawienie pozycji w AnchorPane
        AnchorPane.setBottomAnchor(positionBox, 140.0);
        AnchorPane.setRightAnchor(positionBox, 220.0); // Po lewej stronie joysticka
    }

    private void handleConfirmButtonPress() {
        // Aktualizowanie etykiety wyświetlającej pozycję na podstawie zmienionej wartości pos_x
        Image joystickImage = new Image(getClass().getResource("/images/base_move_center.png").toExternalForm());
        joystickImageView.setImage(joystickImage);
        pos_x += x;
        //posXLabel.setText("Position: " + pos_x); // Ustawienie nowej pozycji na etykiecie

        // Resetowanie wartości pos_x (zerojemy pozycję)
        x = 0;

        // Ponownie ustawienie wartości pozycji na "Move" w górnej części ekranu
        posXLabel.setText("Move: " + x); // Zresetowanie wartości do 0

        logMessage("Position confirmed: " + pos_x);  // Potwierdzenie w konsoli
    }

    private void handleConfirmButtonPress2() {
        // Aktualizowanie etykiety wyświetlającej pozycję na podstawie zmienionej wartości pos_x
        Image joystickImage2 = new Image(getClass().getResource("/images/base_turn_center.png").toExternalForm());
        joystickImageView2.setImage(joystickImage2);
        pos_turn += turn;

        // Resetowanie wartości pos_x (zerojemy pozycję)
        turn = 0;

        // Ponownie ustawienie wartości pozycji na "Move" w górnej części ekranu
        posTurnLabel.setText("Turn: " + turn); // Zresetowanie wartości do 0

        logMessage("Turn confirmed: " + pos_turn);  // Potwierdzenie w konsoli
    }


    private void displayImage() {
        Image image = new Image(getClass().getResource("/images/out_view.png").toExternalForm());
        shipImageView = new ImageView(image);
        shipImageView.setFitWidth(og_width * 1.2);
        shipImageView.setFitHeight(og_height * 1.2);
        shipImageView.setPreserveRatio(true);
        shipImageView.setLayoutX(15);
        shipImageView.setLayoutY(25);
        //shipImageView.getStyleClass().add("borderedImage");
        rootPane.getChildren().add(shipImageView);

        //System.out.println("Image displayed in the top-left corner.");
        //System.out.println("ImageView style classes: " + shipImageView.getStyleClass());
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
        AnchorPane.setBottomAnchor(keyboardBox, 90.0);
        AnchorPane.setLeftAnchor(keyboardBox, 20.0);
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
                logMessage("OK button pressed, value: " + keyboardDisplay.getText());
                try {
                    immersion_time = Integer.parseInt(keyboardDisplay.getText());
                    //state.setText("Immersion");
                    disableControls(true);
                    changeImageDuringImmersion();
                    startImmersionTimer();
                    keyboardDisplay.clear();
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number of seconds.");
                }
                break;
            case "keyboard_x":
                logMessage("Cancel button pressed");
                keyboardDisplay.clear(); // Clear the display
                break;
            default:
                // Extract the number from the label (e.g., "keyboard_0" -> "0")
                if (label.startsWith("keyboard_")) {
                    String number = label.substring(9); // Extract the number after "keyboard_"
                    keyboardDisplay.appendText(number);
                }
                logMessage("Number button pressed: " + label);
                break;
        }
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
        // Disable buttons in the button grid
        for (var node : buttonGrid.getChildren()) {
            node.setDisable(disable);
        }

        // Disable keyboard grid buttons
        if (keyboardGrid != null) {
            for (var node : keyboardGrid.getChildren()) {
                node.setDisable(disable);
            }
        }

        // Disable the text field for keyboard input
        keyboardDisplay.setDisable(disable);

        // Disable joysticks and arrow buttons
        joystickImageView.setDisable(disable); // Disabling joystick image
        arrowUpButton.setDisable(disable);     // Disabling the arrow up button
        arrowDownButton.setDisable(disable);   // Disabling the arrow down button
        confirmButton.setDisable(disable);
        arrowLeftButton.setDisable(disable);
        arrowRightButton.setDisable(disable);
        confirmButton2.setDisable(disable);
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


