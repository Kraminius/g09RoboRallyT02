package dk.dtu.compute.se.pisd.roborally;


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
        gameState = makeGameState(game);
    }

    //Making JSON into a GameState
    /**
     * @param obj - A JSONObject that has info from a a json file of a new game.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a GameState.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    private static GameState makeGameState(JSONObject obj){
        GameState gameState = new GameState();
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
        return gameState;
    }

    //Retrieving the game we have in gameState
    public JSONObject getGame(){
        return saveGame(gameState);
    }


    public JSONObject saveGame(GameState controller){
        JSONObject obj = new JSONObject();

        //Board betterBoard = new Board();
        obj.put("board",controller.getBoard()); //String
        obj.put("step", controller.getStep());  //Int
        obj.put("playerAmount", controller.getPlayerAmount());  //Int
        obj.put("currentPlayer", controller.getCurrentPlayer()); //String
        obj.put("phase", phaseToString(controller.getPhase())); //String
        obj.put("isStepMode", booleanToString(controller.isStepmode())); //String

        JSONArray playersName = new JSONArray();
        JSONArray playersColor = new JSONArray();
        JSONArray playersX = new JSONArray();
        JSONArray playersY = new JSONArray();
        JSONArray playersHeading = new JSONArray();
        JSONArray playersCheckpoints = new JSONArray();
        JSONArray playersProgrammingDeck = new JSONArray();
        JSONArray playersPulledCards = new JSONArray();
        JSONArray playersProgram = new JSONArray();
        JSONArray playersDiscardCards = new JSONArray();
        JSONArray playerUpgradeCards = new JSONArray();
        JSONArray playerEnergyCubes = new JSONArray();
        JSONArray mapEnergyCubes = new JSONArray();
        for(int i = 0; i < controller.getPlayerAmount(); i++){
            //Player player = board.getPlayer(i);
            playersName.add(controller.getPlayerNames()[i]);
            playersColor.add(controller.getPlayerColors()[i]);
            playerEnergyCubes.add(controller.getPlayerEnergyCubes()[i]);
            playersX.add(controller.getPlayersXPosition()[i]);
            playersY.add(controller.getPlayersYPosition()[i]);
            playersHeading.add(controller.getPlayerHeadings()[i]);
            playersCheckpoints.add(controller.getPlayerCheckPoints()[i]); //The checkpoints of each player
            //We need to make the Command into CommandCardField
            CommandCardField[] program = makeCardField(controller.getPlayerCurrentProgram()[i]);
            //CommandCardField[] program = controller.getPlayerCurrentProgram()[i];
            CommandCardField[] pulled = makeCardField(controller.getPlayersPulledCards()[i]);
            //CommandCardField[] pulled = player.getCards();
            CommandCardField[] upgradeCards = makeCardField(controller.getPlayerUpgradeCards()[i]);
            //CommandCardField[] upgradeCards = player.getUpgradeCards();
            ArrayList<CommandCard> discardPile = makeCard(controller.getPlayerDiscardPile()[i]);
            //ArrayList<CommandCard> discardPile = player.getDiscardPile();
            ArrayList<CommandCard> programmingDeck = makeCard(controller.getPlayerProgrammingDeck()[i]);
            //ArrayList<CommandCard> programmingDeck = player.getCardDeck();


            playersProgram.add("#");
            for(int j = 0; j < program.length; j++){
                if(program[j].getCard() != null) playersProgram.add(program[j].getCard().command.toString());
            }

            playersPulledCards.add("#");
            for(int j = 0; j < pulled.length; j++){
                if(pulled[j].getCard() != null) playersPulledCards.add(pulled[j].getCard().command.toString());
            }
            playerUpgradeCards.add("#");
            for(int j = 0; j < upgradeCards.length; j++){
                if(upgradeCards[j].getCard() != null) playerUpgradeCards.add(upgradeCards[j].getCard().command.toString());
            }
            playersDiscardCards.add("#");
            for(int j = 0; j < discardPile.size(); j++){
                if(discardPile.get(j) != null) playersDiscardCards.add(discardPile.get(j).command.toString());
            }
            playersProgrammingDeck.add("#");
            for(int j = 0; j < programmingDeck.size(); j++){
                if(programmingDeck.get(j) != null) playersProgrammingDeck.add(programmingDeck.get(j).command.toString());
            }
        }
        JSONArray upgradeCardsDeck = new JSONArray();
        JSONArray upgradeDiscardDeck = new JSONArray();
        JSONArray upgradeOutDeck = new JSONArray();
        //if(controller.upgradeShop != null){
            ArrayList<CommandCard> outUpgradeCards = makeCard(controller.getUpgradeOutDeck());
            //ArrayList<CommandCard> outUpgradeCards = controller.upgradeShop.getOut();
            ArrayList<CommandCard> discardUpgradeCards = makeCard(controller.getUpgradeDiscardDeck());
            //ArrayList<CommandCard> discardUpgradeCards = controller.upgradeShop.getDiscarded();
            ArrayList<CommandCard> upgradeDeck = makeCard(controller.getUpgradeCardsDeck());
            //ArrayList<CommandCard> upgradeDeck = controller.upgradeShop.getDeck();


            for(CommandCard c : outUpgradeCards){
                upgradeOutDeck.add(c.command.toString());
            }
            for(CommandCard c : discardUpgradeCards){
                upgradeDiscardDeck.add(c.command.toString());
            }
            for(CommandCard c : upgradeDeck){
                upgradeCardsDeck.add(c.command.toString());
            }
        //}

        saveCubes(mapEnergyCubes, controller);



        obj.put("playersName", playersName);
        obj.put("playerCubes", playerEnergyCubes);
        obj.put("playerColor", playersColor);
        obj.put("playersX", playersX);
        obj.put("playersY", playersY);
        obj.put("playersHeading", playersHeading);
        obj.put("playersCheckpoints", playersCheckpoints);
        obj.put("playersProgrammingDeck", playersProgrammingDeck);
        obj.put("playersPulledCards", playersPulledCards);
        obj.put("playersProgram", playersProgram);
        obj.put("playersDiscardCards", playersDiscardCards);
        obj.put("playerUpgradeCards", playerUpgradeCards);
        obj.put("upgradeCardsDeck", upgradeCardsDeck);
        obj.put("upgradeDiscardDeck", upgradeDiscardDeck);
        obj.put("upgradeOutDeck", upgradeOutDeck);
        obj.put("mapCubes", mapEnergyCubes);

        //json.save(name, obj, "game");
        return obj;
    }

    private String phaseToString(Phase phase){
        switch (phase){
            case UPGRADE : return "UPGRADE";
            case ACTIVATION : return "ACTIVATION";
            case PROGRAMMING : return "PROGRAMMING";
            case INITIALISATION : return "INITIALISATION";
            case PLAYER_INTERACTION : return "PLAYER_INTERACTION";
        }
        return null;
    }
    private String headingToString(Heading heading){
        switch (heading){
            case NORTH: return "NORTH";
            case EAST: return "EAST";
            case SOUTH: return "SOUTH";
            case WEST: return "WEST";
        }
        return null;
    }
    private String booleanToString(boolean bool){
        if(bool) return "TRUE";
        else return "FALSE";
    }

    private void saveCubes(JSONArray mapEnergyCubes, GameState controller){
        //ArrayList<Space> energyCubeSpaces = new ArrayList<>();
        int i = 0;
        while(i < controller.getMapCubePositions().length){
            mapEnergyCubes.add(controller.getMapCubePositions()[i]);
            i++;
            mapEnergyCubes.add(controller.getMapCubePositions()[i]);
            i++;
            mapEnergyCubes.add(controller.getMapCubePositions()[i]);
            i++;
        }

        //Old
        /*
        ArrayList<Space> energyCubeSpaces = board.getEnergyFieldSpaces();
        for(int i = 0; i < energyCubeSpaces.size(); i++){
            mapEnergyCubes.add(energyCubeSpaces.get(i).x);
            mapEnergyCubes.add(energyCubeSpaces.get(i).y);
            mapEnergyCubes.add(energyCubeSpaces.get(i).getElement().getEnergyField().getCubes());
        }
        */
    }

    private int getCheckpointReached(boolean[] arr){
        int reached = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i])  reached++;
        }
        return reached;
    }

    //I am suspicious that this may not work
    private CommandCardField[] makeCardField(Command[] com){
        CommandCardField[] commandCardFields = new CommandCardField[com.length];
        for(int i = 0; i < com.length; i++){
            CommandCard commandCard = new CommandCard(com[i]);
            CommandCardField commandCardField = new CommandCardField(null);
            commandCardField.setCard(commandCard);
            commandCardFields[i] = commandCardField;
        }
        return commandCardFields;
    }

    private ArrayList<CommandCard> makeCard(Command[] com){
        ArrayList<CommandCard> commandCards = new ArrayList<>();
        for(int i = 0; i < com.length; i++){
            CommandCard commandCard = new CommandCard(com[i]);
            commandCards.add(commandCard);
        }
        return commandCards;
    }



}
