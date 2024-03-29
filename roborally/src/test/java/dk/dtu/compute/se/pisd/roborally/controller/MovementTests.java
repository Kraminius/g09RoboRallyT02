package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.controller.Exceptions.OutsideBoardException;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MovementTests {
    private GameController gameController;

    @BeforeEach
    public void setUp() {

        Board board = new Board();
        board.width = 8;
        board.height = 8;
        board.spaces = new Space[board.width][board.height];
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                board.spaces[x][y] = new Space(board, x, y);
            }
        }
        SpaceElement respawn = new SpaceElement();
        respawn.setRespawn(true);
        board.getSpace(4, 4).setElement(respawn);

        gameController = new GameController(board);

        for (int i = 0; i < 6; i++) {
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
    void moveTwoForwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        try {
            gameController.fastForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
            Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.fastForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 4).getPlayer(), "Player " + current.getName() + " should beSpace (0,4)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");
            Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        current.setSpace(board.getSpace(0, 7));
        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should be on (0,7)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");

        try {
            gameController.fastForward(current);
            Assertions.assertNull(board.getSpace(0, 7).getPlayer(), "Space (0,7) should be empty!");
            Assertions.assertNotNull(board.getSpace(4, 4).getPlayer(), "Space (4,4) should not be empty!");
            Assertions.assertEquals(current, board.getSpace(4, 4).getPlayer(), "Player " + current.getName() + " should be on (4,4)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH!");
        } catch (OutsideBoardException e) {
            Assertions.assertEquals("Outside board", e.getMessage());
        }
    }

    @Test
    void moveOneForwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        try {
            gameController.moveForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should beSpace (0,1)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.moveForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        current.setSpace(board.getSpace(0, 7));
        Assertions.assertEquals(current, board.getSpace(0, 7).getPlayer(), "Player " + current.getName() + " should be on (0,7)!");
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,6) should be empty!");

        try {
            gameController.moveForward(current);
            Assertions.assertNull(board.getSpace(0, 7).getPlayer(), "Space (0,7) should be empty!");
            Assertions.assertNotNull(board.getSpace(4, 4).getPlayer(), "Space (4,4) should not be empty!");
            Assertions.assertEquals(current, board.getSpace(4, 4).getPlayer(), "Player " + current.getName() + " should be on (4,4)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH!");
        } catch (OutsideBoardException e) {
            Assertions.assertEquals("Outside board", e.getMessage());
        }
    }

    @Test
    void moveThreeForwardTest() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        try {
            gameController.sprintForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
            Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
            Assertions.assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,0) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.sprintForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 6).getPlayer(), "Player " + current.getName() + " should beSpace (0,6)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");
            Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");
            Assertions.assertNull(board.getSpace(0, 5).getPlayer(), "Space (0,5) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.sprintForward(current);
            Assertions.assertNull(board.getSpace(0, 6).getPlayer(), "Space (0,6) should be empty!");
            Assertions.assertNotNull(board.getSpace(4, 4).getPlayer(), "Space (4,4) should not be empty!");
            Assertions.assertEquals(current, board.getSpace(4, 4).getPlayer(), "Player " + current.getName() + " should be on (4,4)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH!");
        } catch (OutsideBoardException e) {
            Assertions.assertEquals("Outside board", e.getMessage());
        }
    }

    @Test
    void turnLeftTest(){
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
    void turnRightTest(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);
        Assertions.assertEquals(WEST, current.getHeading(), "Player " + current.getName() + " should be heading WEST");

        gameController.turnRight(current);
        Assertions.assertEquals(NORTH, current.getHeading(), "Player " + current.getName() + " should be heading NORTH");

        gameController.turnRight(current);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading EAST");

        gameController.turnRight(current);
        Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH");
    }

    @Test
    void backUpTest(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(0, 4));

        try {
            gameController.backUp(current);
            Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.backUp(current);
            Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        current.setSpace(board.getSpace(0, 0));

        try {
            gameController.backUp(current);
            Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
            Assertions.assertNotNull(board.getSpace(4, 4).getPlayer(), "Space (4,4) should not be empty!");
            Assertions.assertEquals(current, board.getSpace(4, 4).getPlayer(), "Player " + current.getName() + " should be on (4,4)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player " + current.getName() + " should be heading SOUTH!");
        } catch (OutsideBoardException e) {
            Assertions.assertEquals("Outside board", e.getMessage());
        }
    }
    @Test
    void getSpaceAtTest(){
        Board board = gameController.board;
        Space startSpace = board.getSpace(4,4);
        Space space;

        try {
            space = gameController.getSpaceAt(1, SOUTH, startSpace.x,startSpace.y);
            Assertions.assertEquals(5, space.getY(), "Space found should be growing in y's direction by one");
            Assertions.assertEquals(4, space.getX(), "Space found should not have moved in x direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(-1, SOUTH, startSpace.x,startSpace.y);
            Assertions.assertEquals(3, space.getY(), "Space found should be decreasing in y's direction by one");
            Assertions.assertEquals(4, space.getX(), "Space found should not have moved in x direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(1, NORTH, startSpace.x,startSpace.y);
            Assertions.assertEquals(3, space.getY(), "Space found should be decreasing in y's direction by one");
            Assertions.assertEquals(4, space.getX(), "Space found should not have moved in x direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(-1, NORTH, startSpace.x,startSpace.y);
            Assertions.assertEquals(5, space.getY(), "Space found should be increasing in y's direction by one");
            Assertions.assertEquals(4, space.getX(), "Space found should not have moved in x direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(1, EAST, startSpace.x,startSpace.y);
            Assertions.assertEquals(5, space.getX(), "Space found be increasing in x's direction by one");
            Assertions.assertEquals(4, space.getY(), "Space found should not have moved in y direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(-1, EAST, startSpace.x,startSpace.y);
            Assertions.assertEquals(3, space.getX(), "Space found be decreasing in x's direction by one");
            Assertions.assertEquals(4, space.getY(), "Space found should not have moved in y direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(1, WEST, startSpace.x,startSpace.y);
            Assertions.assertEquals(3, space.getX(), "Space found be decreasing in x's direction by one");
            Assertions.assertEquals(4, space.getY(), "Space found should not have moved in y direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            space = gameController.getSpaceAt(-1, WEST, startSpace.x,startSpace.y);
            Assertions.assertEquals(5, space.getX(), "Space found be increasing in x's direction by one");
            Assertions.assertEquals(4, space.getY(), "Space found should not have moved in y direction");
        } catch (OutsideBoardException e){
            fail("Unexpected OutsideBoardException thrown");
        }
    }
    @Test
    void shittyAsFuckBugThatKeepsReturningTest(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        current.setSpace(board.getSpace(0, 4));

        try {
            gameController.backUp(current);
            Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
            Assertions.assertEquals(SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
            Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        gameController.turnLeft(current);
        Assertions.assertEquals(EAST, current.getHeading(), "Player " + current.getName() + " should be heading EAST!");

        try {
            gameController.moveForward(current);
            Assertions.assertEquals(current, board.getSpace(1, 3).getPlayer(), "Player " + current.getName() + " should beSpace (1,3)!");
            Assertions.assertEquals(EAST, current.getHeading(), "Player 0 should be heading EAST!");
            Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (1,3) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.backUp(current);
            Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
            Assertions.assertEquals(EAST, current.getHeading(), "Player 0 should be heading EAST!");
            Assertions.assertNull(board.getSpace(1, 3).getPlayer(), "Space (1,3) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }


        gameController.turnLeft(current);
        Assertions.assertEquals(NORTH, current.getHeading(), "Player " + current.getName() + " should be heading North!");

        try {
            gameController.backUp(current);
            Assertions.assertEquals(current, board.getSpace(0, 4).getPlayer(), "Player " + current.getName() + " should beSpace (0,4)!");
            Assertions.assertEquals(NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
            Assertions.assertNull(board.getSpace(0, 3).getPlayer(), "Space (0,3) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

        try {
            gameController.moveForward(current);
            Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
            Assertions.assertEquals(NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
            Assertions.assertNull(board.getSpace(0, 4).getPlayer(), "Space (0,4) should be empty!");
        } catch (OutsideBoardException e) {
            fail("Unexpected OutsideBoardException thrown");
        }

    }

}
