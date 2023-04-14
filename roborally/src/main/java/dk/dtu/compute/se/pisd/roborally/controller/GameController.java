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

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController {

    final public Board board;

    /**
     *
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

    // XXX: V2
    public void startProgrammingPhase() {
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

    // XXX: V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

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

    /*public void executeCommandForTest(@NotNull Player player, Command command) {
        executeCommand(player, command);
    }*/
    // XXX: V2

    /**
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
            default:
                throw new RuntimeException("Should not happen");
                // DO NOTHING (for now)
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


    /**@author Tobias Gørlyk, s224271@dtu.dk
     *
     * Move Forward is a recursive method, that moves a player if there is space in the direction that is free.
     * If there is a player there it moves them forward if they can move forward.
     * If no one can move because of an obstacle or it being outside the board, no one moves.
     * @param player The player to move
     * @param amount The amount to move, for pushing to work it must be either 1 or -1, depending on going forward or backward ones.
     * @param heading This should always be null when called outside this method, unless for belts or pushing. The heading is used during the pushing as they won't necessarily be pushed the direction they are facing. The player will find the heading itself.
     * @param isBelt This should always be null, unless on a belt, where it should ignore players as they move at the same time.
     * @return a boolean value returning true if it can move into the space, returning false if it can't move at all.
     */
    private boolean moveForward(Player player, int amount, Heading heading, boolean isBelt) {
        int x = player.getSpace().x;
        int y = player.getSpace().y;
        Space space = null;
        if(heading == null) heading = player.getHeading();
        space = getSpaceAt(amount, heading, x, y);
        if(space == null) return false; //If space was out of bounds return here.
        if(obstacleInSpace(player.getSpace(), space, heading)) return false;
        Player playerToMove = space.getPlayer();
        if(isBelt){
            player.setSpace(space);
            return true;
        }
        if(playerToMove != null){ //Check if there is a player already on this field.
            if(moveForward(playerToMove, amount, heading, false)){
                player.setSpace(space); //There is a player in front and they can move, so we move too.
                return true;
            }
            else return false; //There is a player there and they cannot move forward so no one moves.
        }
        else{
            player.setSpace(space); //There is no player or obstacle in front and we will therefore move there.
            return true;
        }
    }

    private Space getSpaceAt(int amount, Heading heading, int x, int y){
        Space space = null;
        switch (heading) {
            case NORTH:
                if ((amount < 0 && y < board.height + amount) || (y > amount - 1 && amount > 0)) space = board.getSpace(x, y - amount);
                else System.out.println("outside of board");
                break;
            case EAST:
                if ((amount < 0 && x > 0) || (x < board.width - amount)) space =board.getSpace(x + amount, y);
                else System.out.println("outside of board");
                break;
            case WEST:
                if ((amount < 0 && x < (board.width + amount)) || (x > amount - 1)) space = board.getSpace(x - amount, y);
                else System.out.println("outside of board");
                break;
            case SOUTH:
                if ((amount < 0 && y > 0) || (y < board.height - amount)) space = board.getSpace(x, y + amount);
                else System.out.println("outside of board");
                break;
        }
        return space;
    }
    /**@author
     *
     * Checks for obstacles in a given space, used when moving a player into a field
     *
     * @param fromSpace The Space where the player currently is and will move from
     * @param toSpace The space where the player is moving to.
     * @param heading The direction the players is coming from.
     * @return
     */
    private boolean obstacleInSpace(Space fromSpace, Space toSpace, Heading heading){


        return false;

    }

    // TODO Assignment V2
    public void turnRight(@NotNull Player player) {
        player.setHeading(player.getHeading().next());
    }

    // TODO Assignment V2
    public void turnLeft(@NotNull Player player) {
        player.setHeading(player.getHeading().prev());
    }

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
     *
     * Activates the belts on the board for all players one at a time but right after each other. While a player is on a belt they don't have any push collision.
     *
     */
    public void activateBelts(){
        for(int i = 0; i < board.getPlayersNumber(); i++){
            Player player = board.getPlayer(i);
            int moving;
            if(player.getSpace().belt != null){
                moving = player.getSpace().belt.speed;
                for(int n = moving; n > 0; n--){
                    Heading heading = player.getSpace().belt.heading;
                    Space spaceInFront = null;
                    switch (heading){
                        case EAST:
                            spaceInFront = getSpaceAt(1, heading, player.getSpace().x+1, player.getSpace().y);
                            break;
                        case WEST:
                            spaceInFront = getSpaceAt(1, heading, player.getSpace().x-1, player.getSpace().y);
                            break;
                        case NORTH:
                            spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y-1);
                            break;
                        case SOUTH:
                            spaceInFront = getSpaceAt(1, heading, player.getSpace().x, player.getSpace().y+1);
                            break;
                        default:
                            System.out.println("No Heading"); //This should not happen
                            break;
                    }
                    if(spaceInFront == null) return;

                    if(spaceInFront.belt == null){
                        moveForward(player, 1, heading, false); //Can move players as this would be outside of belt.
                        moving = 0; //No longer moving on a belt so this is set to 0.
                    }
                    else moveForward(player, 1, heading, true); //Won't move players as they are also on a belt and just haven't moved yet.

                    if(spaceInFront.belt.turn.equals("Left")) turnLeft(player);
                    else if(spaceInFront.belt.turn.equals("Right")) turnRight(player);

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
