package org.example;

import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.controller.CommService;
import org.example.controller.SubmarineController;
import org.example.model.Command;
import com.fazecast.jSerialComm.SerialPort;

import javafx.collections.FXCollections;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.scene.layout.GridPane;

public class ShipController {
    @FXML
    private AnchorPane rootPane;  //anchors for display
    @FXML
    private Label state;  //state - emergence or immersion
    @FXML
    private Label shipName;  //name of connected ship
    @FXML
    private Label pres;  //pressure
    @FXML
    private Label depth;  //depth
    @FXML
    private Label temperature;  //temperature
    @FXML
    private Label latitude;  //latitude
    @FXML
    private Label longitude;  //longitude
    @FXML
    private Label pitch;  //pitch gyro
    @FXML
    private Label roll;  //roll gyro
    @FXML
    private Label yaw;  //yaw gyro
    @FXML
    private Label battery;  //battery state in %
    @FXML
    private Label engine;  //engine power
    @FXML
    private Label direction;  //ship direction

    @FXML
    private Label posXLabel;  //label next to move joystick
    @FXML
    private Label posTurnLabel;  //label next to turn joystick
    @FXML
    private ImageView shipImageView;  //image/view of the "outside"
    @FXML
    private ImageView joystickMoveImageView;  //view for move fwd/back joystick
    @FXML
    private ImageView joystickTurnImageView;  //view for turn left/right joystick
    @FXML
    private Button arrowUpButton;  //up button next to move joystick
    @FXML
    private Button arrowDownButton;  //down button next to move joystick
    @FXML
    private Button confirmMoveButton;  //confirm button next to move joystick
    @FXML
    private Button arrowLeftButton;  //left button next to turn joystick
    @FXML
    private Button arrowRightButton;  //right button next to turn joystick
    @FXML
    private Button confirmTurnButton;  //confirm button next to turn joystick
    @FXML
    private TextArea logTextArea;  //logs area
    @FXML
    private Button helpButton;  //help button
    @FXML
    private Button tankupButton;  //tanking up button
    @FXML
    private Button drainButton;  //draining button
    @FXML
    private Button stopButton;  //stop the tanking/draining button
    @FXML
    private Slider depthSlider;

    private int pos_x = 0;  //global position move
    private int pos_turn = 0;  //global turn
    private int immersion_time;  //immersion time TODO usunąć jak będzie obsługa otrzymywania info od okrętu o zakończeniu manewru
    private int og_width = 240;  //original size of shipImage
    private int og_height = 180;  //original size of shipImage
    private TextField keyboardDisplay;  //display next to immersion keyboard
    private GridPane keyboardGrid;  //grid for keyboard (immersion)
    private int x = 0;  //value of move next to move joystick
    private int turn =0;  //value of turn next to turn joystick
    private SubmarineController submarineController;
    private CommService commService;


    public ShipController() {
        submarineController = new SubmarineController();
    }

