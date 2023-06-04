package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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


    }

    public void addPlayer(String playerName){

        gameSettings.getPlayerNames().add(playerName);

    }


}
