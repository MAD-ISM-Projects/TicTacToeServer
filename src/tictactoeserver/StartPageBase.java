package tictactoeserver;

import java.net.ServerSocket;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import server.Server;
import javafx.application.Platform;


public class StartPageBase extends AnchorPane {

    protected final Label label;
    protected final Text tictoeText;
    protected final Text miniText;
    protected final Text gamText;
    protected final Text text;
    protected final ImageView imageView;
    protected final ImageView xoImage;
    protected final Button startStopButton;
    //ServerSocket serverSocket;
      static Server server;

    public StartPageBase() {

        label = new Label();
        tictoeText = new Text();
        miniText = new Text();
        gamText = new Text();
        text = new Text();
        imageView = new ImageView();
        xoImage = new ImageView();
        startStopButton = new Button();

        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(850.0);
        setStyle("-fx-background-color: #34365C;");

        label.setLayoutX(126);
        label.setLayoutY(120);
        label.setMinHeight(16);
        label.setMinWidth(69);

        tictoeText.setFill(javafx.scene.paint.Color.valueOf("#8b91b5"));
        tictoeText.setLayoutX(24.0);
        tictoeText.setLayoutY(181.0);
        tictoeText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        tictoeText.setStrokeWidth(0.0);
        tictoeText.setText("Tic       Toe");
        tictoeText.setWrappingWidth(324.765625);
        tictoeText.setFont(new Font("Times New Roman Bold Italic", 70.0));

        miniText.setFill(javafx.scene.paint.Color.valueOf("#8b91b5"));
        miniText.setLayoutX(37.0);
        miniText.setLayoutY(279.0);
        miniText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        miniText.setStrokeWidth(0.0);
        miniText.setText("Server");
        miniText.setWrappingWidth(121.9999908208847);
        miniText.setFont(new Font("Times New Roman Italic", 45.0));

        gamText.setFill(javafx.scene.paint.Color.valueOf("#8b91b5"));
        gamText.setLayoutX(161.0);
        gamText.setLayoutY(282.0);
        gamText.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        gamText.setStrokeWidth(0.0);
        gamText.setText("Luncher");
        gamText.setWrappingWidth(199.94921875);
        gamText.setFont(new Font("Times New Roman Bold Italic", 55.0));

        text.setFill(javafx.scene.paint.Color.valueOf("#ff8fda"));
        text.setLayoutX(126.0);
        text.setLayoutY(181.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.setText("Tac");
        text.setFont(new Font("Times New Roman Bold Italic", 70.0));

        imageView.setFitHeight(248.0);
        imageView.setFitWidth(345.0);
        imageView.setLayoutX(445.0);
        imageView.setLayoutY(154.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        xoImage.setFitHeight(318.0);
        xoImage.setFitWidth(316.0);
        xoImage.setLayoutX(437.0);
        xoImage.setLayoutY(118.0);
        xoImage.setImage(new Image(getClass().getResource("/tictactoeserver/xoImg.png").toExternalForm()));

        startStopButton.setLayoutX(98.0);
        startStopButton.setLayoutY(334.0);
        startStopButton.setPrefHeight(51.0);
        startStopButton.setPrefWidth(145.0);
        startStopButton.setStyle("-fx-background-radius: 15;");
        startStopButton.setText("Start");
        startStopButton.setTextFill(javafx.scene.paint.Color.valueOf("#aea5b8"));
        startStopButton.setFont(new Font(24.0));
        startStopButton.addEventHandler(ActionEvent.ACTION, (event) -> {
            if ("Start".equals(startStopButton.getText())) {
                startStopButton.setText("Stop");

                // Run server creation on a separate thread
                new Thread(() -> {
                    try {
                        server = new Server();
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception appropriately
                        Platform.runLater(() -> startStopButton.setText("Start"));
                    }
                }).start();
            } else {
                startStopButton.setText("Start");

                // Run server stopping on the JavaFX Application Thread
                Platform.runLater(() -> {
                    if (server != null) {
                        server.stopServer();
                    }
                });
            }
        });


        
        getChildren().add(label);
        getChildren().add(tictoeText);
        getChildren().add(miniText);
        getChildren().add(gamText);
        getChildren().add(text);
        getChildren().add(imageView);
        getChildren().add(xoImage);
        getChildren().add(startStopButton);

    }
}
