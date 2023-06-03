package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;
import java.util.List;

public class GameLobby {
    private final String lobbyId;
    private final GameSettings gameSettings;
    private List<String> players;

    public GameLobby(String lobbyId, GameSettings gameSettings) {
        this.lobbyId = lobbyId;
        this.gameSettings = gameSettings;
        this.players = new ArrayList<>();
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void addPlayer(String playerName) {
        this.players.add(playerName);
    }
}
