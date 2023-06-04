package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BoardBuildElement {
    public final double WIDTH = 60;
    public final double HEIGHT = 60;
    private int x;
    private int y;
    private boolean showing;
    private boolean antenna;
    private boolean energyField;
    private boolean hole;
    private boolean laserPointer;
    private boolean laserRay;
    private boolean repair;
    private boolean respawn;
    private int startField;
    private int checkpoint;
    private int wall;
    private int push;
    private int gear;
    private int greenBelt;
    private int blueBelt;
    private int wallRotation = 0;
    private int beltRotation = 0;
    private int pushRotation = 0;
    private int laserRotation = 0;

    public StackPane getView(){
        StackPane holder = new StackPane();
        holder.setAlignment(Pos.CENTER);
        holder.getChildren().add(getImage(ImageLoader.get().empty, true, true));
        if(antenna) holder.getChildren().add(getImage(ImageLoader.get().antenna, true, true));
        if(energyField) holder.getChildren().add(getImage(ImageLoader.get().energyField, true, true));
        if(hole) holder.getChildren().add(getImage(ImageLoader.get().hole, true, true));
        if(repair) holder.getChildren().add(getImage(ImageLoader.get().repair, true, true));
        if(respawn) holder.getChildren().add(getImage(ImageLoader.get().respawn, true, true));
        if(startField > 0){
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(getImage(ImageLoader.get().startField, true, true));
            Label label = new Label("" + startField);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
            stackPane.getChildren().add(label);
            holder.getChildren().add(stackPane);
        }
        if(checkpoint > 0) holder.getChildren().add(getImage(ImageLoader.get().checkpoints[checkpoint-1], true, true));
        if(gear > 0) holder.getChildren().add(getImage(ImageLoader.get().gear[gear-1], true, true));
        if(greenBelt > 0){
            ImageView belt = getImage(ImageLoader.get().greenBelts[greenBelt-1], true, true);
            holder.getChildren().add(belt);
            rotate(belt, beltRotation);
        }
        if(blueBelt > 0){
            ImageView belt = getImage(ImageLoader.get().blueBelts[blueBelt-1], true, true);
            holder.getChildren().add(belt);
            rotate(belt, beltRotation);
        }
        if(push > 0) {
            ImageView pusher = new ImageView(ImageLoader.get().push[push-1]);
            pusher.setFitWidth(20);
            pusher.setFitHeight(HEIGHT);
            holder.getChildren().add(getAtEdge(pusher, 0));
            rotate(pusher, pushRotation);
        }
        if(wall > 0){
            StackPane wallHolder = new StackPane();
            switch (wall){
                case 1: //One Wall
                    wallHolder.getChildren().add(formatWall(0));
                    break;
                case 2: //Corner
                    wallHolder.getChildren().add(formatWall(0));
                    wallHolder.getChildren().add(formatWall(1));
                    break;
                case 3: //Opposite
                    wallHolder.getChildren().add(formatWall(0));
                    wallHolder.getChildren().add(formatWall(2));
                    break;
                case 4: //End
                    wallHolder.getChildren().add(formatWall(0));
                    wallHolder.getChildren().add(formatWall(1));
                    wallHolder.getChildren().add(formatWall(3));
                    break;
            }
            rotate(wallHolder, wallRotation);
            holder.getChildren().add(wallHolder);
        }
        if(laserRay){
            ImageView laser = new ImageView(ImageLoader.get().laserBeam);
            laser.setFitHeight(5);
            laser.setFitWidth(WIDTH);
            holder.getChildren().add(getAtEdge(laser, 0));
            rotate(laser, laserRotation);
        }
        if(laserPointer){
            ImageView laser = new ImageView(ImageLoader.get().laserStart);
            laser.setFitHeight(30);
            laser.setFitWidth(20);
            holder.getChildren().add(getAtEdge(laser, 0));
            rotate(laser, laserRotation);
        }
        if(showing) {
            VBox showBorder = new VBox();
            showBorder.setStyle("-fx-border-color: #ffb900; -fx-border-width: 2");
            showBorder.setPrefSize(WIDTH, HEIGHT);
            holder.getChildren().add(showBorder);
        }
        return holder;
    }
    private Node formatWall(int rotation){
        ImageView wall = new ImageView(ImageLoader.get().wall);
        wall.setFitWidth(20);
        wall.setFitHeight(HEIGHT);
        VBox wallBox = getAtEdge(wall, rotation);
        rotate(wallBox, wallRotation);
        return wallBox;
    }
    private ImageView getImage(Image image, boolean fitWidth, boolean fitHeight){
        ImageView imageView = new ImageView(image);
        if(fitWidth)imageView.setFitWidth(WIDTH);
        if(fitHeight)imageView.setFitHeight(HEIGHT);
        return imageView;
    }
    private VBox getAtEdge(Node node, int rotation){
        return BoardBuildLogic.getAtEdge(node, rotation);
    }
    private void rotate(Node node, int rotation){
        BoardBuildLogic.rotate(node, rotation);
    }
    public void nextRotation(String type){
        switch (type){
            case "wall":
                if(wallRotation < 3) wallRotation++;
                else wallRotation = 0;
                break;
            case "belt":
                if(beltRotation < 3) beltRotation++;
                else beltRotation = 0;
                break;
            case "push":
                if(pushRotation < 3) pushRotation++;
                else pushRotation = 0;
                break;
            case "laser":
                if(laserRotation < 3) laserRotation++;
                else laserRotation = 0;
                break;
        }
    }
    public void prevRotation(String type){
        switch (type){
            case "wall":
                if(wallRotation > 0) wallRotation--;
                else wallRotation = 3;
                break;
            case "belt":
                if(beltRotation > 0) beltRotation--;
                else beltRotation = 3;
                break;
            case "push":
                if(pushRotation > 0) pushRotation--;
                else pushRotation = 3;
                break;
            case "laser":
                if(laserRotation > 0) laserRotation--;
                else laserRotation = 3;
                break;
        }
    }
    public int getWallRotation(){
        return wallRotation;
    }
    public int getBeltRotation(){
        return beltRotation;
    }

    public boolean isAntenna() {
        return antenna;
    }

    public void setAntenna(boolean antenna) {
        this.antenna = antenna;
    }

    public boolean isEnergyField() {
        return energyField;
    }

    public void setEnergyField(boolean energyField) {
        this.energyField = energyField;
    }

    public boolean isHole() {
        return hole;
    }

    public void setHole(boolean hole) {
        this.hole = hole;
    }

    public boolean isLaserPointer() {
        return laserPointer;
    }

    public void setLaserPointer(boolean laserPointer) {
        this.laserPointer = laserPointer;
    }

    public boolean isLaserRay() {
        return laserRay;
    }

    public void setLaserRay(boolean laserRay) {
        this.laserRay = laserRay;
    }

    public boolean isRepair() {
        return repair;
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public int getStartField() {
        return startField;
    }

    public void setStartField(int startField) {
        this.startField = startField;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }

    public int getGreenBelt() {
        return greenBelt;
    }

    public void setGreenBelt(int greenBelt) {
        this.greenBelt = greenBelt;
    }

    public int getBlueBelt() {
        return blueBelt;
    }

    public void setBlueBelt(int blueBelt) {
        this.blueBelt = blueBelt;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
