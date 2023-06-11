package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.Command;

public class SendCurrentCards {

    private int playerNumber;
    private Command[] pickedCards;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public Command[] getPickedCards() {
        return pickedCards;
    }

    public void setPickedCards(Command[] pickedCards) {
        this.pickedCards = pickedCards;
    }
}
