package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import static com.sun.xml.internal.ws.api.message.Packet.State.ClientRequest;
import dto.Authentication;
import dto.ClientRequest;
import dto.DTOPlayer;
import dto.GameStatus;
import sqlconnection.db.DBHandler;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
                if (!running) {
                    // If the server is not running, break out of the loop
                    break;
                }
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }



    void registerClient(String playerName, TicTacToeHandler handler) {
        clientsMap.put(playerName, handler);
    }

   void removeClient(String playerName) {
         if (playerName != null) {
             clientsMap.remove(playerName);
         }
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
    String clientRequest;

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
            ClientRequest clientRequest = new Gson().fromJson(clientRequestBody, ClientRequest.class);

//            JsonObject jsonObject = new Gson().fromJson(clientRequestBody, JsonObject.class);
//            clientRequest = jsonObject.get("request").getAsString();
            int result;

            switch (clientRequest.request) {
                case "signUp":
                    //player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString());
                    //player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                    Authentication signUp = new Gson().fromJson(clientRequest.data, Authentication.class);                 
                    player.setName(signUp.userName);
                    player.setPassword(signUp.password);
                    player.setScore(0);
                    result = dbHandler.signUp(player);
                    server.registerClient(player.getName(), this);
                    server.sendResponseToClient(player.getName(), String.valueOf(result));
                    break;
                
              case "signIn":
                    Authentication signIn = new Gson().fromJson(clientRequest.data, Authentication.class);                 
                    player.setName(signIn.userName);
                    player.setPassword(signIn.password);
                    result = dbHandler.signIn(player);
                    server.registerClient(player.getName(), this);
                    server.sendResponseToClient(player.getName(),  String.valueOf(result));
                    break;
                case "onlineUsers":
                    GameStatus gameStatus = new Gson().fromJson(clientRequest.data, GameStatus.class);
                    player.setName(gameStatus.userName);
                    ArrayList<DTOPlayer> availablePlayersList = dbHandler.getOnlinePlayers();
                    System.out.println(availablePlayersList);
                    Gson gson = new Gson();
                    String json = gson.toJson(availablePlayersList);
                    server.registerClient(player.getName(), this);
                    server.sendResponseToClient(player.getName(),json);
            }

        } catch (SocketException e) {
            // Handle socket closed gracefully
           // Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "Socket closed", e);
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "IO error", ex);
        } finally {
            // Clean up resources and remove client from the server's map
            if(clientRequest=="SignUp")server.removeClient(player.getName());
            try {
                if (dis != null) dis.close();
                if (ps != null) ps.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "Error closing resources", ex);
            }
        }
    }

    void sendMessage(String msg) {
        ps.println(msg);
    }
}


