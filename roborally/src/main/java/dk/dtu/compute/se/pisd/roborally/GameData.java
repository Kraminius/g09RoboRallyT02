package dk.dtu.compute.se.pisd.roborally;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {



    String currentGameMap;
    int numPlayers;

    boolean[] readyList;
    private boolean allPlayersConnected;



    public GameData(int numPlayers){

        this.numPlayers = numPlayers;

        readyList = new boolean[numPlayers];

        for (int i = 0; i < readyList.length; i++) {
            readyList[i] = false;
        }

    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public boolean[] getReadyList() {
        return readyList;
    }

    public void setReadyList(boolean[] readyList) {
        this.readyList = readyList;
    }


    public String getCurrentGameMap() {
        return currentGameMap;
    }

    public void setCurrentGameMap(String currentGameMap) {
        this.currentGameMap = currentGameMap;
    }

    public boolean isAllPlayersConnected() {
        return allPlayersConnected;
    }

    public void setAllPlayersConnected(boolean allPlayersConnected) {
        this.allPlayersConnected = allPlayersConnected;
    }
}
