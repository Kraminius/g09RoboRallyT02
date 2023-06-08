package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.PlayerStartData;
import dk.dtu.compute.se.pisd.roborally.model.Space;
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


    @GetMapping(value = "/allConnected")
    public ResponseEntity<Boolean> allConnected() {
        Boolean temp = gameDataRep.checkerPlayersConnected();
        return ResponseEntity.ok().body(temp);
    }

    @GetMapping(value = "/getLobbyId")
    public ResponseEntity<String> getLobbyId() {
        String temp = gameDataRep.gameData.getId();
        return ResponseEntity.ok().body(temp);
    }

    @PostMapping (value = "/pressJoinButton")
    public ResponseEntity<String> pressJoinButton() {

        gameInfo.setJoinButtonPressed(true);
        return ResponseEntity.ok().body("hej");
    }

    @GetMapping (value = "/isJoinButton")
    public ResponseEntity<Boolean> isJoinButton() {


        return ResponseEntity.ok().body(gameInfo.isJoinButtonPressed());
    }


    @PostMapping (value = "/instaGameData")
    public ResponseEntity<Integer> instaGameData(@RequestParam("numberOfPlayers") String playerNumStr) {
        int numberOfPlayers = Integer.parseInt(playerNumStr);
        gameDataRep.instantiateGameData(numberOfPlayers);
        gameInfo.instaGameInfo(gameDataRep.gameData.getId(), gameDataRep.gameData.getGameSettings());
        System.out.println("Size: " + gameDataRep.gameData.getReadyList().length + "& " + gameDataRep.gameData.getGameSettings().getNumberOfPlayers());
        return ResponseEntity.ok().body(5);
    }


    @GetMapping(value = "/getPlayerNumber")
    public ResponseEntity<Integer> allConnectedNot() {
        Integer temp = gameDataRep.playerNumber();
        return ResponseEntity.ok().body(temp);
    }


    @PostMapping (value = "/addMapName")
    public ResponseEntity<Integer> instaGameName(@RequestParam("mapName") String map) {
        gameDataRep.gameData.getGameSettings().setGameName(map);
        //System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

    @GetMapping(value = "/getMapName")
    public ResponseEntity<String> getMapName() {

        String temp = gameDataRep.gameData.getGameSettings().getGameName();
        return ResponseEntity.ok().body(temp);
    }

    @PostMapping(value = "/addGame")
    public ResponseEntity<String> addGame(@RequestBody String[] settings){

        gameDataRep.createGame(settings[0],settings[1], Integer.parseInt(settings[2]), settings[3]);

        gameInfo.instaGameInfo(gameDataRep.gameData.getId(), gameDataRep.gameData.getGameSettings());
        //System.out.println(gameRepository.getGameSettings().toString());

        return ResponseEntity.ok("hej");
    }


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

    @GetMapping(value = "/getAllPlayers")
    public ResponseEntity<ArrayList<String>> getPlayerNames(){

        return ResponseEntity.ok(gameDataRep.gameData.getGameSettings().getPlayerNames());


    }




    @GetMapping(value = "/isGameRunning")
    public ResponseEntity<Boolean> isGameRunning(){


        return ResponseEntity.ok().body(gameDataRep.gameData.isGameRunning());

    }


    @GetMapping(value = "/getCurrentPlayer")
    public ResponseEntity<Integer> getCurrentPlayer(){


        return ResponseEntity.ok().body(gameInfo.getCurrentPlayer());

    }

    @PostMapping(value = "/nextPlayer")
    public ResponseEntity<String> nextPlayer(){

        gameInfo.nextPlayer();

        //System.out.println(gameRepository.getGameSettings().toString());

        return ResponseEntity.ok("hej");
    }

    @PostMapping (value = "/addStartPosition")
    public ResponseEntity<Integer> addStartPosition(@RequestParam("pos") int pos) {
        gameInfo.addStartPosition(pos);
        //System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

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


    @GetMapping(value = "/allPlayersPicked")
    public ResponseEntity<Boolean> allPlayersPicked(){
        Boolean temp = gameDataRep.checkerAllPlayersPicked();
        return ResponseEntity.ok().body(temp);
    }

    @PostMapping(value = "/picked/{playerNumber}")
    public ResponseEntity<Boolean> picked(@PathVariable("playerNumber") int playerNumber){
        gameDataRep.gameData.getAllPickedList()[playerNumber] = true;
        return ResponseEntity.ok().body(true);
    }

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

    @GetMapping(value = "/getSaveNames")
    public ResponseEntity<String[]> getSaveNames() {

        System.out.println("we have been asked for saveNames");
        String[] strings = gameDataRep.getNames();
        System.out.println("We have found saveGames: " + strings.toString());
        return ResponseEntity.ok().body(strings);
    }


}
