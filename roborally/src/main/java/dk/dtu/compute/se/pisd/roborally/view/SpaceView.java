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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Belt;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;

    VBox playerLayer = new VBox();
    ImageView backgroundLayer = new ImageView();
    ImageView beltLayer = new ImageView();







    Image background;
    Image wall;
    Image laser;
    Image laserStart;
    Image laserEnd;
    Image greenBelt;
    Image greenBeltRight;
    Image greenBeltLeft;
    Image blueBelt;
    Image blueBeltRight;
    Image blueBeltLeft;





    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }
        if(initImages()) initLayers();
        else{
            playerLayer.setAlignment(Pos.CENTER);
            this.getChildren().add(playerLayer);
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }
    private void setSizeAndAdd(ImageView iw, Image image){
        iw = new ImageView(image);
        resizeImageView(iw);
        this.getChildren().add(iw);
    }

    private void initLayers(){

        setSizeAndAdd(backgroundLayer, background);
        if(space.belt != null) getBelt();
        if(space.getWallHeading() != null) insertWalls();
        playerLayer.setAlignment(Pos.CENTER);
        this.getChildren().add(playerLayer);
    }
    private void insertWalls(){
        StackPane walls = new StackPane();
        walls.setPrefSize(SPACE_WIDTH, SPACE_HEIGHT);
        Heading[] headings = space.getWallHeading();
        for(int i = 0; i < space.getWallHeading().length; i++){
            ImageView newWall = new ImageView(wall);
            resizeImageView(newWall);
            rotateImageView(Heading.SOUTH, headings[i], newWall);
            walls.getChildren().add(newWall);
        }
        this.getChildren().add(walls);
    }
    private void getBelt(){
        Belt belt = space.belt;
        Image beltType = null;
        Heading heading = null;
        if(belt.speed == 1){
            if(belt.turn.equals("")){
                beltType = greenBelt;
                heading = Heading.WEST; //Pictures direction (Direction of the arrow)
            }
            else if(belt.turn.equals("Right")){
                beltType = greenBeltRight;
                heading = Heading.SOUTH; //Pictures direction (Direction of the arrow)
            }
            else if(belt.turn.equals("Left")){
                beltType = greenBeltLeft;
                heading = Heading.SOUTH; //Pictures direction (Direction of the arrow)
            }
        }
        else if(belt.speed == 2){
            if(belt.turn.equals("")){
                beltType = blueBelt;
                heading = Heading.EAST; //Pictures direction (Direction of the arrow)
            }
            else if(belt.turn.equals("Right")){
                beltType = blueBeltRight;
                heading = Heading.EAST; //Pictures direction (Direction of the arrow)
            }
            else if(belt.turn.equals("Left")){
                beltType = blueBeltLeft;
                heading = Heading.SOUTH; //Pictures direction (Direction of the arrow)
            }
        }
        if(heading == null || beltType == null){
            System.out.println("Failure in initializing belt");
        }
        System.out.println("Rotation Before: " + beltLayer.getRotate());
        System.out.println("Current: " + heading + "\nTo: " + belt.heading);
        setSizeAndAdd(beltLayer, beltType);
        rotateImageView(heading, belt.heading, beltLayer);
        System.out.println("Rotation after: " + beltLayer.getRotate());


    }
    private void resizeImageView(ImageView iw){
        iw.setFitWidth(SPACE_WIDTH);
        iw.setFitHeight(SPACE_HEIGHT);
    }
    private void rotateImageView(Heading from, Heading to, ImageView iw){
        while(from != to){
            iw.setRotate(iw.getRotate() + 90);
            from = from.next();
        }
    }
    private boolean initImages(){
        try{
            background = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/emptySpace.png"));
            wall = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/wall.png"));
            laser =  new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/laser.png"));
            laserStart = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/laserStart.png"));
            laserEnd =  new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/laserEnd.png"));
            greenBelt = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/greenBelt.png"));
            greenBeltRight = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/greenBeltTurnRight.png"));
            greenBeltLeft = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/greenBeltTurnLeft.png"));
            blueBelt = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/blueBelt.png"));
            blueBeltRight = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/blueBeltRight.png"));
            blueBeltLeft = new Image(new FileInputStream("roborally/src/main/java/dk/dtu/compute/se/pisd/roborally/view/images/blueBeltLeft.png"));
            return true;
        }catch(Exception e){
            System.out.println("Could not load images");
            System.out.println(e.getMessage());
            return false;
        }
    }
    private void updatePlayer() {
        playerLayer.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0 );
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90*player.getHeading().ordinal())%360);
            playerLayer.getChildren().add(arrow);
        }
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
        }
    }

}
