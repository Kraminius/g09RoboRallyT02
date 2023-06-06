package dk.dtu.compute.se.pisd.roborally;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonString;
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

    //instantiating a gamestate to run from
    @PostMapping (value = "/instaGameState")
    public ResponseEntity<String> instaGameData(@RequestBody JSONObject game) {

        //JSONObject newGame = new JSONObject(game);
        //int numberOfPlayers = Integer.parseInt(playerNumStr);
        System.out.println("insta request recieved");
        gameDataRep.instantiateGameState(game);
        //gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Instantiated a gameState");
        return ResponseEntity.ok().body("instantiated");
    }



    /*
    @RequestMapping(value = "/instaGameState", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PostMapping(value = "/instaGameState", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<String> handleOctetStream(@RequestBody JSONObject payload) {
        //JSONObject newGame = new JSONObject(game);
        //int numberOfPlayers = Integer.parseInt(playerNumStr);
        System.out.println("insta request recieved");
        gameDataRep.instantiateGameState(payload);
        //gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Instantiated a gameState");
        return ResponseEntity.ok().body("instantiated");
    }
         */


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

    @PostMapping(value = "/sendingGame")
    public ResponseEntity<String> postGame(@RequestBody JSONObject newGame){
        gameDataRep.setCurrentGame(newGame);
        return ResponseEntity.ok().body("acknowledged");
    }

    @GetMapping(value = "/GetGame")
    public ResponseEntity<JSONObject> getGame(){
        JSONObject game = gameDataRep.getGame();
        return ResponseEntity.ok().body(game);
    }





}
