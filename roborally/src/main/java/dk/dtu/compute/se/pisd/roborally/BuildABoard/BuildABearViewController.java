package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import com.beust.ah.A;
import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BuildABearViewController {
    final double SCREEN_WIDTH = 1200;
    final double SCREEN_HEIGHT = 600;
    private final BoardBuildHandler handler;
    private ImageLoader imageLoader;
    private VBox window;
    private Stage stage;
    private TextField boardName;
    private TextField boardWidth;
    private TextField boardHeight;
    private Button saveBoard;
    private VBox boardHolder;
    private HBox bottom;
    private HBox elementChooserWindow;
    private ArrayList<VBox> spots = new ArrayList<>();

    public BuildABearViewController(BoardBuildHandler handler){
        this.handler = handler;
    }
    public void show(){
        if(stage == null || stage.isShowing()) return;
        createScene();
        createWindow();
        stage.showAndWait();
    }
    public void close(){
        stage.close();
    }
    private void createScene(){
        stage = new Stage();
        stage.setScene(new Scene(window, SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
    }
    private void createWindow(){
        window = new VBox();
        HBox top = createTop();
        boardHolder = new VBox();
        bottom = createBottom();
    }

    private HBox createTop(){
        HBox top = new HBox();
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setSpacing(20);
        VBox nameBox = addLabelInBox(boardName, "Board Name");
        VBox widthBox = addLabelInBox(boardWidth, "Board Width");
        VBox heightBox = addLabelInBox(boardWidth, "Board Height");
        HBox sizeBox = new HBox();
        sizeBox.setSpacing(5);
        sizeBox.getChildren().addAll(widthBox, heightBox);
        setStyle(saveBoard);
        saveBoard.setOnAction(e->saveBoardPressed());
        top.getChildren().addAll(nameBox, sizeBox, saveBoard);
        return top;
    }
    private HBox createBottom(){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public void updateGrid(ArrayList<ArrayList<BoardBuildElement>> board){

    }
    public void updateElementGrid(BoardBuildElement element){
        if(elementChooserWindow == null) spots = createGrid();
        ArrayList<Image> images = BoardBuildLogic.getBoardElementImages();
        ArrayList<Boolean> actives = BoardBuildLogic.getActiveElements(element);
        for(int i = 0; i < spots.size(); i++){
            VBox active = new VBox();
            VBox addAble = new VBox();
            ImageView imageView = new ImageView();
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(imageView, addAble, active);
            if(actives.get(i)){
                active.setStyle("-fx-background-color: #606060");
                active.setOpacity(10);
            }
            else stackPane.getChildren().remove(active);
            spots.get(i).getChildren().add(stackPane);
        }
    }

    private ArrayList<VBox> createGrid(){
        elementChooserWindow = new HBox();
        ArrayList<VBox> spots = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            VBox box = new VBox();
            VBox spot1 = new VBox();
            VBox spot2 = new VBox();
            box.getChildren().addAll(spot1, spot2);
            spots.add(spot1);
            spots.add(spot2);
            elementChooserWindow.getChildren().add(box);
        }
        return spots;
    }
    private VBox addLabelInBox(Node node, String labelText){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label label = new Label(labelText);
        setStyle(label);
        vbox.getChildren().add(boardName);
        return vbox;
    }
    private void setStyle(Node node){
        node.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
    }
    private void fieldPressed(int x, int y){

    }
    private void saveBoardPressed(){

    }


}
