package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;
import org.springframework.stereotype.Service;

@Service
public class LobbyRep {

    LobbyManager lobbyManager;
    GameSettings gameSettings;

    public void instaGameLobby(){
        this.lobbyManager = new LobbyManager();
        this.gameSettings = new GameSettings();
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public void setLobbyManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }
}
