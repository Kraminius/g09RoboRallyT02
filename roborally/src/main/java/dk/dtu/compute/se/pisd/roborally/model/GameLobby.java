package dk.dtu.compute.se.pisd.roborally.model;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public class GameLobby {
    private String lobbyId;
    private GameSettings gameSettings;


    public GameLobby() {
        // Default constructor
    }

    public GameLobby(String lobbyId, GameSettings gameSettings) {
        this.lobbyId = lobbyId;
        this.gameSettings = gameSettings;
    }




    public void InitializeGameLobby(){


    }


    public String getLobbyId() {
        return lobbyId;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }


    @Override
    public String toString() {
        return "GameLobby{" +
                "lobbyId='" + lobbyId + '\'' +
                ", gameSettings=" + gameSettings +
                '}';
    }
}
