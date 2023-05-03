package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Antenna;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

class PriorityTest {

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
        //Giving it an antenna
        SpaceElement antennaSpace = new SpaceElement();
        Antenna antenna = new Antenna();
        antenna.setSpace(board.getSpace(1,1));
        antennaSpace.setAntenna(true);
        board.getSpace(1,1).setElement(antennaSpace);

        board.setAntenna(board.getSpace(1,1));



        SpaceElement respawn = new SpaceElement();
        respawn.setRespawn(true);
        board.getSpace(4, 4).setElement(respawn);

        gameController = new GameController(board);

        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null, "Player " + i, i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(2+i, 2+i));
            //Assertions.assertEquals(player, board.getSpace(i, i).getPlayer(), "Player " + player.getName() + " should be at Space (" + i + "," + i + ")!");
            //Assertions.assertEquals(SOUTH, player.getHeading(), "Player " + player.getName() + " should be heading SOUTH!");
        }
        board.setCurrentPlayer(board.getPlayer(0));

    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }


    @Test
    void  SimplePriority() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);
        Player fourth = board.getPlayer(3);
        Player fifth = board.getPlayer(4);
        Player sixth = board.getPlayer(5);

        //Sets players at different distances
        fourth.setSpace(board.getSpace(2,2)); //4 is closest
        second.setSpace(board.getSpace(2,3)); //2
        third.setSpace(board.getSpace(3,3)); //3
        current.setSpace(board.getSpace(3,4)); //1
        fifth.setSpace(board.getSpace(4,4)); //5
        sixth.setSpace(board.getSpace(5,5)); //6
        gameController.antennaPriority();
        Assertions.assertEquals(fourth, gameController.getSequence().get(0), "The highest prioritised player should be" +
                "Player " + fourth.getName() );
        Assertions.assertEquals(second, gameController.getSequence().get(1), "The 2nd prioritised player should be" +
                "Player " + second.getName() );
        Assertions.assertEquals(third, gameController.getSequence().get(2), "The 3th prioritised player should be" +
                "Player " + third.getName() );
        Assertions.assertEquals(current, gameController.getSequence().get(3), "The 4th prioritised player should be" +
                "Player " + current.getName() );
        Assertions.assertEquals(fifth, gameController.getSequence().get(4), "The 5th prioritised player should be" +
                "Player " + fifth.getName() );
        Assertions.assertEquals(sixth, gameController.getSequence().get(5), "The lowest prioritised player should be" +
                "Player " + sixth.getName() );

    }
}