package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;
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

    public static void addPlayers(GameLobby gameLobby, ArrayList<String> playerNames) {
        if (gameLobby != null) {
/*
            for (int i = 0; i < gameLobby.getGameSettings().getPlayerNames().size(); i++) {
                gameLobby.getGameSettings().getPlayerNames().add(playerNames.get(i));
            }
            */


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
