package dk.dtu.compute.se.pisd.roborally.controller;
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

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.BoardLoader;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk & Nicklas Christensen ss224314
 *This is a new class used so Junit-test can reach and manipulate the board easily
 */
public class BoardTest extends Subject {

    public int width;

    public int height;

    public String boardName;

    private Integer gameId;

    public Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    private final int CHECKPOINTS = 3;

    private Space[] checkpoints;

    private Space rebootToken;

    dk.dtu.compute.se.pisd.roborally.model.Board board;

    private Space antenna;


    /**@Author Nicklas Christensen - s224314@dtu.dk
     * A way to make boards for testing
     */
    public dk.dtu.compute.se.pisd.roborally.model.Board boardTest(int width, int height){

        //Board board = new Board("eh");
        board = new dk.dtu.compute.se.pisd.roborally.model.Board("board_0");
        //BoardLoader.getInstance().loadBoard("board_0", board);

        board.width = width;
        board.height = height;
        board.spaces = new Space[width-1][height-1];

        for(int i = 0; i < width-1; i++){
            for(int j = 0; j < height-1; j++){
                Space space = new Space(board,i, j);
                board.spaces[i][j] = space;
            }
        }
        board.boardName = "Test";
        board.setGameId(666);

        return board;
    }

    public Integer getGameId() {
        return board.getGameId();
    }

    public void setGameId(int gameId) {
        if (board.getGameId() == null) {
            board.setGameId(gameId);
        } else {
            if (!board.getGameId().equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < board.width &&
                y >= 0 && y < board.height) {
            return board.getSpace(x, y);
        } else {
            return null;
        }
    }

    public int getPlayersNumber() {
        return board.getPlayersNumber();
    }

    public void addPlayer(@NotNull Player player) {
        Player playeries = null;
        for(int i = 0; i < board.getPlayersNumber(); i++){
            if(board.getPlayer(i) == player){
                playeries = player;
            }
        }
        if (player.board == board && playeries == null) {
            board.addPlayer(player);
            //notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < board.getPlayersNumber()) {
            return board.getPlayer(i);
        } else {
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return board.getCurrentPlayer();
    }

    public void setCurrentPlayer(Player player) {
        board.setCurrentPlayer(player);
        /*
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }*/
    }

    public Phase getPhase() {
        return board.getPhase();
    }

    public void setPhase(Phase phase) {
        board.setPhase(phase);
        /*
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }*/
    }

    public int getStep() {
        return board.getStep();
    }

    public void setStep(int step) {
        board.setStep(step);
        /*
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
                */
    }

    public boolean isStepMode() {
        return board.isStepMode();
    }

    public void setStepMode(boolean stepMode) {
       board.setStepMode(stepMode);
        /*
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
                 */
    }

    public int getPlayerNumber(@NotNull Player player) {
        return board.getPlayerNumber(player);
        /*
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
                 */
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        return board.getNeighbour(space, heading);
        /*
        int x = space.x;
        int y = space.y;
        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }

        return (x < 0 || x >= width || y < 0 || y >= height) ? null : getSpace(x, y);
        */
    }

    public List<Player> findPlayerWithinRadius(Player currentPlayer){
        return board.findPlayerWithinRadius(currentPlayer);
        /*
        List<Player> playersWithinRadius = new ArrayList<>();
        int currentX = currentPlayer.getSpace().x;
        int currentY = currentPlayer.getSpace().y;
        int radius = 6;
        for(int x = currentX - radius; x <= currentX + radius; x++){
            for(int y = currentY - radius; x <= currentY + radius; y++){
                if(x < 0 || x >= board.height || y < 0 || y >= board.width){
                    continue;
                }
                Space space = getSpace(x, y);
                Player player = space.getPlayer();
                if(player != null && player != currentPlayer){
                    playersWithinRadius.add(player);
                }

                for(Heading heading : Heading.values()){
                    Space neighbor = board.getNeighbour(space, heading);
                    if(neighbor != null){
                        Player neighborPlayer = neighbor.getPlayer();
                        if(neighborPlayer != null && neighborPlayer != currentPlayer){
                            playersWithinRadius.add(neighborPlayer);
                        }
                    }
                }
            }
        }
        return playersWithinRadius; */
    }

    public String getStatusMessage() {
        return board.getStatusMessage();
        /*
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
                */
    }

    public Space getAntenna() {
        return board.getAntenna();
    }

    public void setAntenna(Space antenna) {
        board.setAntenna(antenna);
        //this.antenna = antenna;
    }

    /*
    Lasers
    EnergyFields
    Belts
    Gear
    Push
    StartFields
    Walls

    bools
    Antenna
    Respawn
    Hole
    OutsideOfBoardSpaces
    Repair
     */

    /**
     * getter for checkpoints on board
     * @return an unsorted ArrayList of Spaces containing that specific Element
     */

    public ArrayList<Space> getCheckPointSpaces(){
        return board.getCheckPointSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getCheckpoint() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;*/
    }
    public ArrayList<Space> getLaserSpaces(){
        return board.getLaserSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getLaser() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                */
    }
    public ArrayList<Space> getEnergyFieldSpaces(){
        return board.getEnergyFieldSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getEnergyField() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }
    public ArrayList<Space> getGearSpaces(){
        return board.getGearSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getGear() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }
    public ArrayList<Space> getPushSpaces(){
        return board.getPushSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getPush() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }
    public ArrayList<Space> getStartFieldSpaces(){
        return board.getStartFieldSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getStartField() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }

    public ArrayList<Space> getWallSpaces(){
        return board.getWallSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getWall() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }
    public Space getAntennaSpace(){
        return board.getAntennaSpace();
        /*
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isAntenna()) return this.spaces[x][y];
            }
        }
        return null;

         */
    }

    public Space getRespawnSpaces(){
        return board.getRespawnSpaces();
        /*
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isRespawn()) return this.spaces[x][y];
            }
        }
        return null;
         */
    }
    public ArrayList<Space> getHoleSpaces(){
        return board.getHoleSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isHole()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
                 */
    }
    public ArrayList<Space> getOutSideOfBoardSpaces(){
        return board.getOutSideOfBoardSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(!this.spaces[x][y].getElement().isSpace()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;

         */
    }
    public ArrayList<Space> getRepairSpaces(){
        return board.getRepairSpaces();
        /*
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isRepair()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;

         */
    }
}
