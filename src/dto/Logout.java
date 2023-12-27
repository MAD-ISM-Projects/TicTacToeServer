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
public class Logout extends GsonHandler{
    public String username;
    public String status;
    
    public Logout(String username , String status) {
        this.username = username;
        this.status=status;
    }
    
}
