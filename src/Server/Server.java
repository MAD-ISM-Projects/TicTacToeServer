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
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    private ServerSocket serverSocket;
    private volatile boolean running = true;
    private ConcurrentHashMap<String, TicTacToeHandler> clientsMap = new ConcurrentHashMap<>();

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
                new TicTacToeHandler(clientSocket, this);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    void registerClient(String playerName, TicTacToeHandler handler) {
        clientsMap.put(playerName, handler);
    }

    void removeClient(String playerName) {
        clientsMap.remove(playerName);
    }

    void sendResponseToClient(String playerName, String result) {
        TicTacToeHandler handler = clientsMap.get(playerName);
        if (handler != null) {
            handler.sendMessage(result);
        }
    }
}

class TicTacToeHandler extends Thread {

    private DataInputStream dis;
    private PrintStream ps;
    private Socket clientSocket;
    private Server server;
    private DTOPlayer player; // Declare the player variable here

    public TicTacToeHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        start();
    }

    public void run() {
        player = new DTOPlayer(); // Initialize the player object here
        try {
            this.dis = new DataInputStream(clientSocket.getInputStream());
            this.ps = new PrintStream(clientSocket.getOutputStream());
            String clientRequestBody = dis.readLine();
            DBHandler dbHandler = new DBHandler();
            JsonObject jsonObject = new Gson().fromJson(clientRequestBody, JsonObject.class);
            String clientRequest = jsonObject.get("request").getAsString();

            int result;

            switch (clientRequest) {
                case "signUp":
                    player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString());
                    player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                    player.setScore(0);
                    result = dbHandler.signUp(player);
                    server.registerClient(player.getName(), this);
                    server.sendResponseToClient(player.getName(), String.valueOf(result));
                    break;
                case "signIn":
                    player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString());
                    player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                    result = dbHandler.signIn(player);
                    server.registerClient(player.getName(), this);
                    server.sendResponseToClient(player.getName(),  String.valueOf(result));
                    break;
            }

        } catch (IOException ex) {
            Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Clean up resources and remove client from the server's map
            server.removeClient(player.getName());
        }
    }

    void sendMessage(String msg) {
        ps.println(msg);
    }
}

