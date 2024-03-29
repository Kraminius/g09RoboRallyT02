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
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.MainMenu.MainMenuLoader;
import dk.dtu.compute.se.pisd.roborally.chat.ChatController;
import dk.dtu.compute.se.pisd.roborally.chat.ClientInfo;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;
import dk.dtu.compute.se.pisd.roborally.view.*;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.Lobby;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RoboRally extends Application {

    private static final int MIN_APP_WIDTH = 600;
    private static RoboRally instance;

    private Stage stage;
    private BorderPane boardRoot;
    private VBox chatView;
    private BoardView boardView;
    String playerName;

    private static Lobby lobby;
    // private RoboRallyMenuBar menuBar;

    // private AppController appController;

    private static AppController appController;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenuLoader mainMenu;
        do {
            mainMenu = new MainMenuLoader();
        } while (!mainMenu.run()); //Returns true when playing game.
        //Everything that will run after the main menu, is what happens when the player has pressed Play Game.


        instance = this;

        LobbyManager lobbyManager = new LobbyManager();

        lobby = new Lobby(lobbyManager);

        if (GameClient.isGameRunning()) {
            System.out.println("Vi kommer her");
            GameLobby gameLobby = GameClient.getGame();
            System.out.println(gameLobby.toString());
            lobbyManager.createGame(gameLobby);
            lobby.addLobbyToLobby(gameLobby);
            lobby.getGameLobbyMap().put(gameLobby.getLobbyId(), gameLobby);

        }


        lobby.show();

        stage = primaryStage;

    }

    public void createChatWindow(String name){
        if(name != null) playerName = name;
        if(chatView != null) return;
        chatView = new VBox();
        chatView.setMinWidth(200);
        //Activate a ChatClient and add their view to this chatView

        ChatController chatController = new ChatController();
        chatController.addChatClient(playerName);
        chatView = chatController.getChatView().getChatRoomView();

        //Remove the following two lines of code, these are just to show where the chat will be located.
        //Once the real chatView is added to the chatView VBox then these are no longer needed.
    }

    public void startGame(GameSettings gameSettings, Stage primaryStage) throws Exception {
        stage = primaryStage;
        appController = new AppController(this, gameSettings);
        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();} );
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setX(700);
        stage.setY(100);
        stage.show();
        appController.newGame();
    }

    public void startLoadedGame(GameSettings gameSettings, Stage primaryStage, String saveName) throws Exception {
        stage = primaryStage;
        appController = new AppController(this, gameSettings);
        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        Scene primaryScene = new Scene(vbox);

        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    appController.exit();} );
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setX(700);
        stage.setY(100);
        stage.show();
        System.out.println("Test ny");
        appController.loadServerGameFromStart(saveName);
    }

    public void createBoardView(GameController gameController) {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();

        if (gameController != null) {
            // create and add view for new board
            boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
            boardView.disablePlayerViews();
        }

        stage.sizeToScene();
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        // XXX just in case we need to do something here eventually;
        //     but right now the only way for the user to exit the app
        //     is delegated to the exit() method in the AppController,
        //     so that the AppController can take care of that.
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static RoboRally getInstance() { // And add this method
        return instance;
    }

    public static Lobby getLobby(){
        return lobby;
    }

    public static AppController getAppController(){return appController;}

    public VBox getChatView(){
        if(chatView != null) return chatView;
        System.out.println("Chat view is null");
        return null;
    }
    public BoardView getBoardView(){
        return boardView;
    }
}