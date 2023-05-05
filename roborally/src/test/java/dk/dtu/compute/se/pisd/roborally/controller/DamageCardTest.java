package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

public class DamageCardTest {

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
    void addDamageCardTest(){
        Player player = gameController.board.getPlayer(0);

        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        ArrayList<CommandCard> cardDeck = player.getCardDeck();

        gameController.addDamageCard(player, Command.SPAM);
        Assertions.assertEquals(cardDeck.size(), 0, "Players card deck should not have any cards");
        Assertions.assertEquals(discardPile.size(), 1, "Players discard pile should have one card");
        Assertions.assertEquals(discardPile.get(0).getName(), Command.SPAM.displayName, "It should be a SPAM card in the discardpile");

        gameController.addDamageCard(player, Command.WORM);
        Assertions.assertEquals(cardDeck.size(), 0, "Players card deck should not have any cards");
        Assertions.assertEquals(discardPile.size(), 2, "Players discard pile should have two cards");
        Assertions.assertEquals(discardPile.get(1).getName(), Command.WORM.displayName, "It should be a WORM card in the discardpile");

        gameController.addDamageCard(player, Command.TROJAN_HORSE);
        Assertions.assertEquals(cardDeck.size(), 0, "Players card deck should not have any cards");
        Assertions.assertEquals(discardPile.size(), 3, "Players discard pile should have two cards");
        Assertions.assertEquals(discardPile.get(2).getName(), Command.TROJAN_HORSE.displayName, "It should be a TROJAN_HORSE card in the discardpile");

        gameController.addDamageCard(player, Command.VIRUS);
        Assertions.assertEquals(cardDeck.size(), 0, "Players card deck should not have any cards");
        Assertions.assertEquals(discardPile.size(), 4, "Players discard pile should have two cards");
        Assertions.assertEquals(discardPile.get(3).getName(), Command.VIRUS.displayName, "It should be a VIRUS card in the discardpile");

    }

    @Test
    void spamCardTest(){
        Player player = gameController.board.getPlayer(0);
        gameController.board.setStep(0);
        gameController.fillStartDeck(player.getCardDeck());

        ArrayList<CommandCard> cardDeck = player.getCardDeck();
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        Assertions.assertEquals(20, cardDeck.size(), "Players card deck should have 20 cards");
        Assertions.assertEquals(0, discardPile.size(), "Players discard pile should be empty");

        CommandCard topCard = player.getCardDeck().get(player.getCardDeck().size() - 1);
        CommandCard damageCard = new CommandCard(Command.SPAM);
        player.getProgramField(0).setCard(damageCard);

        gameController.executeCommand(player, player.getProgramField(0).getCard().command);
        Assertions.assertEquals(topCard, player.getProgramField(0).getCard(), "New card on field 0 should be topcard from player deck");
        Assertions.assertEquals(19, cardDeck.size(),"Players card deck should have 19 cards");
        Assertions.assertEquals(0, discardPile.size(), "Players discard pile should not have any cards");
    }

    @Test
    void trojanHorseTest(){
        Player player = gameController.board.getPlayer(0);
        gameController.board.setStep(0);
        gameController.fillStartDeck(player.getCardDeck());

        ArrayList<CommandCard> cardDeck = player.getCardDeck();
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        Assertions.assertEquals(20, cardDeck.size(), "Players card deck should have 20 cards");
        Assertions.assertEquals(0, discardPile.size(), "Players discard pile should be empty");

        CommandCard topCard = player.getCardDeck().get(player.getCardDeck().size() - 1);
        CommandCard damageCard = new CommandCard(Command.TROJAN_HORSE);
        player.getProgramField(0).setCard(damageCard);

        gameController.executeCommand(player, player.getProgramField(0).getCard().command);
        Assertions.assertEquals(topCard, player.getProgramField(0).getCard(), "New card on field 0 should be topcard from player deck");
        Assertions.assertEquals(19, cardDeck.size(),"Players card deck should have 19 cards");
        Assertions.assertEquals(2, discardPile.size(), "Players discard pile should not have two cards");
    }

}
