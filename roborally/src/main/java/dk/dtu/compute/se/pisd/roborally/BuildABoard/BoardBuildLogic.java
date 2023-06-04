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
        if(element.isAntenna()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isEnergyField()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isHole()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isLaserPointer()) arrayList.add(true);
        else arrayList.add(false);
        if(element.isLaserRay()) arrayList.add(true);
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
        images.add(getElement("laserStart", 0).getView());
        images.add(getElement("repair", 0).getView());
        images.add(getElement("respawn", 0).getView());
        images.add(getElement("startField", 1).getView());
        images.add(getElement("checkpoint", 1).getView());
        images.add(getElement("wall", 1).getView());
        images.add(getElement("push", 1).getView());
        images.add(getElement("gear", 1).getView());
        images.add(getElement("greenBelt", 1).getView());
        images.add(getElement("blueBelt", 1).getView());
        return images;
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
                return laserStart;
            case "laserBeam":
                BoardBuildElement laserBeam = new BoardBuildElement();
                laserBeam.setLaserRay(true);
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

}
