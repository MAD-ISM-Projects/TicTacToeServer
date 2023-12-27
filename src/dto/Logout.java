/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author mayar
 */
public class Logout extends GsonHandler {
     public String username;
    
    
    public Logout(String username ) {
        this.username = username;
  
    }
}
