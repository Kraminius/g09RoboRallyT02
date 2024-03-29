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
package dk.dtu.compute.se.pisd.roborally.model;

import com.fasterxml.jackson.annotation.*;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.BoardLoader;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;
import static java.lang.Integer.parseInt;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */

public class Board extends Subject {

    public int width;

    public int height;
    private boolean isWaiting = false;


    public String boardName;

    private Integer gameId;

    public Space[][] spaces;


    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    Board board;

    private Space antenna;



    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Creates a Board by loading a .json file under an id, if it doesn't exist it loads a testboard instead.
     *
     *
     */
    public Board(String boardName) {
        if (!BoardLoader.getInstance().loadBoard(boardName, this)) {
            System.out.println("Board not found with the name [" + boardName + "], loaded \" TESTBOARD\" instead.");
            BoardLoader.getInstance().loadBoard("TESTBOARD", this);
        }
    }

    public Board(){
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method return the number of players in the game.
     * @return the number of players in the game.
     */
    public int getPlayersNumber() {
        return players.size();
    }

    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
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
    }

    /**
     * @Author Freja Egelund Grønnemose, s224286@dtu.dk
     * This method finds all players in a radius of 6 from the given player.
     * @param currentPlayer the given player.
     * @return arraylist with all players in an radius of 6 from the given player.
     */
    public ArrayList<Player> findPlayerWithinRadius(Player currentPlayer){
        ArrayList<Player> playersWithinRadius = new ArrayList<>();
        int currentX = currentPlayer.getSpace().x;
        int currentY = currentPlayer.getSpace().y;
        int radius = 6;
        for(int x = Math.max(0, currentX - radius); x <= Math.min(width - 1, currentX + radius); x++){
            for(int y = Math.max(0, currentY - radius); y<= Math.min(height - 1, currentY + radius); y++){
                Space space = getSpace(x,y);
                Player player = space.getPlayer();
                if(player != null && player != currentPlayer){
                    playersWithinRadius.add(player);
                }
            }
        }
        return playersWithinRadius;
    }

    public String getStatusMessage() {
        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V2 changed the status so that it shows the phase, the player and the step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }

    public Space getAntenna() {
        return antenna;
    }

    public void setAntenna(Space antenna) {
        this.antenna = antenna;
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
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getCheckpoint() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getLaserSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getLaser() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getEnergyFieldSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getEnergyField() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getGearSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getGear() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getPushSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getPush() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getStartFieldSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getStartField() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }

    public ArrayList<Space> getWallSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().getWall() != null) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public Space getAntennaSpace(){
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isAntenna()) return this.spaces[x][y];
            }
        }
        return null;
    }

    public Space getRespawnSpaces(){
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isRespawn()) return this.spaces[x][y];
            }
        }
        return null;
    }
    public ArrayList<Space> getHoleSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isHole()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getOutSideOfBoardSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(!this.spaces[x][y].getElement().isSpace()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public ArrayList<Space> getRepairSpaces(){
        ArrayList<Space> spaces = new ArrayList<>();
        for(int x = 0; x < this.spaces.length; x++){
            for(int y = 0; y < this.spaces[x].length; y++){
                if(this.spaces[x][y].getElement().isRepair()) spaces.add(this.spaces[x][y]);
            }
        }
        return spaces;
    }
    public void useCard(Command command, Player player){

    }
    public boolean getIsWaiting(){
        return isWaiting;
    }
    public void setIsWaiting(boolean isWaiting){
        this.isWaiting = isWaiting;
    }
}
