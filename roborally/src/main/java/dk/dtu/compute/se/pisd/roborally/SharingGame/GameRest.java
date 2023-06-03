package dk.dtu.compute.se.pisd.roborally.SharingGame;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.LoadInstance;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameRest {


    @Autowired
    GameRep gameRep;

    @GetMapping(value = "/Game")
    public ResponseEntity<JSONObject> getGame(){
        JSONObject obj = gameRep.getJsonGame();
        System.out.println("We are sending a JSON\n" + obj);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/Game")
    public ResponseEntity<String > addProduct(@RequestBody JSONObject json) {
        //Now we load in the game
        //Needs acces to AppController
        System.out.println("We have received a JSON\n" + json);
        Load load = GameUnpack.getGame(json);
        boolean yes = true;
        //boolean gotIt = LoadInstance.load(app, load);
        if(yes)
            return ResponseEntity.ok().body("Got JSON");
        else
            return ResponseEntity.internalServerError().body("didnt get JSON");

    }


}
