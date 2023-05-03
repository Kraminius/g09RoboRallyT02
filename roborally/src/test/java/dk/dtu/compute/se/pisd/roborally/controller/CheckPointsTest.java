package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
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



        Board board = new Board();
        board.width = 8;
        board.height = 8;
        board.spaces = new Space[board.width][board.height];
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                board.spaces[x][y] = new Space(board, x, y);
            }
        }
        //Giving it checkpoints
        for(int i = 0; i < 3; i++){
            SpaceElement checkpointSpace = new SpaceElement();
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setNumber(i+1);
            checkpointSpace.setCheckpoint(checkpoint);
            board.getSpace(5+i,5+i).setElement(checkpointSpace);
        }
        SpaceElement respawn = new SpaceElement();
        respawn.setRespawn(true);
        board.getSpace(4, 4).setElement(respawn);

        gameController = new GameController(board);

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, null, "Player " + i, i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            Assertions.assertEquals(player, board.getSpace(i, i).getPlayer(), "Player " + player.getName() + " should be at Space (" + i + "," + i + ")!");
            Assertions.assertEquals(SOUTH, player.getHeading(), "Player " + player.getName() + " should be heading SOUTH!");
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
        boolean[] checkpointStat2 = second.getCheckpointReadhed();
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
        Assertions.assertFalse(checkpointStat2[0], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 1");
        Assertions.assertFalse(checkpointStat2[1], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 2");

        //Player should after activating checkpoints get checkpoint 2 (has already 1)
        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[1], "Player " + current.getName() + " Should have reached" +
                "checkpoint 2");
        Assertions.assertFalse(checkpointStat2[0], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 1");
        Assertions.assertFalse(checkpointStat2[1], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 2");

        //Testing you cant get check 2 before 1
        current.setSpace(board.getSpace(1,1));
        second.setSpace(board.getSpace(6,6));
        gameController.activateCheckpoints();
        Assertions.assertTrue(checkpointStatus[1], "Player " + current.getName() + " Should have reached" +
                "checkpoint 2");
        Assertions.assertFalse(checkpointStat2[0], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 1");
        Assertions.assertFalse(checkpointStat2[1], "Player: " + second.getName() + "should NOT have reached " +
                "checkpoint 2");

    }
}