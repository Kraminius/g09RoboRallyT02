package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;
import dk.dtu.compute.se.pisd.roborally.view.Lobby;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class GameRepository {



    GameSettings gameSettings;






    public void createGame(String gameName, String creatorName, int numberOfplayers, String boardToPlay){


        gameSettings = new GameSettings();

        gameSettings.setGameName(gameName);
        gameSettings.setCreatorName(creatorName);
        gameSettings.setNumberOfPlayers(numberOfplayers);
        gameSettings.setBoardToPlay(boardToPlay);
        gameSettings.getPlayerNames().add(creatorName);

        String lobbyID = UUID.randomUUID().toString();

        /*GameLobby gameLobby = new GameLobby(lobbyID, gameSettings); // create a new game lobby
        this.gameLobby = gameLobby;
        lobbyManager.createGame(gameLobby);
        addLobbyToLobby(gameLobby, lobbyID);*/


    }



    public void addPlayer(String playerName){

        gameSettings.getPlayerNames().add(playerName);

    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
}
