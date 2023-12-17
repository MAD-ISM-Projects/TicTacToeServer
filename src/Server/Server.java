/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.DTOPlayer;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlconnection.db.DBHandler;
import sqlconnection.db.DBHandler;



/**
 *
 * @author Ramez
 */

public class Server extends Thread{

ServerSocket serverSocket;
 public Server()
 {
    try {
        serverSocket = new ServerSocket(5005);
        start();
    } catch (IOException ex) {
        System.out.println("issue in server socket");
        stopServer();
    }

 }
     private volatile boolean running = true;

 public void stopServer() {
        running =false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run(){
        while(running)
       {
            Socket s;
           try {
               s = serverSocket.accept();
               new TicTacToeHandler(s);
           } catch (IOException ex) {
               Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
           }

       }
 
    }

    
}
class TicTacToeHandler extends Thread
{
        DataInputStream dis;
        PrintStream ps;
        static TicTacToeHandler player;
        String jsonObject;
                static Vector<TicTacToeHandler> clientsVector =
        new Vector<>();


        public TicTacToeHandler(Socket cs)
        {

        
            try (
               DataInputStream dis = new DataInputStream(cs.getInputStream());
               PrintStream ps = new PrintStream(cs.getOutputStream())){
                              
                     // TicTacToeHandler currentPlayer=TicTacToeHandler.player;
                       
                           
                        String clientRequestBody = dis.readLine();
                        //ToDo: implement DataAccessLayer Interface then re initialize the statement below
                        //      with an object of the class  
                        DBHandler dbHandler = new DBHandler();
                        JsonObject jsonObject = new Gson().fromJson(clientRequestBody, JsonObject.class); 
                        String clientRequest=jsonObject.get("request").getAsString();
                        DTOPlayer player=new DTOPlayer();
                        TicTacToeHandler.clientsVector.add(this);

                        int result;

                        switch(clientRequest){
                            case ("signUp"):
                                player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString()); 
                                player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                               player.setIp(jsonObject.getAsJsonObject("player").get("ip").getAsString());
                               player.setScore(0);
                              // player.setStatus("online");
                          try {
                              result= dbHandler.signUp(player);
                              sendMessageToAll(result);
                              
                          } catch (SQLException ex) {
                              Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          
                      
                                break;
                             case("signIn"):
                                player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString()); 
                                player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                               // result= dbHandler.signUp(player); 
                                 
                             break;
                                
                        }
              
                
            }
             catch (IOException ex) {
                Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

         void sendMessageToAll(int msg)
         {
        
            for(int i=0 ; i<clientsVector.size() ; i++)
            {
                TicTacToeHandler clientHandler=clientsVector.get(i);
                if(clientHandler!=null)
                clientHandler.ps.println(msg);
            }
         }

}