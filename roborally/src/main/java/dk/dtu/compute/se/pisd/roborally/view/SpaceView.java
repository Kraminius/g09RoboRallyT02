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
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
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
    VBox playerLayer;
    StackPane elementLayer;

    public SpaceView(@NotNull Space space) {

        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        this.setStyle("-fx-border-color: #404040; -fx-background-color: #909090");
        colorSpace();

        // updatePlayer();


        //Layers
        layers = new StackPane();
        this.getChildren().add(layers);
        layers.getChildren().add(playerLayer = new VBox());
        playerLayer.setAlignment(Pos.CENTER);

        layers.getChildren().add(elementLayer = new StackPane());


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
