package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class MyRest {


    @Autowired
    GameDataRep gameDataRep;

    @Autowired
    GameRepository gameRepository;


    @Autowired
    GameInfo gameInfo;



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

    @PostMapping(value = "/addGame")
    public ResponseEntity<String> addGame(@RequestBody String[] settings){

        gameRepository.createGame(settings[0],settings[1], Integer.parseInt(settings[2]), settings[3]);

        String lobbyID = UUID.randomUUID().toString();

        gameInfo.instaGameInfo(lobbyID, gameRepository.getGameSettings());

        System.out.println(gameInfo.toString());


        System.out.println(gameRepository.getGameSettings().toString());

        return ResponseEntity.ok("hej");
    }


    @GetMapping(value = "/getGameLobby", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameLobby> getGameLobby(){
        GameLobby gameLobby = gameInfo.convertGameInfoToGameLobby(gameInfo);
        return ResponseEntity.ok(gameLobby);
    }



    @GetMapping(value = "/isGameRunning")
    public ResponseEntity<Boolean> isGameRunning(){


        return ResponseEntity.ok().body(gameInfo.isGameRunning());

    }


}
