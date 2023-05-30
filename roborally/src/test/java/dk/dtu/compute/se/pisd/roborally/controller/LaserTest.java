package dk.dtu.compute.se.pisd.roborally.controller;

import com.beust.ah.A;
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

        //Giving it checkpoints
        for(int i = 0; i < 3; i++){
            SpaceElement checkpointSpace = new SpaceElement();
            Checkpoint checkpoint = new Checkpoint();
            checkpoint.setNumber(i+1);
            checkpointSpace.setCheckpoint(checkpoint);
            board.getSpace(5+i,0+i).setElement(checkpointSpace);
        }

        //Giving it lasers
        for(int i = 0; i < 2; i++){
            SpaceElement laserSpace = new SpaceElement();
            SpaceElement wallSpace = new SpaceElement();
            Laser laser = new Laser();
            Wall wall = new Wall();
            laser.setHeading(WEST);
            laser.setDamage(1);
            ArrayList<Heading> wallHead = new ArrayList<>();
            wallHead.add(EAST);
            wall.setWallHeadings(wallHead);
            laserSpace.setLaser(laser);
            board.getSpace(4+i,4+i).setElement(laserSpace);
            board.getSpace(4+i,3+i).setElement(laserSpace);
            board.getSpace(4+i,2+i).setElement(laserSpace);
            board.getSpace(4+i,1+i).setElement(wallSpace);
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

        second.setSpace(board.getSpace(5,4));
        current.setSpace(board.getSpace(0,0));
        third.setSpace(board.getSpace(1,1));
        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 1, "Expect player "+second.getName()+" to get take 1 damage");
        Assertions.assertEquals(spamFound3, 0, "Dont expect player "+third.getName()+" to get any damage");

    }

    @Test
    void hitByDubbelBoardLaser(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(3,3));
        current.setSpace(board.getSpace(0,0));
        third.setSpace(board.getSpace(1,1));
        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 2, "Expect player "+second.getName()+" to get take 1 damage");
        Assertions.assertEquals(spamFound3, 0, "Dont expect player "+third.getName()+" to get any damage");

    }

    @Test
    void hitByPlayerLaser(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(1,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(2,4));
        current.setHeading(NORTH);
        third.setSpace(board.getSpace(1,2));
        third.setHeading(SOUTH);
        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound3, 0, "Dont expect player "+third.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 1, "Expect player "+second.getName()+" to take 1 damage");

    }

    @Test
    void hitByPlayerWallLaser(){
        /*
        Testin a wall in the middle of the eay
        but being on the beside the laser
        meaning it isnt obscuring and player 2 is hit
         */
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(0,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(1,4));
        current.setHeading(SOUTH);
        third.setSpace(board.getSpace(0,2));
        third.setHeading(SOUTH);

        //Setting up a wall
        SpaceElement wallSpace = new SpaceElement();
        Wall wall = new Wall();
        ArrayList<Heading> wallHead = new ArrayList<>();
        wallHead.add(EAST);
        wall.setWallHeadings(wallHead);
        wallSpace.setWall(wall);
        board.getSpace(0,3).setElement(wallSpace);

        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 1, "Expect player "+second.getName()+" to take 1 damage");
        Assertions.assertEquals(spamFound3, 0, "Expect player "+third.getName()+" to take 0 damage");

    }

    @Test
    void hitByPlayerWall2Laser(){
        /*
        Testing a wall from the one shoting but on the opposite site
        meaning the laser isnt obstructed and hits player 2
         */
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(0,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(1,4));
        current.setHeading(SOUTH);
        third.setSpace(board.getSpace(0,2));
        third.setHeading(SOUTH);

        //Setting up a wall
        SpaceElement wallSpace = new SpaceElement();
        Wall wall = new Wall();
        ArrayList<Heading> wallHead = new ArrayList<>();
        wallHead.add(NORTH);
        wall.setWallHeadings(wallHead);
        wallSpace.setWall(wall);
        board.getSpace(0,2).setElement(wallSpace);

        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 1, "Expect player "+second.getName()+" to take 1 damage");
        Assertions.assertEquals(spamFound3, 0, "Expect player "+third.getName()+" to take 0 damage");

    }

    @Test
    void hitByPlayerWall3Laser(){
        /*
        Testing a wall in the middle of the way to player 2
        meaning player 2 isnt hit
         */
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(0,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(1,4));
        current.setHeading(SOUTH);
        third.setSpace(board.getSpace(0,2));
        third.setHeading(SOUTH);

        //Setting up a wall
        SpaceElement wallSpace = new SpaceElement();
        Wall wall = new Wall();
        ArrayList<Heading> wallHead = new ArrayList<>();
        wallHead.add(NORTH);
        wall.setWallHeadings(wallHead);
        wallSpace.setWall(wall);
        board.getSpace(0,3).setElement(wallSpace);

        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 0, "Dont expect player "+second.getName()+" to get any damage");
        Assertions.assertEquals(spamFound3, 0, "Expect player "+third.getName()+" to take 0 damage");

    }

    @Test
    void hitByPlayerWall4Laser(){
        /*
        Testing a wall on the space where the laser is shot from
        emidietly hitting a wall and not reaching player 2
         */
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(0,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(1,4));
        current.setHeading(EAST);
        third.setSpace(board.getSpace(0,2));
        third.setHeading(SOUTH);

        //Setting up a wall
        SpaceElement wallSpace = new SpaceElement();
        Wall wall = new Wall();
        ArrayList<Heading> wallHead = new ArrayList<>();
        wallHead.add(SOUTH);
        wall.setWallHeadings(wallHead);
        wallSpace.setWall(wall);
        board.getSpace(0,2).setElement(wallSpace);

        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound2, 0, "Dont expect player "+second.getName()+" to get any damage");
        Assertions.assertEquals(spamFound3, 0, "Expect player "+third.getName()+" to take 0 damage");

    }

    @Test
    void hitByPlayerWall5Laser(){
        /*
        Testing a player still gets hit if they are on a space
        with a wall, but the wall is on the other side of where the player laser
        is comming from.
         */
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        Player second = board.getPlayer(1);
        Player third = board.getPlayer(2);

        second.setSpace(board.getSpace(0,4));
        second.setHeading(WEST);
        current.setSpace(board.getSpace(1,4));
        current.setHeading(SOUTH);
        third.setSpace(board.getSpace(0,2));
        third.setHeading(SOUTH);

        //Setting up a wall
        SpaceElement wallSpace = new SpaceElement();
        Wall wall = new Wall();
        ArrayList<Heading> wallHead = new ArrayList<>();
        wallHead.add(SOUTH);
        wall.setWallHeadings(wallHead);
        wallSpace.setWall(wall);
        board.getSpace(0,4).setElement(wallSpace);

        activator.activateBoard(board, gameController);

        int spamFound1 = 0;
        for(int i = 0; i < current.getDiscardPile().size(); i++){
            if(current.getDiscardPile().get(i).command == Command.SPAM){
                spamFound1++;
            }
        }
        int spamFound2 = 0;
        for(int i = 0; i < second.getDiscardPile().size(); i++){
            if(second.getDiscardPile().get(i).command == Command.SPAM){
                spamFound2++;
            }
        }
        int spamFound3 = 0;
        for(int i = 0; i < third.getDiscardPile().size(); i++){
            if(third.getDiscardPile().get(i).command == Command.SPAM){
                spamFound3++;
            }
        }

        Assertions.assertEquals(spamFound1, 0, "Dont expect player "+current.getName()+" to get any damage");
        Assertions.assertEquals(spamFound3, 0, "Dont expect player "+third.getName()+" to get any damage");
        Assertions.assertEquals(1, spamFound2, "Expect player "+second.getName()+" to take 1 damage");

    }

   //Add more test for this one
}