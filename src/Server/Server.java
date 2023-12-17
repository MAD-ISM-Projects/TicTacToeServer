package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.DTOPlayer;
import sqlconnection.db.DBHandler;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    ServerSocket serverSocket;
    private volatile boolean running = true;
    static Vector<TicTacToeHandler> clientsVector = new Vector<>();

    public Server() {
        try {
            serverSocket = new ServerSocket(5005);
            start();
        } catch (IOException ex) {
            System.out.println("Issue in server socket");
            stopServer();
        }
    }

    public void stopServer() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new TicTacToeHandler(clientSocket);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class TicTacToeHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    static Vector<TicTacToeHandler> clientsVector = new Vector<>();
    private Socket clientSocket;

    public TicTacToeHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.dis = new DataInputStream(clientSocket.getInputStream());
            this.ps = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        clientsVector.add(this);
        start();
    }

    public void run() {
        try {
            String clientRequestBody = dis.readLine();
            DBHandler dbHandler = new DBHandler();
            JsonObject jsonObject = new Gson().fromJson(clientRequestBody, JsonObject.class);
            String clientRequest = jsonObject.get("request").getAsString();
            DTOPlayer player = new DTOPlayer();

            int result;

            switch (clientRequest) {
                case "signUp":
                    player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString());
                    player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                    player.setIp(jsonObject.getAsJsonObject("player").get("ip").getAsString());
                    player.setScore(0);

                    try {
                        result = dbHandler.signUp(player);
                        sendMessageToAll(result);
                    } catch (SQLException ex) {
                        Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;
                case "signIn":
                    player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString());
                    player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                    // result= dbHandler.signUp(player); 
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendMessageToAll(int msg) {
        for (TicTacToeHandler clientHandler : clientsVector) {
            if (clientHandler != null)
                clientHandler.ps.println(msg);
        }
    }
}
