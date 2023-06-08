package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.GameClient;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static java.lang.Integer.parseInt;

public class GameLoader {

    private static GameController gameController;

    private static JSONHandler handler = new JSONHandler();

    public static GameController loadGame(String name, AppController appController) {
        JSONObject obj = handler.load(name, "game");
        if (obj == null) {
            System.out.println("Error in loading");
            return null;
        }
        gameController = LoadInstance.load(appController, loadData(obj));

        return gameController;
    }

    /**
     * @param obj - A JSONObject that has info from a a json file of a new game.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a Load.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    public static Load loadData(JSONObject obj){
        Load load = new Load();
        load.setBoard((String)obj.get("board"));
        load.setStep(parseInt(String.valueOf(obj.get("step"))));
        load.setCurrentPlayer((String)obj.get("currentPlayer"));
        load.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        load.setPhase(Converter.getPhase ((String) obj.get("phase")));
        load.setPlayerAmount(parseInt(String.valueOf(obj.get("playerAmount"))));
        int amount = load.getPlayerAmount();
        load.setPlayerNames(new String[amount]);
        load.setPlayerColors(new String[amount]);
        load.setPlayersXPosition(new int[amount]);
        load.setPlayersYPosition(new int[amount]);
        load.setPlayerEnergyCubes(new int[amount]);
        load.setPlayerCheckPoints(new int[amount]);
        load.setPlayerHeadings(new Heading[amount]);
        load.setPlayerProgrammingDeck(new Command[amount][]);
        load.setPlayerCurrentProgram(new Command[amount][]);
        load.setPlayerDiscardPile(new Command[amount][]);
        load.setPlayerUpgradeCards(new Command[amount][]);
        load.setPlayersPulledCards(new Command[amount][]);
        String[][] playersProgrammingDeck  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgrammingDeck")), "#");
        String[][] playersProgram  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgram")), "#");
        String[][] playerUpgradeCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playerUpgradeCards")), "#");
        String[][] playersDiscardCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersDiscardCards")), "#");
        String[][] playersPulledCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersPulledCards")), "#");
        //for(int i = 0; i < amount; i++){
        int i = 0;
            load.getPlayerNames()[i] = Converter.jsonArrToString((JSONArray)obj.get("playersName"))[i];
            load.getPlayerColors()[i] = Converter.jsonArrToString((JSONArray)obj.get("playerColor"))[i];
            load.getX()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersX"))[i];
            load.getY()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersY"))[i];
            load.getPlayerEnergyCubes()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playerCubes"))[i];
            load.getPlayerHeadings()[i] = Converter.getHeading (Converter.jsonArrToString((JSONArray)obj.get("playersHeading"))[i]);
            load.getPlayerCheckPoints()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersCheckpoints"))[i];
            load.getPlayerProgrammingDeck()[i] = Converter.getCommands(playersProgrammingDeck[i]);
            load.getPlayerCurrentProgram()[i] = Converter.getCommands(playersProgram[i]);
            load.getPlayerUpgradeCards()[i] = Converter.getCommands(playerUpgradeCards[i]);
            load.getPlayerDiscardPile()[i] = Converter.getCommands(playersDiscardCards[i]);
            load.getPlayersPulledCards()[i] = Converter.getCommands(playersPulledCards[i]);
        //}
        load.setMapCubePositions(Converter.jsonArrToInt((JSONArray)obj.get("mapCubes")));
        load.setUpgradeCardsDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeCardsDeck"))));
        load.setUpgradeOutDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeOutDeck"))));
        load.setUpgradeDiscardDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeDiscardDeck"))));
        return load;
    }


    // This is a dumb fix, just made sure it placed all players in the Load
    /**
     * @param obj - A JSONObject that has info from a a json file of a new game.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a Load.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    public static Load loadData2(JSONObject obj){
        Load load = new Load();
        load.setBoard((String)obj.get("board"));
        load.setStep(parseInt(String.valueOf(obj.get("step"))));
        load.setCurrentPlayer((String)obj.get("currentPlayer"));
        load.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        load.setPhase(Converter.getPhase ((String) obj.get("phase")));
        load.setPlayerAmount(parseInt(String.valueOf(obj.get("playerAmount"))));
        int amount = load.getPlayerAmount();
        load.setPlayerNames(new String[amount]);
        load.setPlayerColors(new String[amount]);
        load.setPlayersXPosition(new int[amount]);
        load.setPlayersYPosition(new int[amount]);
        load.setPlayerEnergyCubes(new int[amount]);
        load.setPlayerCheckPoints(new int[amount]);
        load.setPlayerHeadings(new Heading[amount]);
        load.setPlayerProgrammingDeck(new Command[amount][]);
        load.setPlayerCurrentProgram(new Command[amount][]);
        load.setPlayerDiscardPile(new Command[amount][]);
        load.setPlayerUpgradeCards(new Command[amount][]);
        load.setPlayersPulledCards(new Command[amount][]);
        String[][] playersProgrammingDeck  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgrammingDeck")), "#");
        String[][] playersProgram  = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersProgram")), "#");
        String[][] playerUpgradeCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playerUpgradeCards")), "#");
        String[][] playersDiscardCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersDiscardCards")), "#");
        String[][] playersPulledCards = Converter.splitSeries(Converter.jsonArrToString((JSONArray)obj.get("playersPulledCards")), "#");
        for(int i = 0; i < amount; i++){
        //int i = 0;
        load.getPlayerNames()[i] = Converter.jsonArrToString((JSONArray)obj.get("playersName"))[i];
        load.getPlayerColors()[i] = Converter.jsonArrToString((JSONArray)obj.get("playerColor"))[i];
        load.getX()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersX"))[i];
        load.getY()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersY"))[i];
        load.getPlayerEnergyCubes()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playerCubes"))[i];
        load.getPlayerHeadings()[i] = Converter.getHeading (Converter.jsonArrToString((JSONArray)obj.get("playersHeading"))[i]);
        load.getPlayerCheckPoints()[i] = Converter.jsonArrToInt((JSONArray)obj.get("playersCheckpoints"))[i];
        load.getPlayerProgrammingDeck()[i] = Converter.getCommands(playersProgrammingDeck[i]);
        load.getPlayerCurrentProgram()[i] = Converter.getCommands(playersProgram[i]);
        load.getPlayerUpgradeCards()[i] = Converter.getCommands(playerUpgradeCards[i]);
        load.getPlayerDiscardPile()[i] = Converter.getCommands(playersDiscardCards[i]);
        load.getPlayersPulledCards()[i] = Converter.getCommands(playersPulledCards[i]);
        }
        load.setMapCubePositions(Converter.jsonArrToInt((JSONArray)obj.get("mapCubes")));
        load.setUpgradeCardsDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeCardsDeck"))));
        load.setUpgradeOutDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeOutDeck"))));
        load.setUpgradeDiscardDeck(Converter.getCommands(Converter.jsonArrToString((JSONArray)obj.get("upgradeDiscardDeck"))));
        return load;
    }

}