    @FXML
    public void initialize() {
        defaultShipInfo();  //default values
        showShipInfo();  //displaying shipInfo
        displayImage();  //displaying shipImage
        createKeyboard();  //displaying immersion keyboard

        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();

        //logs area
        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setPrefHeight(250);
        logTextArea.setPrefWidth(200);
        logTextArea.setStyle("-fx-control-inner-background: black; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-border-color: gray; " +
                "-fx-border-width: 2px;");
        rootPane.getChildren().add(logTextArea);
        AnchorPane.setTopAnchor(logTextArea, Double.valueOf(20.0));
        AnchorPane.setRightAnchor(logTextArea, Double.valueOf(10.0));

        logMessage("Application initialized.");

        //move joystick
        Image joystickMoveImage = new Image(getClass().getResource("/images/base_move_center.png").toExternalForm());
        joystickMoveImageView = new ImageView(joystickMoveImage);
        joystickMoveImageView.setFitWidth(300);
        joystickMoveImageView.setFitHeight(300);
        joystickMoveImageView.setPreserveRatio(true);
        rootPane.getChildren().add(joystickMoveImageView);
        AnchorPane.setBottomAnchor(joystickMoveImageView, Double.valueOf(20.0));
        AnchorPane.setLeftAnchor(joystickMoveImageView, Double.valueOf(190.0));

        //turn joystick
        Image joystickTurnImage = new Image(getClass().getResource("/images/base_turn_center.png").toExternalForm());
        joystickTurnImageView = new ImageView(joystickTurnImage);
        joystickTurnImageView.setFitWidth(200);
        joystickTurnImageView.setFitHeight(200);
        joystickTurnImageView.setPreserveRatio(true);
        rootPane.getChildren().add(joystickTurnImageView);
        AnchorPane.setBottomAnchor(joystickTurnImageView, Double.valueOf(125.0));
        AnchorPane.setRightAnchor(joystickTurnImageView, Double.valueOf(15.0));

        //help
        helpButton = createHelpButton();
        VBox helpBox = new VBox(10);
        helpBox.getChildren().addAll(helpButton);
        rootPane.getChildren().add(helpBox);
        AnchorPane.setBottomAnchor(helpBox, Double.valueOf(5.0));
        AnchorPane.setRightAnchor(helpBox, Double.valueOf(5.0));

        //tanking/draining
        tankupButton = createTankingButtons(0);
        drainButton = createTankingButtons(1);
        HBox tankingBox = new HBox(10);
        tankingBox.getChildren().addAll(tankupButton, drainButton);
        rootPane.getChildren().add(tankingBox);
        AnchorPane.setTopAnchor(tankingBox, Double.valueOf(275.0));
        AnchorPane.setLeftAnchor(tankingBox, Double.valueOf(375.0));

        //stoping tanking/draining
        stopButton = createStopButton();
        VBox stopBox = new VBox();
        stopBox.getChildren().addAll(stopButton);
        rootPane.getChildren().add(stopBox);
        AnchorPane.setTopAnchor(stopBox, Double.valueOf(275.0));
        AnchorPane.setLeftAnchor(stopBox, Double.valueOf(420.0));
        stopButton.setVisible(false);

        //arrows next to joysticks
        arrowUpButton = createArrowButton("arrow_up.png", "up");
        arrowDownButton = createArrowButton("arrow_down.png", "down");
        arrowLeftButton = createArrowButton("arrow_left.png", "left");
        arrowRightButton = createArrowButton("arrow_right.png", "right");

        VBox arrowBox = new VBox(10);
        arrowBox.getChildren().addAll(arrowUpButton, arrowDownButton);
        rootPane.getChildren().add(arrowBox);
        AnchorPane.setBottomAnchor(arrowBox, Double.valueOf(70.0));
        AnchorPane.setLeftAnchor(arrowBox, Double.valueOf(390.0));

        HBox arrowBox2 = new HBox(60);
        arrowBox2.getChildren().addAll(arrowLeftButton, arrowRightButton);
        rootPane.getChildren().add(arrowBox2);
        AnchorPane.setBottomAnchor(arrowBox2, Double.valueOf(70.0));
        AnchorPane.setRightAnchor(arrowBox2, Double.valueOf(35.0));

        //depth slider
        depthSlider = createSlider();
        depthSlider = createSlider();
        //TODO: dodac obsluge wysylania komunikatow o wychyleniu steru glebokosci
        depthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int sliderValue = newValue.intValue();
            switch (sliderValue) {
                case 0:
                    logMessage("Depth rudder set to UP");
                    break;
                case 1:
                    logMessage("Depth rudder set to NEUTRAL");
                    break;
                case 2:
                    logMessage("Depth rudder set to DOWN");
                    break;
                default:
                    logMessage("Unknown slider value");
                    break;
            }
        });
        VBox sliderBox = new VBox();
        sliderBox.getChildren().addAll(depthSlider);
        rootPane.getChildren().add(sliderBox);
        AnchorPane.setTopAnchor(sliderBox, Double.valueOf(280.0));
        AnchorPane.setLeftAnchor(sliderBox, Double.valueOf(180.0));

        createPositionDisplay();  //move joystick
        createTurnDisplay();  //turn joystick
    }

    //Setting default values for ship info
    private void defaultShipInfo(){
        shipName.setText("Ship Name: ShipName");
        state.setText("State: Emergence");
        pres.setText("Pres: 0.0");
        depth.setText("Depth: 0.0");
        temperature.setText("Temp: 0.0");
        latitude.setText("Latitude: 0.0");
        longitude.setText("Longitude: 0.0");
        pitch.setText("Pitch: 0");
        roll.setText("Roll: 0");
        yaw.setText("Yaw: 0");
        battery.setText("Battery: 100");
        engine.setText("Engine: 0");
        direction.setText("Direction: 0");
    }

    private Button createStopButton(){
        Button button = new Button();
        button.setPrefSize(90, 40);
        button.setStyle("-fx-font-size: 16px;");
        button.setText("Stop");
        button.setOnAction(event -> {
            disableControls(false);
            logMessage("Process stopped");
            button.setVisible(false);
            //TODO: dodac obsluge wysylania rozkazu zatrzymania zapelniania
        });

        return button;
    }

    private Slider createSlider(){
        Slider slider = new Slider(0, 2, 0);
        slider.setMin(0);
        slider.setMax(2);
        slider.setValue(1);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                if (n < 0.5) return "Up";
                if (n < 1.5) return "Neutral";
                return "Down";
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case "Up":
                        return 0d;
                    case "Neutral":
                        return 1d;
                    case "Down":
                        return 2d;
                    default:
                        return 1d;
                }
            }
        });

        slider.setMinWidth(150);

        return slider;
    }

    //Handling tanking button
    private void handleTankButton(){
        stopButton.setVisible(true);
        logMessage("Tanking up");
        disableControls(true);

        //TODO: dodac obsluge wysylania rozkazu o zapelnianiu

    }

    //Handling draining button
    private void handleDrainButton(){
        stopButton.setVisible(true);
        logMessage("Draining");
        disableControls(true);

        //TODO: dodac obsluge wysylania rozkazu o oproznianiu

    }

    //Creating buttons for tanking
    private Button createTankingButtons(int choice){
        Button button = new Button();
        button.setPrefSize(90, 40);
        button.setStyle("-fx-font-size: 16px;");

        switch(choice){
            case 0:
                button.setText("Tank Up");
                button.setOnAction(event -> handleTankButton());
                break;
            case 1:
                button.setText("Drain");
                button.setOnAction(event -> handleDrainButton());
                break;
        }
         return button;
    }

    //Setting ship info values
    private void showShipInfo(){
        //TODO pobieranie od okrętu shipName,pres,depth,temperature,latitude,longitude,pitch,roll,yaw,battery,engine,direction
    }

    //Showing logs (stdout) in app
    private void logMessage(String message) {
        Platform.runLater(() -> {
            logTextArea.appendText(message + "\n");
        });
        System.out.println(message);
    }

    //Creating help button
    private Button createHelpButton(){
        Button button = new Button();
        button.setPrefSize(50, 50);

        Image image = new Image(getClass().getResource("/images/help_icon.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);

        button.setOnAction(event -> handleHelpButton());
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        return button;
    }

    //Handling help button
    @FXML
    protected void handleHelpButton(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("");
        Image image = new Image(getClass().getResource("/images/help.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    //Creating image buttons for arrows next to joysticks
    private Button createArrowButton(String imageFileName, String direction) {
        Button button = new Button();
        button.setPrefSize(50, 50);

        Image image = new Image(getClass().getResource("/images/" + imageFileName).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);

        button.setOnAction(event -> handleArrowButtonPress(direction));
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        return button;
    }

    //Handling buttons with arrows
    private void handleArrowButtonPress(String direction) {
        switch (direction) {
            case "up":
                Image upImage = new Image(getClass().getResource("/images/base_move_up.png").toExternalForm());
                joystickMoveImageView.setImage(upImage);
                x++;
                posXLabel.setText("Move: " + x);
                break;
            case "down":
                Image downImage = new Image(getClass().getResource("/images/base_move_down.png").toExternalForm());
                joystickMoveImageView.setImage(downImage);
                x--;
                posXLabel.setText("Move: " + x);
                break;
            case "left":
                Image leftImage = new Image(getClass().getResource("/images/base_turn_left.png").toExternalForm());
                joystickTurnImageView.setImage(leftImage);
                turn--;
                posTurnLabel.setText("Turn: " + turn);
                break;
            case "right":
                Image rightImage = new Image(getClass().getResource("/images/base_turn_right.png").toExternalForm());
                joystickTurnImageView.setImage(rightImage);
                turn++;
                posTurnLabel.setText("Turn: " + turn);
                break;
        }
    }

    //Displaying value of move next to fwd/back move joystick
    private void createPositionDisplay() {
        posXLabel = new Label("Move: " + x);
        posXLabel.setStyle("-fx-font-size: 18px;");

        confirmMoveButton = new Button("Confirm");
        confirmMoveButton.setPrefSize(80, 40);
        confirmMoveButton.setStyle("-fx-font-size: 16px;");
        confirmMoveButton.setOnAction(event -> handleConfirmMoveButtonPress());

        VBox positionBox = new VBox(10);
        positionBox.getChildren().addAll(posXLabel, confirmMoveButton);
        rootPane.getChildren().add(positionBox);

        AnchorPane.setBottomAnchor(positionBox, Double.valueOf(80.0));
        AnchorPane.setLeftAnchor(positionBox, Double.valueOf(180.0));
    }

    //Displaying value of turn next to left/right turn joystick
    private void createTurnDisplay() {
        posTurnLabel = new Label("Turn: " + x);
        posTurnLabel.setStyle("-fx-font-size: 18px;");

        confirmTurnButton = new Button("Confirm");
        confirmTurnButton.setPrefSize(80, 40);
        confirmTurnButton.setStyle("-fx-font-size: 16px;");
        confirmTurnButton.setOnAction(event -> handleConfirmTurnButtonPress());

        VBox positionBox = new VBox(10);
        positionBox.getChildren().addAll(posTurnLabel, confirmTurnButton);
        rootPane.getChildren().add(positionBox);

        AnchorPane.setBottomAnchor(positionBox, Double.valueOf(140.0));
        AnchorPane.setRightAnchor(positionBox, Double.valueOf(220.0));
    }

    //Handling confirmation of move fwd/back joystick
    private void handleConfirmMoveButtonPress() {
        Image joystickMoveImage = new Image(getClass().getResource("/images/base_move_center.png").toExternalForm());
        joystickMoveImageView.setImage(joystickMoveImage);
        pos_x += x;

        if (x==0) logMessage("You can't move by 0");
        else if (x<-100 || x>100) logMessage("Move must be in range (-100, 100)");
        else{
            logMessage("Sending command: move by " + x);
            //TODO przesyłanie do okretu wartosci "x" czyli o ile się ruszyć przod(x>0)/tyl(x<0)
        }

        x = 0;
        posXLabel.setText("Move: " + x);
        logMessage("Position: " + pos_x);
    }

    //Handling confirmation of turn left/right joystick
    private void handleConfirmTurnButtonPress() {
        Image joystickTurnImage = new Image(getClass().getResource("/images/base_turn_center.png").toExternalForm());
        joystickTurnImageView.setImage(joystickTurnImage);
        pos_turn += turn;

        if (turn==0) logMessage("You can't turn by 0");
        else if (turn<-40 || turn>40) logMessage("Turn must be in range (-40, 40)");
        else{
            logMessage("Sending command: turn by "+ turn);
            //TODO przesyłanie do okretu wartosci "turn" czyli o ile się obrócić lewo(x<0)/prawo(x>0)
        }

        turn = 0;
        posTurnLabel.setText("Turn: " + turn);
        logMessage("Turned: " + pos_turn);
    }

    //Displaying image/view of the "outside"
    private void displayImage() {
        Image image = new Image(getClass().getResource("/images/out_view.png").toExternalForm());
        shipImageView = new ImageView(image);
        shipImageView.setFitWidth(og_width * 1.2);
        shipImageView.setFitHeight(og_height * 1.2);
        shipImageView.setPreserveRatio(true);
        shipImageView.setLayoutX(15);
        shipImageView.setLayoutY(25);
        rootPane.getChildren().add(shipImageView);
    }

    //Creating/Displaying keyboard for immersion
    private void createKeyboard() {
        keyboardGrid = new GridPane();
        keyboardGrid.setAlignment(Pos.CENTER);

        keyboardDisplay = new TextField();
        keyboardDisplay.setPrefWidth(200);
        keyboardDisplay.setStyle("-fx-font-size: 18px; -fx-alignment: center;");
        keyboardDisplay.setEditable(false);

        VBox keyboardBox = new VBox(3);
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
        AnchorPane.setBottomAnchor(keyboardBox, Double.valueOf(70.0));
        AnchorPane.setLeftAnchor(keyboardBox, Double.valueOf(20.0));
    }

    //Creating image buttons for keyboard
    private Button createImageButton(String imageFileName) {
        Button button = new Button();
        button.setPrefSize(40, 40);

        Image image = new Image(getClass().getResource("/images/" + imageFileName).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);

        String label = imageFileName.substring(0, imageFileName.indexOf('.'));
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
        button.setOnAction(event -> handleKeyboardButtonPress(label));

        return button;
    }

    //Handling usage of keyboard buttons for immersion
    private void handleKeyboardButtonPress(String label) {
        switch (label) {
            case "keyboard_ok":
                logMessage("Sending command: immersion for " + keyboardDisplay.getText() + " sec");
                try {
                    if (keyboardDisplay.getText()=="0"){
                        logMessage("You can't have immersion for 0");
                        break;
                    }
                    else{
                        immersion_time = Integer.parseInt(keyboardDisplay.getText());
                        state.setText("State: Immersion");
                        //TODO obsługa wysyłania do okrętu wartości "immersion_time", czyli na ile sekund ma się zanurzyć
                        //TODO otrzymywanie komunikatu z okrętu że zakończył manewr, użyj "disableControls" do blokowania wszystkiego
                        // zmodyfikuj to ponizej do "} } catch", bo teraz używa odliczania w apce na ile jest disable
                        disableControls(true);
                        logMessage("Immersion for " + immersion_time + "sec");
                        logMessage("Controls disabled");
                        logMessage("Immersion process may take a while...");
                        changeImageDuringImmersion();
                        startImmersionTimer();
                        keyboardDisplay.clear();
                        //logMessage("Controls enabled");
                    }
                } catch (NumberFormatException e) {
                    showErrorAlert("Invalid input", "Please enter a valid number of seconds.");
                }
                break;
            case "keyboard_x":
                logMessage("Cancel button pressed");
                keyboardDisplay.clear();
                break;
            default:
                if (label.startsWith("keyboard_")) {
                    String number = label.substring(9);
                    keyboardDisplay.appendText(number);
                }
                break;
        }
    }

    //Changing image/view of the "outside" during immersion
    private void changeImageDuringImmersion() {
        shipImageView.setImage(new Image(getClass().getResource("/images/in_view.png").toExternalForm()));

        Timeline revertImageTimeline = new Timeline(
                new KeyFrame(Duration.seconds(immersion_time), event -> {
                    shipImageView.setImage(new Image(getClass().getResource("/images/out_view.png").toExternalForm()));
                })
        );
        revertImageTimeline.setCycleCount(1);
        revertImageTimeline.play();
    }

    //Disabling controls
    private void disableControls(boolean disable) {
        if (keyboardGrid != null) {
            for (var node : keyboardGrid.getChildren()) {
                node.setDisable(disable);
            }
        }

        keyboardDisplay.setDisable(disable);
        joystickMoveImageView.setDisable(disable);
        arrowUpButton.setDisable(disable);
        arrowDownButton.setDisable(disable);
        confirmMoveButton.setDisable(disable);
        joystickTurnImageView.setDisable(disable);
        arrowLeftButton.setDisable(disable);
        arrowRightButton.setDisable(disable);
        confirmTurnButton.setDisable(disable);
        tankupButton.setDisable(disable);
        drainButton.setDisable(disable);
        depthSlider.setDisable(disable);
    }

    //Apps timer for immersion - TODO do usunięcia po zrobieniu odbierania wiadomości od okrętu o zakończeniu manewru
    private void startImmersionTimer() {
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(immersion_time), event -> {
                    state.setText("State: Emergence");
                    disableControls(false);
                    logMessage("Controls enabled");
                })
        );
        timer.setCycleCount(1);
        timer.play();
    }

    //Error alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


