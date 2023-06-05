package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.GameClient;
import dk.dtu.compute.se.pisd.roborally.MyClient;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.controller.LobbyController;
import dk.dtu.compute.se.pisd.roborally.model.GameLobby;
import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import dk.dtu.compute.se.pisd.roborally.model.LobbyManager;


import java.util.*;

public class Lobby {

    private Map<String, GameLobby> gameLobbyMap = new HashMap<>();
    private Map<String, HBox> lobbyHBoxMap = new HashMap<>();

    private Map<String, VBox> specificLobbyLabels = new HashMap<>();

    Stage stage;
    VBox lobbyLayout;
    Stage waitStage;
    VBox window;
    VBox lobbyList;
    VBox lobbyVbox;
    Label lobbyText;
    Label lobbyPlayer;
    HBox lobbyHbox;
    ScrollPane lobbySP;
    BorderPane rootLayout;
    private LobbyWindow lobbyWindow;
    private VBox joinWaitWindow;
    String answer;

    String[] options;
    boolean yesNo;

    GameSettings gameSettings;
    private LobbyManager lobbyManager;
    private GameLobby gameLobby;

    private RoboRally roboRally;



    /**
     * Default constructor for the Lobby class.
     * @author Mikkel Jürs, s224279@student.dtu.dk
     */
    public Lobby(LobbyManager lobbyManager) {
        this.gameSettings = gameSettings;
        this.lobbyManager = lobbyManager;
        lobbyWindow = new LobbyWindow();
        stage = new Stage();
        window = new VBox();
        lobbySP = new ScrollPane();
        lobbyList = new VBox();
        window.setMaxSize(900, 550);
        window.setMinSize(900, 550);
        window.setAlignment(Pos.TOP_CENTER);
        lobbySP.setHmax(Double.MAX_VALUE);
        lobbySP.setHmin(Double.MAX_VALUE);

        VBox top = new VBox();
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(10, 10, 10, 10));
        top.setStyle("-fx-background-color: #dadada");
        Label textLabel = new Label("RoboRally Lobby");
        textLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #000000");
        top.getChildren().add(textLabel);

        // Adding the buttons to top
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setSpacing(10);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button createGameButton = new Button("Create Game");
        createGameButton.setOnAction(e -> openCreateGameScene());
        Button loadGameButton = new Button("Load Game");

        buttonBox.getChildren().addAll(spacer, createGameButton, loadGameButton);
        top.getChildren().add(buttonBox);  // Add the HBox to top

        lobbySP.setContent(lobbyList);
        window.getChildren().add(top);
        window.getChildren().add(lobbySP);
    }


    private void getButtStuffForLoad(){
        LoadGameWindow lgw = new LoadGameWindow();//Where i have method to get all saves
        ComboBox<String> comboBox = new ComboBox<>();
        lgw.addFiles(comboBox); //fill the combobox with the names of saves
        comboBox.setValue(comboBox.getItems().get(0)); //Sets the value to the first so there always one chosen
        //After chosen a save to load, these following names should be shown to the players so they can choose which to take over.
    }



    private String[] getNames(String saveName){
        JSONHandler json = new JSONHandler(); //Where I can access files
        String[] names = json.getNamesOfGame(saveName); //Get names of files with a method that only loads the names of the json file.
        return names;
    }


    /**
     * Method to display the lobby window.
     * @author Mikkel Jürs, s224279@student.dtu.dk
     */
    public void show(){
        Scene scene = new Scene(window);
        stage.setScene(scene);
        //stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        //stage.setOnCloseRequest(Event::consume);
        stage.showAndWait();
    }

    public void openJoinGame(GameLobby gameLobby) throws Exception {

        // Create new Stage for this scene
        Stage createGameStage = new Stage();

        // Create a new BorderPane layout
        rootLayout = new BorderPane();

        // Create a new VBox layout
        VBox createGameLayout = new VBox(10);

        // Create the form inputs
        Label gameNameLabel = new Label("Name of the game:");
        TextField gameNameInput = new TextField();
        gameNameInput.setText(gameLobby.getGameSettings().getGameName());

        Label creatorNameLabel = new Label("Creator name:");
        TextField creatorNameInput = new TextField();
        creatorNameInput.setText(gameLobby.getGameSettings().getCreatorName());

        Label numberOfPlayersLabel = new Label("How many players:");
        Spinner<Integer> numberOfPlayersInput = new Spinner<>();
        numberOfPlayersInput.setPromptText(String.valueOf(gameLobby.getGameSettings().getNumberOfPlayers()));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2);
        numberOfPlayersInput.setValueFactory(valueFactory);
        numberOfPlayersInput.setEditable(false);

        Label boardToPlayLabel = new Label("What board to play:");
        TextField boardsToPlayInput = new TextField();
        gameNameInput.setText(gameLobby.getGameSettings().getBoardToPlay());




        gameNameInput.setDisable(true);
        creatorNameInput.setDisable(true);
        numberOfPlayersInput.setDisable(true);
        boardsToPlayInput.setDisable(true);

