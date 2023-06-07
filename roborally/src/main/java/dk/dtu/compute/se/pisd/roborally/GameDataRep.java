package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameDataRep {


    GameData gameData;

    GameState gameState;


    public GameDataRep(){

    }

    public void instantiateGameData(int numPlayer){
        this.gameData = new GameData(numPlayer);
    }



    public boolean checkerPlayersConnected(){

        for (int i = 0; i < gameData.getReadyList().length; i++) {

            System.out.println("This player: " + gameData.getReadyList()[i]);

            if(!gameData.getReadyList()[i]){
                return false;
            }
        }

        return true;

    }

    public void createGame(String gameName, String creatorName, int numberOfplayers, String boardToPlay){

        gameData = new GameData(numberOfplayers);

        gameData.setGameSettings(new GameSettings());

        gameData.getGameSettings().setGameName(gameName);
        gameData.getGameSettings().setCreatorName(creatorName);
        gameData.getGameSettings().setNumberOfPlayers(numberOfplayers);
        gameData.getGameSettings().setBoardToPlay(boardToPlay);

        String lobbyID = UUID.randomUUID().toString();
        gameData.setId(lobbyID);

        gameData.setGameRunning(true);

        /*GameLobby gameLobby = new GameLobby(lobbyID, gameSettings); // create a new game lobby
        this.gameLobby = gameLobby;
        lobbyManager.createGame(gameLobby);
        addLobbyToLobby(gameLobby, lobbyID);*/


    }

    public int playerNumber(){

        int i;
        for (i = 0; i < gameData.getReadyList().length; i++) {

            System.out.println("This player: " + gameData.getReadyList()[i]);

            if(!gameData.getReadyList()[i]){
                break;
            }
        }

        return i;
    }


    public GameLobby convertGameInfoToGameLobby() {
        // Get the GameSettings object from the GameInfo object
        GameSettings gameSettings = gameData.getGameSettings();

        // Get the id from the GameInfo object
        String lobbyID = gameData.getId();

        // Use the GameSettings object to create a new GameLobby object
        GameLobby gameLobby = new GameLobby(lobbyID, gameSettings);

        return gameLobby;
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method goes through the different parts of gameState and sets
     * them into a Load.
     * @param gameState1 the gameState we want to create a Load version of
     * @return a Load version
     */
    private static Load makeGameLoad(GameState gameState1) {

        int playerAmount = gameState1.getPlayerAmount();
        int step = gameState1.getStep();
        Phase phase = gameState1.getPhase();
        String currentPlayer = gameState1.getCurrentPlayer();
        String board = gameState1.getBoard();
        boolean isStepmode = gameState1.isStepmode();
        Command[] upgradeDiscardDeck = gameState1.getUpgradeDiscardDeck();
        Command[] upgradeOutDeck = gameState1.getUpgradeOutDeck();
        Command[] upgradeCardsDeck = gameState1.getUpgradeCardsDeck();
        String[] playerNames = gameState1.getPlayerNames();
        String[] playerColors = gameState1.getPlayerColors();
        int[] playerEnergyCubes = gameState1.getPlayerEnergyCubes();
        int[] playerCheckPoints = gameState1.getPlayerCheckPoints();
        int[] playersXPosition = gameState1.getPlayersXPosition();
        int[] playersYPosition = gameState1.getPlayersYPosition();
        int[] mapCubePositions = gameState1.getMapCubePositions();
        Heading[] playerHeadings = gameState1.getPlayerHeadings();
        Command[][] CplayerProgrammingDeck = gameState1.getPlayerProgrammingDeck();
        Command[][] CplayerCurrentProgram = gameState1.getPlayerCurrentProgram();
        Command[][] CplayerDiscardPile = gameState1.getPlayerDiscardPile();
        Command[][] CplayerUpgradeCards = gameState1.getPlayerUpgradeCards();
        Command[][] CplayersPulledCards = gameState1.getPlayersPulledCards();

        Load load = new Load();
        load.setPlayerAmount(playerAmount);
        load.setStep(step);
        load.setPhase(phase);
        load.setCurrentPlayer(currentPlayer);
        load.setBoard(board);
        load.setStepmode(isStepmode);
        load.setUpgradeDiscardDeck(upgradeDiscardDeck);
        load.setUpgradeOutDeck(upgradeOutDeck);
        load.setUpgradeCardsDeck(upgradeCardsDeck);
        load.setPlayerNames(playerNames);
        load.setPlayerColors(playerColors);
        load.setPlayerEnergyCubes(playerEnergyCubes);
        load.setPlayerCheckPoints(playerCheckPoints);
        load.setPlayersXPosition(playersXPosition);
        load.setPlayersYPosition(playersYPosition);
        load.setMapCubePositions(mapCubePositions);
        load.setPlayerHeadings(playerHeadings);
        load.setPlayerProgrammingDeck(CplayerProgrammingDeck);
        load.setPlayerCurrentProgram(CplayerCurrentProgram);
        load.setPlayerDiscardPile(CplayerDiscardPile);
        load.setPlayerUpgradeCards(CplayerUpgradeCards);
        load.setPlayersPulledCards(CplayersPulledCards);
        return load;
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk and Tobias Gørlyk - s224271@dtu.dk
     * @param load - A Load that has info of a new game.
     * Creates a GameState.java object that can hold all the data from a given load.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    private static GameState makeGameState(Load load){

        int playerAmount = load.getPlayerAmount();
        int step = load.getStep();
        Phase phase = load.getPhase();
        String currentPlayer = load.getCurrentPlayer();
        String board = load.getBoard();
        boolean isStepmode = load.isStepmode();
        Command[] upgradeDiscardDeck = load.getUpgradeDiscardDeck();
        Command[] upgradeOutDeck = load.getUpgradeOutDeck();
        Command[] upgradeCardsDeck = load.getUpgradeCardsDeck();
        String[] playerNames = load.getPlayerNames();
        String[] playerColors = load.getPlayerColors();
        int[] playerEnergyCubes = load.getPlayerEnergyCubes();
        int[] playerCheckPoints = load.getPlayerCheckPoints();
        int[] playersXPosition = load.getPlayersXPosition();
        int[] playersYPosition = load.getPlayersYPosition();
        int[] mapCubePositions = load.getMapCubePositions();
        Heading[] playerHeadings = load.getPlayerHeadings();
        Command[][] CplayerProgrammingDeck = load.getPlayerProgrammingDeck();
        Command[][] CplayerCurrentProgram = load.getPlayerCurrentProgram();
        Command[][] CplayerDiscardPile = load.getPlayerDiscardPile();
        Command[][] CplayerUpgradeCards = load.getPlayerUpgradeCards();
        Command[][] CplayersPulledCards = load.getPlayersPulledCards();

        GameState gameState = new GameState( playerAmount, step, phase, currentPlayer, board, isStepmode,
                upgradeDiscardDeck, upgradeOutDeck, upgradeCardsDeck,
                playerNames, playerColors, playerEnergyCubes, playerCheckPoints,
                playersXPosition, playersYPosition, mapCubePositions, playerHeadings,
                CplayerProgrammingDeck, CplayerCurrentProgram, CplayerDiscardPile,
                CplayerUpgradeCards, CplayersPulledCards);
        return gameState;

    }


    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * getGame is a method used to recieve the state of the game saved on the server.
     * it is converted to a Load object before being sent.
     * @return a Load version of the gameState
     */
    public Load getGame(){
        return makeGameLoad(gameState);
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * this method takes the Load of a game sent from a client
     * and uses its information to make a gameState in the server.
     */
    public void instantiateGameState(Load loadGame){
        GameState instantiatetedGameState = makeGameState(loadGame);
        this.gameState = instantiatetedGameState;
        System.out.println("We have :" + gameState.getBoard());
        System.out.println(gameState.getPlayerNames());
        System.out.println(String.valueOf(gameState.getPlayerHeadings()));
        System.out.println(gameState.getCurrentPlayer());
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * a method for changing the position of a player saved on the server.
     * It needs the player number and so the one sending needs to know what player
     * they are sending data about
     */
    public void setPlayerPosition(int player, int xPos, int yPos){
        gameState.setSpecificPlayerPosition(player, xPos, yPos);
        System.out.println("We have made the changes");
    }


    public GameState sendStartGameData(PlayerStartData player, int playerNumber){

        System.out.println("når vi her til");
        gameState.getPlayerNames()[playerNumber] = player.getName();
        gameState.getPlayerColors()[playerNumber] = player.getColor();
        gameState.getPlayerEnergyCubes()[playerNumber] = 5;
        gameState.getPlayersXPosition()[playerNumber] = player.getX();
        gameState.getPlayersYPosition()[playerNumber] = player.getY();
        gameState.getPlayerHeadings()[playerNumber] = player.getHeading();

        ArrayList<CommandCard> deck = new ArrayList<>();

        deck = instaShuffleDeck(deck);

        Command[][] temp = gameState.getPlayerProgrammingDeck();

        if (temp[playerNumber] == null) {
            temp[playerNumber] = new Command[deck.size()];
        }

        for (int i = 0; i < deck.size(); i++) {
            temp[playerNumber][i] = deck.get(i).command;
        }

        gameState.setPlayerProgrammingDeck(temp);

        return gameState;

    }

    public ArrayList<CommandCard> instaShuffleDeck(ArrayList<CommandCard> playerDeck){

        Set<Command> validCommands = EnumSet.of(Command.FORWARD, Command.FAST_FORWARD, Command.LEFT, Command.RIGHT, Command.SPRINT_FORWARD, Command.BACK_UP, Command.U_TURN, Command.AGAIN, Command.POWER_UP);

        int[] counts = {5, 3, 3, 3, 1, 1, 1, 2, 1};
        int index = 0;
        System.out.println(validCommands);
        for(Command command : validCommands){
            int count = counts[index];
            for(int i = 0; i < count; i++){
                playerDeck.add(new CommandCard(command));
            }
            index++;
        }
        Collections.shuffle(playerDeck);

        return playerDeck;

    }


}
