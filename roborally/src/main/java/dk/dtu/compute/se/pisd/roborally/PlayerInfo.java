package dk.dtu.compute.se.pisd.roborally;

public class PlayerInfo {

    private String name;
    private int playerId;

    private String gameId;

    //Needs to differentiate different games
    public PlayerInfo(String name, int playerId) {
        this.name = name;
        this.playerId = playerId;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
