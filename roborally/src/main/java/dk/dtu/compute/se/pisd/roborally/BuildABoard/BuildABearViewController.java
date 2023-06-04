package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import dk.dtu.compute.se.pisd.roborally.view.Option;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class BuildABearViewController {
    final double SCREEN_WIDTH = 1600;
    final double SCREEN_HEIGHT = 900;
    private final BoardBuildHandler handler;
    private ImageLoader imageLoader;
    private HBox window;
    private Stage stage;
    private TextField boardName = new TextField();
    private TextField boardWidth = new TextField();
    private TextField boardHeight = new TextField();
    private Button saveBoard = new Button();
    private Button setSize = new Button();
    private VBox boardHolder = new VBox();;
    private HBox bottom;
    private HBox elementChooserWindow = new HBox();
    private ArrayList<VBox> spots = new ArrayList<>();

    public BuildABearViewController(BoardBuildHandler handler){
        this.handler = handler;
    }

    public void show(){
        if(window == null) createWindow();
        if(stage == null) createScene();
        if(stage.isShowing()) return;
        stage.showAndWait();
    }
    public void close(){
        stage.close();
    }
    public void showError(String text){
        Option option = new Option(text);
        option.getOKPressed();
    }
    private void createScene(){
        stage = new Stage();
        stage.setScene(new Scene(window, SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
    }
    private void createWindow(){
        window = new HBox();
        VBox menu = createMenu();
        window.setAlignment(Pos.TOP_RIGHT);
        createBoardHolder();
        window.getChildren().addAll(boardHolder, menu);
    }
    private void createBoardHolder(){
        boardHolder.setAlignment(Pos.CENTER);
        boardHolder.setStyle("-fx-background-color: #909090");
        boardHolder.setPrefWidth(SCREEN_WIDTH-150);
    }
    private VBox createBoardDescription(){
        VBox menu = new VBox();
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(10, 10, 10, 10));
        menu.setSpacing(20);
        VBox nameBox = addLabelInBox(boardName, "Board Name");
        VBox widthBox = addLabelInBox(boardWidth, "Width");
        VBox heightBox = addLabelInBox(boardHeight, "Height");
        boardWidth.setMaxWidth(72);
        boardHeight.setMaxWidth(72);
        boardWidth.setText("10");
        boardHeight.setText("10");
        HBox sizeHBox = new HBox();
        sizeHBox.setSpacing(5);
        setStyle(saveBoard);
        saveBoard.setText("Save Board");
        saveBoard.setOnAction(e->saveBoardPressed());
        setStyle(setSize);
        setSize.setText("Resize");
        setSize.setOnAction(e->setSizePressed());
        sizeHBox.getChildren().addAll(widthBox, heightBox);
        sizeHBox.setAlignment(Pos.CENTER);
        VBox sizeVBox = new VBox();
        sizeVBox.setAlignment(Pos.CENTER);
        sizeVBox.getChildren().addAll(sizeHBox, setSize);
        setSize.setPrefWidth(150);
        sizeVBox.setSpacing(5);
        VBox boardDescBox = new VBox();
        boardDescBox.getChildren().addAll(nameBox, saveBoard);
        saveBoard.setPrefWidth(150);
        boardDescBox.setSpacing(5);
        menu.getChildren().addAll(boardDescBox, sizeVBox);
        return menu;
    }
    private VBox createMenu(){
        VBox menu = new VBox();
        VBox boardDescription = createBoardDescription();
        bottom = createBottom();
        elementChooserWindow.setPrefSize(150, 350);
        elementChooserWindow.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(boardDescription, bottom, elementChooserWindow);
        menu.setStyle("-fx-border-color: #909090; -fx-background-color: #dddddd");
        return menu;
    }
    private HBox createBottom(){
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        return box;
    }

    public void updateGrid(ArrayList<ArrayList<BoardBuildElement>> board){
        boardHolder.getChildren().clear();
        HBox widthBox = new HBox();
        widthBox.setAlignment(Pos.CENTER);
        for(int x = 0; x < board.size(); x++){
            VBox heightBox = new VBox();
            heightBox.setAlignment(Pos.CENTER);
            widthBox.getChildren().add(heightBox);
            for(int y = 0; y < board.get(x).size(); y++){
                BoardBuildElement element = board.get(x).get(y);
                StackPane field = element.getView();
                Button button = new Button("");
                button.setOpacity(0);
                button.setPrefSize(element.WIDTH, element.HEIGHT);
                final int xPos = x; final int yPos = y;
                button.setOnAction(e->fieldPressed(xPos, yPos));
                field.getChildren().add(button);
                heightBox.getChildren().add(field);
            }
        }
        boardHolder.getChildren().add(widthBox);
    }
    public void updateElementGrid(BoardBuildElement element){
        if(spots.size() == 0) spots = createGrid();
        createElementBoxes(element);
        formatElementBoxes();
    }
    private void formatElementBoxes(){
        elementChooserWindow.getChildren().clear();
        for(int i = 0; i < 2; i++){
            VBox vbox = new VBox();
            for(int j = 0; j < 7; j++){
                int index = i*7+j;
                vbox.getChildren().add(spots.get(index));
            }
            vbox.setPrefSize(70, 350);
            vbox.setSpacing(5);
            elementChooserWindow.getChildren().add(vbox);
        }
    }
    private void createElementBoxes(BoardBuildElement element) {
        ArrayList<StackPane> images = BoardBuildLogic.getBoardElementImages();
        ArrayList<Boolean> actives = BoardBuildLogic.getActiveElements(element);
        for (int i = 0; i < spots.size(); i++) {
            spots.get(i).getChildren().clear();
            VBox active = new VBox();
            VBox addAble = new VBox();
            StackPane stackPane = new StackPane();
            Button button = new Button("");
            final int index = i;
            button.setOnAction(e->elementPressed(index, element.getX(), element.getY()));
            if(BoardBuildLogic.isIndexTrueForElement(index, element)) active.setStyle("-fx-border-color: #6c22a8; -fx-border-width: 2");
            button.setPrefSize(element.WIDTH, element.HEIGHT);
            active.setPrefSize(element.WIDTH, element.HEIGHT);
            stackPane.getChildren().addAll(images.get(i), addAble, active);
            if (actives.get(i)) {
                active.setStyle("-fx-background-color: #606060");
                active.setOpacity(10);
            } else {
                stackPane.getChildren().remove(active);
                spots.get(i).getChildren().add(stackPane);
            }
        }
    }
    private ArrayList<VBox> createGrid(){
        ArrayList<VBox> spots = new ArrayList<>();
        for(int i = 0; i < 14; i++){
            VBox spot1 = new VBox();
            spots.add(spot1);
        }
        return spots;
    }

    private VBox addLabelInBox(Node node, String labelText){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label label = new Label(labelText);
        setStyle(label);
        vbox.getChildren().addAll(label, node);
        return vbox;
    }
    private void setStyle(Node node){
        node.setStyle("-fx-font-weight: bold; -fx-font-size: 14");
    }
    private void fieldPressed(int x, int y){
        handler.showElementMenuFor(x, y);
    }
    private void saveBoardPressed(){

    }
    private void setSizePressed(){
        String[] input = new String[2];
        input[0] = boardWidth.getText();
        input[1] = boardHeight.getText();
        handler.resizeBoard(input);
    }
    private void elementPressed(int index, int x, int y){

    }


}
