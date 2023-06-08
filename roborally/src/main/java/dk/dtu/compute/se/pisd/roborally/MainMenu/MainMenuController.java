package dk.dtu.compute.se.pisd.roborally.MainMenu;

import dk.dtu.compute.se.pisd.roborally.view.BoardLoadWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainMenuController {

    private MainMenuHandler handler;
    private final int SCREEN_WIDTH = 600;
    private final int SCREEN_HEIGHT = 600;

    private VBox root;
    private Stage stage;
    private VBox mainMenu;
    private VBox boardMenu;
    private VBox boardLoadMenu;
    private HBox list = new HBox();

    /**
     * @Author Tobias Gørlyk s224271
     * Constructor for the controller,that sets the handler for the controller and creates the window.
     * @param handler the main menu handler
     */
    public MainMenuController(MainMenuHandler handler){
        this.handler = handler;
        createWindow();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the window by creating different parts of the window and putting them in.
     */
    private void createWindow(){
        root = makeRoot();
        stage = makeStage(root);
        createMainMenu();
        createBoardBuilderMenu();
        createBoardLoaderMenu();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the main menu labels and buttons
     */
    private void createMainMenu(){
        mainMenu = new VBox();
        mainMenu.setSpacing(40);
        mainMenu.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        Button playGameButton = styleButton("Play Game", 18);
        Button makeBoardButton = styleButton("Build a Board", 18);
        Button exit = styleButton("Exit", 18);
        playGameButton.setOnAction(e->handler.playGame());
        makeBoardButton.setOnAction(e->handler.showBoardMenu());
        exit.setOnAction(e->handler.exit());
        buttons.getChildren().addAll(playGameButton, makeBoardButton, exit);
        Label label = new Label("Main Menu");
        label.setStyle("-fx-font-size: 36; -fx-font-weight: bold");
        mainMenu.getChildren().addAll(label, buttons);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * creates the boardbuilder menu
     */
    private void createBoardBuilderMenu(){
        boardMenu = new VBox();
        boardMenu.setSpacing(40);
        boardMenu.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        Button createBoardButton = styleButton("Create New Board",18);
        Button loadBoardButton = styleButton("Edit Board",18);
        Button backToMenuButton = styleButton("Back To Menu",18);
        createBoardButton.setOnAction(e->handler.editNewBoard());
        loadBoardButton.setOnAction(e->handler.showLoadBoardMenu());
        backToMenuButton.setOnAction(e->handler.backToMenu());
        buttons.getChildren().addAll(createBoardButton, loadBoardButton, backToMenuButton);
        Label label = new Label("Build A Board");
        label.setStyle("-fx-font-size: 36; -fx-font-weight: bold");
        boardMenu.getChildren().addAll(label, buttons);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the boardLoader window
     */
    private void createBoardLoaderMenu(){
        boardLoadMenu = new VBox();
        boardLoadMenu.setSpacing(40);
        boardLoadMenu.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        updateBoardList(list);
        Button backToMenuButton = styleButton("Back To Menu",18);
        backToMenuButton.setOnAction(e->handler.backToMenu());
        buttons.getChildren().addAll(list, backToMenuButton);
        Label label = new Label("Manage Loaded Boards");
        label.setStyle("-fx-font-size: 36; -fx-font-weight: bold");
        boardLoadMenu.getChildren().addAll(label, buttons);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Updates the board list along with a scrollpane and buttons
     * @param holder the holder in which to put the list.
     */
    private void updateBoardList(HBox holder){
        BoardLoadWindow loadGetter = new BoardLoadWindow();
        ComboBox<String> boards = new ComboBox<>();
        loadGetter.addFiles(boards);
        ScrollPane scrollPane = new ScrollPane();
        VBox box = new VBox();
        scrollPane.setContent(box);
        scrollPane.setPrefHeight(120);
        holder.getChildren().clear();
        scrollPane.setStyle("-fx-background-color: #909090");
        scrollPane.setPrefWidth(288);
        holder.setAlignment(Pos.CENTER);
        holder.getChildren().addAll(scrollPane);
        for(String boardName : boards.getItems()){
            HBox element = new HBox();
            Button load = styleButton(boardName, 14);
            Button delete = styleButton("Delete", 14);
            delete.setPrefWidth(70);
            load.setOnAction(e->loadBoard(boardName));
            delete.setOnAction(e->deleteBoard(boardName));
            element.getChildren().addAll(load, delete);
            box.getChildren().add(element);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates a button in a specific style
     * @param size size of button
     * @param text the text to display on the button
     * @return the button
     */
    private Button styleButton(String text, int size){
        Button button = new Button(text);
        button.setStyle("-fx-font-size: " + size);
        button.setPrefSize(200, size*2);
        return button;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * makes the stage
     * @param root the root of the window
     * @return Stage
     */
    private Stage makeStage(VBox root){
        Stage stage = new Stage();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        return stage;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Shows the main menu but clearing the root and adding the main menu into it.
     */
    public void showMain(){
        root.getChildren().clear();
        root.getChildren().add(mainMenu);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Shows the main board menu but clearing the root and adding the board menu into it.
     */
    public void showBoardBuilderMenu(){
        root.getChildren().clear();
        root.getChildren().add(boardMenu);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Shows the load board menu but clearing the root and adding the board load menu into it.
     */
    public void showBoardLoaderMenu(){
        root.getChildren().clear();
        root.getChildren().add(boardLoadMenu);
        updateBoardList(list);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates the root
     * @return the VBox of the root
     */
    private VBox makeRoot(){
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: #cccccc");
        return root;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Shows the stage if not already showing
     */
    public void show(){
        if(!stage.isShowing()) stage.showAndWait();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Closes the stage if is it showing
     */
    public void close(){
        if(stage.isShowing()) stage.close();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Loads a board with specific name
     * @param name the name to laod
     */
    private void loadBoard(String name){
        handler.editLoadedBoard(name);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Deletes a board with a specific name
     * @param name name of board
     */
    private void deleteBoard(String name){
        handler.deleteLoadedBoard(name);
        list.getChildren().clear();
        updateBoardList(list);
    }

}
