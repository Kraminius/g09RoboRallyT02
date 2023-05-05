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

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.view.PlayerView;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 9;
    final public static int NO_UPGRADE_CARDS = 6;

    final public Board board;

    private int id;

    private int energyCubes;
    private Label energyCubeLabel;

    private String name;
    private String color;

    private Space space;
    private Heading heading = SOUTH;

    private boolean respawnStatus;
    private PlayerView playerView;

    private CommandCardField[] program;
    private CommandCardField[] cards;
    private CommandCardField[] upgradeCards;

    private ArrayList<CommandCard> cardDeck;

    private ArrayList<CommandCard> discardPile;
    private UpgradeCardInfo info = new UpgradeCardInfo();
    //Har lavet et array til at se alle checkpoints samlet
    private boolean[] checkpointsReached;

    private PowerUps powerUps;

    public Player(@NotNull Board board, String color, @NotNull String name, @NotNull int id) {
        this.board = board;
        this.name = name;
        this.color = color;
        this.id = id;

        this.space = null;

        this.cardDeck = new ArrayList<>();
        this.discardPile = new ArrayList<>();

        this.respawnStatus = false;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
        upgradeCards = new CommandCardField[NO_UPGRADE_CARDS];
        for (int i = 0; i < upgradeCards.length; i++) {
            upgradeCards[i] = new CommandCardField(this);
        }
        //Her gives spilleren antallet af checkpoints
        checkpointsReached = new boolean[board.getCheckPointSpaces().size()];

        this.powerUps = new PowerUps();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    public void setProgramField(int i, CommandCardField newProgram)
    {
        program[i] = newProgram;
    }


    public CommandCardField getCardField(int i) {
        return cards[i];
    }

    public void setCardField(int i, CommandCardField newCard) {
        cards[i] = newCard;
    }

    public CommandCardField getUpgradeCard(int i) {
        return upgradeCards[i];
    }
    public CommandCardField[] getUpgradeCards(){
        return this.upgradeCards;
    }


    public void setUpgradeCards(CommandCardField[] upgradeCards) {
        this.upgradeCards = upgradeCards;
    }

    public ArrayList<CommandCard> getCardDeck(){
        return cardDeck;
    }


    public ArrayList<CommandCard> getDiscardPile(){
        return discardPile;
    }

    public boolean getRespawnStatus(){
        return respawnStatus;
    }

    public void setRespawnStatus(Boolean status){
        respawnStatus = status;
    }
    //Method to get their checkpoints reached
    public boolean[] getCheckpointReadhed() {
        return checkpointsReached;
    }
    //Method to change their checkpoints reached
    public void setCheckpointReadhed(int Checkpoint, boolean state) {
        checkpointsReached[Checkpoint] = state;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }
    public int getEnergyCubes() {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }
    public void setEnergyCubeLabel(Label label){
        energyCubeLabel = label;
    }
    public void updateUpgradeCardView(){
        CommandCardField[] tempArray = new CommandCardField[NO_UPGRADE_CARDS];
        int perm = 0;
        int nonPerm = 3;
        for(int i = 0; i < tempArray.length; i++){
            tempArray[i] = new CommandCardField(this);
        }
        for(int i = 0; i < tempArray.length; i++){
            if(upgradeCards[i].getCard() != null){
                if(info.getPermanent(upgradeCards[i].getCard().command)){
                    tempArray[perm].setCard(upgradeCards[i].getCard());
                    perm++;
                }
                else{
                    tempArray[nonPerm].setCard(upgradeCards[i].getCard());
                    nonPerm++;
                }
            }
        }
        for(int i = 0; i < upgradeCards.length; i++){
            upgradeCards[i].setCard(tempArray[i].getCard());
        }
        updateCubeLabel();
    }
    public void updateCubeLabel(){
        energyCubeLabel.setText(energyCubes + "");
    }

    public PowerUps getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(PowerUps powerUps) {
        this.powerUps = powerUps;
    }

    public CommandCardField[] getProgram() {
        return program;
    }

    public void setProgram(CommandCardField[] program) {
        this.program = program;
    }

    public CommandCardField[] getCards() {
        return cards;
    }

    public void setCards(CommandCardField[] cards) {
        this.cards = cards;
    }

}
