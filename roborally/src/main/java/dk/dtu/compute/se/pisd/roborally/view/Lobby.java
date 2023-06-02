package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.GameSettings;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Lobby {
    Stage stage;
    VBox window;
    String answer;

    String[] options;
    boolean yesNo;

    GameSettings gameSettings;

    public Lobby(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        stage = new Stage();
        window = new VBox();
        ScrollPane lobbySP = new ScrollPane();
        VBox lobbyList = new VBox();
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
        createGameButton.setOnAction(e -> openCreateGameScene(gameSettings));
        Button joinGameButton = new Button("Join Game");
        createGameButton.setId("JoinGameButton");

        buttonBox.getChildren().addAll(spacer, createGameButton, joinGameButton);
        top.getChildren().add(buttonBox);  // Add the HBox to top

        // For each lobby element, add a new vbox.
        for (int i = 0; i < 10; i++) {
            VBox lobbyVbox = new VBox();
            lobbyVbox.prefWidthProperty().bind(Bindings.createObjectBinding(() ->
                    lobbySP.getViewportBounds().getWidth(), lobbySP.viewportBoundsProperty()));
            HBox lobbyHbox = new HBox();
            Label lobbyText = new Label("Lobby " + i + "\n" + "Creator: ");
            Button joinButton = new Button("Join");
            joinButton.setId("JoinButton" + i);
            Region spacerInLobby = new Region();
            HBox.setHgrow(spacerInLobby, Priority.ALWAYS);
            lobbyHbox.getChildren().addAll(lobbyText, spacerInLobby, joinButton);
            lobbyVbox.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #b4b4b4; -fx-background-color: #ffffff");
            lobbyVbox.setPadding(new Insets(2, 2, 2, 2));
            if (i % 2 == 0) {
                lobbyVbox.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-text-fill: #eeeeee; -fx-background-color: #eeeeee");
            }
            lobbyVbox.getChildren().add(lobbyHbox);
            lobbyList.getChildren().add(lobbyVbox);
        }

        lobbySP.setContent(lobbyList);
        window.getChildren().add(top);
        window.getChildren().add(lobbySP);
    }


    //Your second constructor remains unchanged.

    public void show(){
        Scene scene = new Scene(window);
        stage.setScene(scene);
        //stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        //stage.setOnCloseRequest(Event::consume);
        stage.showAndWait();
    }

    private void openCreateGameScene(GameSettings gameSettings) {
        // Create new Stage for this scene
        Stage createGameStage = new Stage();

        // Create a new VBox layout
        VBox createGameLayout = new VBox(10);

        // Create the form inputs
        Label gameNameLabel = new Label("Name of the game:");
        TextField gameNameInput = new TextField();

        Label creatorNameLabel = new Label("Creator name:");
        TextField creatorNameInput = new TextField();

        Label numberOfPlayersLabel = new Label("How many players:");
        TextField numberOfPlayersInput = new TextField();

        Label boardToPlayLabel = new Label("What board to play:");
        TextField boardToPlayInput = new TextField();

        // Create a submit button
        Button submitButton = new Button("Create Game");

        // Add event to the submit button
        submitButton.setOnAction(e -> {
            gameSettings.setGameName(gameNameInput.getText());
            gameSettings.setCreatorName(creatorNameInput.getText());
            gameSettings.setNumberOfPlayers(Integer.parseInt(numberOfPlayersInput.getText()));
            gameSettings.setBoardToPlay(boardToPlayInput.getText());

            System.out.println("Game created with settings: " + gameSettings);

            createGameStage.close();  // close the window after submitting
        });

        // Add all elements to the layout
        createGameLayout.getChildren().addAll(gameNameLabel, gameNameInput, creatorNameLabel, creatorNameInput, numberOfPlayersLabel, numberOfPlayersInput, boardToPlayLabel, boardToPlayInput, submitButton);

        // Create the scene and add it to the stage
        Scene createGameScene = new Scene(createGameLayout, 400, 400);
        createGameStage.setScene(createGameScene);
        createGameStage.show();
    }


    private void close(){
        stage.close();
    }
}
