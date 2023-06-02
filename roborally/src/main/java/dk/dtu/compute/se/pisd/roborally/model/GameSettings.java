package dk.dtu.compute.se.pisd.roborally.model;

public class GameSettings {
    private String gameName;
    private String creatorName;
    private int numberOfPlayers;
    private String boardToPlay;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getBoardToPlay() {
        return boardToPlay;
    }

    public void setBoardToPlay(String boardToPlay) {
        this.boardToPlay = boardToPlay;
    }
}