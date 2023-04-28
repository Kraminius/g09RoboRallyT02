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
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

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

    StackPane layers;
    VBox playerLayer = new VBox();
    ImageView backgroundLayer = new ImageView();
    StackPane elementLayer = new StackPane();
    ImageLoader imageLoader = ImageLoader.get();


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        this.setStyle("-fx-border-color: #eeeeee; -fx-background-color: #eeeeee");
        addPictures();

        // updatePlayer();


        //Layers
        layers = new StackPane();
        this.getChildren().add(layers);
        layers.getChildren().add(backgroundLayer);
        backgroundLayer.setFitHeight(SPACE_HEIGHT);
        backgroundLayer.setFitWidth(SPACE_WIDTH);
        layers.getChildren().add(elementLayer);
        layers.getChildren().add(playerLayer);
        playerLayer.setAlignment(Pos.CENTER);

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }
    private void colorSpace(){
        String style = "";
        if(!this.space.isSpace) style = "-fx-background-color: #eeeeee; -fx-border-color: #eeeeee";
        if(this.space.isHole) style = "-fx-background-color: #101010; -fx-border-color: #fffb3d";
        if(this.space.belt != null){
            VBox box = new VBox();
            this.getChildren().add(box);
            box.setAlignment(Pos.CENTER_RIGHT);
            box.setMaxWidth(SPACE_WIDTH);
            box.setMaxHeight(40);
            box.setPadding(new Insets(10, 10, 10, 10));
            VBox pointer = new VBox();
            pointer.setMaxWidth(10);
            pointer.setMaxHeight(30);
            pointer.setMinWidth(10);
            pointer.setMinHeight(30);
            pointer.setStyle("-fx-background-color: #ffffff");

            box.getChildren().add(pointer);
            setRotation(box,this.space.belt.heading, 0);

            if(this.space.belt.speed == 1){
                box.setStyle("-fx-background-color: #8dfa71; -fx-border-color: #bbbbbb");
            }else if(this.space.belt.speed == 2){
                box.setStyle("-fx-background-color: #4ba3e0; -fx-border-color: #bbbbbb");
            }
        }
        if(this.space.wall != null){
            StackPane walls = new StackPane();
            this.getChildren().add(walls);
            for(int i = 0; i < this.space.wall.wallHeadings.size(); i++){
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER_RIGHT);
                VBox wall = new VBox();
                wall.setMaxWidth(10);
                wall.setMaxHeight(SPACE_HEIGHT);
                wall.setMinWidth(10);
                wall.setMinHeight(SPACE_HEIGHT);
                wall.setStyle("-fx-background-color: #fffb3d; -fx-border-color: #404040");
                box.getChildren().add(wall);
                setRotation(box, this.space.wall.wallHeadings.get(i), 0);
                walls.getChildren().add(box);
            }
        }
        if(this.space.isRespawn){
            VBox box = new VBox();
            this.getChildren().add(box);
            box.setAlignment(Pos.CENTER);
            VBox respawn = new VBox();
            respawn.setMinWidth(50);
            respawn.setMinHeight(50);
            respawn.setMaxWidth(50);
            respawn.setMaxHeight(50);
            respawn.setStyle("-fx-background-color: #73e5d5; -fx-border-color: #404040; -fx-border-radius: 100; -fx-background-radius: 105; -fx-border-width: 5");
            box.getChildren().add(respawn);
        }
        this.setStyle(this.getStyle() + ";" + style);
    }
    private void addPictures(){
        if(this.space.isSpace) backgroundLayer.setImage(imageLoader.empty);
        if(this.space.isHole) backgroundLayer.setImage(imageLoader.hole);
        if(this.space.isRespawn) backgroundLayer.setImage(imageLoader.respawn);
        if(this.space.isAntenna) backgroundLayer.setImage(imageLoader.antenna);
        if(this.space.isRepair) backgroundLayer.setImage(imageLoader.repair);
        if(this.space.energyField != null) backgroundLayer.setImage(imageLoader.energyField);
        if(this.space.startField != null){
            int id = this.space.startField.id;
            backgroundLayer.setImage(imageLoader.startField);
            Text number = new Text("" + id);
            number.setStyle("-fx-font-weight: bold; -fx-font-size: 18");
            elementLayer.getChildren().add(number);
        }
        if(this.space.checkpoint != null){
            int number = this.space.checkpoint.number;
            backgroundLayer.setImage(imageLoader.checkpoints[number-1]);
        }
        if(this.space.gear != null){
            if(this.space.gear.rotation.equals("LEFT")){
                backgroundLayer.setImage(imageLoader.gear[0]);
            }
            else if(this.space.gear.rotation.equals("RIGHT")){
                backgroundLayer.setImage(imageLoader.gear[1]);
            }
            else{
                System.out.println("gear rotation is wrong, should be either LEFT or RIGHT, it is currently: " + this.space.gear.rotation);
            }
        }
        if(this.space.push != null){
            Heading heading = this.space.push.heading;
            ArrayList<Integer> rounds = this.space.push.activateRounds;
            ImageView pusher = new ImageView();
            if(rounds.contains(1) && rounds.contains(3) && rounds.contains(5)){
                pusher.setImage(imageLoader.push[1]);
            }
            else if(rounds.contains(2) && rounds.contains(4)){
                pusher.setImage(imageLoader.push[0]);
            }
            else{
                System.out.println("Image of activation rounds of the pusher doesnt exist, putting 2 and 4 image instead.");
                pusher.setImage(imageLoader.push[0]);
            }
            VBox box = new VBox();
            box.setAlignment(Pos.CENTER_RIGHT);
            pusher.setFitWidth(30);
            pusher.setFitHeight(SPACE_HEIGHT);
            box.getChildren().add(pusher);
            setRotation(box, heading, 180);
            elementLayer.getChildren().add(box);
        }
        if(this.space.belt != null){
            Heading heading = this.space.belt.heading;
            int speed = this.space.belt.speed;
            String turn = this.space.belt.turn;
            int rotationOffset;
            switch (turn){
                case "LEFT":
                    if(speed == 1) backgroundLayer.setImage(imageLoader.greenBelts[1]);
                    if(speed == 2) backgroundLayer.setImage(imageLoader.blueBelts[1]);
                    rotationOffset = 180;
                    break;
                case "RIGHT":
                    if(speed == 1) backgroundLayer.setImage(imageLoader.greenBelts[2]);
                    if(speed == 2) backgroundLayer.setImage(imageLoader.blueBelts[2]);
                    rotationOffset = 0;
                    break;
                case "LEFT_T":
                    if(speed == 1) backgroundLayer.setImage(imageLoader.greenBelts[3]);
                    if(speed == 2) backgroundLayer.setImage(imageLoader.blueBelts[3]);
                    rotationOffset = 270;
                    break;
                case "RIGHT_T":
                    if(speed == 1) backgroundLayer.setImage(imageLoader.greenBelts[4]);
                    if(speed == 2) backgroundLayer.setImage(imageLoader.blueBelts[4]);
                    rotationOffset = 270;
                    break;
                default:
                    if(speed == 1) backgroundLayer.setImage(imageLoader.greenBelts[0]);
                    if(speed == 2) backgroundLayer.setImage(imageLoader.blueBelts[0]);
                    rotationOffset = 90;
                    break;
            }
            setRotation(backgroundLayer, heading, rotationOffset);
        }
        if(this.space.laser != null){
            Heading heading = this.space.laser.heading;
            int damage = this.space.laser.damage;

            VBox box = new VBox();
            box.setMaxSize(SPACE_WIDTH, SPACE_HEIGHT);
            box.setMinSize(SPACE_WIDTH, SPACE_HEIGHT);
            box.setAlignment(Pos.CENTER);
            box.setSpacing(15);
            for(int i = 0; i < damage; i++) {
                ImageView laser = new ImageView();
                laser.setFitHeight(5);
                laser.setFitWidth(SPACE_WIDTH);
                laser.setImage(imageLoader.laserBeam);
                box.getChildren().add(laser);
            }
            setRotation(box, heading, 0);
            elementLayer.getChildren().add(box);


            if(this.space.laser.isStart){
                VBox start = new VBox();
                start.setMaxSize(SPACE_WIDTH, SPACE_HEIGHT);
                start.setMinSize(SPACE_WIDTH, SPACE_HEIGHT);
                start.setPadding(new Insets(10, 10, 10, 10));
                start.setAlignment(Pos.CENTER_LEFT);
                for(int i = 0; i < damage; i++){
                    ImageView laser = new ImageView();
                    laser.setFitHeight(20);
                    laser.setFitWidth(20);
                    laser.setImage(imageLoader.laserStart);
                    start.getChildren().add(laser);
                }
                setRotation(start, heading, 0);
                elementLayer.getChildren().add(start);
            }


        }
        if(this.space.wall != null){
            for(int i = 0; i < this.space.wall.wallHeadings.size(); i++){
                VBox box = new VBox();
                box.setAlignment(Pos.CENTER_RIGHT);
                ImageView wall = new ImageView();
                wall.setImage(imageLoader.wall);
                wall.setFitWidth(15);
                wall.setFitHeight(SPACE_HEIGHT);
                box.getChildren().add(wall);
                setRotation(box, this.space.wall.wallHeadings.get(i), 0);
                elementLayer.getChildren().add(box);
            }
        }
    }

    private void setRotation(Node node, Heading heading, int addedRotation){ //This method believes 0 is facing east.
        switch (heading){
            case NORTH:
                node.setRotate(270 + addedRotation);
                break;
            case EAST:
                node.setRotate(addedRotation);
                break;
            case WEST:
                node.setRotate(180 + addedRotation);
                break;
            case SOUTH:
                node.setRotate(90 + addedRotation);
                break;
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
