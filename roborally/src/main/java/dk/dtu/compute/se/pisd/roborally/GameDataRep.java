package dk.dtu.compute.se.pisd.roborally;


import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameLoader;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
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


    public void resetReadyList(){

        for (int i = 0; i < gameData.getReadyList().length; i++) {
            gameData.getReadyList()[i] = false;
        }

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

    public boolean checkerAllPlayersPicked(){

        for (int i = 0; i < gameData.getAllPickedList().length; i++) {

            //System.out.println("This player picked: " + gameData.getAllPickedList()[i]);

            if(!gameData.getAllPickedList()[i]){
                return false;
            }
        }

        return true;

    }

    public boolean checkerAllPlayerReset(){
        for (int i = 0; i < gameData.getResetCounter().length; i++) {

            //System.out.println("This player picked: " + gameData.getAllPickedList()[i]);

            if(!gameData.getResetCounter()[i]){
                return false;
            }
        }

        return true;

    }

    public void readyAndReset(){

        for (int i = 0; i < gameData.getReadyList().length; i++) {

            gameData.getReadyList()[i] = false;
            gameData.getResetCounter()[i] = false;

        }

    }

    public int checkerAllPlayersUpgrade(){

        int j = 0;
        for (int i = 0; i < gameData.getAllPlayerUpgrade().length; i++) {

            //System.out.println("This player picked: " + gameData.getAllPickedList()[i]);

            if(gameData.getAllPlayerUpgrade()[i]){
               j++;
            }
        }

        return j;

    }

    public void resetAllUpgrade(){

        for (int i = 0; i < gameData.getAllPlayerUpgrade().length; i++) {
            gameData.getAllPlayerUpgrade()[i] = false;
        }

    }

    public void removeCurrProgram(){

        Command[][] temp = gameState.getPlayerCurrentProgram();
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                temp[i][j] = null;
            }
        }

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

        Command[] shopCards = null;
        if(gameState1.getUpgradeShopCards() != null){
            shopCards = gameState1.getUpgradeShopCards();
        }


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
        if(shopCards != null){
            load.setUpgradeShopCards(shopCards);
        }

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

        // Assume temp is Command[][]
        Command[][] temp = gameState.getPlayerProgrammingDeck();

// Create deck outside the loop. This way, you're not creating a new deck
// on each iteration -- just when it's needed.
        ArrayList<CommandCard> deck;

