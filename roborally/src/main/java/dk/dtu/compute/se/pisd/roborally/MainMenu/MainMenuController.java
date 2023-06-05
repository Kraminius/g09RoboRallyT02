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


    public MainMenuController(MainMenuHandler handler){
        this.handler = handler;
        createWindow();
    }
    private void createWindow(){
        root = makeRoot();
        stage = makeStage(root);
        createMainMenu();
        createBoardBuilderMenu();
        createBoardLoaderMenu();
    }
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
    private void createBoardLoaderMenu(){
        boardLoadMenu = new VBox();
        boardLoadMenu.setSpacing(40);
        boardLoadMenu.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(20);
        HBox list = makeBoardList();
        Button backToMenuButton = styleButton("Back To Menu",18);
        backToMenuButton.setOnAction(e->handler.backToMenu());
        buttons.getChildren().addAll(list, backToMenuButton);
        Label label = new Label("Manage Loaded Boards");
        label.setStyle("-fx-font-size: 36; -fx-font-weight: bold");
        boardLoadMenu.getChildren().addAll(label, buttons);
    }
    private HBox makeBoardList(){
        BoardLoadWindow loadGetter = new BoardLoadWindow();
        ComboBox<String> boards = new ComboBox<>();
        loadGetter.addFiles(boards);
        ScrollPane scrollPane = new ScrollPane();
        VBox box = new VBox();
        scrollPane.setContent(box);
        scrollPane.setPrefHeight(120);
        HBox holder = new HBox();
        scrollPane.setStyle("-fx-background-color: #909090");
        scrollPane.setPrefWidth(400);
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

        return holder;
    }
    private Button styleButton(String text, int size){
        Button button = new Button(text);
        button.setStyle("-fx-font-size: " + size);
        button.setPrefSize(200, size*2);
        return button;
    }
    private Stage makeStage(VBox root){
        Stage stage = new Stage();
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setScene(scene);
        return stage;
    }
    public void showMain(){
        root.getChildren().clear();
        root.getChildren().add(mainMenu);
    }
    public void showBoardBuilderMenu(){
        root.getChildren().clear();
        root.getChildren().add(boardMenu);
    }
    public void showBoardLoaderMenu(){
        root.getChildren().clear();
        root.getChildren().add(boardLoadMenu);
    }
    private VBox makeRoot(){
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setStyle("-fx-background-color: #cccccc");
        return root;
    }
    public void show(){
        if(!stage.isShowing()) stage.showAndWait();

    }
    public void close(){
        if(stage.isShowing()) stage.close();
    }
    private void loadBoard(String name){
        handler.editLoadedBoard(name);
    }
    private void deleteBoard(String name){
        handler.deleteLoadedBoard(name);
    }

}
