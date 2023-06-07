package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
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
    private boolean noField;
    private int startField;
    private int checkpoint;
    private int wall;
    private int push;
    private int gear;
    private int greenBelt;
    private int blueBelt;
    private int laserStrength;
    private int wallRotation = 0;
    private int beltRotation = 0;
    private int pushRotation = 0;
    private int laserRotation = 0;

    /**
     * @Author Tobias Gørlyk s224271
     * Create a StackPane containing layers of ImageViews of the different elements lasers going over belts as an example. It also rotates the images by using the variables in this class.
     * @return StackPane of ImageViews of each element.
     */

    public StackPane getView(){
        StackPane holder = new StackPane();
        holder.setAlignment(Pos.CENTER);
        holder.getChildren().add(getImage(ImageLoader.get().empty));
        if(antenna) holder.getChildren().add(getImage(ImageLoader.get().antenna));
        if(energyField) holder.getChildren().add(getImage(ImageLoader.get().energyField));
        if(hole) holder.getChildren().add(getImage(ImageLoader.get().hole));
        if(repair) holder.getChildren().add(getImage(ImageLoader.get().repair));
        if(respawn) holder.getChildren().add(getImage(ImageLoader.get().respawn));
        if(startField > 0){
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(getImage(ImageLoader.get().startField));
            Label label = new Label("" + startField);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
            stackPane.getChildren().add(label);
            holder.getChildren().add(stackPane);
        }
        if(checkpoint > 0) holder.getChildren().add(getImage(ImageLoader.get().checkpoints[checkpoint-1]));
        if(gear > 0) holder.getChildren().add(getImage(ImageLoader.get().gear[gear-1]));
        if(greenBelt > 0){
            ImageView belt = getImage(ImageLoader.get().greenBelts[greenBelt-1]);
            holder.getChildren().add(belt);
            rotate(belt, beltRotation);
        }
        if(blueBelt > 0){
            ImageView belt = getImage(ImageLoader.get().blueBelts[blueBelt-1]);
            holder.getChildren().add(belt);
            rotate(belt, beltRotation);
        }
        if(push > 0) {
            ImageView pusher = new ImageView(ImageLoader.get().push[push-1]);
            pusher.setFitWidth(20);
            pusher.setFitHeight(HEIGHT);
            VBox pushHolder = new VBox();
            pushHolder.getChildren().add(getAtEdge(pusher, 0));
            rotate(pushHolder, pushRotation);
            holder.getChildren().add(pushHolder);
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
                case 5: //End
                    wallHolder.getChildren().add(formatWall(0));
                    wallHolder.getChildren().add(formatWall(1));
                    wallHolder.getChildren().add(formatWall(2));
                    wallHolder.getChildren().add(formatWall(3));
                    break;
            }
            rotate(wallHolder, wallRotation);
            holder.getChildren().add(wallHolder);
        }
        if(laserRay){
            holder.getChildren().add(spaceOutLasers(laserStrength,false));
        }
        if(laserPointer){
            holder.getChildren().add(spaceOutLasers(laserStrength,true));
        }
        if(noField){
            VBox cover = new VBox();
            cover.setStyle("-fx-background-color: #909090");
            cover.setPrefSize(WIDTH, HEIGHT);
            holder.getChildren().add(cover);
        }
        if(showing) {
            VBox showBorder = new VBox();
            showBorder.setStyle("-fx-border-color: #ffb900; -fx-border-width: 2");
            showBorder.setPrefSize(WIDTH, HEIGHT);
            holder.getChildren().add(showBorder);
        }
        return holder;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Get a Wall at the edge in a specific direction
     * @param rotation the rotation of the wall from 0 to 3 [0=0, 1=90, 2=180, 3=270]
     * @return The VBox containing the wall rotated.
     */
    private VBox formatWall(int rotation){
        ImageView wall = new ImageView(ImageLoader.get().wall);
        wall.setFitWidth(10);
        wall.setFitHeight(HEIGHT);
        VBox wallBox = getAtEdge(wall, 0);
        rotate(wallBox, rotation);
        return wallBox;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Get the ImageView containing an Image that is set to the right size fitting the element.
     * This is used to get the elements that has the size of the whole field, so not walls and pushers, but instead things like the antenna or the reboot field.
     * @param image the image that goes into the ImageView
     * @return The ImageView Containing the image sized to the width and height of the element.
     */
    private ImageView getImage(Image image){
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        return imageView;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates and spaces out the lasers if there are multiple.
     * @param amount the amount of lasers
     * @param isLaserPointer if it is a laser ray or a pointer
     * @return A VBox containgen all lasers facing/shooting EAST.
     */
    private VBox spaceOutLasers(int amount, boolean isLaserPointer){
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        for(int i = 0; i < amount; i++){
            VBox part = new VBox();
            part.setPrefSize(WIDTH, 20);
            part.setAlignment(Pos.CENTER);
            if(isLaserPointer){
                ImageView laser = new ImageView(ImageLoader.get().laserStart);
                laser.setFitHeight(20);
                laser.setFitWidth(14);
                VBox laserHolder = getAtEdge(laser, 0);
                part.getChildren().add(laserHolder);
            }else{
                ImageView laser = new ImageView(ImageLoader.get().laserBeam);
                laser.setFitHeight(5);
                laser.setFitWidth(WIDTH);
                part.getChildren().add(laser);
            }
            box.getChildren().add(part);
        }
        if(amount == 2){
            box.getChildren().get(0).setLayoutY(10);
            box.getChildren().get(1).setLayoutY(-10);
        }
        else if(amount == 3){
            box.getChildren().get(0).setLayoutY(20);
            box.getChildren().get(2).setLayoutY(-20);
        }

        rotate(box, laserRotation);
        return box;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Moves a node to the edge of the element's box, which is used for walls, laser-pointers and push elements.
     * @param rotation an offSet rotation of that element
     * @param node the node that needs to be moved to the edge
     * @return the VBox that now holds the node at the EAST edge.
     */
    private VBox getAtEdge(Node node, int rotation){
        return BoardBuildLogic.getAtEdge(node, rotation);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * rotates a Node 90 degrees an amount of times
     * @param node the node that needs rotating
     * @param rotation the amount that it needs to be rotated 90 degrees starting with 0 facing EAST
     */
    private void rotate(Node node, int rotation){
        BoardBuildLogic.rotate(node, rotation);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * make the rotation variable for the different type of Element one more, but if going over 3 it sohuld be 0 instead.
     * @param type types of elements with rotation = ["wall", "belt", "push", "laser"]
     */
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
    /**
     * @Author Tobias Gørlyk s224271
     * make the rotation variable for the different type of Element one less, but if going under 0 it sohuld be 3 instead.
     * @param type type of element = ["wall", "belt", "push", "laser"]
     */
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

    /**
     * @Author Tobias Gørlyk s224271
     * Below is a lot of getters and setters for private variables in the class.
     */
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

    public int getWallRotation() {
        return wallRotation;
    }

    public void setWallRotation(int wallRotation) {
        this.wallRotation = wallRotation;
    }

    public int getBeltRotation() {
        return beltRotation;
    }

    public void setBeltRotation(int beltRotation) {
        this.beltRotation = beltRotation;
    }

    public int getPushRotation() {
        return pushRotation;
    }

    public void setPushRotation(int pushRotation) {
        this.pushRotation = pushRotation;
    }

    public int getLaserRotation() {
        return laserRotation;
    }

    public void setLaserRotation(int laserRotation) {
        this.laserRotation = laserRotation;
    }

    public int getLaserStrength() {
        return laserStrength;
    }

    public void setLaserStrength(int laserStrength) {
        this.laserStrength = laserStrength;
    }

    public boolean isNoField() {
        return noField;
    }

    public void setNoField(boolean noField) {
        this.noField = noField;
    }
}
