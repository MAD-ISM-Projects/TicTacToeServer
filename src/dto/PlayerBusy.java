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

public class PlayerBusy extends GsonHandler{
    public String name;
    public String status;

    public PlayerBusy(String name,  String status) {
        this.name = name;
        this.status =name; 
        
    }
}