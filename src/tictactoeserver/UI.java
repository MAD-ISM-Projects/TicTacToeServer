package tictactoeserver;

import java.net.ServerSocket;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class UI extends BorderPane {

    protected final Pane pane;
    protected final Button startServerButton;
    protected final Button stopServerButton;
    ServerSocket serverSocket;


    public UI() {

        pane = new Pane();
        startServerButton = new Button();
        stopServerButton = new Button();

        setId("pane");
        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(320.0);
        getStylesheets().add("/tictactoeserver/style.css");

        BorderPane.setAlignment(pane, javafx.geometry.Pos.CENTER);
        pane.setPrefHeight(200.0);
        pane.setPrefWidth(200.0);

        startServerButton.setLayoutX(94.0);
        startServerButton.setLayoutY(168.0);
        startServerButton.setMnemonicParsing(false);
        startServerButton.setPrefHeight(32.0);
        startServerButton.setPrefWidth(131.0);
        startServerButton.setText("Start Server");

        stopServerButton.setLayoutX(96.0);
        stopServerButton.setLayoutY(213.0);
        stopServerButton.setMnemonicParsing(false);
        stopServerButton.setPrefHeight(32.0);
        stopServerButton.setPrefWidth(130.0);
        stopServerButton.setText("Stop Server");
        setCenter(pane);

        pane.getChildren().add(startServerButton);
        pane.getChildren().add(stopServerButton);

    }
}
