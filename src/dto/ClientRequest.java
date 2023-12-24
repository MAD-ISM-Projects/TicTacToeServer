/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import com.google.gson.Gson;


/**
 *
 * @author U S B
 */

abstract class GsonHandler{
//public abstract ClientRequest fromJson(String data);
//public abstract String toJson();
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public ClientRequest fromJson(String data) {
           Gson gson = new Gson();
        return gson.fromJson(data,ClientRequest.class);
    }
}



public class ClientRequest extends GsonHandler{
    public String request;
    public String data; 


    public ClientRequest(ClientRequestHeader request,String name,String password) {
        this.request = request.toString();
         Authentication auth=new Authentication(name,password);
        data=auth.toJson();
        
    }
    
    public ClientRequest(String playerName,String opponentName,ClientRequestHeader request) {
        this.request = request.toString();
        Invitation inv = new Invitation(playerName,opponentName);
        data= inv.toJson();
    }
    
        public ClientRequest(ClientRequestHeader request,String name) {
        this.request = request.toString();
         GameStatus status=new GameStatus(name);
        data=status.toJson();
    }




}
