package dk.dtu.compute.se.pisd.roborally;


import com.google.gson.Gson;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Converter;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

@Service
public class GameDataRep {


    GameData gameData;
    GameState gameState;

    public GameDataRep(){

    }

    public void instantiateGameData(int numPlayer){
        this.gameData = new GameData(numPlayer);
    }

    public void instantiateGameState(Load loadGame){
        GameState instantiatetedGameState = makeGameState(loadGame);
        this.gameState = instantiatetedGameState;
        System.out.println("We have :" + gameState.getBoard());
        System.out.println(gameState.getPlayerNames());
        System.out.println(String.valueOf(gameState.getPlayerHeadings()));
        System.out.println(gameState.getCurrentPlayer());
    }




    public boolean checkerPlayersConnected(){

        for (int i = 0; i < gameData.readyList.length; i++) {

            System.out.println("This player: " + gameData.readyList[i]);

            if(!gameData.readyList[i]){
                return false;
            }
        }

        return true;

    }

    public int playerNumber(){

        int i;
        for (i = 0; i < gameData.readyList.length; i++) {

            System.out.println("This player: " + gameData.readyList[i]);

            if(!gameData.readyList[i]){
                break;
            }
        }

        return i;
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method goes through the different parts of gameState and sets
     * them into a Load.
     * @param gameState1 the gameState we want to create a Load version of
     * @return a Load version
     */
    private static Load makeGameState(GameState gameState1) {

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

    //Making JSON into our GameState
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
        return makeGameState(gameState);
    }

    /**
     * @author Nicklas Christensen      s224314.dtu.dk
     * @param commands an array of the programmingCards are player is sending.
     * @param player the player who's programmingCards we are changing.
     */
    public boolean newProgramCards(Command[] commands, int player){
        gameState.setPlayerProgramCards(commands, player);
        return true;
    }


}
