package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UpgradeShop {
    private CardFieldView[] cardsToBuy;
    private GameController controller;
    private Board board;

    private Stage stage;
    private VBox window;
    private Button nextPlayer;
    private Player player;
    private GridPane cards;
    private GridPane chooseCards;


    private void createWindow(){
        window = new VBox();
        window.setAlignment(Pos.TOP_CENTER);
        window.setSpacing(10);
        window.setPadding(new Insets(30, 50, 30, 50));
        Label label = new Label("Upgrade Shop");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold");
        nextPlayer = new Button("Next Player");
        nextPlayer.setOnAction(e -> switchToNextPlayer());
        nextPlayer.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        VBox cardHolder = new VBox();
        cards = new GridPane();
        chooseCards = new GridPane();
        for(int i = 0; i < board.getPlayersNumber(); i++){
            Button button = new Button();
            final int at = i;
            button.setOnAction(e -> chooseCardAt(at));
            chooseCards.add(button, i, 0);
        }
        Button closeWindow = new Button("Finish Upgrade Phase");
        closeWindow.setOnAction(e -> finishUpgradePhase());
        cardHolder.getChildren().add(cards);
        window.getChildren().add(label);
        window.getChildren().add(nextPlayer);
        Scene scene = new Scene(window, 900, 600);
        stage = new Stage();
        stage.setTitle("Uprade Shop");
        stage.setScene(scene);
        stage.setX(500);
        stage.setY(100);
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
        stage.showAndWait();
    }
    private void finishUpgradePhase(){
        stage.close();
    }
    private CommandCardField getNextCardField(){

        return null;
    }
    private void switchToNextPlayer(){

    }

    public void openShop(Board board, GameController controller){
        this.board = board;
        this.controller = controller;
        createWindow();
    }

    private void fillCards(){
        cards = new GridPane();
        cardsToBuy = new CardFieldView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardsToBuy[i] = new CardFieldView(controller, cardField);
                cards.add(cardsToBuy[i], i, 0);
            }
        }
    }
    private void setCardAt(int i, CommandCardField cardField){
        cardsToBuy[i] = new CardFieldView(controller, cardField);
        cards.add(cardsToBuy[i], i, 0);
    }
    private void chooseCardAt(int i){

    }

    private Player getNextPlayer(){


        return null;
    }

    private Player[] getPlayerPriority(){

        return null;
    }


}
