package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.Command;

public class UpgradeCardsShopRequest {

    private Command[] cards;
    private int nextPlayer;

    private int currentPlayer;

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Command[] getCards() {
        return cards;
    }

    public void setCards(Command[] cards) {
        this.cards = cards;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }
}
