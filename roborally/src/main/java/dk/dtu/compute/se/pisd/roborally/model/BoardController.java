package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.BoardLoader;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameSave;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    @Autowired
    private IBoardService boardService;

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

}
