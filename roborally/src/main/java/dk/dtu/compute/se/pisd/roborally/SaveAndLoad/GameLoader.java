package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
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
    private static Load loadData(JSONObject obj){
        Load load = new Load();
        load.setBoard((String)obj.get("board"));
        load.setStep((int)obj.get("step"));
        load.setCurrentPlayer((String)obj.get("currentPlayer"));
        load.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        load.setPhase(Converter.getPhase ((String) obj.get("phase")));
        load.setPlayerAmount((int)obj.get("playerAmount"));
        int amount = load.getPlayerAmount();
        load.setPlayerNames(new String[amount]);
        load.setPlayerColors(new String[amount]);
        load.setPlayersXPosition(new int[amount]);
        load.setPlayersYPosition(new int[amount]);
        load.setPlayerEnergyCubes(new int[amount]);
        load.setPlayerHeadings(new Heading[amount]);
        load.setPlayerProgrammingDeck(new Command[amount][]);
        load.setPlayerCurrentProgram(new Command[amount][]);
        load.setPlayerDiscardPile(new Command[amount][]);
        load.setPlayerUpgradeCards(new Command[amount][]);
        String[][] playersProgrammingDeck  = Converter.splitSeries((String[]) obj.get("playersProgrammingDeck"), "#");
        String[][] playersProgram  = Converter.splitSeries((String[]) obj.get("playersProgram"), "#");
        String[][] playerUpgradeCards = Converter.splitSeries((String[]) obj.get("playerUpgradeCards"), "#");
        String[][] playersDiscardCards = Converter.splitSeries((String[]) obj.get("playersDiscardCards"), "#");
        for(int i = 0; i < amount; i++){
            load.getPlayerNames()[i] = ((String[])obj.get("playersName"))[i];
            load.getPlayerColors()[i] = ((String[])obj.get("playerColor"))[i];
            load.getX()[i] = ((int[])obj.get("playersX"))[i];
            load.getY()[i] = ((int[])obj.get("playersY"))[i];
            load.getPlayerEnergyCubes()[i] = ((int[])obj.get("playerCubes"))[i];
            load.getPlayerHeadings()[i] = Converter.getHeading (((String[])obj.get("playersHeading"))[i]);
            load.getPlayerCheckPoints()[i] = ((int[])obj.get("playersCheckpoints"))[i];
            load.getPlayerProgrammingDeck()[i] = Converter.getCommands(playersProgrammingDeck[i]);
            load.getPlayerCurrentProgram()[i] = Converter.getCommands(playersProgram[i]);
            load.getPlayerUpgradeCards()[i] = Converter.getCommands(playerUpgradeCards[i]);
            load.getPlayerDiscardPile()[i] = Converter.getCommands(playersDiscardCards[i]);
        }
        load.setUpgradeCardsDeck(Converter.getCommands((String[])obj.get("upgradeCardsDeck")));
        load.setUpgradeOutDeck(Converter.getCommands((String[])obj.get("upgradeOutDeck")));
        load.setUpgradeDiscardDeck(Converter.getCommands((String[])obj.get("upgradeDiscardDeck")));
        return load;
    }



}
