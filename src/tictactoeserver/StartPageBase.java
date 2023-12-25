package tictactoeserver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Server;
import sqlconnection.db.DBHandler;

public class StartPageBase extends AnchorPane {

    protected final Label label;
    protected final Text tictoeText;
    protected final Text miniText;
    protected final Text gamText;
    protected final Text text;
    protected final ImageView imageView;
    protected final Button startStopButton;
    protected final PieChart pieChart;
    protected final Label label0;
    protected final Label label1;
    protected final Label label2;
    protected final Label online;
    protected final Label offline;
    protected final Label busy;
    static Server server;
    private DBHandler test;
    private Thread dataUpdateThread;

    public StartPageBase(Stage stage) throws IOException, SQLException {
        test = new DBHandler();
        label = new Label();
        tictoeText = new Text();
        miniText = new Text();
        gamText = new Text();
        text = new Text();
        imageView = new ImageView();
        startStopButton = new Button();
        pieChart = new PieChart();
        label0 = new Label();
        label1 = new Label();
        label2 = new Label();
        online = new Label();
        offline = new Label();
        busy = new Label();

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
        imageView.setLayoutX(456.0);
        imageView.setLayoutY(72.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

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

                online.setVisible(true);
                offline.setVisible(true);
                busy.setVisible(true);
                pieChart.setVisible(true);

                dataUpdateThread = new Thread(() -> {
                    try {
                        server = new Server();
                        while ("Stop".equals(startStopButton.getText())) {
                            fetchDataAndUpdateLabels(pieChart);
                            TimeUnit.MICROSECONDS.sleep(500);
                        }
                    } catch (InterruptedException e) {
                        // Handle interruption
                        System.out.println("Exiting loop");
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception appropriately
                        Platform.runLater(() -> startStopButton.setText("Start"));
                    }
                });
                dataUpdateThread.start();
            } else {
                startStopButton.setText("Start");
                pieChart.setVisible(false);
                online.setText("0");
                offline.setText("0");
                busy.setText("0");
                online.setVisible(false);
                offline.setVisible(false);
                busy.setVisible(false);

                if (dataUpdateThread != null && dataUpdateThread.isAlive()) {
                    dataUpdateThread.interrupt();
                }

                Platform.runLater(() -> {
                    if (server != null) {
                        server.stopServer();
                    }
                    fetchDataAndUpdateLabels(pieChart);
                    updateAllPlayersStatusOffline();
                });
            }
        });

        pieChart.setLayoutX(435.0);
        pieChart.setLayoutY(62.0);
        pieChart.setPrefHeight(258.0);
        pieChart.setPrefWidth(366.0);

        label0.setLayoutX(480.0);
        label0.setLayoutY(334.0);
        label0.setText("Online");
        label0.setTextFill(javafx.scene.paint.Color.WHITE);

        label1.setLayoutX(480.0);
        label1.setLayoutY(360.0);
        label1.setText("Offline");
        label1.setTextFill(javafx.scene.paint.Color.valueOf("#f518ec"));

        label2.setLayoutX(487.0);
        label2.setLayoutY(392.0);
        label2.setText("Basy");
        label2.setTextFill(javafx.scene.paint.Color.valueOf("#1a0538"));

        online.setLayoutX(551.0);
        online.setLayoutY(334.0);
        online.setText("0");
        online.setTextFill(javafx.scene.paint.Color.WHITE);
        online.setVisible(false);

        offline.setLayoutX(551.0);
        offline.setLayoutY(360.0);
        offline.setText("0");
        offline.setTextFill(javafx.scene.paint.Color.WHITE);
        offline.setVisible(false);
        busy.setLayoutX(551.0);
        busy.setLayoutY(392.0);
        busy.setText("0");
        busy.setTextFill(javafx.scene.paint.Color.WHITE);
        busy.setVisible(false);
        initPieChart(pieChart);
        initLabels();
        getChildren().add(label);
        getChildren().add(tictoeText);
        getChildren().add(miniText);
        getChildren().add(gamText);
        getChildren().add(text);
        getChildren().add(imageView);
        getChildren().add(startStopButton);
        getChildren().add(pieChart);
        getChildren().add(label0);
        getChildren().add(label1);
        getChildren().add(label2);
        getChildren().add(online);
        getChildren().add(offline);
        getChildren().add(busy);

    }

    private void initPieChart(PieChart pieChart) {
        pieChart.setLayoutX(435.0);
        pieChart.setLayoutY(62.0);
        pieChart.setPrefHeight(258.0);
        pieChart.setPrefWidth(366.0);
        int onlineValue = Integer.parseInt(online.getText());
        int offlineValue = Integer.parseInt(offline.getText());
        int busyValue = Integer.parseInt(busy.getText());

        pieChart.getData().addAll(
                new PieChart.Data("Online", onlineValue),
                new PieChart.Data("Offline", offlineValue),
                new PieChart.Data("Busy", busyValue)
        );
        traverseSceneGraph(pieChart, Color.WHITE);
        setCustomColors();
    }

    private void initLabels() {
        label0.setLayoutX(480.0);
        label0.setLayoutY(334.0);
        label0.setText("Online");
        label0.setTextFill(javafx.scene.paint.Color.WHITE);

        label1.setLayoutX(480.0);
        label1.setLayoutY(360.0);
        label1.setText("Offline");
        label1.setTextFill(javafx.scene.paint.Color.valueOf("#f518ec"));

        label2.setLayoutX(487.0);
        label2.setLayoutY(392.0);
        label2.setText("Busy");
        label2.setTextFill(javafx.scene.paint.Color.valueOf("#1a0538"));

        online.setLayoutX(551.0);
        online.setLayoutY(334.0);
        online.setText("0");
        online.setTextFill(javafx.scene.paint.Color.WHITE);

        offline.setLayoutX(551.0);
        offline.setLayoutY(360.0);
        offline.setText("0");
        offline.setTextFill(javafx.scene.paint.Color.WHITE);

        busy.setLayoutX(551.0);
        busy.setLayoutY(392.0);
        busy.setText("0");
        busy.setTextFill(javafx.scene.paint.Color.WHITE);
    }

    private void fetchDataAndUpdateLabels(PieChart pieChart) {
        if ("Stop".equals(startStopButton.getText())) {
            try {
                // Fetch online, offline, busy values from the database
                int onlineValue = test.getOnlineRate();
                int offlineValue = test.getOfflineRate();
                int busyValue = test.getbusyeRate();

                // Update the labels and pie chart on the JavaFX Application Thread
                Platform.runLater(() -> {
                    online.setText(String.valueOf(onlineValue));
                    offline.setText(String.valueOf(offlineValue));
                    busy.setText(String.valueOf(busyValue));

                    updatePieChart(onlineValue, offlineValue, busyValue);

                });

            } catch (SQLException ex) {
                Logger.getLogger(StartPageBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updatePieChart(int onlineValue, int offlineValue, int busyValue) {
        ObservableList<PieChart.Data> pieChartData = pieChart.getData();
        pieChartData.get(0).setPieValue(onlineValue);
        pieChartData.get(1).setPieValue(offlineValue);
        pieChartData.get(2).setPieValue(busyValue);
        setCustomColors();
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
            // System.out.println("Style applied for " + dataSlice.getName() + ": " + style);
        }
    }

    private void traverseSceneGraph(PieChart chart, Color color) {
        for (Node node : chart.lookupAll(".text.chart-pie-label")) {
            if (node instanceof Text) {
                ((Text) node).setFill(color);
            }
        }
    }

    private void updateAllPlayersStatusOffline() {
        try {
            DBHandler dbHandler = new DBHandler();
            dbHandler.updateAllPlayersStatusOffline();
        } catch (SQLException e) {
            // Handle SQLException appropriately (print or log the exception)
            e.printStackTrace();
        }
    }
}
