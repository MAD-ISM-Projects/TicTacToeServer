package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import static com.sun.xml.internal.ws.api.message.Packet.State.ClientRequest;

import dto.Authentication;
import dto.ClientRequest;
import dto.ClientRequestHeader;
import dto.DTOPlayer;
import dto.GameStatus;
import dto.Invitation;
import dto.Logout;
import dto.PlayerBusy;
import dto.NextStep;
import dto.invitationResponseStatus;
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
            serverSocket = new ServerSocket(5001);
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

    synchronized void registerClient(String playerName, TicTacToeHandler handler) {
        clientsMap.put(playerName, handler);
    }

    synchronized void removeClient(String playerName) {
        if (playerName != null) {
            clientsMap.remove(playerName);
        }
    }

    synchronized void sendResponseToClient(String playerName, String result) {
        TicTacToeHandler handler = clientsMap.get(playerName);
        if (handler != null) {

            System.out.println("handler " + handler.toString() + " for " + playerName);
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
     private DTOPlayer playe2;
    String clientRequest;

    public TicTacToeHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        start();
    }

    @Override
    public void run() {
        player = new DTOPlayer(); // Initialize the player object here
        playe2 = new DTOPlayer();
        try {
            this.dis = new DataInputStream(clientSocket.getInputStream());
            this.ps = new PrintStream(clientSocket.getOutputStream());

            DBHandler dbHandler = new DBHandler();
            while (true) {
                String clientRequestBody = dis.readLine();
                if (clientRequestBody != null) {
                    System.out.println(clientRequestBody + " ++++++++++++++++++++++++++");
                }
                ClientRequest clientRequest = new Gson().fromJson(clientRequestBody, ClientRequest.class);

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
                        //ps.println(String.valueOf(result));
                        server.sendResponseToClient(player.getName(), String.valueOf(result));
                        break;
                    case "signIn":
                        Authentication signIn = new Gson().fromJson(clientRequest.data, Authentication.class);
                        player.setName(signIn.userName);
                        player.setPassword(signIn.password);
                        result = dbHandler.signIn(player);
                        server.registerClient(player.getName(), this);
                        server.sendResponseToClient(player.getName(), String.valueOf(result));
                        break;
                    case "onlineUsers":
                        System.out.println("done");
                        GameStatus gameStatus = new Gson().fromJson(clientRequest.data, GameStatus.class);
                        System.out.println("done");
                        player.setName(gameStatus.userName);
                        ArrayList<DTOPlayer> availablePlayersList = dbHandler.getOnlinePlayers(player.getName());
                        Gson gson = new Gson();
                        String json = gson.toJson(availablePlayersList);
                        server.registerClient(player.getName(), this);
                        server.sendResponseToClient(player.getName(), json);

                        break;
                    case "gameInvitation":
                        Invitation inv = new Gson().fromJson(clientRequest.data, Invitation.class);
                        server.sendResponseToClient(inv.getOpponentName(), clientRequest.toJson());
                        System.out.println("game invi p1 from server  " + inv.getPlayerName() + " p2 from server " + inv.getOpponentName());
                        break;
                    case "responseInvitation":
                        Invitation res = new Gson().fromJson(clientRequest.data, Invitation.class);
                        server.sendResponseToClient(res.getPlayerName(), clientRequest.toJson());
                        System.out.println("response p1 from server  " + res.getPlayerName() + " p2 from server " + res.getOpponentName());
                        System.out.println("ReqInvitation================1");
                        break;
                    case "refusedInvitation":
                        Invitation ref = new Gson().fromJson(clientRequest.data, Invitation.class);
                        server.sendResponseToClient(ref.getPlayerName(), clientRequest.toJson());
                        break;
                    case "nextStep":
                        NextStep nextStep = new Gson().fromJson(clientRequest.data, NextStep.class);
                        server.sendResponseToClient(nextStep.getOpponentName(), nextStep.toJson());
                        System.out.println("Next step sent by  " + nextStep.getOpponentName());
                        break;
                    case "signOut":
                        Logout playerStatus = new Gson().fromJson(clientRequest.data, Logout.class);
                        if (playerStatus != null && playerStatus.username != null) {
                            player.setName(playerStatus.username);

                            // player.setStatus("offline");
                            result = dbHandler.signOut(player);
                            server.sendResponseToClient(player.getName(), String.valueOf(result));
                            closeThread();
                            server.removeClient(player.getName());
                        } else {
                            System.out.println("Invalid signOut request: " + clientRequest.data);
                        }
                        break;
                       case "playerBusy":
                        PlayerBusy busy = new Gson().fromJson(clientRequest.data, PlayerBusy.class);
                            player.setName(busy.name);
                            result = dbHandler.makePlayerBusy(player);
                            server.sendResponseToClient(player.getName(), String.valueOf(result));
                 
                            System.out.println("Invalid Update request: " + clientRequest.data);
                        
                        break;
                }
            }

        } catch (SocketException e) {
            // Handle socket closed gracefully
            System.out.println("Socket closed");
            //  Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "Socket closed", e);
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "IO error", ex);
        } finally {
            closeThread();
        }
    }
    //close the thread and release resources

    private void closeThread() {
        try {

            if (dis != null) {
                dis.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, "Error closing resources", ex);
        }
    }

    void sendMessage(String msg) {
        ps.println(msg);
    }
}