/*
        ArrayList<String> names = GameClient.getPlayerNames();
        Label amount = new Label();
        amount.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
        amount.setText("Players Joined: " + names.size());
        createGameLayout.getChildren().addAll(amount);
            for(String name : gameLobby.getGameSettings().getPlayerNames()){
                VBox background = new VBox();
                background.setStyle("-fx-border-color: #dddddd");
                background.setPadding(new Insets(2, 2, 2, 2));
                background.setAlignment(Pos.CENTER);
                Label label = new Label(name);
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-font-size: 14");
            background.getChildren().add(label);
            createGameLayout.getChildren().add(background);
        }

 */


        createGameLayout.getChildren().addAll(gameNameLabel, gameNameInput, creatorNameLabel, creatorNameInput, numberOfPlayersLabel, numberOfPlayersInput, boardToPlayLabel, boardsToPlayInput);
        rootLayout.setLeft(createGameLayout);
        // Create the scene and add it to the stage
        Scene createGameScene = new Scene(rootLayout, 500, 400); // Increased width to accommodate for lobby
        createGameStage.setScene(createGameScene);

        rootLayout.setRight(createLobbyLayout(gameLobby));

        specificLobbyLabels.put(gameLobby.getLobbyId(), createGameLayout);

        createGameStage.show();



    }



    /**
     * Method to open a new scene for creating a game.
     * @author Mikkel Jürs, s224279@student.dtu.dk
     */
    private void openCreateGameScene() {

        // Create new Stage for this scene
        Stage createGameStage = new Stage();

        // Create a new BorderPane layout
        rootLayout = new BorderPane();

        // Create a new VBox layout
        VBox createGameLayout = new VBox(10);

        // Create the form inputs
        Label gameNameLabel = new Label("Name of the game:");
        TextField gameNameInput = new TextField();

        Label creatorNameLabel = new Label("Creator name:");
        TextField creatorNameInput = new TextField();

        Label numberOfPlayersLabel = new Label("How many players:");
        Spinner<Integer> numberOfPlayersInput = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 6, 2);
        numberOfPlayersInput.setValueFactory(valueFactory);
        numberOfPlayersInput.setEditable(false);

        Label boardToPlayLabel = new Label("What board to play:");
        ComboBox<String> boardsToPlayInput = new ComboBox<>();
        BoardLoadWindow boardLoadWindow = new BoardLoadWindow();
        boardLoadWindow.addFiles(boardsToPlayInput);
        boardsToPlayInput.setValue(boardsToPlayInput.getItems().get(0));

        // Create a submit button
        Button submitButton = new Button("Create Game");


        // Add event to the submit button
        submitButton.setOnAction(e -> {
            // Set the game settings
            if (!gameNameInput.getText().isEmpty() && !creatorNameInput.getText().isEmpty()) {
                gameNameInput.setStyle(null);
                creatorNameInput.setStyle(null);

                String[] arr = {gameNameInput.getText(), creatorNameInput.getText(), String.valueOf(numberOfPlayersInput.getValue()),
                        boardsToPlayInput.getValue() };

                try {
                    GameClient.addGame(arr);
                    System.out.println("this works");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                /*GameSettings gameSettings = new GameSettings();
                gameSettings.setGameName(gameNameInput.getText());
                gameSettings.setCreatorName(creatorNameInput.getText());
                gameSettings.setNumberOfPlayers(numberOfPlayersInput.getValue());
                gameSettings.setBoardToPlay(boardsToPlayInput.getValue());
                gameSettings.getPlayerNames().add(gameSettings.getCreatorName());*/




                gameNameInput.setDisable(true);
                creatorNameInput.setDisable(true);
                numberOfPlayersInput.setDisable(true);
                boardsToPlayInput.setDisable(true);
                submitButton.setDisable(true);




                GameLobby gameLobbyTemp;


                //APi call lobbyid gameSettings
                try {
                    System.out.println("what");
                    gameLobbyTemp = GameClient.getGame();
                    System.out.println("up");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Her står noget: " + gameLobbyTemp);

                GameLobby gameLobby = gameLobbyTemp;
                this.gameLobby = gameLobby;
                lobbyManager.createGame(gameLobby);




                try {
                    MyClient.weConnect(0, gameLobby.getGameSettings().getCreatorName());
                    gameLobby.getGameSettings().setPlayerNames(GameClient.getPlayerNames());


                    System.out.println(gameLobby.toString());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }



                addLobbyToLobby(gameLobby);



                lobbyPlayer.setText("Players: " + gameLobby.getGameSettings().getPlayerNames().size() + "/" + gameLobby.getGameSettings().getNumberOfPlayers());
                rootLayout.setRight(createLobbyLayout(gameLobby));
            }
            if (gameNameInput.getText().isEmpty()) {
                gameNameInput.setStyle("-fx-border-color: #ff0000");
            }
            if (creatorNameInput.getText().isEmpty()){
                creatorNameInput.setStyle("-fx-border-color: #ff0000");
            }
            if (!gameNameInput.getText().isEmpty()) {
                gameNameInput.setStyle(null);
            }
            if(!creatorNameInput.getText().isEmpty()){
                creatorNameInput.setStyle(null);
                }

            specificLobbyLabels.put(this.gameLobby.getLobbyId(), createGameLayout);
            GameClient.startPlayerNamesPolling();
        });

        createGameLayout.getChildren().addAll(gameNameLabel, gameNameInput, creatorNameLabel, creatorNameInput, numberOfPlayersLabel, numberOfPlayersInput, boardToPlayLabel, boardsToPlayInput, submitButton);
        rootLayout.setLeft(createGameLayout);
        // Create the scene and add it to the stage
        Scene createGameScene = new Scene(rootLayout, 500, 400); // Increased width to accommodate for lobby



        createGameStage.setScene(createGameScene);
        createGameStage.show();
    }

    private VBox createLobbyLayout(GameLobby gameLobby) {
        lobbyLayout = new VBox();
        lobbyLayout.setPadding(new Insets(10, 10, 10, 10));
        lobbyLayout.setAlignment(Pos.TOP_CENTER);
        lobbyLayout.setSpacing(2);

        int amountOfPlayers = gameLobby.getGameSettings().getPlayerNames().size();
        Label amount = new Label();
        amount.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
        amount.setText("Players Joined: " + amountOfPlayers);
        lobbyLayout.getChildren().add(amount);

        for(String name : gameLobby.getGameSettings().getPlayerNames()){
            VBox background = new VBox();
            background.setStyle("-fx-border-color: #dddddd");
            background.setPadding(new Insets(2, 2, 2, 2));
            background.setAlignment(Pos.CENTER);
            Label label = new Label(name);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 14");
            background.getChildren().add(label);
            lobbyLayout.getChildren().add(background);
        }
        String lobbyId = gameLobby.getLobbyId();
        //specificLobbyLabels.put(lobbyId, lobbyLayout);
        return lobbyLayout;
    }

    public void addLobbyToLobby(GameLobby gameLobby){
        lobbyText = new Label();
        lobbyText.setText("Game Name: " + gameLobby.getGameSettings().getGameName() + "\nCreator: " + gameLobby.getGameSettings().getCreatorName());
        lobbyVbox = new VBox();
        lobbyPlayer = new Label();
        lobbyVbox.prefWidthProperty().bind(Bindings.createObjectBinding(() -> lobbySP.getViewportBounds().getWidth(), lobbySP.viewportBoundsProperty()));
        lobbyHbox = new HBox();
        Button joinButton = new Button("Join");
        lobbyPlayer.setText("Players: " + gameLobby.getGameSettings().getPlayerNames().size() + "/" + gameLobby.getGameSettings().getNumberOfPlayers());
        joinButton.setId(gameLobby.getLobbyId());
        joinButton.setOnAction(e -> openJoinGamePopup(joinButton.getId()));
        Region spacerInLobby = new Region();
        HBox.setHgrow(spacerInLobby, Priority.ALWAYS);
        lobbyHbox.getChildren().addAll(lobbyText, spacerInLobby, lobbyPlayer, joinButton);
        lobbyVbox.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #b4b4b4; -fx-background-color: #ffffff");
        lobbyVbox.setPadding(new Insets(2, 2, 2, 2));
        lobbyVbox.getChildren().add(lobbyHbox);
        lobbyList.getChildren().add(lobbyVbox);

        // store the GameLobby in a HashMap, so we can update and distinguish between each lobby individually
        gameLobbyMap.put(gameLobby.getLobbyId(), gameLobby);
        lobbyHBoxMap.put(gameLobby.getLobbyId(), lobbyHbox);
    }

    private void openJoinGamePopup(String lobbyId) {
        gameLobby = gameLobbyMap.get(lobbyId);

        lobbyHbox = lobbyHBoxMap.get(lobbyId);
        // Create a new dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Join Game");
        dialog.setHeaderText("Enter Your Name");

        // Set the button types (OK and CANCEL)
        ButtonType joinButtonType = new ButtonType("Join", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(joinButtonType, ButtonType.CANCEL);

        // Create a text field for entering the name
        TextField nameField = new TextField();
        nameField.setPromptText("Your Name");

        // Enable the join button only if the name is not empty
        Node joinButton = dialog.getDialogPane().lookupButton(joinButtonType);
        joinButton.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            joinButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the content of the dialog
        dialog.getDialogPane().setContent(nameField);

        // Request focus on the name field by default
        Platform.runLater(nameField::requestFocus);

        // Convert the result of the dialog to the player's name
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == joinButtonType) {
                return nameField.getText();
            }

            return null;
        });
        // Show the dialog and wait for the user's input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(playerName -> {
            try {
                joinLobby(gameLobby, playerName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        //Polling player names
        GameClient.startPlayerNamesPolling();


    }

    private void joinLobby(GameLobby gameLobby, String playerName) throws Exception {

        int playerNumber = Integer.parseInt(MyClient.playerNumber());
        MyClient.weConnect(playerNumber, playerName);
        ArrayList<String> playerNames = GameClient.getPlayerNames();
        gameLobby.getGameSettings().setPlayerNames(playerNames);

        // Add the player to the game lobby
        lobbyManager.addPlayers(gameLobby, playerNames);
        openJoinGame(gameLobby);



        //updateJoinWindow(gameLobby); //Update joinWindow

        System.out.println(GameClient.getPlayerNames());

    }

    public void updaterQueueLobby(GameLobby gameLobby){





    }


    public void updateJoinWindow(GameLobby gameLobby){

        String lobbyId = gameLobby.getLobbyId();
        VBox specificLobbyLayout = specificLobbyLabels.get(lobbyId);

        specificLobbyLayout.getChildren().clear();
        specificLobbyLayout.setPadding(new Insets(10, 10, 10, 10));
        specificLobbyLayout.setAlignment(Pos.TOP_CENTER);
        specificLobbyLayout.setSpacing(2);
        //Button startGame = new Button();
        //startGame.setId(lobbyId);
        //startGame.setText("Start Game");
        //startGame.setVisible(false);
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        /*startGame.setOnAction(e -> {
            // Retrieve the GameLobby using the button's ID (which is the lobbyId)
            GameLobby gameLobbyButton = gameLobbyMap.get(startGame.getId());

            // Retrieve the GameSettings from the GameLobby
            GameSettings gameSettings = gameLobbyButton.getGameSettings();

            // Call the startGame method with these GameSettings
            try {
                RoboRally.getInstance().startGame(gameSettings, stage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });*/



        int amountOfPlayers = gameLobby.getGameSettings().getPlayerNames().size();
        Label amount = new Label();
        amount.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
        amount.setText("Players Joined: " + amountOfPlayers);
        specificLobbyLayout.getChildren().addAll(amount);
        for(String name : gameLobby.getGameSettings().getPlayerNames()){
            VBox background = new VBox();
            background.setStyle("-fx-border-color: #dddddd");
            background.setPadding(new Insets(2, 2, 2, 2));
            background.setAlignment(Pos.CENTER);
            Label label = new Label(name);
            label.setAlignment(Pos.CENTER);
            label.setStyle("-fx-font-size: 14");
            background.getChildren().add(label);
            specificLobbyLayout.getChildren().add(background);
        }
        specificLobbyLayout.getChildren().addAll(spacer);
        updateLabels(gameLobby.getLobbyId());
        /*if(gameLobby.getGameSettings().getPlayerNames().size() == gameLobby.getGameSettings().getNumberOfPlayers()){
           startGame.setVisible(true);
        }*/
        rootLayout.setRight(specificLobbyLayout);

    }

    public void updateLabels(String lobbyId){
        HBox lobbyHbox = lobbyHBoxMap.get(lobbyId);
        Label gameNameCreatorName = (Label) lobbyHbox.getChildren().get(0);
        Button joinButton = (Button) lobbyHbox.getChildren().get(3);
        Label lobbyPlayerLabel = (Label) lobbyHbox.getChildren().get(2);



        GameLobby gameLobby = gameLobbyMap.get(lobbyId);
        gameNameCreatorName.setText("Game Name: " + gameLobby.getGameSettings().getGameName() + "\nCreator: " + gameLobby.getGameSettings().getCreatorName());
        lobbyPlayerLabel.setText("Players: " + gameLobby.getGameSettings().getPlayerNames().size() + "/" + gameLobby.getGameSettings().getNumberOfPlayers());
        if(gameLobby.getGameSettings().getPlayerNames().size() == gameLobby.getGameSettings().getNumberOfPlayers()){
            joinButton.setDisable(true);
        }
    }

    private void openWaitScene(){
        if(waitStage != null) return;
        waitStage = new Stage();
        waitStage.show();
    }


    /**
     * Method to close the lobby window.
     * @author Mikkel Jürs, s224279@student.dtu.dk
     */
    private void close(){
        stage.close();
    }

    public Map<String, GameLobby> getGameLobbyMap() {
        return gameLobbyMap;
    }

    public void setGameLobbyMap(Map<String, GameLobby> gameLobbyMap) {
        this.gameLobbyMap = gameLobbyMap;
    }
}
