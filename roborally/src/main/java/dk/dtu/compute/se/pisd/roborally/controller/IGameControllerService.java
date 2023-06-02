package dk.dtu.compute.se.pisd.roborally.controller;

import org.json.simple.JSONObject;

public interface IGameControllerService {

    public static JSONObject getGame() {
        return null;
    }

    static void setGame(GameController game){
        GameControllerService.setGame(game);
    }
}
