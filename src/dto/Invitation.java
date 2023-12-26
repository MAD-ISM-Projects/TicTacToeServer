/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author NEW LAP
 */
public class Invitation extends GsonHandler{
    String playerName;
    String opponentName;
    String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Invitation(String playerName, String opponentName, invitationResponseStatus response) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        this.response = response.toString();
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }
    
    
}
