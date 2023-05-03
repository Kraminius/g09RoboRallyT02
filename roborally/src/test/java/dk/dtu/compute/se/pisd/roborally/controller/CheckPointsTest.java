package dk.dtu.compute.se.pisd.roborally.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

class CheckPointsTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() throws IOException {
        BoardTest board = new BoardTest();
        board.boardTest(10,10);
        gameController = new GameController(board.board);

        //Lets try this
        //giving it checkpoints
        for(int k = 0; k < 3; k++) {
            SpaceElement checkpointSpace = new SpaceElement();
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setNumber(k+1);
            checkpointSpace.setCheckpoint(checkpoint);
            board.getSpace(5+k, 5+k).setElement(checkpointSpace);
        }

        //Giving it players
        for (int i = 0; i < 3; i++) {
            Player player = new Player(board.board, null,"Player " + i, i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, (i)));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }


    @Test
    void  collectCheckpoint() {

        Board board = gameController.board;


        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);

        //Sets player at first checkpoint
        current.setSpace(board.getSpace(5,5));
        boolean[] checkpointStatus = current.getCheckpointReadhed();
        Assertions.assertFalse(checkpointStatus[0], "Player " + current.getName() + " Should NOT have reached" +
                "checkpoint 1");

        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[0], "Player " + current.getName() + " Should have reached" +
                "checkpoint 1");

        //Move current player to checkpoint 2
        current.setSpace(board.getSpace(6,6));
        Assertions.assertTrue(checkpointStatus[0], "Player " + current.getName() + " Should have reached" +
                "checkpoint 1");
        Assertions.assertFalse(checkpointStatus[1], "Player " + current.getName() + " Should NOT have reached" +
                "checkpoint 2");

        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[1], "Player " + current.getName() + " Should have reached" +
                "checkpoint 2");

        current.setSpace(board.getSpace(7,7));
        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[0], "Player " + current.getName() + " Should have reached" +
                "checkpoint 1");
        Assertions.assertTrue(checkpointStatus[0], "Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 1");
        Assertions.assertTrue(checkpointStatus[1], "Player " + current.getName() + " Should have reached" +
                "checkpoint 2");
        Assertions.assertTrue(checkpointStatus[1], "Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 2");
        Assertions.assertTrue(checkpointStatus[2], "Player " + current.getName() + " Should have reached" +
                "checkpoint 3");
        Assertions.assertTrue(checkpointStatus[2], "Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 3");

        second.setSpace(board.getSpace(6,6));
        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[0], "Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 1");
        Assertions.assertTrue(checkpointStatus[0], "Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 2");
    }
}