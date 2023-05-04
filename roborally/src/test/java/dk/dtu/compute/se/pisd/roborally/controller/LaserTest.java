package dk.dtu.compute.se.pisd.roborally.controller;

import com.beust.ah.A;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;
import static dk.dtu.compute.se.pisd.roborally.model.Heading.WEST;

class LaserTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private Activator activator;

    @BeforeEach
    void setUp() {

        activator = new Activator();

        Board board = new Board();
        board.width = 8;
        board.height = 8;
        board.spaces = new Space[board.width][board.height];
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                board.spaces[x][y] = new Space(board, x, y);
            }
        }
        //Giving it lasers
        for(int i = 0; i < 2; i++){
            SpaceElement laserSpace = new SpaceElement();
            Laser laser = new Laser();
            laser.setHeading(WEST);
            laser.setDamage(1);
            laserSpace.setLaser(laser);
            board.getSpace(4+i,4+i).setElement(laserSpace);
        }

        //Double laser
        SpaceElement laserSpace2 = new SpaceElement();
        Laser laser2 = new Laser();
        laser2.setHeading(SOUTH);
        laser2.setDamage(2);
        laserSpace2.setLaser(laser2);
        board.getSpace(3,3).setElement(laserSpace2);

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
    void hitByBoardLaser(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(4,5));
        current.setSpace(board.getSpace(0,0));
        third.setSpace(board.getSpace(1,1));
        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getCardDeck().size(); i++){
            if(current.getCardDeck().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getCardDeck().size(); i++){
            if(second.getCardDeck().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getCardDeck().size(); i++){
            if(third.getCardDeck().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 1, "Expect player "+current.getName()+" to get take 1 damage");
        Assertions.assertEquals(spamFound3, 0, "Dont expect player "+current.getName()+" to get any damage");

    }

   //Add more test for this one
}