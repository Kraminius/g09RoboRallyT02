package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class BoardTest {

    @BeforeEach
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File boardFile = new File("roborally/src/main/resources/files/empty.json");
        Board board = objectMapper.readValue(boardFile, Board.class);

        GameController gameController = new GameController(board);
        for(int i = 0; i < 6; i++){
            Player player = new Player(board, null, "Player" + i, i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
        }
    }
}
