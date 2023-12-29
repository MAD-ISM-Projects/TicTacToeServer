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
public class NextStep extends GsonHandler {

    private String opponentName;
    private int nextStepIndex;

    public NextStep(String opponentName, int nextStepIndex) {
        this.opponentName = opponentName;
        this.nextStepIndex = nextStepIndex;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public int getNextStepIndex() {
        return nextStepIndex;
    }

    public void setNextStepIndex(int nextStepIndex) {
        this.nextStepIndex = nextStepIndex;
    }

}
