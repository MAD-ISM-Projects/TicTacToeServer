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
public class Authentication extends GsonHandler{
   
    public String userName;
    public String password;
    public Authentication(String userName,String password) {
        this.userName = userName;
        this.password=password;    
    }
    
}
