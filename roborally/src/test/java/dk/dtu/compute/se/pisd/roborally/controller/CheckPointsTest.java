package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

class CheckPointsTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for(int i = 0; i < 3; i++) {
            Checkpoint checkpoint = new Checkpoint(i);
            board.getSpace((i+2), (i+1)).checkpoint = checkpoint;
        }
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
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
        current.setSpace(board.getSpace(2,1));

        Assertions.assertEquals(false, current.getCheckpointReadhed(0),"Player " + current.getName() + " Should NOT have reached" +
                "checkpoint 1");

        gameController.activateCheckpoints();

        Assertions.assertEquals(true, current.getCheckpointReadhed(0),"Player " + current.getName() + " Should have reached" +
                "checkpoint 1");

        current.setSpace(board.getSpace(3,2));
        gameController.activateCheckpoints();
        Assertions.assertEquals(true, current.getCheckpointReadhed(0),"Player " + current.getName() + " Should have reached" +
                "checkpoint 1");
        Assertions.assertEquals(true, current.getCheckpointReadhed(1),"Player " + current.getName() + " Should have reached" +
                "checkpoint 2");

        current.setSpace(board.getSpace(4,3));
        gameController.activateCheckpoints();
        Assertions.assertEquals(true, current.getCheckpointReadhed(0),"Player " + current.getName() + " Should have reached" +
                "checkpoint 1");
        Assertions.assertEquals(true, current.getCheckpointReadhed(1),"Player " + current.getName() + " Should have reached" +
                "checkpoint 2");
        Assertions.assertEquals(true, current.getCheckpointReadhed(2),"Player " + current.getName() + " Should have reached" +
                "checkpoint 3");

        second.setSpace(board.getSpace(3,2));
        gameController.activateCheckpoints();
        Assertions.assertEquals(false, second.getCheckpointReadhed(0),"Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 1");
        Assertions.assertEquals(false, second.getCheckpointReadhed(1),"Player " + second.getName() + " Should NOT have reached" +
                "checkpoint 2");

    }




}