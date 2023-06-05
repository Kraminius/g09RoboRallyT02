package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BoardBuildLogic {
    private static ImageLoader imageLoader = new ImageLoader();


    public static ArrayList<Boolean> getActiveElements(BoardBuildElement element){
        ArrayList<Boolean> arrayList = new ArrayList<>();
        arrayList.add(false); //Empty
        if(element.isAntenna()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isEnergyField()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isHole()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isLaserPointer() || element.isLaserRay()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isRepair()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isRespawn()) arrayList.add(true);
        else arrayList.add(false);
        if(element.getStartField() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getCheckpoint() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getWall() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getPush() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getGear() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getGreenBelt() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.getBlueBelt() > 0) arrayList.add(true);
        else arrayList.add(false);
        if(element.isNoField()) arrayList.add(true);
        else arrayList.add(false);
        return arrayList;
    }


    public static String checkSizeInput(String[] input){
        int x = 0;
        int y = 0;
        String error;
        try{
            x = parseInt(input[0]);
            y = parseInt(input[1]);
        }catch (Exception e){
            error = "Input not recognised as number";
            return error;
        }
        if(x > 24) {
            error = "The width is larger than the maximum of 24";
            return error;
        }
        if(x < 2) {
            error = "The width is smaller than the minimum of 2";
            return error;
        }
        if(y > 15) {
            error = "The height is larger than the maximum of 15";
            return error;
        }
        if(y < 2) {
            error = "The height is smaller than the minimum of 2";
            return error;
        }
        else return null;
    }
    public static ArrayList<StackPane> getBoardElementImages(){
        ArrayList<StackPane> images = new ArrayList<>();
        images.add(getElement("empty", 0).getView());
        images.add(getElement("antenna", 0).getView());
        images.add(getElement("energyField", 0).getView());
        images.add(getElement("hole", 0).getView());
        images.add(getElement("laserStart", 1).getView());
        images.add(getElement("repair", 0).getView());
        images.add(getElement("respawn", 0).getView());
        images.add(getElement("startField", 1).getView());
        images.add(getElement("checkpoint", 1).getView());
        images.add(getElement("wall", 1).getView());
        images.add(getElement("push", 1).getView());
        images.add(getElement("gear", 1).getView());
        images.add(getElement("greenBelt", 1).getView());
        images.add(getElement("blueBelt", 1).getView());
        images.add(getElement("noField", 0).getView());
        return images;
    }
    public static ArrayList<StackPane> getBoardVariants(int type){
        ArrayList<StackPane> images = new ArrayList<>();
        images.add(getElement("empty", 0).getView());
        switch (type){
            case 4: //Laser
                images.add(getElement("laserStart", 1).getView());
                images.add(getElement("laserBeam", 1).getView());
                images.add(getElement("laserStart", 2).getView());
                images.add(getElement("laserBeam", 2).getView());
                images.add(getElement("laserStart", 3).getView());
                images.add(getElement("laserBeam", 3).getView());
                break;
            case 7:
                images.add(getElement("startField", 1).getView());
                images.add(getElement("startField", 2).getView());
                images.add(getElement("startField", 3).getView());
                images.add(getElement("startField", 4).getView());
                images.add(getElement("startField", 5).getView());
                images.add(getElement("startField", 6).getView());
                break;
            case 8:
                images.add(getElement("checkpoint", 1).getView());
                images.add(getElement("checkpoint", 2).getView());
                images.add(getElement("checkpoint", 3).getView());
                images.add(getElement("checkpoint", 4).getView());
                images.add(getElement("checkpoint", 5).getView());
                images.add(getElement("checkpoint", 6).getView());
                break;
            case 9: //Wall
                images.add(getElement("wall", 1).getView());
                images.add(getElement("wall", 2).getView());
                images.add(getElement("wall", 3).getView());
                images.add(getElement("wall", 4).getView());
                images.add(getElement("wall", 5).getView());
                break;
            case 10: //Push
                images.add(getElement("push", 1).getView());
                images.add(getElement("push", 2).getView());
                break;
            case 11: //Gear
                images.add(getElement("gear", 1).getView());
                images.add(getElement("gear", 2).getView());
                break;
            case 12: //Green Belt
                images.add(getElement("greenBelt", 1).getView());
                images.add(getElement("greenBelt", 2).getView());
                images.add(getElement("greenBelt", 3).getView());
                images.add(getElement("greenBelt", 4).getView());
                images.add(getElement("greenBelt", 5).getView());
                images.add(getElement("greenBelt", 6).getView());
                images.add(getElement("greenBelt", 7).getView());
                break;
            case 13: //Blue Belt
                images.add(getElement("blueBelt", 1).getView());
                images.add(getElement("blueBelt", 2).getView());
                images.add(getElement("blueBelt", 3).getView());
                images.add(getElement("blueBelt", 4).getView());
                images.add(getElement("blueBelt", 5).getView());
                images.add(getElement("blueBelt", 6).getView());
                images.add(getElement("blueBelt", 7).getView());
                break;
        }
        return images;
    }
    public static void changeElementVariant(BoardBuildElement element, int type, int value){
        switch (type){
            case 4: //Laser
                switch (value){
                    case 0:
                        element.setLaserPointer(false);
                        element.setLaserRay(false);
                        element.setLaserStrength(0);
                        break;
                    case 1:
                    case 3:
                    case 5:
                        element.setLaserPointer(true);
                        element.setLaserRay(true);
                        element.setLaserStrength((value+1)/2);
                        break;
                    case 2:
                    case 4:
                    case 6:
                        element.setLaserPointer(false);
                        element.setLaserRay(true);
                        element.setLaserStrength(value/2);
                        break;
                }
                break;
            case 7: //StartField
                element.setStartField(value);
                break;
            case 8: //Checkpoint
                element.setCheckpoint(value);
                break;
            case 9: //Wall
                element.setWall(value);
                break;
            case 10: //Push
                element.setPush(value);
            case 11: //Gear
                element.setGear(value);
                break;
            case 12: //Green Belt
                element.setGreenBelt(value);
                break;
            case 13: //Blue Belt
                element.setBlueBelt(value);
                break;
        }
    }
    public static boolean hasVariant(int type){
        switch (type){
            case 4:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
                return true;
            default:
                return false;
        }
    }
    public static ArrayList<Boolean> getActiveVariant(BoardBuildElement element, int type){
        ArrayList<Boolean> actives = new ArrayList<>();
        switch (type){
            case 4: //Laser
                actives.add(!element.isLaserRay() && !element.isLaserPointer());
                actives.add(element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 1);
                actives.add(!element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 1);
                actives.add(element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 2);
                actives.add(!element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 2);
                actives.add(element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 3);
                actives.add(!element.isLaserPointer() && element.isLaserRay() && element.getLaserStrength() == 3);
                break;
            case 7: //startField
                actives.add(element.getStartField() == 0);
                actives.add(element.getStartField() == 1);
                actives.add(element.getStartField() == 2);
                actives.add(element.getStartField() == 3);
                actives.add(element.getStartField() == 4);
                actives.add(element.getStartField() == 5);
                actives.add(element.getStartField() == 6);
                break;
            case 8: //checkpoint
                actives.add(element.getCheckpoint() == 0);
                actives.add(element.getCheckpoint() == 1);
                actives.add(element.getCheckpoint() == 2);
                actives.add(element.getCheckpoint() == 3);
                actives.add(element.getCheckpoint() == 4);
                actives.add(element.getCheckpoint() == 5);
                actives.add(element.getCheckpoint() == 6);
                break;
            case 9: //Wall
                actives.add(element.getWall() == 0);
                actives.add(element.getWall() == 1);
                actives.add(element.getWall() == 2);
                actives.add(element.getWall() == 3);
                actives.add(element.getWall() == 4);
                actives.add(element.getWall() == 5);
                break;
            case 10: //Push
                actives.add(element.getPush() == 0);
                actives.add(element.getPush() == 1);
                actives.add(element.getPush() == 2);
                break;
            case 11: //Gear
                actives.add(element.getGear() == 0);
                actives.add(element.getGear() == 1);
                actives.add(element.getGear() == 2);
                break;
            case 12: //Green Belt
                actives.add(element.getGreenBelt() == 0);
                actives.add(element.getGreenBelt() == 1);
                actives.add(element.getGreenBelt() == 2);
                actives.add(element.getGreenBelt() == 3);
                actives.add(element.getGreenBelt() == 4);
                actives.add(element.getGreenBelt() == 5);
                actives.add(element.getGreenBelt() == 6);
                actives.add(element.getGreenBelt() == 7);
                break;
            case 13: //Blue Belt
                actives.add(element.getBlueBelt() == 0);
                actives.add(element.getBlueBelt() == 1);
                actives.add(element.getBlueBelt() == 2);
                actives.add(element.getBlueBelt() == 3);
                actives.add(element.getBlueBelt() == 4);
                actives.add(element.getBlueBelt() == 5);
                actives.add(element.getBlueBelt() == 6);
                actives.add(element.getBlueBelt() == 7);
                break;
        }
        return actives;
    }
    public static boolean addIfNotExistent(int index, BoardBuildElement element){
        element.setNoField(false);
        switch (index){
            case 0: //Empty
                element.setAntenna(false);
                element.setEnergyField(false);
                element.setHole(false);
                element.setLaserRay(false);
                element.setLaserPointer(false);
                element.setRepair(false);
                element.setRespawn(false);
                element.setStartField(0);
                element.setCheckpoint(0);
                element.setWall(0);
                element.setPush(0);
                element.setGear(0);
                element.setGreenBelt(0);
                element.setBlueBelt(0);
                element.setNoField(false);
                return true;
            case 1: //antenna
                if(!element.isAntenna()){
                    element.setAntenna(true);
                    return true;
                } else return false;
            case 2: //energyField
                if(!element.isEnergyField()){
                    element.setEnergyField(true);
                    return true;
                } else return false;
            case 3: //hole
                if(!element.isHole()){
                    element.setHole(true);
                    return true;
                } else return false;
            case 4: //laserStart
                if(!element.isLaserPointer() && !element.isLaserRay()){
                    element.setLaserPointer(true);
                    element.setLaserRay(true);
                    element.setLaserStrength(1);
                    return true;
                } else return false;
            case 5: //repair
                if(!element.isRepair()){
                    element.setRepair(true);
                    return true;
                } else return false;
            case 6: //respawn
                if(!element.isRespawn()){
                    element.setRespawn(true);
                    return true;
                } else return false;
            case 7: //startField
                if(element.getStartField() == 0){
                    element.setStartField(1);
                    return true;
                } else return false;
            case 8: //checkpoint
                if(element.getCheckpoint() == 0){
                    element.setCheckpoint(1);
                    return true;
                } else return false;
            case 9: //wall
                if(element.getWall() == 0){
                    element.setWall(1);
                    return true;
                } else return false;
            case 10: //push
                if(element.getPush() == 0){
                    element.setPush(1);
                    return true;
                } else return false;
            case 11: //gear
                if(element.getGear() == 0){
                    element.setGear(1);
                    return true;
                } else return false;
            case 12: //green belt
                if(element.getGreenBelt() == 0){
                    element.setGreenBelt(1);
                    return true;
                } else return false;
            case 13: //blue belt
                if(element.getBlueBelt() == 0){
                    element.setBlueBelt(1);
                    return true;
                } else return false;
            case 14: //noField
                if(!element.isNoField()){
                    addIfNotExistent(0, element);
                    element.setNoField(true);
                    return true;
                } else return false;
        }
        return true;
    }
    public static BoardBuildElement getElement(String type, int variant){
        switch (type){
            case "antenna":
                BoardBuildElement antenna = new BoardBuildElement();
                antenna.setAntenna(true);
                return antenna;
            case "energyField":
                BoardBuildElement energyField = new BoardBuildElement();
                energyField.setEnergyField(true);
                return energyField;
            case "hole":
                BoardBuildElement hole = new BoardBuildElement();
                hole.setHole(true);
                return hole;
            case "laserStart":
                BoardBuildElement laserStart = new BoardBuildElement();
                laserStart.setLaserPointer(true);
                laserStart.setLaserRay(true);
                laserStart.setLaserStrength(variant);
                return laserStart;
            case "laserBeam":
                BoardBuildElement laserBeam = new BoardBuildElement();
                laserBeam.setLaserRay(true);
                laserBeam.setLaserStrength(variant);
                return laserBeam;
            case "repair":
                BoardBuildElement repair = new BoardBuildElement();
                repair.setRepair(true);
                return repair;
            case "respawn":
                BoardBuildElement respawn = new BoardBuildElement();
                respawn.setRespawn(true);
                return respawn;
            case "startField":
                BoardBuildElement startField = new BoardBuildElement();
                startField.setStartField(variant);
                return startField;
            case "checkpoint":
                BoardBuildElement checkpoint = new BoardBuildElement();
                checkpoint.setCheckpoint(variant);
                return checkpoint;
            case "wall":
                BoardBuildElement wall = new BoardBuildElement();
                wall.setWall(variant);
                return wall;
            case "push":
                BoardBuildElement push = new BoardBuildElement();
                push.setPush(variant);
                return push;
            case "gear":
                BoardBuildElement gear = new BoardBuildElement();
                gear.setGear(variant);
                return gear;
            case "greenBelt":
                BoardBuildElement greenBelt = new BoardBuildElement();
                greenBelt.setGreenBelt(variant);
                return greenBelt;
            case "blueBelt":
                BoardBuildElement blueBelt = new BoardBuildElement();
                blueBelt.setBlueBelt(variant);
                return blueBelt;
            case "noField":
                BoardBuildElement noField = new BoardBuildElement();
                noField.setNoField(true);
                return noField;
            case "empty":
            default:
                BoardBuildElement empty = new BoardBuildElement();
                return empty;
        }
    }
    public static VBox getAtEdge(Node node, int rotation){
        VBox box = new VBox();
        box.setMaxSize(55, 55);
        rotate(node, rotation);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().add(node);
        return box;
    }
    public static void rotate(Node node, int rotation){
        node.setRotate(rotation*90+180);
    }

    public static boolean isIndexTrueForElement(int index, BoardBuildElement element){
        if(element.isAntenna() && index == 1) return true;
        if(element.isEnergyField() && index == 2) return true;
        if(element.isHole() && index == 3) return true;
        if(element.isLaserPointer() && index == 4) return true;
        if(element.isRepair() && index == 5) return true;
        if(element.isRespawn() && index == 6) return true;
        if(element.getStartField() > 0 && index == 7) return true;
        if(element.getCheckpoint() > 0 && index == 8) return true;
        if(element.getWall() > 0 && index == 9) return true;
        if(element.getPush() > 0 && index == 10) return true;
        if(element.getGear() > 0 && index == 11) return true;
        if(element.getGreenBelt() > 0 && index == 12) return true;
        if(element.getBlueBelt() > 0 && index == 13) return true;
        return false;
    }
    public static String checkName(String name){

        return null;
    }
    public static boolean shouldShowTurns(int index){
        if(index == 4) return true; //Laser
        if(index == 9) return true; //Wall
        if(index == 10) return true; //Push
        if(index == 12) return true; //Green Belt
        if(index == 13) return true; //Blue Belt
        return false;
    }
    public static void turn(BoardBuildElement element, int index, boolean direction){
        if(direction) { //Right
            if(index == 4) element.nextRotation("laser"); //Laser
            if(index == 9) element.nextRotation("wall"); //Wall
            if(index == 10) element.nextRotation("push"); //Push
            if(index == 12) element.nextRotation("belt"); //Green Belt
            if(index == 13) element.nextRotation("belt"); //Blue Belt
        }else{ //Left
            if(index == 4) element.prevRotation("laser"); //Laser
            if(index == 9) element.prevRotation("wall"); //Wall
            if(index == 10) element.prevRotation("push"); //Push
            if(index == 12) element.prevRotation("belt"); //Green Belt
            if(index == 13) element.prevRotation("belt"); //Blue Belt
        }
    }

}
