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
    public Image energyCube;

    public Image[] checkpoints;
    public Image[] greenBelts;
    public Image[] blueBelts;
    public Image[] gear;
    public Image[] push;
    public Image winner;

    private static ImageLoader loader;

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Singleton gets instance of the loader so we only have to load from files once.
     */
    public static ImageLoader get(){
        if(loader == null) loader = new ImageLoader();
        return loader;
    }

    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Loads all the images when the class is instantiated.
     */
    public ImageLoader(){
        empty = loadImage("empty.png");
        antenna = loadImage("antenna.png");
        energyField = loadImage("energyField.png");
        hole = loadImage("hole.png");
        laserBeam = loadImage("laser.png");
        laserStart = loadImage("laserStart.png");
        repair = loadImage("repair.png");
        respawn = loadImage("respawn.png");
        startField = loadImage("startField.png");
        wall = loadImage("wall.png");
        energyCube = loadImage("energyCube.png");
        checkpoints = new Image[8];
        checkpoints[0] = loadImage("1.png");
        checkpoints[1] = loadImage("2.png");
        checkpoints[2] = loadImage("3.png");
        checkpoints[3] = loadImage("4.png");
        checkpoints[4] = loadImage("5.png");
        checkpoints[5] = loadImage("6.png");
        checkpoints[6] = loadImage("7.png");
        checkpoints[7] = loadImage("8.png");
        greenBelts = new Image[7];
        greenBelts[0] = loadImage("green.png");
        greenBelts[1] = loadImage("greenTurnLeft.png");
        greenBelts[2] = loadImage("greenTurnRight.png");
        greenBelts[3] = loadImage("tGreen1.png");
        greenBelts[4] = loadImage("tGreen2.png");
        greenBelts[5] = loadImage("tGreen3.png");
        greenBelts[6] = loadImage("tGreen4.png");
        blueBelts = new Image[7];
        blueBelts[0] = loadImage("blue.png");
        blueBelts[1] = loadImage("blueTurnLeft.png");
        blueBelts[2] = loadImage("blueTurnRight.png");
        blueBelts[3] = loadImage("tBlue1.png");
        blueBelts[4] = loadImage("tBlue2.png");
        blueBelts[5] = loadImage("tBlue3.png");
        blueBelts[6] = loadImage("tBlue4.png");
        gear = new Image[2];
        gear[0] = loadImage("gearLeft.png");
        gear[1] = loadImage("gearRight.png");
        push = new Image[2];
        push[0] = loadImage("push24.png");
        push[1] = loadImage("push135.png");
        winner = loadImage("robot-dancing.gif");
    }



    private Image loadImage(String name){
        try{
            FileInputStream stream = new FileInputStream("roborally/src/main/resources/images/" + name );
            Image image = new Image(stream);
            return image;
        }catch (Exception e){
            System.out.println("Cannot load image [" + name + "]");
            return null;
        }
    }






}
