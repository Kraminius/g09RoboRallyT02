package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class GameInfo {


    private String id;

    private GameSettings gameSettings;

    private boolean gameIsRunning = false;

    private boolean joinButtonPressed = false;

    private int currentPlayer;

    private ArrayList<Integer> chosenStartPlaces = new ArrayList<>();


    public void instaGameInfo(String id, GameSettings gameSettings){

        this.id = id;
        this.gameSettings = gameSettings;
        gameIsRunning = true;
        currentPlayer = 0;
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

    public void nextPlayer(){

        currentPlayer++;

        if(currentPlayer > gameSettings.getNumberOfPlayers()-1){
            currentPlayer = 0;
        }

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

    public boolean isJoinButtonPressed() {
        return joinButtonPressed;
    }

    public void setJoinButtonPressed(boolean joinButtonPressed) {
        this.joinButtonPressed = joinButtonPressed;
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }



    public void addStartPosition(int pos){
        chosenStartPlaces.add(pos);
        Collections.sort(chosenStartPlaces);
    }

    public ArrayList<Integer> getChosenStartPlaces() {
        return chosenStartPlaces;
    }

    public void setChosenStartPlaces(ArrayList<Integer> chosenStartPlaces) {
        this.chosenStartPlaces = chosenStartPlaces;
    }
}
