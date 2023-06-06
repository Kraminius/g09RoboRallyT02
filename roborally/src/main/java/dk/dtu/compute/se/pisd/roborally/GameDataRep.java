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


    //Changing the game we save in gameState
    public void setCurrentGame(JSONObject game){
        setGameState(gameState, game);
    }

    //placing JSON into our GameState
    /**
     * @param obj - A JSONObject that has info from a a json file of a new game.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a GameState.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    private static void setGameState(GameState gameState, JSONObject obj){
        //GameState gameState = new GameState();
        gameState.setBoard((String)obj.get("board"));
        gameState.setStep(parseInt(String.valueOf(obj.get("step"))));
        gameState.setCurrentPlayer((String)obj.get("currentPlayer"));
        gameState.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        gameState.setPhase(Converter.getPhase ((String) obj.get("phase")));
        gameState.setPlayerAmount(parseInt(String.valueOf(obj.get("playerAmount"))));
        int amount = gameState.getPlayerAmount();
        gameState.setPlayerNames(new String[amount]);
        gameState.setPlayerColors(new String[amount]);
        gameState.setPlayersXPosition(new int[amount]);
        gameState.setPlayersYPosition(new int[amount]);
        gameState.setPlayerEnergyCubes(new int[amount]);
        gameState.setPlayerCheckPoints(new int[amount]);
        gameState.setPlayerHeadings(new Heading[amount]);
        gameState.setPlayerProgrammingDeck(new Command[amount][]);
        gameState.setPlayerCurrentProgram(new Command[amount][]);
        gameState.setPlayerDiscardPile(new Command[amount][]);
        gameState.setPlayerUpgradeCards(new Command[amount][]);
        gameState.setPlayersPulledCards(new Command[amount][]);
        String[][] playersProgrammingDeck  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgrammingDeck")), "#");
        String[][] playersProgram  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgram")), "#");
        String[][] playerUpgradeCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playerUpgradeCards")), "#");
        String[][] playersDiscardCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersDiscardCards")), "#");
        String[][] playersPulledCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersPulledCards")), "#");
        for(int i = 0; i < amount; i++){
            gameState.getPlayerNames()[i] = Converter.jsonArrToString((JSONArray)obj.get("playersName"))[i];
            gameState.getPlayerColors()[i] = Converter.jsonArrToString((JSONArray)obj.get("playerColor"))[i];
            gameState.getX()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersX"))[i];
            gameState.getY()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersY"))[i];
            gameState.getPlayerEnergyCubes()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playerCubes"))[i];
            gameState.getPlayerHeadings()[i] = Converter.getHeading (Converter.jsonArrToString((JSONArray)obj.get("playersHeading"))[i]);
            gameState.getPlayerCheckPoints()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersCheckpoints"))[i];
            gameState.getPlayerProgrammingDeck()[i] = Converter.getCommands(playersProgrammingDeck[i]);
            gameState.getPlayerCurrentProgram()[i] = Converter.getCommands(playersProgram[i]);
            gameState.getPlayerUpgradeCards()[i] = Converter.getCommands(playerUpgradeCards[i]);
            gameState.getPlayerDiscardPile()[i] = Converter.getCommands(playersDiscardCards[i]);
            gameState.getPlayersPulledCards()[i] = Converter.getCommands(playersPulledCards[i]);
        }
        gameState.setMapCubePositions(Converter.jsonArrToInt((JSONArray)obj.get("mapCubes")));
        gameState.setUpgradeCardsDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeCardsDeck"))));
        gameState.setUpgradeOutDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeOutDeck"))));
        gameState.setUpgradeDiscardDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeDiscardDeck"))));
        //return gameState;
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


}
