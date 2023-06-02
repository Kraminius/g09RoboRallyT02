package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameSave;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GameControllerService implements IGameControllerService {
    public static GameController gameController;

    public GameControllerService(){
        Board board = new Board();
        GameController gameController1 = new GameController(board);
        this.gameController = gameController1;
    }

    //@Override
    public static JSONObject getGame()
    {

        GameSave gameSave = new GameSave();
        gameSave.saveGame(gameController, "RestfullMessage");
        JSONHandler handler = new JSONHandler();
        JSONObject obj = handler.load("RestfullMessage", "game");

        return obj;
    }

    //@Override
    public static void setGame(GameController game){
        gameController = game;
    }


}

