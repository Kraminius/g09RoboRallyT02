package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.Exceptions.OutsideBoardException;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;
import static org.junit.jupiter.api.Assertions.fail;

public class OutOfBoardTest {
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
    void jumpOut() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        try {
            second.setSpace(board.getSpace(0,0));
            current.setSpace(board.getSpace(0,1));
            second.setHeading(WEST);
            third.setSpace(board.getSpace(0,2));
            //Player current moves player 2 out of the board, 3 is untouched
            gameController.moveForward(second);
            Assertions.assertEquals( board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals( board.getSpace(0,1), current.getSpace(), "Player " + current.getName() +
                    "should have stay at" + board.getSpace(0,1) + ", but is " + current.getSpace());
            Assertions.assertEquals( board.getSpace(0,2), third.getSpace(), "Player " + third.getName() +
                    "should still be at" + board.getSpace(0,2) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

    @Test
    void pushAnotherOut() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        try {
            second.setSpace(board.getSpace(0,0));
            current.setSpace(board.getSpace(0,1));
            current.setHeading(WEST);
            third.setSpace(board.getSpace(0,2));
            //Player current moves player 2 out of the board, 3 is untouched
            gameController.moveForward(current);
            Assertions.assertEquals( board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals( board.getSpace(0,0), current.getSpace(), "Player " + current.getName() +
                    "should have moved to" + board.getSpace(0,0) + ", but is " + current.getSpace());
            Assertions.assertEquals( board.getSpace(0,2), third.getSpace(), "Player " + third.getName() +
                    "should still be at" + board.getSpace(0,2) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }


    @Test
    void pushAnotherOutDomino() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        try {
            second.setSpace(board.getSpace(0, 0));
            current.setSpace(board.getSpace(0, 1));
            current.setHeading(NORTH);
            third.setSpace(board.getSpace(0, 2));
            third.setHeading(WEST);
            //Player three moves current, who then moves second out of the board
            gameController.moveForward(third);
            Assertions.assertEquals(board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals(board.getSpace(0, 0), current.getSpace(), "Player " + current.getName() +
                    "should have been moved over to" + board.getSpace(0, 0) + ", but is " + current.getSpace());
            Assertions.assertEquals(board.getSpace(0, 1), third.getSpace(), "Player " + third.getName() +
                    "should have moved to" + board.getSpace(0, 1) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

    @Test
    void pushTwoOut() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        try {
            second.setSpace(board.getSpace(0, 0));
            current.setSpace(board.getSpace(0, 1));
            current.setHeading(NORTH);
            third.setSpace(board.getSpace(0, 2));
            third.setHeading(WEST);
            //Player three pushes both current and second out of bounds
            gameController.fastForward(third);
            Assertions.assertEquals(board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals(board.getRespawnSpaces(), current.getSpace(), "Player " + current.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + current.getSpace());
            Assertions.assertEquals(board.getSpace(0, 0), third.getSpace(), "Player " + third.getName() +
                    "should have moved to" + board.getSpace(0, 0) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

    @Test
    void pushAnotherOutPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        //Putting a pit at 7,1
        SpaceElement pitSpace = new SpaceElement();
        pitSpace.setHole(true);
        board.getSpace(7,1).setElement(pitSpace);


        try {
            second.setSpace(board.getSpace(7,2));
            current.setSpace(board.getSpace(7,3));
            current.setHeading(NORTH);
            third.setSpace(board.getSpace(7,4));
            //Player current moves player 2 out of the board, 3 is untouched
            gameController.moveForward(current);
            Assertions.assertEquals( board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen in and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals( board.getSpace(7,2), current.getSpace(), "Player " + current.getName() +
                    "should have moved to" + board.getSpace(7,2) + ", but is " + current.getSpace());
            Assertions.assertEquals( board.getSpace(7,4), third.getSpace(), "Player " + third.getName() +
                    "should still be at" + board.getSpace(7,4) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

    @Test
    void jumpOutPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        //Putting a pit at 7,1
        SpaceElement pitSpace = new SpaceElement();
        pitSpace.setHole(true);
        board.getSpace(7,1).setElement(pitSpace);

        try {
            second.setSpace(board.getSpace(7,2));
            current.setSpace(board.getSpace(7,3));
            second.setHeading(NORTH);
            third.setSpace(board.getSpace(7,4));
            //Player current moves player 2 out of the board, 3 is untouched
            gameController.moveForward(second);
            Assertions.assertEquals( board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals( board.getSpace(0,1), current.getSpace(), "Player " + current.getName() +
                    "should have stay at" + board.getSpace(0,1) + ", but is " + current.getSpace());
            Assertions.assertEquals( board.getSpace(0,2), third.getSpace(), "Player " + third.getName() +
                    "should still be at" + board.getSpace(0,2) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }


    @Test
    void pushAnotherOutDominoPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        //Putting a pit at 7,1
        SpaceElement pitSpace = new SpaceElement();
        pitSpace.setHole(true);
        board.getSpace(7,1).setElement(pitSpace);

        try {
            second.setSpace(board.getSpace(7, 2));
            current.setSpace(board.getSpace(7, 3));
            current.setHeading(EAST);
            third.setSpace(board.getSpace(7, 4));
            third.setHeading(NORTH);
            //Player three moves current, who then moves second out of the board
            gameController.moveForward(third);
            Assertions.assertEquals(board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals(board.getSpace(0, 0), current.getSpace(), "Player " + current.getName() +
                    "should have been moved over to" + board.getSpace(0, 0) + ", but is " + current.getSpace());
            Assertions.assertEquals(board.getSpace(0, 1), third.getSpace(), "Player " + third.getName() +
                    "should have moved to" + board.getSpace(0, 1) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

    @Test
    void pushTwoOutPit() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        //Putting a pit at 7,1
        SpaceElement pitSpace = new SpaceElement();
        pitSpace.setHole(true);
        board.getSpace(7,1).setElement(pitSpace);

        try {
            second.setSpace(board.getSpace(7, 2));
            current.setSpace(board.getSpace(7, 3));
            current.setHeading(WEST);
            third.setSpace(board.getSpace(7, 4));
            third.setHeading(NORTH);
            //Player three pushes both current and second out of bounds
            gameController.fastForward(third);
            Assertions.assertEquals(board.getRespawnSpaces(), second.getSpace(), "Player " + second.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + second.getSpace());
            Assertions.assertEquals(board.getRespawnSpaces(), current.getSpace(), "Player " + current.getName() +
                    "should have fallen out and respawned at" + board.getRespawnSpaces() + ", but is " + current.getSpace());
            Assertions.assertEquals(board.getSpace(0, 0), third.getSpace(), "Player " + third.getName() +
                    "should have moved to" + board.getSpace(0, 0) + ", but is " + third.getSpace());
        } catch (OutsideBoardException e) {
        }
    }

}
