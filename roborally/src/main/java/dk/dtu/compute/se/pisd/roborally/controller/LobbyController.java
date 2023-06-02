package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import dk.dtu.compute.se.pisd.roborally.view.Lobby;

public class LobbyController {
    private Lobby lobby;
    private GameSettings gameSettings;

    public LobbyController() {
        gameSettings = new GameSettings();
        lobby = new Lobby(gameSettings);
    }

}