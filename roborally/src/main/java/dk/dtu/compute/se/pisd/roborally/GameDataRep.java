package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GameDataRep {


    GameData gameData;


    public GameDataRep(){

    }

    public void instantiateGameData(int numPlayer){
        this.gameData = new GameData(numPlayer);
    }



    public boolean checkerPlayersConnected(){

        for (int i = 0; i < gameData.getReadyList().length; i++) {

            System.out.println("This player: " + gameData.getReadyList()[i]);

            if(!gameData.getReadyList()[i]){
                return false;
            }
        }

        return true;

    }

    public void createGame(String gameName, String creatorName, int numberOfplayers, String boardToPlay){

        gameData = new GameData(numberOfplayers);

        gameData.setGameSettings(new GameSettings());

        gameData.getGameSettings().setGameName(gameName);
        gameData.getGameSettings().setCreatorName(creatorName);
        gameData.getGameSettings().setNumberOfPlayers(numberOfplayers);
        gameData.getGameSettings().setBoardToPlay(boardToPlay);

        String lobbyID = UUID.randomUUID().toString();
        gameData.setId(lobbyID);

        gameData.setGameRunning(true);

        /*GameLobby gameLobby = new GameLobby(lobbyID, gameSettings); // create a new game lobby
        this.gameLobby = gameLobby;
        lobbyManager.createGame(gameLobby);
        addLobbyToLobby(gameLobby, lobbyID);*/


    }

    public int playerNumber(){

        int i;
        for (i = 0; i < gameData.getReadyList().length; i++) {

            System.out.println("This player: " + gameData.getReadyList()[i]);

            if(!gameData.getReadyList()[i]){
                break;
            }
        }

        return i;
    }


    public GameLobby convertGameInfoToGameLobby() {
        // Get the GameSettings object from the GameInfo object
        GameSettings gameSettings = gameData.getGameSettings();

        // Get the id from the GameInfo object
        String lobbyID = gameData.getId();

        // Use the GameSettings object to create a new GameLobby object
        GameLobby gameLobby = new GameLobby(lobbyID, gameSettings);

        return gameLobby;
    }








}
