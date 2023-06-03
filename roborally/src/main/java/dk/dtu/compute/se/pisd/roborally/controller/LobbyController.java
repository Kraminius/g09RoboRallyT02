package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;
import dk.dtu.compute.se.pisd.roborally.view.LobbyWindow;

public class LobbyController {
    private LobbyManager lobbyManager;

    public LobbyController() {
        this.lobbyManager = LobbyManager.getInstance();
    }

    public void openCreateGameScene() {
        GameSettings gameSettings = new GameSettings(); // create a new game settings
        String lobbyID = String.valueOf(lobbyManager.getLobbies().size() + 1); // generate lobby ID based on the number of existing lobbies
        GameLobby gameLobby = new GameLobby(lobbyID, gameSettings); // create a new game lobby
        lobbyManager.createGame(gameLobby); // Pass the GameLobby instance to the createGame method
    }
}
