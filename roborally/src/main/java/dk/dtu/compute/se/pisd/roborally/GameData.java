package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.GameSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {


    private String id;

    private boolean[] readyList;

    private GameSettings gameSettings;

    private boolean allPlayersConnected;

    private boolean isGameRunning = false;



    public GameData(int numPlayers){

        gameSettings = new GameSettings();
        readyList = new boolean[numPlayers];

        for (int i = 0; i < readyList.length; i++) {
            readyList[i] = false;
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
}
