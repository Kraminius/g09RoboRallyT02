package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LobbyManager {
    private static LobbyManager instance;
    private static Map<String, GameLobby> lobbies;

    public LobbyManager() {
        lobbies = new HashMap<>();
    }

    public static synchronized LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }

    public static void addPlayer(GameLobby gameLobby, String playerName) {
        if (gameLobby != null) {
            gameLobby.getGameSettings().getPlayerNames().add(playerName);

        } else {
            System.out.println("Lobby not found");
        }
    }

    public void createGame(GameLobby gameLobby) {
        lobbies.put(gameLobby.getLobbyId(), gameLobby);
    }

    public Collection<GameLobby> getLobbies() {
        return lobbies.values();
    }
}
