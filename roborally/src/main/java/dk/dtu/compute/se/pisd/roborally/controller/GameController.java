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
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.UpgradeShop;
import dk.dtu.compute.se.pisd.roborally.view.ViewObserver;
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
        board.setPhase(Phase.UPGRADE);
    }

    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        //board.setCurrentPlayer(sequence.get(0));
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

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

    // XXX: V2

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
     * This methods creates an EnumSet of all valid commands (damage cards removed), and then adds new CommandCard objects to the players card deck,
     * with the valid enum. It has an int array that contains the value of how many cards of a type that exist in a player deck.
     * @param playerDeck the ArrayList that holds the players cards.
     */
    public void fillStartDeck(@NotNull ArrayList<CommandCard> playerDeck){
        Set<Command> validCommands = EnumSet.allOf(Command.class);
        validCommands.removeAll(EnumSet.of(Command.SPAM, Command.TROJAN_HORSE, Command.WORM, Command.VIRUS, Command.OPTION_LEFT_RIGHT, Command.SPEED, Command.WEASEL, Command.SANDBOX, Command.SPAM_FOLDER, Command.ENERGY, Command.REPEAT, Command.RAMMING_GEAR_PUPG, Command.SPAM_BLOCKER_TUPG, Command.ENERGY_ROUTINE_TUPG, Command.SPAM_FOLDER_TUPG));

        int[] counts = {5, 3, 3, 3, 1, 1, 1, 2, 1};
        int index = 0;
        for(Command command : validCommands){
            int count = counts[index];
            for(int i = 0; i < count; i++){
                playerDeck.add(new CommandCard(command));
            }
            index++;
        }
        Collections.shuffle(playerDeck);
    }
    public void openUpgradeShop(){
        UpgradeShop upgradeShop = new UpgradeShop();
        upgradeShop.openShop(board, this);
        startProgrammingPhase();
    }

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
                Command.VIRUS));

        for(Command command : upgradeCards){
            upgradeDeck.add(new CommandCard(command));
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
        //Checks who has priority
        antennaPriority();
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
                    if (terminate){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                }
                //int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                /*if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }*/

                if(sequence.size() == 0){
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        antennaPriority();
                        board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
                    } else {
                        //Probably upgrade phase here?
                        startProgrammingPhase();
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
                antennaPriority();
                board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
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
    private boolean executeCommand(@NotNull Player player, @NotNull Command command) {
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
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
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
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
                    return true;
                }
            case SPRINT_FORWARD:
                try {
                    this.sprintForward(player);
                    return false;
                } catch (OutsideBoardException e){
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
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
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
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
                        prevCommand = player.getProgramField(prevProgramStep - 1).getCard();
                        this.again(player, prevCommand.command);
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
            case REPEAT:
                executeCommand(player, Command.AGAIN);
                return false;
            case SPEED:
                try {
                    this.sprintForward(player);
                } catch (OutsideBoardException e){
                    player.setSpace(board.getRespawnSpaces());
                    player.setRespawnStatus(true);
                    return true;
                }
                return false;
            case SPAM_FOLDER:
                this.playSpamFolder(player);
                return false;
            case ENERGY:
                this.playEnergyRoutine(player);
                return false;
            case RAMMING_GEAR_PUPG:
                return false;
            case SPAM_BLOCKER_TUPG:
                spamBlockerTemp(player);
                return false;
            case ENERGY_ROUTINE_TUPG:
                CommandCard card = new CommandCard(Command.ENERGY);
                this.discardCard(player, card);
                return false;
            case SPAM_FOLDER_TUPG:

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
                if (movePlayerForward(playerToMove, amount, heading, false)) {
                    player.setSpace(space);//There is a player in front and they can move, so we move too.
                    return true;
                } else return false; //There is a player there and they cannot move forward so no one moves.
            } else {
                player.setSpace(space); //There is no player or obstacle in front and we will therefore move there.
                return true;
            }
    }

    /**
     *
     * @param amount the amount of spaces the player should move
     * @param heading the players heading
     * @param x the x value of the space they want to move to
     * @param y the y value of the space they want to move to
     * @return the target space
     * @throws OutsideBoardException if player ends up outside of board
     */
    private Space getSpaceAt(int amount, Heading heading, int x, int y) throws OutsideBoardException {
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
                        space = board.getSpace(x + Math.abs(amount), y);
                    } else if (x < board.width - amount && amount >= 0) {
                        space = board.getSpace(x + amount, y);
                    } else {
                        throw new OutsideBoardException();
                    }
                    break;
                case WEST:
                    if (amount < 0 && x < board.width - 1) {
                        space = board.getSpace(x - Math.abs(amount), y);
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

    /**@author Freja Egelund Grønnemose, s224286@dtu.dk
     * This methods set the players space to the space of the rebootToken. At some point the method should cover more than 1 reboot token.
     * The method also removes ALL the players remaining program cards.
     * @param player the player that should be respawned
     */
    public void respawnPlayer(@NotNull Player player, @NotNull Heading heading){
        Player currentPlayer = player;
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
        if(sequence.size() == 0){
            int step = board.getStep();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                antennaPriority();
                board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
            } else {
                startUpgradePhase();
            }
        }else{
            board.setCurrentPlayer(board.getPlayer(sequence.get(0).getId()-1));
        }
    }

    /**
     * @param fromSpace The Space where the player currently is and will move from
     * @param toSpace   The space where the player is moving to.
     * @return
     * @author Checks for obstacles in a given space, used when moving a player into a field
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
                if (toSpace.getWallHeading().get(i) == directionHeadingTo) {
                    obstacle = true;
                }
            }
        }

        if (fromSpace.getWallHeading() != null && directionHeadingFrom != null) {
            for (int i = 0; i < fromSpace.getWallHeading().size(); i++) {
                if (fromSpace.getWallHeading().get(i) == directionHeadingFrom) {
                    obstacle = true;
                }
            }
        }


        return obstacle;

    }

    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

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

    public void powerUp(@NotNull Player player, int cubeAmount){
        player.setEnergyCubes(cubeAmount);
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
                    player.setCheckpointReadhed(0, true);
                    System.out.println("Player: " + (i + 1) + " has reached checkpoint: " + (number));
                } else if (checkpointStatus[(number - 2)]) {
                    player.setCheckpointReadhed(number - 1, true);
                    System.out.println("Player: " + (i + 1) + " has reached checkpoint: " + (number));
                }
            }
        }
    }

    public void addDamageCard(Player player, Command type){
        CommandCard damageCard = new CommandCard(type);
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        discardPile.add(damageCard);
    }

    /**
     * This method get the topCard from the players deck, places it in the current register (and shows it),
     * then it calls the executeCommand() with the topCards command.
     * @param player
     */
    public void playSpam(Player player){
        int step = board.getStep();
        CommandCard topCard = drawTopCard(player);
        CommandCardField currentRegister = player.getProgramField(step);
        currentRegister.setCard(topCard);
        currentRegister.setVisible(true);
        executeCommand(player, topCard.command);
    }

    public void playTrojan(Player player){
        for(int i = 0; i < 2; i++){
            addDamageCard(player, Command.SPAM);
        }
        playSpam(player);
    }

    /**
     * Method for the temp upgrade card: Spam Blocker "Replace Each SPAM damage card in your hand with a card from the
     * top of your deck. Permanently discard the SPAM damage cards.
     * @param player
     */
    public void spamBlockerTemp(Player player){
        for (int i = 0; i< Player.NO_CARDS; i++){
            if(player.getCardField(i).getCard().getName().equals("SPAM")){
                player.getCardField(i).setCard(null);
                player.getCardField(i).setCard(drawTopCard(player));
            }

        }
    }

    public void playVirus(Player player){
        List<Player> playersInRadius = board.findPlayerWithinRadius(player);
        for(Player otherPlayer : playersInRadius){
            addDamageCard(otherPlayer, Command.SPAM);
        }
        playSpam(player);
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
     * Method for determining in what order the players will play their turn.
     */
    public void antennaPriority(){

        List<Player> sortedPlayers = findPlayerSequence(board.getAntenna());

        //Testing to see the player sequence in console
        System.out.println("Player sequence:");
        for (Player player : sortedPlayers) {
            System.out.println("Player " + player.getName());
        }

        //Maybe change how we save the sequence playerlist
        this.sequence = sortedPlayers;
    }


    /**
     * Calculates the sequence.
     * @param antenna
     * @return
     */
    public List<Player> findPlayerSequence(Space antenna) {

        List<Player> players = new ArrayList<>();

        //Used for checking the position of each player
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            players.add(board.getPlayer(i));
            System.out.println("x: " + board.getPlayer(i).getSpace().x+ " y: " + board.getPlayer(i).getSpace().x);

        }

        //Antenna sequence
        System.out.println("Antenna: " + antenna.x + " " +antenna.y);

        List<Player> sortedPlayers = new ArrayList<>(players);

        Collections.sort(sortedPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                double distance1 = calculateDistance(antenna, p1.getSpace());
                double distance2 = calculateDistance(antenna, p2.getSpace());
                if (distance1 == distance2) {
                    return compareByClockwiseRadar(antenna, p1, p2);
                } else {
                    return Double.compare(distance1, distance2);
                }

            }
        });

        return sortedPlayers;
    }

    /**
     * Calculates the distance between player and antenna.
     * @param space
     * @param antenna
     * @return
     */
    public double calculateDistance(Space space, Space antenna){
        int dx = Math.abs(space.x - antenna.x);
        int dy = Math.abs(space.y - antenna.y);

        return dx + dy;

    }

    /**
     * If 2 or more players have the same distance a rader will determine who goes first. This is based on a direction
     * then based on that direction it follows a clockwise movement.
     * @param antenna
     * @param p1
     * @param p2
     * @return
     */
    public int compareByClockwiseRadar(Space antenna, Player p1, Player p2) {

        //Right now the direction is always east, Antenna object should have a direction value on it
        String initialDirection = "east";

        double angle1 = Math.atan2(p1.getSpace().y - antenna.y, p1.getSpace().x - antenna.x);
        double angle2 = Math.atan2(p2.getSpace().y - antenna.y, p2.getSpace().x - antenna.x);

        angle1 = adjustAngle(angle1, initialDirection);
        angle2 = adjustAngle(angle2, initialDirection);

        if (angle1 < 0) {
            angle1 += 2 * Math.PI;
        }
        if (angle2 < 0) {
            angle2 += 2 * Math.PI;
        }

        return Double.compare(angle1, angle2);
    }

    /**
     * Adjusts the angle based on direction (North, west, east and south).
     * @param angle
     * @param initialDirection
     * @return
     */
    public double adjustAngle(double angle, String initialDirection) {
        double adjustment;
        switch (initialDirection.toLowerCase()) {
            case "north":
                adjustment = Math.PI / 2;
                break;
            case "west":
                adjustment = Math.PI;
                break;
            case "south":
                adjustment = -Math.PI / 2;
                break;
            case "east":
            default:
                adjustment = 0;
                break;
        }
        return angle + adjustment;
    }

    public void pickStartPosition(){


        //Used for checking the position of each player
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            //Maybe set current player
            //Space space = drop down menu;
            //board.getPlayer(i).setSpace(space);

        }




    }







    /**
     * Make all the given players shoot a laser.
     * The laser gives 1 SPAM Card to any players hit
     * @param players an array with all the players, this should be available through board.
     */
    public void playerLaserActivate(Player[] players){
        for(int i = 0; i < players.length; i++){
            Space start = players[i].getSpace();
            Heading direction = players[i].getHeading();
            Heading directOposite;
            int move;
            boolean end = false;
            switch (direction){
                case SOUTH:
                    move = -1; //y
                    directOposite = NORTH;
                    break;
                case NORTH:
                    move = 1; //y
                    directOposite = SOUTH;
                    break;
                case WEST:
                    move = -1; //x
                    directOposite = EAST;
                    break;
                case EAST:
                    move = 1; //x
                    directOposite = WEST;
                    break;
                default:
                    throw new IllegalStateException("PlayerLaser - Unexpected (Heading)value: " + direction);
            }
            //Maybe a Do while
            while(end == false){
                //Are we moving into a wall?
                if(start.getWallHeading().contains(direction)){
                    end = true;
                }
                //We are moving verticaly
                if(direction == SOUTH || direction == NORTH){
                    if(start.y <= 0 || start.y >= board.height){
                        end = true;
                    }
                    else{start = board.getSpace(start.x,(start.y + move));}
                    //are we hitting a wall
                    if(start.getWallHeading().contains(directOposite)){
                        end = true;
                    }
                    //Are we moving into a player?
                    else if(start.getPlayer() != null){
                        end = true;
                        //Deal damage to player
                        addDamageCard(start.getPlayer(), Command.SPAM);
                    }
                }
                //We are moving horisontaly
                if(direction == EAST || direction == WEST){
                    if(start.x < 0 || start.x > board.width){
                        end = true;
                    }
                    else{start = board.getSpace((start.x + move), start.y);
                        //are we hitting a wall
                        if(start.getWallHeading().contains(directOposite)){
                            end = true;
                        }
                        //Are we moving into a player?
                        else if(start.getPlayer() != null){
                            end = true;
                            //Deal damage to player
                            addDamageCard(start.getPlayer(), Command.SPAM);
                        }
                    }
                }
            }
        }
    }

    public void tempUpgradeCards(int tempCardID){

        switch (tempCardID){

        }

    }



}
