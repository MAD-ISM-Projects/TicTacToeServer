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
        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
    }

 }
    public void run(){
        while(true)
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


        public TicTacToeHandler(Socket cs)
        {
        
            try {
                //get data from client/s
                dis = new DataInputStream(cs.getInputStream());
                //send data to client/s
                ps = new PrintStream(cs.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

         TicTacToeHandler.player=this;
         
         start();
        }
                 @Override
                  public void run(){
                      TicTacToeHandler currentPlayer=TicTacToeHandler.player;
                       try {
                           
                        String clientRequestBody = dis.readLine();
                        //ToDo: implement DataAccessLayer Interface then re initialize the statement below
                        //      with an object of the class  
                        DBHandler dbHandler = new DBHandler();
                        JsonObject jsonObject = new Gson().fromJson(clientRequestBody, JsonObject.class); 
                        String clientRequest=jsonObject.get("request").getAsString();
                        int result=0;
                        DTOPlayer player=new DTOPlayer();
                        switch(clientRequestBody){
                            case ("signUp"):
                                player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString()); 
                                player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                                result= dbHandler.insertPlayer(player);     
                                break;
                             case("signIn"):
                                player.setName(jsonObject.getAsJsonObject("player").get("name").getAsString()); 
                                player.setPassword(jsonObject.getAsJsonObject("player").get("password").getAsString());
                                result= dbHandler.isPlayerSignedUp(player); 
                                 
                             break;
                                
                        }

//                            jsonString="{\"player\":{\"name\":\""+yourNameTextField.getText()+"\""
//                                         + ","
//                                         + "\"password\":\""+passwordTextField.getText()+"\"}}";                         
                        reply(result,currentPlayer);

                   } catch (IOException ex) {
                       Logger.getLogger(TicTacToeHandler.class.getName()).log(Level.SEVERE, null, ex);
                   }
                  }
            
         void reply(int result,TicTacToeHandler specificPlayerHandler)
         {
         // for(TicTacToeHandler ch : playersVector)
          
            
               
                if(specificPlayerHandler!=null){
                    TicTacToeHandler.player.ps.println(result);
                }
            
         }

}