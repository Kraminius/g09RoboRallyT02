package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import javafx.scene.image.Image;

import java.util.ArrayList;

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
    public static ArrayList<Image> getBoardElementImages(){
        ArrayList<Image> images = new ArrayList<>();
        images.add(imageLoader.antenna);
        images.add(imageLoader.energyField);
        images.add(imageLoader.hole);
        images.add(imageLoader.laserStart);
        images.add(imageLoader.laserBeam);
        images.add(imageLoader.repair);
        images.add(imageLoader.respawn);
        images.add(imageLoader.startField);
        images.add(imageLoader.checkpoints[0]);
        images.add(imageLoader.wall);
        images.add(imageLoader.push[0]);
        images.add(imageLoader.gear[0]);
        images.add(imageLoader.greenBelts[0]);
        images.add(imageLoader.blueBelts[0]);
        return images;
    }
}
