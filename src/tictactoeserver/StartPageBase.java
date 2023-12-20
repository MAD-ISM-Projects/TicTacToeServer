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
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;


public class StartPageBase extends AnchorPane {

    protected final Label label;
    protected final Text tictoeText;
    protected final Text miniText;
    protected final Text gamText;
    protected final Text text;
    protected final ImageView imageView;
    protected final PieChart pieChart;
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
        startStopButton = new Button();
        pieChart = new PieChart();


        setId("AnchorPane");
        setPrefHeight(550.0);
        setPrefWidth(800.0);
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

        pieChart.setLayoutX(393.0);
        pieChart.setLayoutY(62.0);
        pieChart.getData().addAll(
            new PieChart.Data("Online", 10),
            new PieChart.Data("Offline", 10),
            new PieChart.Data("Busy", 10)
        );
            
        traverseSceneGraph(pieChart, Color.WHITE);
        pieChart.setLegendVisible(true);
        pieChart.setVisible(false);
        pieChart.setFocusTraversable(true);
        pieChart.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        pieChart.setOpacity(1.0);
        pieChart.setPickOnBounds(true);
        pieChart.setPrefHeight(347.0);
        pieChart.setPrefWidth(403.0);
        pieChart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pieChart.setRotate(0.0);
        pieChart.setStartAngle(141.0);
        pieChart.setOpaqueInsets(new Insets(0.0));
        setCustomColors();

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

                        pieChart.setVisible(true);

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
                        pieChart.setVisible(false);


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
        getChildren().add(pieChart);
        getChildren().add(startStopButton);

    }

    private void setCenter(PieChart pieChart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private void traverseSceneGraph(PieChart chart, Color color) {
        for (javafx.scene.Node node : chart.lookupAll(".text.chart-pie-label")) {
            if (node instanceof javafx.scene.text.Text) {
                ((javafx.scene.text.Text) node).setFill(color);
            }
        }
    }
    private void setCustomColors() {
        // Get the data slices
        PieChart.Data[] dataSlices = new PieChart.Data[pieChart.getData().size()];
        pieChart.getData().toArray(dataSlices);

        // Set custom colors for each data slice
        for (PieChart.Data dataSlice : dataSlices) {
            String style = "-fx-pie-color: #18317;"; // Default style
            if (dataSlice.getName().equals("Online")) {
                style = "-fx-pie-color: #1577FF;"; // Online color
            } else if (dataSlice.getName().equals("Busy")) {
                style = "-fx-pie-color: #8B91B5;"; // Busy color
            } else if (dataSlice.getName().equals("Offline")) {
                style = "-fx-pie-color: #FF8FDA;"; // Offline color
            }
            dataSlice.getNode().setStyle(style);
            System.out.println("Style applied for " + dataSlice.getName() + ": " + style);
        }
    }

}
