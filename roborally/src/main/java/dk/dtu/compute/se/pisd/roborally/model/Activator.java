package dk.dtu.compute.se.pisd.roborally.model;

import com.beust.ah.A;
import dk.dtu.compute.se.pisd.roborally.Exceptions.OutsideBoardException;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.EnergyField;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Gear;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Laser;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.Push;

import java.util.ArrayList;

public class Activator {

    private static Activator activator;
    private Board board;
    private GameController controller;

    public static Activator getInstance(){
        if(activator == null) activator = new Activator();
        return activator;
    }

    public void activateBoard(Board board, GameController controller){
        this.board = board;
        this.controller = controller;
        activateBelts();
        activatePush();
        activateGears();
        activateLasers();
        activatePlayerLasers();
        activateEnergyFields();
        activateCheckPoints();
    }

    private void activateBelts(){
        try{
            controller.activateBelts();
        }catch (OutsideBoardException e){

        }
    }
    private void activatePush(){
        ArrayList<Space> spaces = board.getPushSpaces();
        for(int i = 0; i < spaces.size(); i++){
            if(spaces.get(i).getPlayer() != null){
                Player player = spaces.get(i).getPlayer();
                Push push = spaces.get(i).getElement().getPush();
                if(push.getActivateRounds().contains(board.getStep()+1)){
                    try{
                        controller.movePlayerForward(player, 1, push.getHeading(), false);
                    }catch (OutsideBoardException e){

                    }
                }
            }
        }
    }
    private void activateGears(){
        ArrayList<Space> spaces = board.getGearSpaces();
        for(int i = 0; i < spaces.size(); i++){
            if(spaces.get(i).getPlayer() != null){
                Player player = spaces.get(i).getPlayer();
                Gear gear = spaces.get(i).getElement().getGear();
                if(gear.getRotation().equals("RIGHT")) controller.turnRight(player);
                if(gear.getRotation().equals("LEFT")) controller.turnLeft(player);
            }
        }
    }

    private void activateLasers(){
        ArrayList<Space> spaces = board.getLaserSpaces();
        for(int i = 0; i < spaces.size(); i++){
            if(spaces.get(i).getPlayer() != null){
                Player player = spaces.get(i).getPlayer();
                    int amount = spaces.get(i).getElement().getLaser().getDamage();
                    //Deal damage for the amount
                    for(int j = 0; j < amount; j++) {
                        controller.addDamageCard(player, Command.SPAM);
                }
                //Laser laser = spaces.get(i).getElement().getLaser();
            }
        }
    }
    private void activatePlayerLasers(){
        Player[] players = new Player[board.getPlayersNumber()];
        for(int i = 0; i < players.length; i++){
            players[i] = board.getPlayer(i);
        }
        controller.playerLaserActivate(players);
    }

    private void activateEnergyFields(){
        ArrayList<Space> spaces = board.getEnergyFieldSpaces();
        for(int i = 0; i < spaces.size(); i++){
            if(spaces.get(i).getPlayer() != null){
                Player player = spaces.get(i).getPlayer();
                EnergyField energyField = spaces.get(i).getElement().getEnergyField();
                if(energyField.getCubes() > 0) {
                    player.setEnergyCubes(player.getEnergyCubes() + 1);
                    player.updateCubeLabel();
                    energyField.setCubes(energyField.getCubes() - 1);
                    if(energyField.getCubes() > 0) energyField.setShowCube(true);
                    else energyField.setShowCube(false);
                }
                else{
                    if(board.getStep() == 5){
                        player.setEnergyCubes(player.getEnergyCubes() + 1);
                        player.updateCubeLabel();
                    }
                }
            }
        }
    }
    private void activateCheckPoints(){
        controller.activateCheckpoints();
    }


}
