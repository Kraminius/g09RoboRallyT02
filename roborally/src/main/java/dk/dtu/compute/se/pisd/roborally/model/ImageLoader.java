package dk.dtu.compute.se.pisd.roborally.model;

import javafx.scene.image.Image;

import java.io.FileInputStream;

public class ImageLoader {




    public Image empty;
    public Image antenna;
    public Image energyField;
    public Image hole;
    public Image laserBeam;
    public Image laserStart;
    public Image repair;
    public Image respawn;
    public Image startField;
    public Image wall;

    public Image[] checkpoints;
    public Image[] greenBelts;
    public Image[] blueBelts;
    public Image[] gear;
    public Image[] push;

    private static ImageLoader loader;

    public static ImageLoader get(){
        if(loader == null) loader = new ImageLoader();
        return loader;
    }
    public ImageLoader(){
        empty = loadImage("empty");
        antenna = loadImage("antenna");
        energyField = loadImage("energyField");
        hole = loadImage("hole");
        laserBeam = loadImage("laser");
        laserStart = loadImage("laserStart");
        repair = loadImage("repair");
        respawn = loadImage("respawn");
        startField = loadImage("startField");
        wall = loadImage("wall");
        checkpoints = new Image[8];
        checkpoints[0] = loadImage("1");
        checkpoints[1] = loadImage("2");
        checkpoints[2] = loadImage("3");
        checkpoints[3] = loadImage("4");
        checkpoints[4] = loadImage("5");
        checkpoints[5] = loadImage("6");
        checkpoints[6] = loadImage("7");
        checkpoints[7] = loadImage("8");
        greenBelts = new Image[7];
        greenBelts[0] = loadImage("green");
        greenBelts[1] = loadImage("greenTurnLeft");
        greenBelts[2] = loadImage("greenTurnRight");
        greenBelts[3] = loadImage("tGreen1");
        greenBelts[4] = loadImage("tGreen2");
        greenBelts[5] = loadImage("tGreen3");
        greenBelts[6] = loadImage("tGreen4");
        blueBelts = new Image[7];
        blueBelts[0] = loadImage("blue");
        blueBelts[1] = loadImage("blueTurnLeft");
        blueBelts[2] = loadImage("blueTurnRight");
        blueBelts[3] = loadImage("tBlue1");
        blueBelts[4] = loadImage("tBlue2");
        blueBelts[5] = loadImage("tBlue3");
        blueBelts[6] = loadImage("tBlue4");
        gear = new Image[2];
        gear[0] = loadImage("gearLeft");
        gear[1] = loadImage("gearRight");
        push = new Image[2];
        push[0] = loadImage("push24");
        push[1] = loadImage("push135");
    }



    private Image loadImage(String name){
        try{
            FileInputStream stream = new FileInputStream("roborally/src/main/resources/images/" + name + ".png");
            Image image = new Image(stream);
            return image;
        }catch (Exception e){
            System.out.println("Cannot load image [" + name + "]");
            return null;
        }
    }






}
