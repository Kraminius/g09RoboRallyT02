package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;

import java.util.Arrays;

/**
 * @author Nicklas Christensen     s224314.dtu.dk
 * This class is used to hold all the information in a game within the server
 */
public class GameState {



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
        Command[] upgradeShopCards;


        //Player Info
        String[] playerNames;
        String[] playerColors;
        int[] playerEnergyCubes;
        int[] playerCheckPoints;
        int[] playersXPosition;
        int[] playersYPosition;
        int[] mapCubePositions;
        Heading[] playerHeadings;
        Command[][] playerProgrammingDeck;
        Command[][] playerCurrentProgram;
        Command[][] playerDiscardPile;
        Command[][] playerUpgradeCards;
        Command[][] playersPulledCards;

        public GameState(int playerAmount, int step, Phase phase, String currentPlayer, String board, boolean isStepmode,
                         Command[] upgradeDiscardDeck, Command[] upgradeOutDeck, Command[] upgradeCardsDeck,
                         String[] playerNames, String[] playerColors, int[] playerEnergyCubes, int[] playerCheckPoints,
                         int[] playersXPosition, int[] playersYPosition, int[] mapCubePositions, Heading[] playerHeadings,
                         Command[][] playerProgrammingDeck, Command[][] playerCurrentProgram, Command[][] playerDiscardPile,
                         Command[][] playerUpgradeCards, Command[][] playersPulledCards){
            this.playerAmount = playerAmount;
            this.step = step;
            this.phase = phase;
            this.currentPlayer = currentPlayer;
            this.board = board;
            this.isStepmode = isStepmode;
            this.upgradeDiscardDeck = upgradeDiscardDeck;
            this.upgradeOutDeck = upgradeOutDeck;
            this.upgradeCardsDeck = upgradeCardsDeck;
            this.playerNames = playerNames;
            this.playerColors = playerColors;
            this.playerEnergyCubes = playerEnergyCubes;
            this.playerCheckPoints = playerCheckPoints;
            this.playersXPosition = playersXPosition;
            this.playersYPosition = playersYPosition;
            this.mapCubePositions = mapCubePositions;
            this.playerHeadings = playerHeadings;
            this.playerProgrammingDeck = playerProgrammingDeck;
            this.playerCurrentProgram = playerCurrentProgram;
            this.playerDiscardPile = playerDiscardPile;
            this.playerUpgradeCards = playerUpgradeCards;
            this.playersPulledCards = playersPulledCards;
        }

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

        public int[] getX() {
            return playersXPosition;
        }

        public void setPlayersXPosition(int[] playersXPosition) {
            this.playersXPosition = playersXPosition;
        }

        public void setSpecificPlayerPosition(int playerNumber, int xPosition, int yPosition){
            playersXPosition[playerNumber] = xPosition;
            playersYPosition[playerNumber] = yPosition;
        }

        public int[] getY() {
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

        public int[] getPlayerEnergyCubes() {
            return playerEnergyCubes;
        }

        public void setPlayerEnergyCubes(int[] playerEnergyCubes) {
            this.playerEnergyCubes = playerEnergyCubes;
        }

        public int[] getPlayersXPosition() {
            return playersXPosition;
        }

        public int[] getPlayersYPosition() {
            return playersYPosition;
        }

        public Command[][] getPlayersPulledCards() {
            return playersPulledCards;
        }

        public void setPlayersPulledCards(Command[][] playersPulledCards) {
            this.playersPulledCards = playersPulledCards;
        }

        public int[] getMapCubePositions() {
            return mapCubePositions;
        }

        public void setMapCubePositions(int[] mapCubePositions) {
            this.mapCubePositions = mapCubePositions;
        }

        public void setPlayerProgramCards(Command[] commands, int player){
            this.playerProgrammingDeck[player] = commands;
        }

    public Command[] getUpgradeShopCards() {
        return upgradeShopCards;
    }

    public void setUpgradeShopCards(Command[] upgradeShopCards) {
        this.upgradeShopCards = upgradeShopCards;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "playerAmount=" + playerAmount +
                ", step=" + step +
                ", phase=" + phase +
                ", currentPlayer='" + currentPlayer + '\'' +
                ", board='" + board + '\'' +
                ", isStepmode=" + isStepmode +
                ", upgradeDiscardDeck=" + Converter.commandArrayToString(upgradeDiscardDeck) +
                ", upgradeOutDeck=" + Converter.commandArrayToString(upgradeOutDeck) +
                ", upgradeCardsDeck=" + Converter.commandArrayToString(upgradeCardsDeck) +
                ", upgradeShopCards=" + Converter.commandArrayToString(upgradeShopCards) +
                ", playerNames=" + Converter.stringArrayToString(playerNames) +
                ", playerColors=" + Converter.stringArrayToString(playerColors) +
                ", playerEnergyCubes=" + Converter.intArrayToString(playerEnergyCubes) +
                ", playerCheckPoints=" + Converter.intArrayToString(playerCheckPoints) +
                ", playersXPosition=" + Converter.intArrayToString(playersXPosition) +
                ", playersYPosition=" + Converter.intArrayToString(playersYPosition) +
                ", mapCubePositions=" + Converter.intArrayToString(mapCubePositions) +
                ", playerHeadings=" + Converter.headingArrayToString(playerHeadings) +
                ", playerProgrammingDeck=" + Converter.commandArrArrToString(playerProgrammingDeck) +
                ", playerCurrentProgram=" + Converter.commandArrArrToString(playerCurrentProgram) +
                ", playerDiscardPile=" + Converter.commandArrArrToString(playerDiscardPile) +
                ", playerUpgradeCards=" + Converter.commandArrArrToString(playerUpgradeCards) +
                ", playersPulledCards=" + Converter.commandArrArrToString(playersPulledCards) + "}";


    }

}
