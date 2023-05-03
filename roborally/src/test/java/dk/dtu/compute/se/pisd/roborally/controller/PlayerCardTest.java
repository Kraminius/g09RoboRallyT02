package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Objects;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;
import static org.junit.jupiter.api.Assertions.*;

class PlayerCardTest {
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
    void fillStartDeckTest(){
        Player current = gameController.board.getPlayer(0);

        ArrayList<CommandCard> playerDeck = current.getCardDeck();
        ArrayList<CommandCard> discardPile = current.getDiscardPile();

        Assertions.assertEquals(0, playerDeck.size());
        Assertions.assertEquals(0, discardPile.size());

        gameController.fillStartDeck(playerDeck);
        Assertions.assertEquals(20, playerDeck.size());
        Assertions.assertEquals(0, discardPile.size());

        int move1 = 0;
        int move2 = 0;
        int move3 = 0;
        int backUp = 0;
        int left = 0;
        int right = 0;
        int u_turn = 0;
        int again = 0;
        int power_up = 0;
        for(CommandCard card: playerDeck){
            if(Objects.equals(card.getName(), Command.FORWARD.displayName)){
                move1++;
            } else if(Objects.equals(card.getName(), Command.FAST_FORWARD.displayName)){
                move2++;
            } else if(Objects.equals(card.getName(), Command.SPRINT_FORWARD.displayName)){
                move3++;
            } else if(Objects.equals(card.getName(), Command.BACK_UP.displayName)){
                backUp++;
            } else if(Objects.equals(card.getName(), Command.LEFT.displayName)){
                left++;
            } else if(Objects.equals(card.getName(), Command.RIGHT.displayName)){
                right++;
            } else if(Objects.equals(card.getName(), Command.U_TURN.displayName)){
                u_turn++;
            } else if(Objects.equals(card.getName(), Command.AGAIN.displayName)){
                again++;
            } else if(Objects.equals(card.getName(), Command.POWER_UP.displayName)){
                power_up++;
            }
        }
        Assertions.assertEquals(5, move1);
        Assertions.assertEquals(3, move2);
        Assertions.assertEquals(1, move3);
        Assertions.assertEquals(1, backUp);
        Assertions.assertEquals(3, left);
        Assertions.assertEquals(3, right);
        Assertions.assertEquals(2, again);
        Assertions.assertEquals(1, u_turn);
        Assertions.assertEquals(1, power_up);

    }
}