/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import com.google.gson.Gson;

/**
 *
 * @author Ramez
 */
public class DTOPlayer {
//    private String ip;
    private String name;
    private String password;
    private int score;
    private String status;
    public DTOPlayer(){}
        public DTOPlayer( String name, String password, int score, String status) {
//        this.ip = ip;
        this.name = name;
        this.password = password;
        this.score = score;
        this.status = status;
    }

//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
        
    }



 

   
}
