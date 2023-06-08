/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.Exceptions.OutsideBoardException;
import dk.dtu.compute.se.pisd.roborally.GameClient;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Wall;
import dk.dtu.compute.se.pisd.roborally.view.Option;
import dk.dtu.compute.se.pisd.roborally.view.UpgradeShop;
import dk.dtu.compute.se.pisd.roborally.view.WinnerDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.*;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;
import static dk.dtu.compute.se.pisd.roborally.model.Heading.WEST;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;
    public UpgradeShop upgradeShop;
    public AntennaHandler antennaHandler = new AntennaHandler();

   private List<Player> sequence;

    /**
     * @param board the board which the game is played on
     */
    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

    }
    /** @Author Freja Egelund Grønnemose s224286@dtu.dk
     * Prepares the game for the programming phase by clearing previous programming, adding the previous programming cards to the players discard pile,
     * setting the game state and give the players new cards from their deck.
     */
    // XXX: V2
    public void startUpgradePhase(){
        System.out.println("Upgrade Phase Started");
        int playerNumber = GameClient.getPlayerNumber();
        if(playerNumber != 0){
            GameClient.startWaitingForOpenShop();
        }

        board.setPhase(Phase.UPGRADE);
    }

    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        //board.setCurrentPlayer(sequence.get(0));
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
        setGameStateUpgradeCards();

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    CommandCard card = field.getCard();
                    if(card != null) {
                        discardCard(player, card);
                    }
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(drawTopCard(player));
                    field.setVisible(true);
                }
            }
        }
    }

    public void setGameStateUpgradeCards(){
        //First index is if they have the card, second index is if card has been used.
        boolean[] newPhase = {true, false};

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if(player != null){
                if (player.getPowerUps().getDefragGizmo()[0]){
                    player.getPowerUps().setDefragGizmo(newPhase);
                }
            }
        }
    }

    /**
     * @Author Freja Egelund Grønnemose s224286@dtu.dk
     * Creates new CommandCard object with a random command taken from an EnumSet containing all valid commands.
     * Meaning that the damage cards has been removed.
     * @return
     */
    public CommandCard generateRandomCommandCard() {
        Set<Command> validCommands = EnumSet.allOf(Command.class);
        validCommands.removeAll(EnumSet.of(Command.SPAM, Command.TROJAN_HORSE, Command.WORM, Command.VIRUS));
        int random = (int) (Math.random() * validCommands.size());
        Command randomCommand = validCommands.toArray(new Command[0])[random];
        return new CommandCard(randomCommand);
    }

    /**
     * @Author Freja Egelund Grønnemose s224286@dtu.dk
     * These methods creates an EnumSet of all valid commands (damage cards removed), and then adds new CommandCard objects to the players card deck,
     * with the valid enum. It has an int array that contains the value of how many cards of a type that exist in a player deck.
     * @param playerDeck the ArrayList that holds the players cards.
     */
    public void fillStartDeck(@NotNull ArrayList<CommandCard> playerDeck){
        Set<Command> validCommands = EnumSet.of(Command.FORWARD, Command.FAST_FORWARD, Command.LEFT, Command.RIGHT, Command.SPRINT_FORWARD, Command.BACK_UP, Command.U_TURN, Command.AGAIN, Command.POWER_UP);

        int[] counts = {5, 3, 3, 3, 1, 1, 1, 2, 1};
        int index = 0;
        System.out.println(validCommands);
        for(Command command : validCommands){
            int count = counts[index];
            for(int i = 0; i < count; i++){
                playerDeck.add(new CommandCard(command));
            }
            index++;
        }
        Collections.shuffle(playerDeck);
    }

    /**
     * @Author Mikkel Jürs, s224279@student.dtu.dk
     * Method for filling the UpgradeCard Deck.
     * @param upgradeDeck ArrayList of CommandCards that holds all upgradeCards, with all the Unems from Command without
     *                    the non-upgradeable ones.
     */
    public void fillUpgradeCardDeck(@NotNull ArrayList<CommandCard> upgradeDeck){
        Set<Command> upgradeCards = EnumSet.allOf(Command.class);
        upgradeCards.removeAll(EnumSet.of(
                Command.FORWARD,
                Command.FAST_FORWARD,
                Command.LEFT,
                Command.RIGHT,
                Command.SPRINT_FORWARD,
                Command.BACK_UP,
                Command.U_TURN,
                Command.AGAIN,
                Command.OPTION_LEFT_RIGHT,
                Command.SPAM,
                Command.WORM,
                Command.TROJAN_HORSE,
                Command.VIRUS,
                Command.MOVE_LEFT,
                Command.MOVE_RIGHT,
                Command.POWER_UP,
                Command.SPAM_FOLDER,
                Command.SPEED,
                Command.WEASEL,
                Command.ENERGY,
                Command.SANDBOX));

        //Adding 5 of each card. Will implement a discard upgradeCards pile in next Scope. Planned in next PI.
        for(Command command : upgradeCards){
            for (int i=0;i<5;i++) {
                upgradeDeck.add(new CommandCard(command));
            }
        }
        Collections.shuffle(upgradeDeck);
    }

    /**
     * @Author Freja Egelund Grønnemose s224286@dtu.dk
     * This method fetches a players two ArrayLists - discardPile & cardDeck.
     * It then adds all the elements from the discardPile to the cardDeck array, removes the elements from the discardPile and shuffles the cardDeck.
     * @param player the player whos discardPile should be shuffled into their cardDeck.
     */
    protected void shuffleDiscardPileToDeck(Player player){
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        ArrayList<CommandCard> cardDeck = player.getCardDeck();
        cardDeck.addAll(discardPile);
        Collections.shuffle(cardDeck);
        discardPile.clear();
    }

    /**
     * @Author Freja Egelund Grønnemose s224286@dtu.dk
     * This method fetches a players cardDeck arrayList.
     * It then checks the size of the array, and if it is empty it calls the method that shuffles the dicardPile into the cardDeck.
     * The method then removes the topCard - the last element in the array.
     * @param player the player that draws a card.
     * @return the last element of the cardDeck array - the topCard.
     */
    protected CommandCard drawTopCard(Player player){
        ArrayList<CommandCard> cardDeck = player.getCardDeck();
        int i = cardDeck.size() - 1;
        if(i < 0){
            shuffleDiscardPileToDeck(player);
            cardDeck = player.getCardDeck();
            i = cardDeck.size() - 1;
            if(i < 0){
                return null;
            }
        }
        CommandCard topCard = cardDeck.get(i);
        cardDeck.remove(i);
        return topCard;
    }

    /**
     * @Author Freja Egelund Grønnemose s224286@dtu.dk
     * This method adds a card to a players discardPile.
     * @param player the player that owns the discardPile
     * @param card the card that should be placed in the discardPile.
     */
    protected void discardCard(Player player, CommandCard card){
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        discardPile.add(card);
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method iterates over every player and all of their remaining cards in their cards array. (The ones that have not been placed in program fields).
     * It then discards those cards, if there isn't any card in a field, it skips to the next.
     */
    protected void discardUnusedCards(){
        for (int i = 0; i < board.getPlayersNumber(); i++){
            Player player = board.getPlayer(i);
            for(int j = 0; j < Player.NO_CARDS; j++ ){
                CommandCard card = player.getCardField(j).getCard();
                if(card != null){
                    discardCard(player, card);
                    CommandCardField field = player.getCardField(j);
                    field.setCard(null);
                }
            }
        }
    }
    // XXX: V2
    public void finishProgrammingPhase() {

        discardUnusedCards();
        //Checks who have priority
        sequence = antennaHandler.antennaPriority(board);
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
        //sequence.remove(0);
        //board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX: V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     *  Makes a specific programming field invisible.
     */
    // XXX: V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Setting stemode to false, so we can execute multiple programs at a time.
     */
    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    /**
     * Setting stepmode to true.
     */
    // XXX: V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2
    private void continuePrograms() {

        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }



    // XXX: V2
    private void executeNextStep() {
        int step = board.getStep();
        Player currentPlayer = board.getCurrentPlayer();
        sequence.remove(0);
        System.out.println("Curr: " + currentPlayer.getId());
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {

            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    boolean terminate = executeCommand(currentPlayer, command);
                    if (terminate) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                }
                if(sequence.size() == 0){
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        sequence = antennaHandler.antennaPriority(board);
                        board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
                        Activator.getInstance().activateBoard(board, this);

                    } else {
                        //Probably upgrade phase here?
                        startUpgradePhase();
                    }
                }else{
                    board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
                }

            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    private void switchCurrentPlayer(){
        int step = board.getStep();
        if(sequence.size() == 0){
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                antennaHandler.antennaPriority(board);
                board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
            } else {
                //Probably upgrade phase here?
                startUpgradePhase();
            }
        }else{
            board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
            Activator.getInstance().activateBoard(board, this);
        }
    }

    /**
     *
     * @param command
     * resets the phase to activation, executes command and goes to the next player.
     * Checks if stepmode is enabled, if not continues the programs.
     */
    public void executeCommandOptionAndContinue(@NotNull Command command){
        board.setPhase(Phase.ACTIVATION);
        Player currentPlayer = board.getCurrentPlayer();
        executeCommand(currentPlayer, command);
        if(sequence.size() == 0){
            int step = board.getStep();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                antennaHandler.antennaPriority(board);
                board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
                Activator.getInstance().activateBoard(board, this);

            } else {
                startUpgradePhase();
            }
        }else{
            board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
        }
    }
    // XXX: V2




    /**@author Freja Egelund Grønnemose, s224286@dtu.dk
     * A method that via a switch statement over the given command either calls the corresponding comand method,
     * if the given commandcard is an interactive card the methods returns true.
     * @param player
     * @param command
     * @return true if the player interaction mode is enabled.
     */
    protected boolean executeCommand(@NotNull Player player, @NotNull Command command) {
        if (player.board != board) {
            throw new RuntimeException("Player board different from current board");
        }
        if(command.isInteractive()){
            return true;
        }
        // XXX This is a very simplistic way of dealing with some basic cards and
        //     their execution. This should eventually be done in a more elegant way
        //     (this concerns the way cards are modelled as well as the way they are executed).

        switch (command) {
            case FORWARD:
                try {
                    this.moveForward(player);
                    return false;
                } catch (OutsideBoardException e){
                    board.getCurrentPlayer().setRespawnStatus(true);
                    movePlayerToRespawn(board.getCurrentPlayer(), null);
                    if(player == board.getCurrentPlayer()){
                        return true;
                    } else {
                      executeCommand(player, command);
                    }
                    return true;
                }
            case RIGHT:
                this.turnRight(player);
                return false;
            case LEFT:
                this.turnLeft(player);
                return false;
            case FAST_FORWARD:
                try {
                    this.fastForward(player);
                    return false;
                } catch (OutsideBoardException e){
                    board.getCurrentPlayer().setSpace(board.getRespawnSpaces());
                    board.getCurrentPlayer().setRespawnStatus(true);
                    return true;
                }
            case SPRINT_FORWARD:
                try {
                    this.sprintForward(player);
                    return false;
                } catch (OutsideBoardException e){
                    board.getCurrentPlayer().setSpace(board.getRespawnSpaces());
                    board.getCurrentPlayer().setRespawnStatus(true);
                    return true;
                }
            case U_TURN:
                this.uTurn(player);
                return false;
            case BACK_UP:
                try {
                    this.backUp(player);
                    return false;
                } catch (OutsideBoardException e){
                    board.getCurrentPlayer().setSpace(board.getRespawnSpaces());
                    board.getCurrentPlayer().setRespawnStatus(true);
                    return true;
                }
            case AGAIN:
                int prevProgramStep = board.getStep() - 1;
                if(prevProgramStep < 0){
                    //Should not be able to happen.
                    return false;
                } else {
                    CommandCard prevCommand = player.getProgramField(prevProgramStep).getCard();
                    if(prevCommand == null){
                        //Should also not happen
                        return false;
                    } else if (prevCommand.command == Command.AGAIN) {
                        prevProgramStep -= 1;
                        if(prevProgramStep < 0){
                            return false;
                        } else {
                            prevCommand = player.getProgramField(prevProgramStep).getCard();
                            this.again(player, prevCommand.command);
                        }
                        return false;
                    } else if(prevCommand.command == Command.SPAM || prevCommand.command == Command.VIRUS || prevCommand.command == Command.TROJAN_HORSE || prevCommand.command == Command.WORM){
                        CommandCard topCard = drawTopCard(player);
                        discardCard(player, player.getProgramField(board.getStep()).getCard());
                        player.getProgramField(board.getStep()).setCard(topCard);
                        this.again(player, topCard.command);
                        return false;
                    } else {
                        this.again(player, prevCommand.command);
                        return false;
                    }
                }
            case POWER_UP:
                this.powerUp(player, 1);
                return false;
            case SPAM:
                this.playSpam(player);
                return false;
            case TROJAN_HORSE:
                this.playTrojan(player);
                return false;
            case WORM:
                player.setSpace(board.getRespawnSpaces());
                player.setRespawnStatus(true);
                return true;
            case VIRUS:
                this.playVirus(player);
                return false;
            case SPEED:
                try {
                    this.sprintForward(player);
                } catch (OutsideBoardException e){
                    board.getCurrentPlayer().setSpace(board.getRespawnSpaces());
                    board.getCurrentPlayer().setRespawnStatus(true);
                    return true;
                }
                return false;
            case SPAM_FOLDER:
                playSpamFolder(player);
                return false;

            case MOVE_LEFT:
                moveToLeftSpace(player);
                return false;

            case MOVE_RIGHT:
                moveToRightSpace(player);
                return false;

            case SANDBOX:
                return true;



            default:
                throw new RuntimeException("Should not happen");
        }
    }



    // TODO Assignment V2
    public void moveForward(@NotNull Player player) throws OutsideBoardException {
        movePlayerForward(player, 1, null, false);
    }

    // TODO Assignment V2
    public void fastForward(@NotNull Player player) throws OutsideBoardException {
        movePlayerForward(player, 1, null, false);
        movePlayerForward(player, 1, null, false);
    }

    public void sprintForward(@NotNull Player player) throws OutsideBoardException {
        movePlayerForward(player, 1, null, false);
        movePlayerForward(player, 1, null, false);
        movePlayerForward(player, 1, null, false);
    }

    /**
     * @param player  The player to move
     * @param amount  The amount to move, for pushing to work it must be either 1 or -1, depending on going forward or backward ones.
     * @param heading This should always be null when called outside this method, unless for belts or pushing. The heading is used during the pushing as they won't necessarily be pushed the direction they are facing. The player will find the heading itself.
     * @param isBelt  This should always be null, unless on a belt, where it should ignore players as they move at the same time.
     * @return a boolean value returning true if it can move into the space, returning false if it can't move at all.
     * @catch this method catches the OutsideBoardException thrown by the getSpaceAt method throws.
     * @author Tobias Gørlyk, s224271@dtu.dk
     * <p>
     * Move Forward is a recursive method, that moves a player if there is space in the direction that is free.
     * If there is a player there it moves them forward if they can move forward.
     * If no one can move because of an obstacle or it being outside the board, no one moves.
     */
    public boolean movePlayerForward(Player player, int amount, Heading heading, boolean isBelt) throws OutsideBoardException{
            int x = player.getSpace().x;
            int y = player.getSpace().y;
            Space space = null;
            if (heading == null) heading = player.getHeading();
            space = getSpaceAt(amount, heading, x, y);
            if (space == null) return false; //If space was out of bounds return here.
            if (obstacleInSpace(player.getSpace(), space)) return false;
            Player playerToMove = space.getPlayer();
            if (isBelt) {
                player.setSpace(space);
                return true;
            }
            if (playerToMove != null) { //Check if there is a player already on this field.
                try {
                    if (movePlayerForward(playerToMove, amount, heading, false)) {
                        player.setSpace(space);//There is a player in front and they can move, so we move too.
                        rammingGearFunctionality(player, playerToMove);
                        return true;
                    } else return false; //There is a player there and they cannot move forward so no one moves.
                } catch (OutsideBoardException e){
                    board.setCurrentPlayer(playerToMove);
                    throw new OutsideBoardException();
                }
            } else {
                player.setSpace(space); //There is no player or obstacle in front and we will therefore move there.
                return true;
            }
    }

    /** @Author Mikkel Jürs, s224279@dtu.dk
     * Discards entire hand, drawe new cards.
     * @param player
     */
    protected void recompileUpgradeCard(Player player){
        for(int i = 0; i<Player.NO_CARDS; i++){
            discardCard(player, player.getCardField(i).getCard());
            player.getCardField(i).setCard(null);
            player.getCardField(i).setCard(drawTopCard(player));
        }
    }

    /**
     * @Author Freja Egelund Grønnemose, 224286@dtu.dk
     * @param amount the amount of spaces the player should move
     * @param heading the players heading
     * @param x the x value of the space they want to move to
     * @param y the y value of the space they want to move to
     * @return the target space
     * @throws OutsideBoardException if player ends up outside of board
     */
    protected Space getSpaceAt(int amount, Heading heading, int x, int y) throws OutsideBoardException {
        Space space = null;
            switch (heading) {
                case NORTH:
                    if (amount < 0 && y < board.height - 1) {
                        space = board.getSpace(x, y + Math.abs(amount));
                    } else if (y >= amount && amount > 0) {
                        space = board.getSpace(x, y - amount);
                    } else {
                        throw new OutsideBoardException();
                    }
                    break;
                case EAST:
                    if (amount < 0 && x > 0) {
                        space = board.getSpace(x - Math.abs(amount), y);
                    } else if (x < board.width - amount && amount >= 0) {
                        space = board.getSpace(x + amount, y);
                    } else {
                        throw new OutsideBoardException();
                    }
                    break;
                case WEST:
                    if (amount < 0 && x < board.width - 1) {
                        space = board.getSpace(x + Math.abs(amount), y);
                    } else if (x >= amount && amount > 0) {
                        space = board.getSpace(x - amount, y);
                    } else {
                        throw new OutsideBoardException();
                    }
                    break;
                case SOUTH:
                    if (amount < 0 && y > 0) {
                        space = board.getSpace(x, y - Math.abs(amount));
                    } else if (y < board.height - amount && amount >= 0) {
                        space = board.getSpace(x, y + amount);
                    } else {
                        throw new OutsideBoardException();
                    }
                    break;
            }
            if(space.getElement().isHole()){
                throw new OutsideBoardException();
            } else if (!space.getElement().isSpace()){
                throw new OutsideBoardException();
            }
            return space;
        }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * @param player the player which is moving to respawn or the player being pushed away from respawn.
     * @param heading the heading in which the player already on the respawn field is heading.
     */
    private void movePlayerToRespawn(@NotNull Player player, Heading heading){
        Player playerToMove = board.getRespawnSpaces().getPlayer();
        if(playerToMove != null){
            try{
                if(heading == null){
                    heading = playerToMove.getHeading();
                }
                Space space = getSpaceAt(1, heading, playerToMove.getSpace().getX(), playerToMove.getSpace().getY());
                if(obstacleInSpace(board.getRespawnSpaces(), space)){
                    movePlayerToRespawn(playerToMove, playerToMove.getHeading().next());
                } else {
                    playerToMove.setSpace(space);
                    player.setSpace(board.getRespawnSpaces());
                }
            } catch (OutsideBoardException e){
                movePlayerToRespawn(player, playerToMove.getHeading().next());
            }
        } else {
            player.setSpace(board.getRespawnSpaces());
        }
    }
    /**@author Freja Egelund Grønnemose, s224286@dtu.dk
     * This methods set the players space to the space of the rebootToken.
     * The method also removes ALL the players remaining program cards.
     * @param player the player that should be respawned
     */
    public void respawnPlayer(@NotNull Player player, @NotNull Heading heading){
        player.setHeading(heading);
        addDamageCard(player, Command.SPAM);
        addDamageCard(player, Command.SPAM);
        player.setRespawnStatus(false);
        for (int j = board.getStep(); j < Player.NO_REGISTERS; j++) {
            CommandCardField field = player.getProgramField(j);
            discardCard(player, field.getCard());
            field.setCard(null);
            field.setVisible(true);
        }
        board.setPhase(Phase.ACTIVATION);
        switchCurrentPlayer();
    }

    /**
     * @author Kenneth Kaiser, s221064@dtu.dk
     * @param fromSpace The Space where the player currently is and will move from
     * @param toSpace   The space where the player is moving to.
     * @return
     *
     */
    private boolean obstacleInSpace(Space fromSpace, Space toSpace) {

        Heading directionHeadingTo = null;
        Heading directionHeadingFrom = null;
        Boolean obstacle = false;

        if (fromSpace.getX() < toSpace.x) {
            directionHeadingTo = Heading.EAST;
            directionHeadingFrom = Heading.WEST;
        } else if (fromSpace.getX() > toSpace.x) {
            directionHeadingTo = Heading.WEST;
            directionHeadingFrom = Heading.EAST;
        } else if (fromSpace.getY() < toSpace.getY()) {
            directionHeadingTo = Heading.NORTH;
            directionHeadingFrom = Heading.SOUTH;
        } else if (fromSpace.getY() > toSpace.getY()) {
            directionHeadingTo = Heading.SOUTH;
            directionHeadingFrom = Heading.NORTH;
        }

        if (toSpace.getWallHeading() != null && directionHeadingTo != null) {
            for (int i = 0; i < toSpace.getWallHeading().size(); i++) {
                if (toSpace.getWallHeading().get(i) == directionHeadingFrom) {
                    obstacle = true;
                }
            }
        }

        if (fromSpace.getWallHeading() != null && directionHeadingFrom != null) {
            for (int i = 0; i < fromSpace.getWallHeading().size(); i++) {
                if (fromSpace.getWallHeading().get(i) == directionHeadingTo) {
                    obstacle = true;
                }
            }
        }


        return obstacle;

    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method changes the players heading by getting the next enum.
     * @param player whos direction should change.
     */
    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method changes the players heading by getting the next enum.
     * @param player
     */
    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

    /**@author Freja Egelund Grønnemose, 224286@dtu.dk
     * A method to make a u-turn done by turning left two times.
     * @param player the player that should be moved.
     */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().prev().prev());
    }

    /**@author Freja Egelund Grønnemose, 224286@dtu.dk
     *  A method to move the player backwards without changing their heading.
     * @param player the player that should be moved
     */
    public void backUp(@NotNull Player player) throws OutsideBoardException {
        Space space = getSpaceAt(-1, player.getHeading(), player.getSpace().x, player.getSpace().y);
        player.setSpace(space);
    }

    /**
     * @author Freja Egelund Grønnemose, 224286@dtu.dk
     * This method calls the executeCommand() method to get the previous command executed one more time
     * @param player the player that uses the again command
     * @param command the command in the previous programfield
     */
    public void again(@NotNull Player player, @NotNull Command command){
        executeCommand(player,command);
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * this method adds energy cubes to a player
     * @param player the player who should recieve energycubes
     * @param cubeAmount the amount of cubes that should be added.
     */
    public void powerUp(@NotNull Player player, int cubeAmount){
        player.setEnergyCubes(player.getEnergyCubes() + cubeAmount);
        player.updateCubeLabel();
    }

    /**@Author Freja Egelund Grønnemose s224286@dtu.dk
     * This method moves a card from one field to another. It is used by OneDragDrop handler.
     * @param source the CommandCardField the player would like to move a card from
     * @param target the CommandCardField that the player would like to move the card to
     * @return true if operation is succesfull
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @author Tobias Gørlyk     s223271.dtu.dk
     * Activates the belts on the board for all players one at a time but right after each other. While a player is on a belt they don't have any push collision.
     * Tries to move player, and catches the OutsideBoardException if necessary.
     * Not implemented in game yet.
     */
    public void activateBelts() throws OutsideBoardException {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            int moving;
            if (player.getSpace() != null) {
                if (player.getSpace().getElement().getBelt() != null) {
                    moving = player.getSpace().getElement().getBelt().getSpeed();
                    for (int n = moving; n > 0; n--) {
                        Heading heading = player.getSpace().getElement().getBelt().getHeading();
                        Space spaceInFront = null;
                        switch (heading) {
                            case EAST:
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x + 1, player.getSpace().y);
                                break;
                            case WEST:
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x - 1, player.getSpace().y);
                                break;
                            case NORTH:
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y - 1);
                                break;
                            case SOUTH:
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y + 1);
                                break;
                            default:
                                throw new RuntimeException("No Heading"); //This should not happen
                        }
                        if (spaceInFront == null) return;

                        if (spaceInFront.getElement().getBelt() == null) {
                            movePlayerForward(player, 1, heading, false); //Can move players as this would be outside of belt.
                            moving = 0; //No longer moving on a belt so this is set to 0.
                        } else
                            movePlayerForward(player, 1, heading, true); //Won't move players as they are also on a belt and just haven't moved yet.

                        if (spaceInFront.getElement().getBelt().getTurn().equals("LEFT")) turnLeft(player);
                        else if (spaceInFront.getElement().getBelt().getTurn().equals("RIGHT")) turnRight(player);

                    }
                }
            }
        }
    }

    /**
     * @author Nicklas Christensen     s224314.dtu.dk
     *
     * Activates the checkpoints on the board, cheks player for reaching checkpoint
     * unless they haven't gotten previous checkpoint.
     */
    public void activateCheckpoints() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            Space space = player.getSpace();
            boolean[] checkpointStatus = player.getCheckpointReadhed();
            int number;
            if (space.getElement().getCheckpoint() != null) {
                number = space.getElement().getCheckpoint().getNumber();
                if (number == 1) {
                    player.setCheckpointReached(0, true);
                    System.out.println("Player: " + (i + 1) + " has reached checkpoint: " + (number));
                } else if (checkpointStatus[(number - 2)]) {
                    player.setCheckpointReached(number - 1, true);
                    System.out.println("Player: " + (i + 1) + " has reached checkpoint: " + (number));
                }
            }
            checkForWinner();
        }

    }

    public void addDamageCard(Player player, Command type){
        CommandCard damageCard = new CommandCard(type);
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        discardPile.add(damageCard);
    }

    /**@Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method get the topCard from the players deck, places it in the current register (and shows it),
     * then it calls the executeCommand() with the topCards command.
     * @param player the player that plays the spam card.
     */
    public void playSpam(Player player){
        int step = board.getStep();
        CommandCard topCard = drawTopCard(player);
        CommandCardField currentRegister = player.getProgramField(step);
        currentRegister.setCard(topCard);
        currentRegister.setVisible(true);
        executeCommand(player, topCard.command);
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * this method adds two spam cards to the player, and then calls the playSpam() method
     * @param player the player who plays the Trojan horse card.
     */
    public void playTrojan(Player player){
        for(int i = 0; i < 2; i++){
            addDamageCard(player, Command.SPAM);
        }
        playSpam(player);
    }


    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method finds all the players in a fixed radius around the player.
     * Then adds a spam card to all those players, and calls the playSpam() on the original player.
     * @param player the player who plays the virus card
     */
    public void playVirus(Player player){
        ArrayList<Player> playersInRadius = board.findPlayerWithinRadius(player);
        for(Player otherPlayer : playersInRadius){
            addDamageCard(otherPlayer, Command.SPAM);
        }
        playSpam(player);
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method calls the powerUp method.
     * @param player the player who plays the Energy Routine card
     */


    public void playEnergyRoutine(Player player){
        powerUp(player, 1);
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }





    /**
     * Make all the given players shoot a laser.
     * The laser gives 1 SPAM Card to any players hit
     * @param player an single player who's laser will be shot, this should be available through board.
     */
    public void playerLaserActivate(Player player){

        Space laser = player.getSpace();
        Heading direction = player.getHeading();
        Heading directOpposite;
        ArrayList<Heading> wallHeading;
        Wall wall;
        int move;
        boolean end = false;

        switch (direction){
            case SOUTH:
                move = 1; //y
                directOpposite = NORTH;
                break;
            case NORTH:
                move = -1; //y
                directOpposite = SOUTH;
                break;
            case WEST:
                move = -1; //x
                directOpposite = EAST;
                break;
            case EAST:
                move = 1; //x
                directOpposite = WEST;
                break;
            default:
                throw new IllegalStateException("PlayerLaser - Unexpected (Heading)value: " + direction);
        }

        while(end == false){
            //Checks if a wall is in front of laser
            //If there is we end here
            wall = laser.getElement().getWall();
            if(wall != null){
                wallHeading = wall.getWallHeadings();
                for(int i = 0; i < wallHeading.size(); i++){
                    if(wallHeading.get(i) == direction){
                        end = true;
                    }
                }}
            //If not we proceed
            if(end == false){
                //We move forward in one of four directions
                if(direction == SOUTH || direction == NORTH){
                    laser = board.getSpace(laser.x,(laser.y+move));
                    //Marks the next space to visit
                    if(laser != null){
                        wall = board.getSpace(laser.x,(laser.y)).getElement().getWall();
                    }
                    else{
                        wall = null;
                    }

                }
                if(direction == EAST || direction == WEST){
                    laser = board.getSpace((laser.x + move),laser.y);
                    //Marks the next space to visit
                    if(laser != null) {
                        wall = board.getSpace((laser.x), laser.y).getElement().getWall();
                    }
                    else{
                        wall = null;
                    }
                }
                //We check to see if a player is on the way
                //If so we end and give the player a SPAM card
                if(laser != null && laser.getPlayer() != null){
                    end = true;
                    addDamageCard(laser.getPlayer(), Command.SPAM);
                }
                //Check to see if we are facing a wall on the next space
                //We here use wall we made when finding directions
                else{
                    if(wall != null){
                        wallHeading = wall.getWallHeadings();
                        if(wallHeading.contains(directOpposite) == true){
                            end = true;
                        }}
                }
            }
            //No mather what we check to see if we are within the board
            //These needs to be in the bottom so they are always checked
            if(laser == null){
                end = true;
            }
        }
    }

    public void checkForWinner(){
        for(int i = 0; i < board.getPlayersNumber(); i++){
            Player playerTesting = board.getPlayer(i);
            int checkpoints = board.getCheckPointSpaces().size();
            if(playerTesting.getCheckpointReadhed()[checkpoints-1] == true){
                System.out.println("We have a winner! Congratulations " + playerTesting.getName() + "!");

                WinnerDisplay winnerDisplay = new WinnerDisplay();
                winnerDisplay.createWindow(playerTesting);

                //This ends the game, not very exiting
                //Platform.exit();
            }
        }
    }

    public List<Player> getSequence() {
        return sequence;
    }

    public void setSequence(List<Player> sequence) {
        this.sequence = sequence;
    }


//region UPGRADE CARD SECTION

    /**
     * @Author Mikkel Jürs, Tobias Pedersen Gørlyk s224279, s224271.
     * @param player
     * @param card
     */
    public void executeUpgradeCommand(@NotNull Player player, @NotNull CommandCardField card){
        if(card.getCard() != null){
            Command command = card.getCard().command;
            boolean isPermanent = UpgradeCardInfo.getPermanent(command);
            boolean cardCouldBeUsed = true; //You should determine whether this should be true or not. If you cant use your card, you shouldn't lose it.

            switch (command){
                case BOINK_TUPG:
                    boinkFunctionality(player);
                    break;

                case DEFRAG_GIZMO_PUPG:
                    defragGizmoFunctionality(player);
                    break;
                case SPAM_BLOCKER_TUPG:
                    spamBlockerTemp(player);
                    break;

                case ENERGY:
                    this.playEnergyRoutine(player);
                    break;
                case RAMMING_GEAR_PUPG:
                    break;
                case ENERGY_ROUTINE_TUPG:
                    CommandCard cardEnergy = new CommandCard(Command.ENERGY);
                    this.discardCard(player, cardEnergy);
                    break;
                case SPAM_FOLDER_TUPG:
                    CommandCard cardSpam = new CommandCard(Command.SPAM_FOLDER);
                    discardCard(player, cardSpam);
                    break;
                case RECOMPILE_TUPG:
                    recompileUpgradeCard(player);
                    break;
                case RECHARGE_TUPG:
                    rechargeUpgradeCard(player);
                    break;
                case HACK_TUPG:
                    hackUpgradeCard(player);
                    break;
                case SPEED_TUPG:
                    CommandCard speedCard = new CommandCard(Command.SPEED);
                    discardCard(player, speedCard);
                    break;

                /* Removing from this scope. Not working as intended. ISSUE.
                case REBOOT_TUPG:

                    respawnPlayer(player, player.getHeading());
                    break;
                     */
                case REPEAT_ROUTINE_TUPG:
                    CommandCard repeatRoutineTupgCard = new CommandCard(Command.AGAIN);
                    this.discardCard(player, repeatRoutineTupgCard);
                    break;
                //Execute the Command.ZOOP_TUPG command with 3 options: Left, Right or U-turn.
                case ZOOP_TUPG:
                    zoopFunctionality(player);
                    break;
                case SANDBOX_UPG:
                    sandboxUpgradeCard(player);
                    break;
            }



            if(!isPermanent && cardCouldBeUsed){
                upgradeShop.discardCard(card); //Removes the temporary card from the player and adds it to the discarded upgrade card-pile for the shop.
            }

        } //No card at that spot, so nothing happens.
    }

    public void openUpgradeShop() throws Exception {
        if(upgradeShop == null) upgradeShop = new UpgradeShop(this, board);
        CommandCardField[] cardFields = upgradeShop.getCards(board.getPlayersNumber());
        upgradeShop.setCardsForRound(cardFields);

        Command[] temp = new Command[cardFields.length];

        for (int i = 0; i < cardFields.length; i++) {
            temp[i] = cardFields[i].getCard().command;
        }



        for (int i = 0; i < cardFields.length; i++) {
            System.out.println("Shop cards" + cardFields[i].getCard().command);
        }

        upgradeShop.openShop();
        startProgrammingPhase();
    }



    public void rammingGearFunctionality(Player player, Player playerToMove){
        if(player.getPowerUps().isRammingGear()){
            addDamageCard(playerToMove, Command.SPAM);
        }
    }

    public void barrelLaserFunctionality(Player player, Player playerToShoot){
        if(player.getPowerUps().isBarrelLaser()){
            addDamageCard(playerToShoot, Command.SPAM);
        }
    }

    /** @Author Mikkel Jürs, s224279@student.dtu.dk
     * This method is the functionality of the defragGizmoCard.
     * It uses a couple of methods to determine what choice it should present to user.
     * First it have a cardUsed array {true,true}, that sets the player.GetPowerUps().setDefragGizmo 2nd index to true, so it can't be used again.
     * This is then set to false in the "SetGameStateUpgradeCards" method called at the beginning of each programming phase so it can be used in next round.
     * For each type of damage card you have in your hand it presents
     * @param player
     */
    public void defragGizmoFunctionality(Player player) {
        boolean[] cardUsed = {true, true};
        System.out.println(player.getPowerUps().getDefragGizmo()[0]);
        if (player.getPowerUps().getDefragGizmo()[0]) {
            if (board.getPhase() == Phase.PROGRAMMING && !(player.getPowerUps().getDefragGizmo()[1])) {
                String[] defragOptions = getUniqueDamageCardOptions(player);
                if (defragOptions.length > 0){
                    Option defragOption = new Option("Choose a damage card type to discard permanently");
                    switchBlock:
                    switch (defragOption.getChoice(defragOptions)) {
                        case "SPAM":
                            for (int i = 0; i < Player.NO_CARDS; i++) {
                                if (player.getCardField(i).getCard().getName().equalsIgnoreCase("spam")) {
                                    player.getCardField(i).setCard(null);
                                    player.getCardField(i).setCard(drawTopCard(player));
                                    player.getPowerUps().setDefragGizmo(cardUsed);
                                    break switchBlock;
                                }
                            }
                            break;
                        case "TROJAN HORSE":
                            for (int i = 0; i < Player.NO_CARDS; i++) {
                                System.out.println(player.getCardField(i).getCard().getName());
                                if (player.getCardField(i).getCard().getName().equalsIgnoreCase("trojan horse")) {
                                    player.getCardField(i).setCard(null);
                                    player.getCardField(i).setCard(drawTopCard(player));
                                    player.getPowerUps().setDefragGizmo(cardUsed);
                                    break switchBlock;
                                }
                            }
                            break;
                        case "VIRUS":
                            for (int i = 0; i < Player.NO_CARDS; i++) {
                                if (player.getCardField(i).getCard().getName().equalsIgnoreCase("virus")) {
                                    player.getCardField(i).setCard(null);
                                    player.getCardField(i).setCard(drawTopCard(player));
                                    player.getPowerUps().setDefragGizmo(cardUsed);
                                    break switchBlock;
                                }
                            }
                            break;
                        case "WORM":
                            for (int i = 0; i < Player.NO_CARDS; i++) {
                                if (player.getCardField(i).getCard().getName().equalsIgnoreCase("worm")) {
                                    player.getCardField(i).setCard(null);
                                    player.getCardField(i).setCard(drawTopCard(player));
                                    player.getPowerUps().setDefragGizmo(cardUsed);
                                    break switchBlock;
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    /**
     * @Author Mikkel Jürs, s224279@dtu.dk
     * Creates an Options window and executes the command picked.
     * @param player
     */
    public void boinkFunctionality(Player player){
        String[] options = new String[4];
        options[0] = "Forward";
        options[1] = "Backwards";
        options[2] = "Left";
        options[3] = "Right";
        Option option = new Option("Move to an adjacent space. Do not change direction.");
        switch (option.getChoice(options)) {
            case "Forward":
                try {
                    this.moveForward(player);
                } catch (OutsideBoardException e) {
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
                }
                break;
            case "Backwards":
                try {
                    this.backUp(player);
                } catch (OutsideBoardException e) {
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
                }
                break;
            case "Left":
                moveToLeftSpace(player);
                break;
            case "Right":
                moveToRightSpace(player);
                break;
        }
    }

    public void zoopFunctionality(Player player){
        String[] options = new String[4];
        options[0] = "North";
        options[1] = "South";
        options[2] = "East";
        options[3] = "West";
        Option option = new Option("Rotate to face any direction");
        switch (option.getChoice(options)) {
            case "North":
                player.setHeading(NORTH);
                break;
            case "South":
                player.setHeading(SOUTH);
                break;
            case "East":
                player.setHeading(EAST);
                break;
            case "West":
                player.setHeading(WEST);
                break;
        }
    }

    /**
     * @author Mikkel Jürs, s224279@student.dtu.dk
     * This method loops through the different damage cards a player have in their hand, adds them to a HashSet (Unique identifier)
     * converts the hashset back into an array, and returns that array. This is used for
     * @param player
     * @return
     */
    private String[] getUniqueDamageCardOptions(Player player) {
        Set<String> uniqueCards = new HashSet<>();

        for (int i = 0; i < Player.NO_CARDS; i++) {
            String cardName = player.getCardField(i).getCard().getName().toUpperCase();
            if (cardName.equals("TROJAN HORSE") || cardName.equals("VIRUS") || cardName.equals("WORM") || cardName.equals("SPAM")) {
                uniqueCards.add(cardName);
            }
        }

        return uniqueCards.toArray(new String[0]);
    }

    protected void rechargeUpgradeCard(Player player){
        player.setEnergyCubes(player.getEnergyCubes()+3);
    }

    /** @author Mikkel Jürs, s224279@student.dtu.dk
     * Method for the temp upgrade card: Spam Blocker "Replace Each SPAM damage card in your hand with a card from the
     * top of your deck. Permanently discard the SPAM damage cards.
     * @param player
     */
    public void spamBlockerTemp(Player player){
        for (int i = 0; i< Player.NO_CARDS; i++){
            if(player.getCardField(i).getCard() != null){
            if(player.getCardField(i).getCard().getName().equalsIgnoreCase("spam")){
                player.getCardField(i).setCard(null);
                player.getCardField(i).setCard(drawTopCard(player));
            }
            else {
                System.out.println("You have no spam cards in your hand");
            }
        }
        }
    }

    /**
     * Can't test if this works yet, as the "use" button doesn't work. ISSUE
     * Find current register, and execute it again for the player.
     * @param player
     */
    protected void hackUpgradeCard(Player player) {
        int currentStep = board.getStep();
        Player currentPlayer = board.getCurrentPlayer();
        if(board.getPhase().equals(Phase.ACTIVATION)){
        if (currentStep >= 0 && currentStep < Player.NO_REGISTERS && currentPlayer.getProgramField(currentStep) != null) {
            CommandCard card = currentPlayer.getProgramField(currentStep).getCard();
            if (card != null) {
                Command command = card.command;
                boolean terminate = executeCommand(currentPlayer, command);
                if (terminate) {
                    board.setPhase(Phase.PLAYER_INTERACTION);
                }
            }
        } else {
            System.out.println("You have nothing in your register.");
        }
        } else {
            System.out.println("You're not in activation phase.");
        }
    }

    private boolean moveToLeftSpace(Player player){
        turnLeft(player);
        try {
            moveForward(player);
            turnRight(player);
            return false;
        } catch (OutsideBoardException e) {
            player.setSpace(board.getRespawnSpaces());
            player.setRespawnStatus(true);
            return true;
        }
    }

    private boolean moveToRightSpace(Player player){
        turnRight(player);
        try {
            moveForward(player);
            turnLeft(player);
            return false;
        } catch (OutsideBoardException e) {
            player.setSpace(board.getRespawnSpaces());
            player.setRespawnStatus(true);
            return true;
        }
    }

    public void playSpamFolder(Player player){
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        for(int i = 0; i < discardPile.size(); i++){
            CommandCard cardToRemove = discardPile.get(i);
            if(cardToRemove.getName().equals(Command.SPAM.displayName)){
                discardPile.remove(i);
                break;
            }
        }
    }

    public void sandboxUpgradeCard(Player player){
        CommandCard sandboxCard = new CommandCard(Command.SANDBOX);
        discardCard(player, sandboxCard);
    }


//endregion

}