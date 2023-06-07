package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.*;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

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



    //Here we are making so we can save a JSON-file on the server, containing a gamestate that players then can ask for.



    /**
     * @param obj - A JSONObject that has info from a json file of a new gameState.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a Load.java object that can hold all the data from a new load,
     * so all data can be pulled from this file already being converted to the right format.
     * The method uses the Converter.java class to convert after receiving the data from the obj file.
     */
    public static Load loadGameState(JSONObject obj){
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

    public static JSONObject jsonGame(Load load){
        JSONObject obj = new JSONObject();

        //Board board = load.board;

        obj.put("board", load.getBoard()); //String
        obj.put("step", load.getStep()); //Int
        obj.put("playerAmount", load.getPlayerAmount()); //Int
        obj.put("currentPlayer", load.getCurrentPlayer()); //String
        obj.put("phase", phaseToString(load.getPhase())); //String
        obj.put("isStepMode", booleanToString(load.isStepmode())); //String

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
        for(int i = 0; i < load.getPlayerAmount(); i++){
        //int playerNum = GameClient.getPlayerNumber();
        //Player player = load.getPlayer  board.getPlayer(playerNum);
        playersName.add(load.getPlayerNames()[i]);
        playersColor.add(load.getPlayerColors()[i]);
        playerEnergyCubes.add(load.getPlayerEnergyCubes()[i]);
        playersX.add(load.getPlayersXPosition()[i]);
        playersY.add(load.getPlayersYPosition()[i]);
        playersHeading.add(headingToString(load.getPlayerHeadings()[i]));
        playersCheckpoints.add(load.getPlayerCheckPoints()[i]);
        //Back on track
        CommandCardField[] program = new CommandCardField[load.getPlayerProgrammingDeck()[i].length];
                for(int j = 0; j < load.getPlayerProgrammingDeck()[i].length; j++){

                    CommandCard commandCard = new CommandCard(load.getPlayerProgrammingDeck()[i][j]);
                    CommandCardField commandCardField = new CommandCardField(null);
                    commandCardField.setCard(commandCard);
                    program[j] = commandCardField;
                }
        CommandCardField[] pulled = new CommandCardField[load.getPlayersPulledCards()[i].length];
            for(int j = 0; j < load.getPlayersPulledCards()[i].length; j++){

                CommandCard commandCard = new CommandCard(load.getPlayersPulledCards()[i][j]);
                CommandCardField commandCardField = new CommandCardField(null);
                commandCardField.setCard(commandCard);
                program[j] = commandCardField;
            }
            CommandCardField[] upgradeCards = new CommandCardField[load.getPlayerUpgradeCards()[i].length];
            for(int j = 0; j < load.getPlayerUpgradeCards()[i].length; j++){

                CommandCard commandCard = new CommandCard(load.getPlayerUpgradeCards()[i][j]);
                CommandCardField commandCardField = new CommandCardField(null);
                commandCardField.setCard(commandCard);
                program[j] = commandCardField;
            }
            ArrayList<CommandCard> discardPile = new ArrayList<>();
            for(int j = 0; j < load.getPlayerDiscardPile()[i].length; j++){

                CommandCard commandCard = new CommandCard(load.getPlayerDiscardPile()[i][j]);
                discardPile.add(commandCard);
            }
            ArrayList<CommandCard> programmingDeck = new ArrayList<>();
            for(int j = 0; j < load.getPlayerProgrammingDeck()[i].length; j++){

                CommandCard commandCard = new CommandCard(load.getPlayerProgrammingDeck()[i][j]);
                programmingDeck.add(commandCard);
            }


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
        //if(load. upgradeShop != null){
            ArrayList<CommandCard> outUpgradeCards = new ArrayList<>();
            for(int k = 0; k < load.getUpgradeOutDeck().length; k++) {
                CommandCard commandCard = new CommandCard(load.getUpgradeOutDeck()[k]);
                outUpgradeCards.add(commandCard);
            }
            ArrayList<CommandCard> discardUpgradeCards = new ArrayList<>();
            for(int k = 0; k < load.getUpgradeDiscardDeck().length; k++) {
                CommandCard commandCard = new CommandCard(load.getUpgradeDiscardDeck()[k]);
                discardUpgradeCards.add(commandCard);
            }
            ArrayList<CommandCard> upgradeDeck = new ArrayList<>();
        for(int k = 0; k < load.getUpgradeCardsDeck().length; k++) {
            CommandCard commandCard = new CommandCard(load.getUpgradeCardsDeck()[k]);
            upgradeDeck.add(commandCard);
        }


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

        for(int k = 0; k < load.getMapCubePositions().length; k++) {
            mapEnergyCubes.add(load.getMapCubePositions()[k]);
        }


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
    //Assistant method for previous method
    JSONHandler json = new JSONHandler();
    private static String phaseToString(Phase phase){
        switch (phase){
            case UPGRADE : return "UPGRADE";
            case ACTIVATION : return "ACTIVATION";
            case PROGRAMMING : return "PROGRAMMING";
            case INITIALISATION : return "INITIALISATION";
            case PLAYER_INTERACTION : return "PLAYER_INTERACTION";
        }
        return null;
    }
    private static String headingToString(Heading heading){
        switch (heading){
            case NORTH: return "NORTH";
            case EAST: return "EAST";
            case SOUTH: return "SOUTH";
            case WEST: return "WEST";
        }
        return null;
    }
    private static String booleanToString(boolean bool){
        if(bool) return "TRUE";
        else return "FALSE";
    }


    //Here we go, things asked from the client
    public static Load getSave(String saveName){

        JSONHandler handler = new JSONHandler();

        JSONObject json = handler.load(saveName, "game");
        if (json == null) {
            System.out.println("Error in making Load from file on server side");
            return null;
        }

        Load load = GameLoader.loadData(json);

        return load;
    }

    public static void saveAGame(Load load, String saveName){
        JSONHandler handler = new JSONHandler();

        JSONObject json = jsonGame(load);
        handler.save(saveName, json ,"game");
    }

    //idk if this one will work
    public static String[] getSaveNames(){
        String[] names
        return names;
    }

}
