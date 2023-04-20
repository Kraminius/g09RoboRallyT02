package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;
import static org.testng.AssertJUnit.*;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
        }

        gameController.board.getSpace(1,0).setWallHeading(new Heading[]{Heading.WEST, Heading.EAST, SOUTH, NORTH});
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    /*@Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }*/

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");

        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");

        current.setSpace(board.getSpace(0,7));
        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should be on (0,7)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,6) should be empty!");

        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should still be in space (0,7)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,7) should be empty!");
    }

    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);
        Assertions.assertEquals(WEST, current.getHeading(), "Player " + current.getName() + " should be heading West");

        gameController.turnRight(current);
        Assertions.assertEquals(NORTH, current.getHeading(), "Player " + current.getName() + " should be heading North");

        gameController.turnRight(current);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading East");

        gameController.turnRight(current);
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading South");
    }

    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading East");

        gameController.turnLeft(current);
        Assertions.assertEquals(NORTH, current.getHeading(), "Player " + current.getName() + " should be heading North");

        gameController.turnLeft(current);
        Assertions.assertEquals(WEST, current.getHeading(), "Player " + current.getName() + " should be heading West");

        gameController.turnLeft(current);
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading South");
    }

    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        //player should move 2 squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0,2)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,1) should be empty!");

        //player should move 2 additional squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 4).getPlayer(), "Player " + current.getName() + " should be at Space (0,4)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,2) should be empty!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,3) should be empty!");

        //player should move 2 additional squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 6).getPlayer(), "Player " + current.getName() + " should be at Space (0,6)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,4) should be empty!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,5) should be empty!");

        //Player should not move.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0,6).getPlayer(),"Player " + current.getName() + " should be at Space (0,6)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,4) should be empty!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,5) should be empty!");
    }

    @Test
    void getSpaceAtTest(){
        Board board = gameController.board;
        Player player0 = board.getPlayer(0);
        Player player1 = board.getPlayer(1);
        Player player2 = board.getPlayer(2);
        player0.setSpace(gameController.board.getSpace(0,0));
        player1.setSpace(gameController.board.getSpace(3,0));
        player2.setSpace(board.getSpace(1,0));
        gameController.moveForward(player2);
        player0.setHeading(NORTH);
        player1.setHeading(EAST);
        gameController.moveForward(player0);
        gameController.moveForward(player1);

        assertNull(gameController.getSpaceAt(1, NORTH, 0,0));

    }

    @Test
    void moveForwardTest(){
        Board board = gameController.board;
        Player player0 = board.getPlayer(0);
        Player player1 = board.getPlayer(1);
        Player player2 = board.getPlayer(2);
        player0.setSpace(gameController.board.getSpace(0,0));
        player1.setSpace(gameController.board.getSpace(3,0));
        player2.setSpace(board.getSpace(1,0));
        gameController.moveForward(player2);
        player0.setHeading(EAST);
        player1.setHeading(EAST);
        gameController.moveForward(player0);
        gameController.moveForward(player1);

        assertTrue(gameController.obstacleInSpace(board.getSpace(0,0), board.getSpace(1,0)));
        assertEquals(player0, gameController.board.getSpace(0,0).getPlayer());
        assertEquals(null, gameController.board.getSpace(3,0).getPlayer());
        assertEquals(player1, board.getSpace(4,0).getPlayer());
        assertEquals(player2, board.getSpace(1,0).getPlayer());
    }

    @Test
    void obstacleInSpaceTest(){
        Board board = gameController.board;
        Player player0 = board.getPlayer(0);
        Player player1 = board.getPlayer(1);
        Player player2 = board.getPlayer(2);
        player0.setSpace(gameController.board.getSpace(0,0));
        player1.setSpace(gameController.board.getSpace(3,0));
        player2.setSpace(board.getSpace(1,0));
        gameController.moveForward(player2);
        player0.setHeading(EAST);
        player1.setHeading(EAST);
        gameController.moveForward(player0);
        gameController.moveForward(player1);

        assertTrue(gameController.obstacleInSpace(board.getSpace(0,0), board.getSpace(1,0)));
        assertEquals(player0, gameController.board.getSpace(0,0).getPlayer());
        assertEquals(null, gameController.board.getSpace(3,0).getPlayer());
        assertEquals(player1, board.getSpace(4,0).getPlayer());
        assertEquals(player2, board.getSpace(1,0).getPlayer());

    }

    /*
    @Test
    void executeCommand() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.executeCommandForTest(current, Command.FORWARD);
        Assertions.assertEquals(current, board.getSpace(0,1).getPlayer(), "Player " + current.getName() + " should have moved 1 space to (0,1)");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Players heading should be SOUTH!");
        gameController.executeCommandForTest(current, Command.LEFT);
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should not have moved and is still on (0,1)");
        Assertions.assertEquals(EAST, current.getHeading(), "Players heading should be EAST!");
        gameController.executeCommandForTest(current, Command.FAST_FORWARD);
        Assertions.assertEquals(current, board.getSpace(2,1).getPlayer(), "Player " + current.getName() + " should have moved 2 spaces to (2,1)");
        Assertions.assertEquals(EAST, current.getHeading(), "Players heading should be EAST!");
        gameController.executeCommandForTest(current, Command.RIGHT);
        Assertions.assertEquals(current, board.getSpace(2,1).getPlayer(), "Player " + current.getName() + " should not have moved and is still on (2,1)");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Players heading should be SOUTH!");
    }

     */




}