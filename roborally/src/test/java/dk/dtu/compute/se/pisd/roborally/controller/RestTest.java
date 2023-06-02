package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.APIController.ProgramContoller;
import dk.dtu.compute.se.pisd.roborally.APITests.ProgramCards;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Wall;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

class RestTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private Activator activator;

    private ProgramCards programCards;

    @BeforeEach
    void setUp() {

        activator = new Activator();
        programCards = new ProgramCards(gameController.board);

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

        for (int i = 0; i < 3; i++) {
            Player player = new Player(board, null, "Player " + i, i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            Assertions.assertEquals(player, board.getSpace(i, i).getPlayer(), "Player " + player.getName() + " should be at Space (" + i + "," + i + ")!");
            Assertions.assertEquals(SOUTH, player.getHeading(), "Player " + player.getName() + " should be heading SOUTH!");
                System.out.println(player);
        }
        board.setCurrentPlayer(board.getPlayer(0));
        List<Player> list = null;
        for(int i = 0; i < board.getPlayersNumber(); i++) {
            list.add(board.getPlayer(i));
        }
        //programCards.playersService(list);
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }


    @Test
    void RestTest1(){

        Board boardUsed = gameController.board;

        Command command = boardUsed.getPlayer(0).getProgramField(0).getCard().command;
        ProgramContoller programContoller = new ProgramContoller();

        Assertions.assertEquals(command, programContoller.getComCardSpecific(0, 0),
                "Testing");

    }

    //Add more test for this one
}