package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;

public class Load {

    //Current State
    private int playerAmount;
    private int step;
    private Phase phase;
    private String currentPlayer;
    private String board;
    private boolean isStepmode;

    //Upgrade Card Info
    Command[] upgradeDiscardDeck;
    Command[] upgradeOutDeck;
    Command[] upgradeCardsDeck;


    //Player Info
    String[] playerNames;
    String[] playerColors;
    int[] playerCheckPoints;
    int[] playersXPosition;
    int[] playersYPosition;
    Heading[] playerHeadings;
    Command[][] playerProgrammingDeck;
    Command[][] playerCurrentProgram;
    Command[][] playerDiscardPile;
    Command[][] playerUpgradeCards;

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public boolean isStepmode() {
        return isStepmode;
    }

    public void setStepmode(boolean stepmode) {
        isStepmode = stepmode;
    }

    public Command[] getUpgradeDiscardDeck() {
        return upgradeDiscardDeck;
    }

    public void setUpgradeDiscardDeck(Command[] upgradeDiscardDeck) {
        this.upgradeDiscardDeck = upgradeDiscardDeck;
    }

    public Command[] getUpgradeOutDeck() {
        return upgradeOutDeck;
    }

    public void setUpgradeOutDeck(Command[] upgradeOutDeck) {
        this.upgradeOutDeck = upgradeOutDeck;
    }

    public Command[] getUpgradeCardsDeck() {
        return upgradeCardsDeck;
    }

    public void setUpgradeCardsDeck(Command[] upgradeCardsDeck) {
        this.upgradeCardsDeck = upgradeCardsDeck;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public String[] getPlayerColors() {
        return playerColors;
    }

    public void setPlayerColors(String[] playerColors) {
        this.playerColors = playerColors;
    }

    public int[] getPlayersXPosition() {
        return playersXPosition;
    }

    public void setPlayersXPosition(int[] playersXPosition) {
        this.playersXPosition = playersXPosition;
    }

    public int[] getPlayersYPosition() {
        return playersYPosition;
    }

    public void setPlayersYPosition(int[] playersYPosition) {
        this.playersYPosition = playersYPosition;
    }

    public Heading[] getPlayerHeadings() {
        return playerHeadings;
    }

    public void setPlayerHeadings(Heading[] playerHeadings) {
        this.playerHeadings = playerHeadings;
    }

    public int[] getPlayerCheckPoints(){
        return this.playerCheckPoints;
    }

    public void setPlayerCheckPoints(int[] playerCheckPoints) {
        this.playerCheckPoints = playerCheckPoints;
    }

    public Command[][] getPlayerProgrammingDeck() {
        return playerProgrammingDeck;
    }

    public void setPlayerProgrammingDeck(Command[][] playerProgrammingDeck) {
        this.playerProgrammingDeck = playerProgrammingDeck;
    }

    public Command[][] getPlayerCurrentProgram() {
        return playerCurrentProgram;
    }

    public void setPlayerCurrentProgram(Command[][] playerCurrentProgram) {
        this.playerCurrentProgram = playerCurrentProgram;
    }

    public Command[][] getPlayerDiscardPile() {
        return playerDiscardPile;
    }

    public void setPlayerDiscardPile(Command[][] playerDiscardPile) {
        this.playerDiscardPile = playerDiscardPile;
    }

    public Command[][] getPlayerUpgradeCards() {
        return playerUpgradeCards;
    }

    public void setPlayerUpgradeCards(Command[][] playerUpgradeCards) {
        this.playerUpgradeCards = playerUpgradeCards;
    }
}
