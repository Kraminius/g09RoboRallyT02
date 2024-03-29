package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class MyRest {


    @Autowired
    GameDataRep gameDataRep;

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameInfo gameInfo;


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     *
     * Handles the connection of a player to the game server.
     * This method is responsible for processing the payload containing the player information
     * and updating the game data accordingly.
     *
     * @param payload
     * @return
     */
    @PostMapping(value = "/connected")
    public ResponseEntity<String> connector(@RequestBody Map<String, Object> payload){
        int playerNum = (int) payload.get("playerNum");
        String name = (String) payload.get("name");

        gameDataRep.gameData.getReadyList()[playerNum] = true;
        gameDataRep.gameData.getGameSettings().getPlayerNames().add(name);

        System.out.println("mine spillere" + gameDataRep.gameData.getGameSettings().getPlayerNames());
        System.out.println(gameDataRep.gameData.getReadyList()[playerNum]);
        return ResponseEntity.ok().body("we connected");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     *  Checks if all players are connected to the game server.
     *  This method verifies whether all players have successfully connected to the game server.
     *
     * @return
     */

    @GetMapping(value = "/allConnected")
    public ResponseEntity<Boolean> allConnected() {
        Boolean temp = gameDataRep.checkerPlayersConnected();
        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     *  Retrieves the lobby ID from the game server.
     * @return
     */
    @GetMapping(value = "/getLobbyId")
    public ResponseEntity<String> getLobbyId() {
        String temp = gameDataRep.gameData.getId();
        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * It sets the "joinButtonPressed" boolean in the gameInfo object to true, indicating that the button has been pressed.
     * @return
     */
    @PostMapping (value = "/pressJoinButton")
    public ResponseEntity<String> pressJoinButton() {

        gameInfo.setJoinButtonPressed(true);
        return ResponseEntity.ok().body("hej");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Returns the the boolean that tells if join button is pressed or not.
     * @return
     */
    @GetMapping (value = "/isJoinButton")
    public ResponseEntity<Boolean> isJoinButton() {


        return ResponseEntity.ok().body(gameInfo.isJoinButtonPressed());
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * This method instantiates the gameData object that contains current game information.
     * @param playerNumStr
     * @return
     */
    @PostMapping (value = "/instaGameData")
    public ResponseEntity<Integer> instaGameData(@RequestParam("numberOfPlayers") String playerNumStr) {
        int numberOfPlayers = Integer.parseInt(playerNumStr);
        gameDataRep.instantiateGameData(numberOfPlayers);
        gameInfo.instaGameInfo(gameDataRep.gameData.getId(), gameDataRep.gameData.getGameSettings(), false);
        System.out.println("Size: " + gameDataRep.gameData.getReadyList().length + "& " + gameDataRep.gameData.getGameSettings().getNumberOfPlayers());
        return ResponseEntity.ok().body(5);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * This method returns the player number.
     * @return
     */
    @GetMapping(value = "/getPlayerNumber")
    public ResponseEntity<Integer> allConnectedNot() {
        Integer temp = gameDataRep.playerNumber();
        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Adds the mapname to game settings.
     * @param map
     * @return
     */
    @PostMapping (value = "/addMapName")
    public ResponseEntity<Integer> instaGameName(@RequestParam("mapName") String map) {
        gameDataRep.gameData.getGameSettings().setGameName(map);
        //System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Get the map name from game settings
     * @return
     */
    @GetMapping(value = "/getMapName")
    public ResponseEntity<String> getMapName() {

        String temp = gameDataRep.gameData.getGameSettings().getGameName();
        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Adds the game to server and creating instatiating and creating game settings.
     * @param payload
     * @return
     */
    @PostMapping(value = "/addGame")
    public ResponseEntity<String> addGame(@RequestBody Map<String, Object> payload){

        ArrayList<String> settings = (ArrayList<String>) payload.get("settings");
        boolean loadedGame = (Boolean) payload.get("loadedGame");

        gameDataRep.createGame(settings.get(0),settings.get(1), Integer.parseInt(settings.get(2)), settings.get(3));

        gameInfo.instaGameInfo(gameDataRep.gameData.getId(), gameDataRep.gameData.getGameSettings(), loadedGame);


        //System.out.println(gameRepository.getGameSettings().toString());

        return ResponseEntity.ok("hej");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets the gameLobby object.
     * @return
     */
    @GetMapping(value = "/getGameLobby", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameLobby> getGameLobby(){
        GameLobby gameLobby = gameDataRep.convertGameInfoToGameLobby();
        System.out.println("kig her: " + gameLobby);
        return ResponseEntity.ok(gameLobby);
    }

    /*@PutMapping(value = "/joinGameLobby/{lobbyId}")
    public ResponseEntity<String> joinGameLobby(@PathVariable String lobbyId, String player){

        gameRepository.getGameSettings().getPlayerNames().add("Test");

        return new ResponseEntity.ok("test");
    }*/

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets all the player names.
     * @return
     */
    @GetMapping(value = "/getAllPlayers")
    public ResponseEntity<ArrayList<String>> getPlayerNames(){

        return ResponseEntity.ok(gameDataRep.gameData.getGameSettings().getPlayerNames());


    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Checks if there is already a gameRunning, so that the lobby can see it.
     * @return
     */
    @GetMapping(value = "/isGameRunning")
    public ResponseEntity<Boolean> isGameRunning(){

        if(gameDataRep.gameData == null){
            return ResponseEntity.ok().body(false);
        }

        return ResponseEntity.ok().body(gameDataRep.gameData.isGameRunning());

    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Checks if someone opened the upgrade shop.
     * @return
     */
    @GetMapping(value = "/isUpgradeShopOpen")
    public ResponseEntity<Boolean> isUpgradeShopOpen(){



        return ResponseEntity.ok().body(gameInfo.isOpenShop());

    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets the current player.
     * @return
     */
    @GetMapping(value = "/getCurrentPlayer")
    public ResponseEntity<Integer> getCurrentPlayer(){


        return ResponseEntity.ok().body(gameInfo.getCurrentPlayer());

    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sets the next player based on player number. So the next player is current player + 1.
     * @return
     */
    @PostMapping(value = "/nextPlayer")
    public ResponseEntity<String> nextPlayer(){

        gameInfo.nextPlayer();

        //System.out.println(gameRepository.getGameSettings().toString());

        return ResponseEntity.ok("hej");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Adds a player start position to gameinfo object.
     * @param pos
     * @return
     */
    @PostMapping (value = "/addStartPosition")
    public ResponseEntity<Integer> addStartPosition(@RequestParam("pos") int pos) {
        gameInfo.addStartPosition(pos);
        //System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets the start position from all players.
     * @return
     */
    @GetMapping(value = "/getStartPosition")
    public ResponseEntity<ArrayList<Integer>> getStartPosition(){

        return ResponseEntity.ok(gameInfo.getChosenStartPlaces());


    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * @param game the load of the game we want to save on the server
     * @return wether we succeded or not at recieving the game
     */
    @PostMapping (value = "/instaGameState")
    public ResponseEntity<String> instaGameData(@RequestBody Load game) {

        System.out.println("insta request recieved");
        gameDataRep.instantiateGameState(game);
        //gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Instantiated a gameState");

        System.out.println(gameDataRep.gameState);

        return ResponseEntity.ok().body("instantiated");
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method is used to send the current version of the game saved on the server
     * @return a Load containing all the information about the game
     */
    @GetMapping(value = "/GetGame")
    public ResponseEntity<Load> getGame(){
        Load game = gameDataRep.getGame();
        return ResponseEntity.ok().body(game);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Send start data of a player to the game state.
     * @param playerDataString
     * @param playerNumber
     * @return
     */
    @PostMapping (value = "/sendStartData/{playerNumber}")
    public ResponseEntity<GameState> sendStartData(@RequestBody String playerDataString, @PathVariable("playerNumber") int playerNumber){

        ObjectMapper objectMapper = new ObjectMapper();
        PlayerStartData player;
        try {
            player = objectMapper.readValue(playerDataString, PlayerStartData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build(); // return a 400 status in case of parsing error
        }

        System.out.println("Im sending data");
        gameDataRep.sendStartGameData(player, playerNumber);
        //System.out.println(player);
        System.out.println("Printing Updated Data");
        System.out.println(gameDataRep.gameState.toString());

        return ResponseEntity.ok(gameDataRep.gameState);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Checks if all players picked.
     * @return
     */
    @GetMapping(value = "/allPlayersPicked")
    public ResponseEntity<Boolean> allPlayersPicked(){
        Boolean temp = gameDataRep.checkerAllPlayersPicked();
        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Tells the game data that this specific player has picked their start position
     * @param playerNumber
     * @return
     */
    @PostMapping(value = "/picked/{playerNumber}")
    public ResponseEntity<Boolean> picked(@PathVariable("playerNumber") int playerNumber){
        gameDataRep.gameData.getAllPickedList()[playerNumber] = true;
        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sends the upgradeshop and saves it in gamedata.
     * @param request
     * @return
     */
    @PostMapping(value = "/sendUpgradeCardsShop")
    public ResponseEntity<Boolean> sendUpgradeCardsShop(@RequestBody UpgradeCardsShopRequest request){
        gameDataRep.gameState.setUpgradeShopCards(request.getCards());

        System.out.println("Kommer upgrade kort " + gameDataRep.gameState);



        gameDataRep.gameData.getAllPlayerUpgrade()[request.getCurrentPlayer()] = true;


        if (gameDataRep.checkerAllPlayersUpgrade() < gameDataRep.gameData.getGameSettings().getNumberOfPlayers())
        {
            gameInfo.setCurrentPlayer(request.getNextPlayer());
        }else {
            System.out.println("købe runden er færdig");

        }




        System.out.println("my game info: " + gameInfo);

        gameInfo.setOpenShop(true);

        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets turn number
     * @return
     */
    @GetMapping(value = "/turnNumber")
    public ResponseEntity<Integer> turnNumber(){

        int turn = gameDataRep.checkerAllPlayersUpgrade();

        return ResponseEntity.ok().body(turn);

    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Send the bought upgrade cards to the game state.
     * @param playerUpgradeCards
     * @return
     */
    @PostMapping(value = "/sendBoughtUpgradeCard")
    public ResponseEntity<Boolean> sendBoughtUpgradeCard(@RequestBody SendUpgradeCards playerUpgradeCards){
        Command[][] newUpgradeCard = gameDataRep.gameState.getPlayerUpgradeCards();

        newUpgradeCard[playerUpgradeCards.getPlayerNumber()]= playerUpgradeCards.getCommands();

        gameDataRep.gameState.setPlayerUpgradeCards(newUpgradeCard);

        System.out.println("upgs: " + gameDataRep.gameState.toString());



        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Checks if all players has upgraded in a round.
     * @return
     */
    @GetMapping(value = "/allPlayersUpgraded")
    public ResponseEntity<Boolean> allPlayersUpgraded(){

        boolean temp = false;
        if(gameDataRep.checkerAllPlayersUpgrade() == gameDataRep.gameData.getGameSettings().getNumberOfPlayers()){
            temp = true;
        }


        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Changes the phase of the game state.
     * @param phase
     * @return
     */
    @PostMapping(value = "/changePhase")
    public ResponseEntity<Boolean> changePhase(@RequestBody Phase phase) {
        System.out.println("Changed game state to " + phase.name());

        gameDataRep.gameState.setPhase(phase);

        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sends over the 9 pulled cards and saves it in the game state.
     * @param pulledCards
     * @return
     */
    @PostMapping(value = "/sendPlayersPulledCards")
    public ResponseEntity<Boolean> sendPlayersPulledCards(@RequestBody Command[][] pulledCards) {


        gameDataRep.gameState.setPlayersPulledCards(pulledCards);

        return ResponseEntity.ok().body(true);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * sends over the 0-5 picked programming cards.
     * @param info
     * @return
     */
    @PostMapping(value = "/sendOverPickedCards")
    public ResponseEntity<Boolean> sendPlayersPulledCards(@RequestBody SendCurrentCards info) {

        Command[][] currentProgram = gameDataRep.gameState.getPlayerCurrentProgram();
        currentProgram[info.getPlayerNumber()] = info.getPickedCards();

        gameDataRep.gameState.setPlayerCurrentProgram(currentProgram);
        gameDataRep.gameData.getReadyList()[info.getPlayerNumber()] = true;

        System.out.println("1" + gameDataRep.gameState.getPlayerCurrentProgram()[info.getPlayerNumber()]);

        gameDataRep.gameState.setPlayersPulledCards(gameDataRep.removeCardPickedFromPulled(info.getPickedCards(), info.getPlayerNumber()));

        System.out.println(gameDataRep.gameState.getPlayerCurrentProgram()[info.getPlayerNumber()]);

        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Resets the ready list.
     * @return
     */
    @PostMapping(value = "/resetReadyList")
    public ResponseEntity<Boolean> resetReadyList() {

        gameDataRep.resetReadyList();



        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Tells that a player is ready to reset the ready list and they have already updated their data on the client side.
     * @param playerNumber
     * @return
     */
    @PostMapping(value = "/readyToReset/{playerNumber}")
    public ResponseEntity<Boolean> readyToReset(@PathVariable int playerNumber) {

        gameDataRep.gameData.getResetCounter()[playerNumber] = true;

        if(gameDataRep.checkerAllPlayerReset()){
            gameDataRep.readyAndReset();
        }

        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Tells that a player is ready.
     * @param playerNumber
     * @return
     */
    @PostMapping(value = "/readyReady/{playerNumber}")
    public ResponseEntity<Boolean> readyReady(@PathVariable int playerNumber) {

        System.out.println("the number in server: " + playerNumber);

        gameDataRep.gameData.getReadyList()[playerNumber] = true;

        System.out.println("Test1: " + gameDataRep.gameData.getReadyList()[0]);
        System.out.println("Test2: " + gameDataRep.gameData.getReadyList()[1]);


        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * A players sends in their position to the game state.
     * @param payload
     * @return
     */
    @PostMapping(value = "/sendPosition")
    public ResponseEntity<Boolean> sendPosition(@RequestBody Map<String, Object> payload) {
        int playerNumber = (int) payload.get("playerNumber");
        int x = (int) payload.get("x");
        int y = (int) payload.get("y");
        int currUpgrade = (int) payload.get("currUpgrade");
        Heading heading = Heading.valueOf((String) payload.get("heading")); // .valueOf() method returns the enum constant of the specified enum type with the specified name

        gameDataRep.gameState.getPlayersXPosition()[playerNumber] = x;
        gameDataRep.gameState.getPlayersYPosition()[playerNumber] = y;
        gameDataRep.gameState.getPlayerHeadings()[playerNumber] = heading;

        gameDataRep.gameData.setCurrentPlayerUpgrade(currUpgrade);

        gameInfo.setOpenShop(false);

        //Temporary place for resetting players has chosen in interactive cards, so we can you use 1 interactive card each round of play
        gameInfo.setPlayerChosen(false);

        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Resets upgrade ready list.
     * @return
     */
    @PostMapping(value = "/resetUpgradeList")
    public ResponseEntity<Boolean> resetUpgraddeList() {

        gameDataRep.resetAllUpgrade();



        return ResponseEntity.ok().body(true);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sets the player who is currently upgrading.
     * @param player
     * @return
     */
    @PostMapping (value = "/setCurrentUpgradePlayer")
    public ResponseEntity<Integer> setCurrentUpgradePlayer(@RequestParam("player") String player) {
        int numberPlayer = Integer.parseInt(player);

        gameDataRep.gameData.setCurrentPlayerUpgrade(numberPlayer);

        return ResponseEntity.ok().body(5);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Gets the current upgrade player.
     * @return
     */
    @GetMapping(value = "/currentUpgradePlayer")
    public ResponseEntity<Integer> currentUpgradePlayer(){

        int temp = gameDataRep.gameData.getCurrentPlayerUpgrade();


        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Removes the current programs for palyers.
     * @return
     */
    @PostMapping (value = "/removeCurrProgram")
    public ResponseEntity<Integer> removeCurrProgram() {

        gameDataRep.removeCurrProgram();

        return ResponseEntity.ok().body(5);
    }

    //Server save and load
    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     * This method is used to send the current version of the game saved on the server
     * @return a Load containing all the information about the game
     */
    @GetMapping(value = "/getSave/{saveName}")
    public ResponseEntity<Load> getSave(@PathVariable String saveName) {

        System.out.println("we have been asked for : " + saveName + " saveFile");
        Load load = gameDataRep.getSave(saveName);
        System.out.println("We found file sending a Load of it");
        return ResponseEntity.ok().body(load);
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     *
     */
    @PostMapping (value = "/addSave/{saveName}")
    public ResponseEntity<String> saveGame(@PathVariable String saveName) {

        System.out.println("add a save request recieved for: " + saveName);
        gameDataRep.saveAGame(saveName);
        //gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Save made!");

        return ResponseEntity.ok().body("gameSaved");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Get save names from save files.
     * @return
     */
    @GetMapping(value = "/getSaveNames")
    public ResponseEntity<String[]> getSaveNames() {

        System.out.println("we have been asked for saveNames");
        String[] strings = gameDataRep.getNames();
        System.out.println("We have found saveGames: " + strings.toString());
        return ResponseEntity.ok().body(strings);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Checks if a game is loaded or not.
     * @return
     */
    @GetMapping(value = "/isLoadedGame")
    public ResponseEntity<Boolean> isLoadedGame() {

        boolean temp = gameInfo.isLoadedGame();

        return ResponseEntity.ok().body(temp);
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Get the current loaded.
     * @return
     */
    @GetMapping(value = "/getCurrLoaded")
    public ResponseEntity<Integer> getCurrLoaded() {

        int temp = gameInfo.getCurrLoaded();

        gameInfo.setCurrLoaded(gameInfo.getCurrLoaded() + 1);

        return ResponseEntity.ok().body(temp);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sets the game state from a load object.
     * @param load
     * @return
     */
    @PostMapping (value = "/setGameState")
    public ResponseEntity<String> saveGame(@RequestBody Load load) {

        gameDataRep.gameState = gameDataRep.makeGameState(load);

        return ResponseEntity.ok().body("gameSaved");
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Sets the checkpoints for a player
     * @param playerNumberString
     * @return
     */
    @PostMapping (value = "/setCheckpointsForPlayer")
    public ResponseEntity<Boolean> setCheckpointsForPlayer(@RequestBody String playerNumberString) {

        int playerNumber = Integer.parseInt(playerNumberString);



        gameDataRep.gameState.getPlayerCheckPoints()[playerNumber] = gameDataRep.gameState.getPlayerCheckPoints()[playerNumber] + 1;


        System.out.println("PlayerNumber: " +  playerNumber  + " checkpoints: " + gameDataRep.gameState.getPlayerCheckPoints()[playerNumber] );

        return ResponseEntity.ok().body(true);
    }


    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Used for interactive cards. Has a player made choice concernings their interactive card.
     * @return
     */
    @GetMapping(value = "/isPlayerChoice")
    public ResponseEntity<Boolean> isPlayerChoice() {

        return ResponseEntity.ok(gameInfo.isPlayerChosen());
    }

    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Used for interactive cards, sets the heading for a player.
     * @param playerData
     * @return
     */
    @PostMapping (value = "/setPlayerHeadingInteractive")
    public ResponseEntity<String> setPlayerHeadingInteractive(@RequestBody Map<String, Object> playerData) {
        ObjectMapper objectMapper = new ObjectMapper();
        Heading heading = objectMapper.convertValue(playerData.get("heading"), Heading.class);
        int playerNumber = Integer.parseInt(playerData.get("playerNumber").toString());

        gameDataRep.gameState.getPlayerHeadings()[playerNumber] = heading;
        gameInfo.setPlayerChosen(true);

        return ResponseEntity.ok().body("bob");
    }




}
