package dk.dtu.compute.se.pisd.roborally;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        // Now you can use playerNumStr
        Boolean temp = gameDataRep.checkerPlayersConnected();
        return ResponseEntity.ok().body(temp);
    }

    @PostMapping (value = "/instaGameData")
    public ResponseEntity<Integer> instaGameData(@RequestParam("numberOfPlayers") String playerNumStr) {
        // Now you can use playerNumStr
        int numberOfPlayers = Integer.parseInt(playerNumStr);
        gameDataRep.instantiateGameData(numberOfPlayers);
        System.out.println("Size: " + gameDataRep.gameData.readyList.length + "& " + gameDataRep.gameData.numPlayers);
        return ResponseEntity.ok().body(5);
    }


    @GetMapping(value = "/getPlayerNumber")
    public ResponseEntity<Integer> allConnected() {
        // Now you can use playerNumStr
        Integer temp = gameDataRep.playerNumber();
        return ResponseEntity.ok().body(temp);
    }


    @PostMapping (value = "/addMapName")
    public ResponseEntity<Integer> instaGameName(@RequestParam("mapName") String map) {
        // Now you can use playerNumStr

        gameDataRep.gameData.setCurrentGameMap(map);
        System.out.println("Name: " + gameDataRep.gameData.getCurrentGameMap());
        return ResponseEntity.ok().body(5);
    }

    @GetMapping(value = "/getMapName")
    public ResponseEntity<String> getMapName() {

        String temp = gameDataRep.gameData.getCurrentGameMap();
        return ResponseEntity.ok().body(temp);
    }

}
