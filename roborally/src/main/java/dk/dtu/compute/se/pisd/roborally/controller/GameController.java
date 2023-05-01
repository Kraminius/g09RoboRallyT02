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
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;

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

    /**
     * Prepares the game for the programming phase by clearing previous programming, setting the game state and give
     * the players new cards
     */
    // XXX: V2
    public void startProgrammingPhase() {
        activateCheckpoints(); //Ser om nogen spiller har nået et checkpoint
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
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
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
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
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
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
        boolean terminate = executeCommand(currentPlayer, command);

        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
            if(!board.isStepMode()){
                continuePrograms();
            }
        } else {
            int step = board.getStep();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
                if(!board.isStepMode()){
                    continuePrograms();
                }
            } else {
                startProgrammingPhase();
            }
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
                this.moveForward(player);
                return false;
            case RIGHT:
                this.turnRight(player);
                return false;
            case LEFT:
                this.turnLeft(player);
                return false;
            case FAST_FORWARD:
                this.fastForward(player);
                return false;
            case U_TURN:
                this.uTurn(player);
                return false;
            case BACK_UP:
                this.backUp(player);
                return false;
            case AGAIN:
                int prevProgramStep = board.getStep() - 1;
                if(prevProgramStep < 0){
                    return false;
                } else {
                    CommandCard prevCommand = player.getProgramField(prevProgramStep).getCard();
                    if(prevCommand == null){
                        return false;
                    } else if (prevCommand.command == Command.AGAIN) {
                        return false; //For now
                    } else {
                        this.again(player, prevCommand.command);
                        return false;
                    }
                }
            default:
                throw new RuntimeException("Should not happen");
        }
    }

    // TODO Assignment V2
    public void moveForward(@NotNull Player player) {
        moveForward(player, 1, null, false);
    }

    // TODO Assignment V2
    public void fastForward(@NotNull Player player) {
        moveForward(player, 1, null, false);
        moveForward(player, 1, null, false);
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
    private boolean moveForward(Player player, int amount, Heading heading, boolean isBelt) {
        try {
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
                if (moveForward(playerToMove, amount, heading, false)) {
                    player.setSpace(space); //There is a player in front and they can move, so we move too.
                    return true;
                } else return false; //There is a player there and they cannot move forward so no one moves.
            } else {
                player.setSpace(space); //There is no player or obstacle in front and we will therefore move there.
                return true;
            }
        } catch (OutsideBoardException e){
            respawnPlayer(player);
        }
        return true;
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
            return space;
        }

    /**@author Freja Egelund Grønnemose, s224286@dtu.dk
     * This methods set the players space to the space of the rebootToken. At some point the method should cover more than 1 reboot token.
     * The method also removes ALL the players remaining program cards.
     * @param player the player that should be respawned
     */
    private void respawnPlayer(@NotNull Player player){
        Space rebootToken = board.getRebootToken();
        player.setSpace(rebootToken);
        for (int j = board.getStep(); j < Player.NO_REGISTERS; j++) {
            CommandCardField field = player.getProgramField(j);
            field.setCard(null);
            field.setVisible(true);
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
    public void backUp(@NotNull Player player) {
        try {
            Space space = getSpaceAt(-1, player.getHeading(), player.getSpace().x, player.getSpace().y);
            player.setSpace(space);
        } catch (OutsideBoardException e){
            respawnPlayer(player);
        }
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
    public void activateBelts() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            int moving;
            if (player.getSpace().belt != null) {
                moving = player.getSpace().belt.getSpeed();
                for (int n = moving; n > 0; n--) {
                    Heading heading = player.getSpace().belt.getHeading();
                    Space spaceInFront = null;
                    switch (heading) {
                        case EAST:
                            try {
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x + 1, player.getSpace().y);
                            } catch (OutsideBoardException e){
                                respawnPlayer(player);
                            }
                            break;
                        case WEST:
                            try {
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x - 1, player.getSpace().y);
                            } catch (OutsideBoardException e){
                                respawnPlayer(player);
                            }
                            break;
                        case NORTH:
                            try {
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y - 1);
                            } catch (OutsideBoardException e){
                                respawnPlayer(player);
                            }
                            break;
                        case SOUTH:
                            try {
                                spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y + 1);
                            } catch (OutsideBoardException e){
                                respawnPlayer(player);
                            }
                            break;
                        default:
                            throw new RuntimeException("No Heading"); //This should not happen
                    }
                    if (spaceInFront == null) return;

                    if (spaceInFront.belt == null) {
                        moveForward(player, 1, heading, false); //Can move players as this would be outside of belt.
                        moving = 0; //No longer moving on a belt so this is set to 0.
                    } else
                        moveForward(player, 1, heading, true); //Won't move players as they are also on a belt and just haven't moved yet.

                    if (spaceInFront.belt.getTurn().equals("LEFT")) turnLeft(player);
                    else if (spaceInFront.belt.getTurn().equals("RIGHT")) turnRight(player);

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
            if (space.checkpoint != null) {
                number = space.checkpoint.getNumber();
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

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

}
