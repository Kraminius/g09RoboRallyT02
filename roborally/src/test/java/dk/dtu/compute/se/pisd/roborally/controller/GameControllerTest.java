package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
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
    void movePlayerForwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space respawnToken = board.getRebootToken();
        String respawnTokenCoordinates = "(" + respawnToken.x + "," + respawnToken.y + ")";

        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");

        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");

        current.setSpace(board.getSpace(0, 7));
        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should be on (0,7)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,6) should be empty!");

        //This part of the test will move the player outside of the board. The player should be moved to the respawnToken
        gameController.moveForward(current);
        Assertions.assertEquals(current, respawnToken.getPlayer(), "Player " + current.getName() + " should be respawned to" + respawnTokenCoordinates + "!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,7) should be empty!");
    }

    @Test
    void turnPlayerRightTest() {
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
    void turnPlayerLeftTest() {
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
    void fastPlayerForwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space respawnToken = board.getRebootToken();
        String respawnTokenCoordinates = "(" + respawnToken.x + "," + respawnToken.y + ")";

        //player should move 2 squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0,2)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");

        //player should move 2 additional squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 4).getPlayer(), "Player " + current.getName() + " should be at Space (0,4)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");
        Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");

        //player should move 2 additional squares.
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(0, 6).getPlayer(), "Player " + current.getName() + " should be at Space (0,6)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");
        Assertions.assertNull(board.getSpace(0, 5).getPlayer(), "Space (0,5) should be empty!");

        //This part of the test will move the player outside the board. The player should be moved to the respawnToken
        gameController.fastForward(current);
        Assertions.assertEquals(current, respawnToken.getPlayer(), "Player " + current.getName() + " should be at Space " + respawnTokenCoordinates + "!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,6) should be empty!");
    }

    @Test
    void backUpTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Space respawnToken = board.getRebootToken();
        String respawnTokenCoordinates = "(" + respawnToken.x + "," + respawnToken.y + ")";

        //Set the players start space at the end of the board.
        current.setSpace(board.getSpace(0, 3));
        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should be at Space (0,3)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");

        gameController.backUp(current);
        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should be at Space (0,2)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");

        gameController.backUp(current);
        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should be at Space (0,1)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");

        gameController.backUp(current);
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should be at Space (0,0)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");

        //This part of the test will move the player outside the board. The player should be moved to the respawnToken
        gameController.backUp(current);
        Assertions.assertEquals(current, respawnToken.getPlayer(), "Player " + current.getName() + " should be at Space " + respawnTokenCoordinates + "!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");

    }

    @Test
    void performPlayerUTurnTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.uTurn(current);
        Assertions.assertEquals(NORTH, current.getHeading(), "Player " + current.getName() + " should be heading NORTH");

        gameController.uTurn(current);
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH");

        current.setHeading(EAST);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading EAST");

        gameController.uTurn(current);
        Assertions.assertEquals(WEST, current.getHeading(), "Player " + current.getName() + " should be heading WEST");

        gameController.uTurn(current);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading EAST");


    }
}