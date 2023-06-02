package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameSave;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameControllerRest {

    @Autowired
    private IGameControllerService gameControllerService;

    @GetMapping("/Game")
    public ResponseEntity<JSONObject> getGame() {
        //JSONObject obj = GameControllerService.getGame();
        JSONObject obj = GameControllerService.getGame();
        return ResponseEntity.ok().body(obj);

    }

    /*

    @GetMapping("/Board")
    public ResponseEntity<Board> getBoard() {
        Board board = boardService.getBoard();
        return ResponseEntity.ok().body(board);

    }

    @PostMapping("/Board")
    public ResponseEntity<String > setBoard(@RequestBody Board b) {
        boolean added = BoardService.setBoard(b);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

     */

}
