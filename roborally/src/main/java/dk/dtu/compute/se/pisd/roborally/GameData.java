package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.GameSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {


    private String id;

    private boolean[] resetCounter;

    private boolean[] readyList;

    private GameSettings gameSettings;

    private boolean allPlayersConnected;

    private boolean isGameRunning = false;

    private boolean[] allPickedList;

    private boolean[] allPlayerUpgrade;

    private int currentPlayerUpgrade;



    public GameData(int numPlayers){

        gameSettings = new GameSettings();
        readyList = new boolean[numPlayers];

        for (int i = 0; i < readyList.length; i++) {
            readyList[i] = false;
        }

        allPickedList = new boolean[numPlayers];

        for (int i = 0; i < allPickedList.length; i++) {
            allPickedList[i] = false;
        }

        allPlayerUpgrade = new boolean[numPlayers];

        for (int i = 0; i < allPlayerUpgrade.length; i++) {
            allPlayerUpgrade[i] = false;
        }

        resetCounter = new boolean[numPlayers]

        ;for (int i = 0; i < resetCounter.length; i++) {
            resetCounter[i] = false;
        }

    }


    public boolean[] getReadyList() {
        return readyList;
    }

    public void setReadyList(boolean[] readyList) {
        this.readyList = readyList;
    }



    public boolean isAllPlayersConnected() {
        return allPlayersConnected;
    }

    public void setAllPlayersConnected(boolean allPlayersConnected) {
        this.allPlayersConnected = allPlayersConnected;
    }


    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        isGameRunning = gameRunning;
    }


    public boolean[] getAllPickedList() {
        return allPickedList;
    }

    public void setAllPickedList(boolean[] allPickedList) {
        this.allPickedList = allPickedList;
    }


    public boolean[] getAllPlayerUpgrade() {
        return allPlayerUpgrade;
    }

    public void setAllPlayerUpgrade(boolean[] allPlayerUpgrade) {
        this.allPlayerUpgrade = allPlayerUpgrade;
    }

    public boolean[] getResetCounter() {
        return resetCounter;
    }

    public void setResetCounter(boolean[] resetCounter) {
        this.resetCounter = resetCounter;
    }

    public int getCurrentPlayerUpgrade() {
        return currentPlayerUpgrade;
    }

    public void setCurrentPlayerUpgrade(int currentPlayerUpgrade) {
        this.currentPlayerUpgrade = currentPlayerUpgrade;
    }
}