// Iterate over each array in temp.
        for (int i = 0; i < temp.length; i++) {
            // Check if the array at temp[i] is null or any of its elements are null.
            if (temp[i] == null || Arrays.stream(temp[i]).anyMatch(Objects::isNull)) {
                // If it is, create a new deck and shuffle it.
                deck = new ArrayList<>();
                deck = instaShuffleDeck(deck);

                // Convert the deck to a new array and assign it to temp[i].
                temp[i] = new Command[deck.size()];
                for (int j = 0; j < deck.size(); j++) {
                    temp[i][j] = deck.get(j).command;
                }
            }
        }

        gameState.setPlayerProgrammingDeck(temp);

        return gameState;

    }

    public ArrayList<CommandCard> instaShuffleDeck(ArrayList<CommandCard> playerDeck){

        Set<Command> validCommands = EnumSet.of(Command.FORWARD, Command.FAST_FORWARD, Command.LEFT, Command.RIGHT, Command.SPRINT_FORWARD, Command.BACK_UP, Command.U_TURN, Command.AGAIN, Command.POWER_UP);

        int[] counts = {5, 3, 3, 3, 1, 1, 1, 2, 1};
        int index = 0;
        //System.out.println(validCommands);
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

    public Command[][] removeCardPickedFromPulled(Command[] cardsPicked, int playerNumber){

        Command[] temp = Arrays.copyOf(cardsPicked, cardsPicked.length);

        Command[][] allCards = gameState.getPlayersPulledCards();

        for (int i = 0; i < allCards[playerNumber].length; i++) {

            for (int j = 0; j < cardsPicked.length; j++) {
                if(temp[j] == allCards[playerNumber][i]){
                    temp[j] = null;
                    allCards[playerNumber][i] = null;
                }
            }

        }

        return allCards;
    }

    //Save and load on server

    public Load getSave(String saveName){

        JSONHandler handler = new JSONHandler();

        JSONObject json = handler.load(saveName, "game");
        if (json == null) {
            System.out.println("Error in making Load from file on server side");
            return null;
        }

        Load load = GameLoader.loadData2(json);

        return load;
    }

    public void saveAGame(String saveName){
        jsonGame(gameState, saveName );
    }

    public String[] getNames(){
        File folder = new File("roborally/src/main/resources/games");
        File[] listOfFiles = folder.listFiles();
        String[] strings = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                strings[i] = (removeExtension(listOfFiles[i].getName()));
            }
        }

        return strings;
    }

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Removes an extension from the end of the file name.
     * @param val the string to remove the extension of
     * @return the name of the file with no extension
     */

    private static String removeExtension(String val){
        String[] split = val.split("\\.");
        String toReturn = "";
        for(int i = 0; i < split.length-1; i++){
            if(i > 0) toReturn += ".";
            toReturn += split[i];
        }
        return toReturn;
    }



    //A big thing here
    //Turning our Load into a JSONfile we can save
    public static void jsonGame(GameState saveGameState, String name){
        JSONObject obj = new JSONObject();

        //Board board = load.board;

        obj.put("board", saveGameState.getBoard()); //String
        obj.put("step", saveGameState.getStep()); //Int
        obj.put("playerAmount", saveGameState.getPlayerAmount()); //Int
        obj.put("currentPlayer", saveGameState.getCurrentPlayer()); //String
        obj.put("phase", phaseToString(saveGameState.getPhase())); //String
        obj.put("isStepMode", booleanToString(saveGameState.isStepmode())); //String

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
        for(int i = 0; i < saveGameState.getPlayerAmount(); i++){
            //int playerNum = GameClient.getPlayerNumber();
            //Player player = load.getPlayer  board.getPlayer(playerNum);
            playersName.add(saveGameState.getPlayerNames()[i]);
            playersColor.add(saveGameState.getPlayerColors()[i]);
            playerEnergyCubes.add(saveGameState.getPlayerEnergyCubes()[i]);
            playersX.add(saveGameState.getPlayersXPosition()[i]);
            playersY.add(saveGameState.getPlayersYPosition()[i]);
            //if(load.getPlayerHeadings()[i] != null) {
            playersHeading.add(headingToString(saveGameState.getPlayerHeadings()[i]));
            //}
            playersCheckpoints.add(saveGameState.getPlayerCheckPoints()[i]);
            //Back on track

            playersProgram.add("#");
            if(saveGameState.getPlayerCurrentProgram()[i] != null){
                for(int j = 0; j < saveGameState.getPlayerCurrentProgram()[i].length; j++){
                    if(saveGameState.getPlayerProgrammingDeck()[i][j] != null){
                        playersProgram.add(saveGameState.getPlayerCurrentProgram()[i][j].toString());
                    }
                }}

            playersPulledCards.add("#");
            if(saveGameState.getPlayersPulledCards()[i] != null){
                for(int j = 0; j < saveGameState.getPlayersPulledCards()[i].length; j++){
                    if(saveGameState.getPlayersPulledCards()[i][j] != null){
                        playersPulledCards.add(saveGameState.getPlayersPulledCards()[i][j].toString());
                    }
                }}

            playerUpgradeCards.add("#");
            if(saveGameState.getPlayerUpgradeCards()[i] != null){
                for(int j = 0; j < saveGameState.getPlayerUpgradeCards()[i].length; j++){
                    if(saveGameState.getPlayerUpgradeCards()[i][j] != null){
                        playerUpgradeCards.add(saveGameState.getPlayerUpgradeCards()[i][j].toString());
                    }
                }}

            playersDiscardCards.add("#");
            if(saveGameState.getPlayerDiscardPile()[i] != null){
                for(int j = 0; j < saveGameState.getPlayerDiscardPile()[i].length; j++){
                    if(saveGameState.getPlayerDiscardPile()[i][j] != null){
                        playersDiscardCards.add(saveGameState.getPlayerDiscardPile()[i][j].toString());
                    }
                }}
            playersProgrammingDeck.add("#");
            if(saveGameState.getPlayerProgrammingDeck()[i] != null){
                for(int j = 0; j < saveGameState.getPlayerProgrammingDeck()[i].length; j++){
                    if(saveGameState.getPlayerProgrammingDeck()[i][j] != null){
                        playersProgrammingDeck.add(saveGameState.getPlayerProgrammingDeck()[i][j].toString());
                    }
                }}
        }
        JSONArray upgradeCardsDeck = new JSONArray();
        JSONArray upgradeDiscardDeck = new JSONArray();
        JSONArray upgradeOutDeck = new JSONArray();
        //if(load. upgradeShop != null){
        ArrayList<CommandCard> outUpgradeCards = new ArrayList<>();
        for(int k = 0; k < saveGameState.getUpgradeOutDeck().length; k++) {
            if(saveGameState.getUpgradeOutDeck()[k] != null){
                CommandCard commandCard = new CommandCard(saveGameState.getUpgradeOutDeck()[k]);
                outUpgradeCards.add(commandCard);
            }}
        ArrayList<CommandCard> discardUpgradeCards = new ArrayList<>();
        for(int k = 0; k < saveGameState.getUpgradeDiscardDeck().length; k++) {
            if(saveGameState.getUpgradeDiscardDeck()[k] != null){
                CommandCard commandCard = new CommandCard(saveGameState.getUpgradeDiscardDeck()[k]);
                discardUpgradeCards.add(commandCard);
            }}
        ArrayList<CommandCard> upgradeDeck = new ArrayList<>();
        for(int k = 0; k < saveGameState.getUpgradeCardsDeck().length; k++) {
            if(saveGameState.getUpgradeCardsDeck()[k] != null){
                CommandCard commandCard = new CommandCard(saveGameState.getUpgradeCardsDeck()[k]);
                upgradeDeck.add(commandCard);
            }}


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

        for(int k = 0; k < saveGameState.getMapCubePositions().length; k++) {
            mapEnergyCubes.add(saveGameState.getMapCubePositions()[k]);
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

        JSONHandler json = new JSONHandler();
        json.save(name, obj, "game");
        //return obj;
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


}
