package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.model.ImageLoader;
import dk.dtu.compute.se.pisd.roborally.view.Option;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class BuildABoardViewController {
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
    private Button exitBoard = new Button();
    private Button setSize = new Button();
    private VBox boardHolder = new VBox();
    ;
    private HBox bottom;
    private HBox elementWindow = new HBox();
    private HBox turnButtons = new HBox();
    private ArrayList<VBox> spots = new ArrayList<>();
    private BoardBuildElement currentElement;
    private int currentType;
    /**
     * @Author Tobias Gørlyk s224271
     * Constructor of the controllerView of the build a board window.
     * @param handler the handler that clicks and presses should use.
     */
    public BuildABoardViewController(BoardBuildHandler handler) {
        this.handler = handler;
        if (window == null) createWindow();
        if (stage == null) createScene();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Checks to see if the window is already shown,
     * It shows it along with setting the onClose request so the player cannot accidentally close board without saving.
     */
    public void show() {
        if (stage.isShowing()) return;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
                exitPressed();
            }
        });
        stage.showAndWait();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * closes the stage
     */
    public void close() {
        stage.close();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates an error box with a specific error.
     * @param text the text that should be displayed in the error.
     */
    public void showError(String text) {
        Option option = new Option("ERROR");
        option.getOKPressed(text);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * creates the stage and scene and inserts the scene along with making other windows useless.
     */
    private void createScene() {
        stage = new Stage();
        stage.setScene(new Scene(window, SCREEN_WIDTH, SCREEN_HEIGHT));
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        //stage.setOnCloseRequest(Event::consume);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * creates the main window and put the menu and boardholder where it should be.
     */
    private void createWindow() {
        window = new HBox();
        VBox menu = createMenu();
        window.setAlignment(Pos.TOP_RIGHT);
        createBoardHolder();
        window.getChildren().addAll(boardHolder, menu);
        showLeftRight(false);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * creates the boardHolder that holds the board grid.
     */
    private void createBoardHolder() {
        boardHolder.setAlignment(Pos.CENTER);
        boardHolder.setStyle("-fx-background-color: #909090");
        boardHolder.setPrefWidth(SCREEN_WIDTH - 150);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates and stylizes a board description, which is the board name, width and height along with the buttons to save and exit.
     * @return VBox holding labels and buttons.
     */
    private VBox createBoardDescription() {
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
        setStyle(exitBoard);
        saveBoard.setText("Save Board");
        exitBoard.setText("Exit Builder");
        saveBoard.setOnAction(e -> saveBoardPressed());
        exitBoard.setOnAction(e -> exitPressed());
        setStyle(setSize);
        setSize.setText("Resize");
        setSize.setOnAction(e -> setSizePressed());
        sizeHBox.getChildren().addAll(widthBox, heightBox);
        sizeHBox.setAlignment(Pos.CENTER);
        VBox sizeVBox = new VBox();
        sizeVBox.setAlignment(Pos.CENTER);
        sizeVBox.getChildren().addAll(sizeHBox, setSize);
        setSize.setPrefWidth(150);
        sizeVBox.setSpacing(5);
        VBox boardDescBox = new VBox();
        boardDescBox.getChildren().addAll(nameBox, saveBoard, exitBoard);
        saveBoard.setPrefWidth(150);
        exitBoard.setPrefWidth(150);
        boardDescBox.setSpacing(5);
        menu.getChildren().addAll(boardDescBox, sizeVBox);
        return menu;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the menu on the right side of the screen and adds the board description along with the element window and the bottom part.
     * @return the menu VBox
     */
    private VBox createMenu() {
        VBox menu = new VBox();
        VBox boardDescription = createBoardDescription();
        bottom = createBottom();
        elementWindow.setPrefSize(150, 450);
        elementWindow.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(boardDescription, elementWindow, bottom);
        menu.setStyle("-fx-border-color: #909090; -fx-background-color: #dddddd");
        return menu;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the bottom of the menu which holds the turnbuttons.
     * @return a HBox that holds the bottom of the menu.
     */
    private HBox createBottom() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        createLeftRight();
        box.getChildren().add(turnButtons);
        return box;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Updates the grid by clearing the boardholder and creating a new grid from the info sent from the handler.
     * @param board a Two-Dimensional ArrayList of BoardBuildElements that is sent from the handler.
     */
    public void updateGrid(ArrayList<ArrayList<BoardBuildElement>> board) {
        boardHolder.getChildren().clear();
        HBox widthBox = new HBox();
        widthBox.setAlignment(Pos.CENTER);
        for (int x = 0; x < board.size(); x++) {
            VBox heightBox = new VBox();
            heightBox.setAlignment(Pos.CENTER);
            widthBox.getChildren().add(heightBox);
            for (int y = 0; y < board.get(x).size(); y++) {
                BoardBuildElement element = board.get(x).get(y);
                StackPane field = element.getView();
                Button button = new Button("");
                button.setOpacity(0);
                button.setPrefSize(element.WIDTH, element.HEIGHT);
                final int xPos = x;
                final int yPos = y;
                button.setOnAction(e -> fieldPressed(xPos, yPos));
                field.getChildren().add(button);
                heightBox.getChildren().add(field);
            }
        }
        boardHolder.getChildren().add(widthBox);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Update the element menu to show only the different variants from that type.
     * @param element the Element that will be changed
     * @param type the type of element we need to fetch variants for.
     */
    public void updateElementVariants(BoardBuildElement element, int type){
        currentElement = element;
        currentType = type;
        updateElementVariantBoxes();
        formatElementBoxes();
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    public void updateElementGrid(BoardBuildElement element){
        if(spots.size() == 0) spots = createGrid();
        currentElement = element;
        updateElementBoxes(element);
        formatElementBoxes();
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void formatElementBoxes(){
        elementWindow.getChildren().clear();
        for(int i = 0; i < 2; i++){
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            for(int j = 0; j < 15; j+=2){
                HBox hBox = new HBox();
                hBox.setSpacing(5);
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(spots.get(j));
                if(j+1 != 15){
                    hBox.getChildren().add(spots.get(j+1));
                }
                vBox.getChildren().add(hBox);
            }
            vBox.setSpacing(5);
            elementWindow.getChildren().add(vBox);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void updateElementVariantBoxes() {
        ArrayList<StackPane> images = BoardBuildLogic.getBoardVariants(currentType);
        ArrayList<Boolean> actives = BoardBuildLogic.getActiveVariant(currentElement, currentType);
        for(int i = 0; i < spots.size(); i++){
            spots.get(i).getChildren().clear();
        }
        for (int i = 0; i < images.size(); i++) {
            VBox active = new VBox();
            VBox border = new VBox();
            border.setStyle("-fx-border-color: #404040; -fx-border-width: 1");
            StackPane stackPane = new StackPane();
            Button button = new Button("");
            final int index = i;
            button.setOnAction(e -> elementVariantPressed(currentType, index, currentElement.getX(), currentElement.getY()));
            button.setOnMouseEntered(e -> {
                border.setStyle("-fx-border-color: #ffb900; -fx-border-width: 2");
            });
            button.setOnMouseExited(e -> {
                border.setStyle("-fx-border-color: #404040; -fx-border-width: 1");
            });
            button.setOpacity(0);
            if (actives.get(i)) active.setStyle("-fx-background-color: #ffce00");
            active.setOpacity(0.3);
            button.setPrefSize(currentElement.WIDTH, currentElement.HEIGHT);
            active.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            border.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            stackPane.getChildren().addAll(images.get(i), active, border, button);
            spots.get(i).getChildren().add(stackPane);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void updateElementBoxes(BoardBuildElement element) {
        ArrayList<StackPane> images = BoardBuildLogic.getBoardElementImages();
        ArrayList<Boolean> actives = BoardBuildLogic.getActiveElements(element);
        ArrayList<Boolean> canBuilds = BoardBuildLogic.getCanBuild(element);
        for (int i = 0; i < spots.size(); i++) {
            spots.get(i).getChildren().clear();
            VBox background = new VBox();
            VBox active = new VBox();
            VBox addAble = new VBox();
            VBox border = new VBox();
            border.setStyle("-fx-border-color: #404040; -fx-border-width: 1");
            StackPane stackPane = new StackPane();
            Button button = new Button("");
            final int index = i;
            button.setOnAction(e->elementPressed(index, element.getX(), element.getY()));
            button.setOnMouseEntered(e->{border.setStyle("-fx-border-color: #ffb900; -fx-border-width: 2");});
            button.setOnMouseExited(e->{border.setStyle("-fx-border-color: #404040; -fx-border-width: 1");});
            button.setOpacity(0);
            if(actives.get(i)) active.setStyle("-fx-background-color: #ffce00");
            if(!canBuilds.get(i)) {
                addAble.setStyle("-fx-background-color: #575757");
                button.setDisable(true);
            }
            background.setStyle("-fx-background-color: #dddddd");
            active.setOpacity(0.3);
            addAble.setOpacity(0.6);
            button.setPrefSize(currentElement.WIDTH, currentElement.HEIGHT);
            active.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            addAble.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            background.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            border.setMaxSize(currentElement.WIDTH, currentElement.HEIGHT);
            stackPane.getChildren().addAll(background, images.get(i), addAble, active, border, button);
            spots.get(i).getChildren().add(stackPane);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private ArrayList<VBox> createGrid(){
        ArrayList<VBox> spots = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            VBox spot1 = new VBox();
            spots.add(spot1);
        }
        return spots;
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private VBox addLabelInBox(Node node, String labelText){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label label = new Label(labelText);
        setStyle(label);
        vbox.getChildren().addAll(label, node);
        return vbox;
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    public void showLeftRight(boolean show){
        if(show){
            turnButtons.setDisable(false);
            turnButtons.setOpacity(1);
        }
        else{
            turnButtons.setDisable(true);
            turnButtons.setOpacity(0);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void createLeftRight(){
        turnButtons = new HBox();
        Button left = new Button("Turn Left");
        Button right = new Button("Turn Right");
        setStyle(left);
        setStyle(right);
        left.setStyle(left.getStyle() + "; -fx-font-size: 13");
        right.setStyle(left.getStyle());
        left.setAlignment(Pos.CENTER);
        right.setAlignment(Pos.CENTER);
        left.setOnAction(e->leftPressed());
        right.setOnAction(e->rightPressed());
        turnButtons.getChildren().addAll(left, right);
        turnButtons.setPadding(new Insets(10, 0, 0, 0));
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void setStyle(Node node){
        node.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-alignment: center");
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void fieldPressed(int x, int y){
        handler.showElementMenuFor(x, y);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void saveBoardPressed(){
        handler.saveBoard(boardName.getText());
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void setSizePressed(){
        String[] input = new String[2];
        input[0] = boardWidth.getText();
        input[1] = boardHeight.getText();
        handler.resizeBoard(input);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void elementPressed(int index, int x, int y){
        handler.elementClicked(index, x ,y);
        currentType = index;
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void elementVariantPressed(int type, int index, int x, int y){
        handler.elementVariantClicked(type, index, x ,y);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void leftPressed(){
        handler.turn(false, currentElement, currentType);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    private void rightPressed(){
        handler.turn(true, currentElement, currentType);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    public void setBoardName(String name){
        boardName.setText(name);
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    public void setSizeText(int x, int y){
        boardWidth.setText(x + "");
        boardHeight.setText(y + "");
    }
    /**
     * @Author Tobias Gørlyk s224271
     *
     */
    public void exitPressed(){
        handler.exit();
    }
}
