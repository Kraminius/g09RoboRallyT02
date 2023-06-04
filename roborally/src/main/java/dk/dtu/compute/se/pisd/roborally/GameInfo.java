package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import org.springframework.stereotype.Service;

@Service
public class GameInfo {


    private String id;

    private GameSettings gameSettings;

    private boolean gameIsRunning = false;


    public void instaGameInfo(String id, GameSettings gameSettings){

        this.id = id;
        this.gameSettings = gameSettings;
        gameIsRunning = true;
    }

    public boolean isGameRunning(){

        return gameIsRunning;

    }

    public GameLobby convertGameInfoToGameLobby(GameInfo gameInfo) {
        // Get the GameSettings object from the GameInfo object
        GameSettings gameSettings = gameInfo.getGameSettings();

        // Get the id from the GameInfo object
        String lobbyID = gameInfo.getId();

        // Use the GameSettings object to create a new GameLobby object
        GameLobby gameLobby = new GameLobby(lobbyID, gameSettings);

        return gameLobby;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public boolean isGameIsRunning() {
        return gameIsRunning;
    }

    public void setGameIsRunning(boolean gameIsRunning) {
        this.gameIsRunning = gameIsRunning;
    }

    @Override
    public String toString() {
        return "GameInfo{" +
                "id='" + id + '\'' +
                ", gameSettings=" + gameSettings +
                ", gameIsRunning=" + gameIsRunning +
                '}';
    }
}
