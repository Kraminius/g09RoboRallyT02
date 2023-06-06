package dk.dtu.compute.se.pisd.roborally;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonString;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Collection;

@RestController
public class MyRest {


    @Autowired
    GameDataRep gameDataRep;


    @PostMapping(value = "/connected")
    public ResponseEntity<String> connector(@RequestBody String playerNumStr){
        int playerNum = Integer.parseInt(playerNumStr);  // convert the player number from string to int
        gameDataRep.gameData.readyList[playerNum] = true;
        System.out.println(gameDataRep.gameData.readyList[playerNum]);
        return ResponseEntity.ok().body("we connected");
    }


    @GetMapping(value = "/allConnected")
    public ResponseEntity<Boolean> allConnected(@RequestParam("playerNum") String playerNumStr) {
        Boolean temp = gameDataRep.checkerPlayersConnected();
        return ResponseEntity.ok().body(temp);
    }

    @PostMapping (value = "/instaGameData")
    public ResponseEntity<Integer> instaGameData(@RequestParam("numberOfPlayers") String playerNumStr) {
        int numberOfPlayers = Integer.parseInt(playerNumStr);
        gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Size: " + gameDataRep.gameData.readyList.length + "& " + gameDataRep.gameData.numPlayers);
        return ResponseEntity.ok().body(5);
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
        return ResponseEntity.ok().body("instantiated");
    }


    @GetMapping(value = "/getPlayerNumber")
    public ResponseEntity<Integer> allConnected() {
        Integer temp = gameDataRep.playerNumber();
        return ResponseEntity.ok().body(temp);
    }


    @PostMapping (value = "/addMapName")
    public ResponseEntity<Integer> instaGameName(@RequestParam("mapName") String map) {
        gameDataRep.gameData.setCurrentGameMap(map);
        //System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

    @GetMapping(value = "/getMapName")
    public ResponseEntity<String> getMapName() {

        String temp = gameDataRep.gameData.getCurrentGameMap();
        return ResponseEntity.ok().body(temp);
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
 * @author Nicklas Christensen     s224314.dtu.dk
 * @param commands the cards which we are updating in the gameState
 * @param player the player who's cards we are updating
 * @return wether we succeded or not at recieving the game
 */
    @PostMapping (value = "/updateProgramCards/{player}")
    public ResponseEntity<String> updateProgramCards(@PathVariable int player, @RequestBody Command[] commands) {

        System.out.println("Have recieved Post of programCards from player: " + player);
        gameDataRep.newProgramCards(commands, player);
        //gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("cards have been updated");
        return ResponseEntity.ok().body("acknowledged");
    }

    @GetMapping(value = "/phase")
    public ResponseEntity<Phase> getPhase(){
        System.out.println("We have been asked what phase it is");
        Phase phase = gameDataRep.getPhase();
        System.out.println("Phase found its :" + phase);
        return ResponseEntity.ok().body(phase);
    }



}
