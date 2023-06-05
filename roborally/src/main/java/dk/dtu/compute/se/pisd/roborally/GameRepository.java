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










    public void addPlayer(String playerName){

        gameSettings.getPlayerNames().add(playerName);

    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }
}
