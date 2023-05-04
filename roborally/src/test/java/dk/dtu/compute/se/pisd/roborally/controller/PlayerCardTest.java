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

    @Test
    void discardCardTest(){
        Player player = gameController.board.getPlayer(0);

        ArrayList<CommandCard> playerDeck = player.getCardDeck();
        ArrayList<CommandCard> discardPile = player.getDiscardPile();

        Assertions.assertEquals(0, playerDeck.size());
        Assertions.assertEquals(0, discardPile.size());

        gameController.fillStartDeck(playerDeck);
        Assertions.assertEquals(20, playerDeck.size());
        Assertions.assertEquals(0, discardPile.size());

        //The discard function should not remove the card from the player deck. The draw topCard function should do that.
        gameController.discardCard(player, playerDeck.get(playerDeck.size()-1));
        Assertions.assertEquals(20, playerDeck.size());
        Assertions.assertEquals(1, discardPile.size());
    }

    @Test
    void drawTopCardFunction(){
        Player player = gameController.board.getPlayer(0);

        ArrayList<CommandCard> playerDeck = player.getCardDeck();
        ArrayList<CommandCard> discardPile = player.getDiscardPile();

        Assertions.assertEquals(0, playerDeck.size(), "player deck should not contain any cards");
        Assertions.assertEquals(0, discardPile.size(), "player discard pile should not contain any cards");

        CommandCard drawnTopCard;
        CommandCard trueTopCard;
        gameController.fillStartDeck(playerDeck);
        Assertions.assertEquals(20, playerDeck.size(), "player deck should contain 20 cards");
        Assertions.assertEquals(0, discardPile.size(), "players discard pile should not contain any cards");

        trueTopCard = playerDeck.get(playerDeck.size()-1);
        drawnTopCard = gameController.drawTopCard(player);
        Assertions.assertEquals(19, playerDeck.size(), "player deck should contain 19 cards");
        Assertions.assertEquals(0, discardPile.size(), "players discard pile should not contain any cards");
        Assertions.assertEquals(trueTopCard, drawnTopCard, "last element in deck array should be equal to drawn card");

        trueTopCard = playerDeck.get(playerDeck.size()-1);
        drawnTopCard = gameController.drawTopCard(player);
        Assertions.assertEquals(18, playerDeck.size(), "player deck should contain 18 cards");
        Assertions.assertEquals(0, discardPile.size(), "players discard pile should not contain any cards");
        Assertions.assertEquals(trueTopCard, drawnTopCard, "last element in deck array should be equal to drawn card");

        for(int i = 0; i < 18; i++){
            drawnTopCard = gameController.drawTopCard(player);
        }
        Assertions.assertEquals(0, playerDeck.size(), "player deck should not contain any cards");
        Assertions.assertEquals(0, discardPile.size(), "players discard pile should not contain any cards");

        drawnTopCard = gameController.drawTopCard(player);
        Assertions.assertNull(drawnTopCard, "method should return null if no cards can be shuffled to the deck from the discard pile");

        for(int i = 0; i < 2; i++){
            playerDeck.add(new CommandCard(Command.FORWARD));
        }
        Assertions.assertEquals(2, playerDeck.size(), "deck should contain 2 cards");
        Assertions.assertEquals(0, discardPile.size(), "discard pile should not contain any cards");

        trueTopCard = playerDeck.get(playerDeck.size()-1);
        drawnTopCard = gameController.drawTopCard(player);
        discardPile.add(drawnTopCard);
        Assertions.assertEquals(1, playerDeck.size(), "deck should contain 1 card");
        Assertions.assertEquals(1, discardPile.size(), "discard pile should contain 1 card");
        Assertions.assertEquals(trueTopCard, drawnTopCard, "last element in deck array should be equal to drawn card");
        Assertions.assertEquals(drawnTopCard, discardPile.get(discardPile.size()-1), "Element in discard pile should be equal to drawn card");

        trueTopCard = playerDeck.get(playerDeck.size()-1);
        drawnTopCard = gameController.drawTopCard(player);
        discardPile.add(drawnTopCard);
        Assertions.assertEquals(0, playerDeck.size(), "deck should be empty");
        Assertions.assertEquals(2, discardPile.size(), "discard pile should contain 2 cards");
        Assertions.assertEquals(trueTopCard, drawnTopCard, "last element in deck array should be equal to drawn card");
        Assertions.assertEquals(drawnTopCard, discardPile.get(discardPile.size()-1), "Element in discard pile should be equal to drawn card");

        drawnTopCard = gameController.drawTopCard(player);
        Assertions.assertEquals(1, playerDeck.size(), "deck should contain 1 card");
        Assertions.assertEquals(0, discardPile.size(), "discard pile should be empty");
    }

    @Test
    void againTest(){
        Board board = gameController.board;
        Player player = gameController.board.getPlayer(0);
        Assertions.assertEquals(player, board.getSpace(0,0).getPlayer(), "Player " + player.getName() + " should be at space (0,0)");

        player.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        gameController.board.setStep(1);
        gameController.executeCommand(player, Command.AGAIN);

        Assertions.assertEquals(player, board.getSpace(0,1).getPlayer(), "Player " + player.getName() + " should be at space (0,1)");

        player.getProgramField(0).setCard(new CommandCard(Command.FAST_FORWARD));
        gameController.board.setStep(1);
        gameController.executeCommand(player, Command.AGAIN);

        Assertions.assertEquals(player, board.getSpace(0,3).getPlayer(), "Player " + player.getName() + " should be at space (0,3)");

        player.getProgramField(0).setCard(new CommandCard(Command.AGAIN));
        gameController.board.setStep(1);
        gameController.executeCommand(player, Command.AGAIN);

        Assertions.assertEquals(player, board.getSpace(0,3).getPlayer(), "Player " + player.getName() + " should be at space (0,3)");

        player.getProgramField(0).setCard(new CommandCard(Command.FORWARD));
        player.getProgramField(1).setCard(new CommandCard(Command.AGAIN));
        gameController.board.setStep(2);
        gameController.executeCommand(player, Command.AGAIN);

        Assertions.assertEquals(player, board.getSpace(0,4).getPlayer(), "Player " + player.getName() + " should be at space (0,4)");

        ArrayList<CommandCard> cardDeck = player.getCardDeck();
        gameController.fillStartDeck(cardDeck);
        CommandCard topCard = cardDeck.get(cardDeck.size()-1);

        player.getProgramField(0).setCard(new CommandCard(Command.SPAM));
        gameController.board.setStep(1);
        gameController.executeCommand(player, Command.AGAIN);

        Assertions.assertEquals(topCard, player.getProgramField(1).getCard(), "Player " + player.getName() + " should be at space (0,4)");
    }
}