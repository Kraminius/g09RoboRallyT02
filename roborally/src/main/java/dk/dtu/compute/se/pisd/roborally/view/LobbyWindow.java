package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LobbyWindow {
    private final Stage stage;
    private final HBox window;
    private final VBox lobbyLayout;
    private final List<String> joinedPlayers;

    public LobbyWindow() {
        stage = new Stage();
        lobbyLayout = new VBox();
        window = new HBox();
        lobbyLayout.setAlignment(Pos.CENTER);
        lobbyLayout.setSpacing(10);
        lobbyLayout.setPadding(new Insets(10));
        window.getChildren().add(lobbyLayout);
        joinedPlayers = new ArrayList<>();

        // Create the scene and set it to the stage
        Scene scene = new Scene(window, 400, 400);
        stage.setScene(scene);
        stage.setTitle("Lobby");
    }

    public void addPlayer(String playerName) {
        joinedPlayers.add(playerName);

        // Update the lobby layout with the joined players
        updateLobbyLayout();
    }

    private void updateLobbyLayout() {
        lobbyLayout.getChildren().clear();
        for (String playerName : joinedPlayers) {
            Label playerNameLabel = new Label("Player Name: " + playerName);
            playerNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");

            lobbyLayout.getChildren().add(playerNameLabel);
        }
    }

    public void show() {
        stage.show();
    }
}
