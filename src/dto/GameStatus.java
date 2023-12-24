/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author U S B
 */
//used to get the any kind of game status like available players but for specific client
public class GameStatus extends GsonHandler{
   
    public String userName;

    public GameStatus(String userName) {
        this.userName = userName;
    }
    
}
