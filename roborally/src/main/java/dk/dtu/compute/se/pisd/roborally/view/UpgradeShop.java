package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UpgradeShop {
    private CardFieldView[] cardsToBuy;
    private CardFieldView[] playerCards;
    private GameController controller;
    private Board board;

    private Stage stage;
    private VBox shop;
    private VBox playerInfo;
    private Button nextPlayer;
    private FlowPane buyCards;
    private Label messageLabel;
    private int playerOrder;
    private Player currentPlayer;

    private ArrayList<CommandCard> deck;
    private ArrayList<CommandCard> discarded;
    private ArrayList<CommandCard> out;

    private void createWindow(){
        HBox window = new HBox();
        shop = new VBox();
        playerInfo = new VBox();
        window.getChildren().add(shop);
        window.getChildren().add(playerInfo);
        shop.setPrefSize(600, 600);
        playerInfo.setPrefSize(300, 600);
        shop.setAlignment(Pos.TOP_CENTER);
        playerInfo.setAlignment(Pos.TOP_CENTER);
        shop.setStyle("-fx-border-color: #6969d3");
        shop.setSpacing(10);
        playerInfo.setStyle("-fx-border-color: #6969d3");
        //shop.setSpacing(50);
        playerInfo.setSpacing(10);
        shop.setPadding(new Insets(30, 50, 30, 50));
        playerInfo.setPadding(new Insets(30, 50, 30, 50));
        Label label = new Label("Upgrade Shop");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 48; -fx-font-weight: bold");
        messageLabel = new Label("");
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        nextPlayer = new Button("Start Shopping!");
        nextPlayer.setOnAction(e -> switchToNextPlayer());
        nextPlayer.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        VBox cardHolder = new VBox();
        cardHolder.setAlignment(Pos.CENTER);
        buyCards = new FlowPane();
        buyCards.setPadding(new Insets(100, 10, 100, 10));
        buyCards.setHgap(60);
        buyCards.setVgap(100);
        buyCards.setAlignment(Pos.TOP_CENTER);
        cardsToBuy = new CardFieldView[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            CommandCardField cardField = getNextCardField();
            if (cardField != null) {
                VBox card = new VBox();
                StackPane cardStackPane = new StackPane();

                card.getChildren().add(cardStackPane);
                cardsToBuy[i] = new CardFieldView(controller, cardField);
                cardStackPane.getChildren().add(cardsToBuy[i]);
                Button button = new Button("Buy");
                button.setPrefSize(65, 15);
                button.setStyle("-fx-border-color: #6969d3; -fx-font-weight: bold; -fx-font-size: 8;");
                final CardFieldView cardView = cardsToBuy[i];
                button.setOnAction(e -> chooseCardAt(cardView));
                card.getChildren().add(button);
                VBox priceAllign = new VBox();
                cardStackPane.getChildren().add(priceAllign);
                priceAllign.setAlignment(Pos.TOP_RIGHT);
                priceAllign.setPadding(new Insets(4, 4, 4, 4));
                VBox priceBox = new VBox();
                priceBox.setMaxSize(20, 20);
                priceBox.setStyle("-fx-border-color: #6969d3; -fx-border-radius: 200");
                priceBox.setAlignment(Pos.CENTER);
                Label price = new Label();
                price.maxWidth(1);
                price.setText(getPrice(cardsToBuy[i].getField().getCard().command) + "");
                cardsToBuy[i].getLabel().setStyle(cardsToBuy[i].getLabel().getStyle() + ";-fx-font-size: 6");
                price.setStyle("-fx-text-fill: #6969d3; -fx-font-weight: bold; -fx-font-size: 12; -fx-border-width: 2");
                priceBox.getChildren().add(price);
                priceAllign.getChildren().add(priceBox);
                card.setScaleX(2);
                card.setScaleY(2);
                buyCards.getChildren().add(card);
            }
        }
        cardHolder.getChildren().add(buyCards);
        shop.getChildren().add(label);
        shop.getChildren().add(messageLabel);
        shop.getChildren().add(cardHolder);
        shop.getChildren().add(nextPlayer);
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


    private void showForPlayer(Player player){
        currentPlayer = player;
        playerInfo.getChildren().clear();
        Label name = new Label(player.getName());
        name.setStyle("-fx-font-size: 32; -fx-font-weight: bold");
        playerInfo.getChildren().add(name);
        Label currentCardsLabel = new Label("Your Current Cards");
        currentCardsLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
        FlowPane cardHolder = new FlowPane();
        CommandCardField[] cards = player.getUpgradeCards();
        playerCards = new CardFieldView[6];
        for(int i = 0; i < playerCards.length; i++){
            if(i < cards.length && cards[i] != null){
                VBox card = new VBox();
                playerCards[i] = new CardFieldView(controller, cards[i]);
                card.getChildren().add(playerCards[i]);
                Button button = new Button("Discard");
                button.setPrefSize(65, 20);
                button.setStyle("-fx-border-color: #6969d3;");
                final CardFieldView cardFieldView =  playerCards[i];
                button.setOnAction(e -> discardCardsAt(cardFieldView));
                card.getChildren().add(button);
                cardHolder.getChildren().add(card);
            }
        }
        HBox energyCubes = new HBox();
        energyCubes.setAlignment(Pos.CENTER_RIGHT);
        energyCubes.setSpacing(20);
        ImageView energyCube = new ImageView(ImageLoader.get().energyCube);
        energyCube.setFitWidth(50);
        energyCube.setFitHeight(50);
        Label x = new Label("   X ");
        x.setScaleX(2.8);
        x.setScaleY(2.2);
        x.setLayoutY(-50);
        Label amount = new Label("0");
        amount.setScaleX(3.5);
        amount.setScaleY(3);
        amount.setLayoutY(-50);
        energyCubes.getChildren().add(amount);
        energyCubes.getChildren().add(x);
        energyCubes.getChildren().add(energyCube);

        playerInfo.getChildren().add(energyCubes);
        playerInfo.getChildren().add(currentCardsLabel);
        playerInfo.getChildren().add(cardHolder);
    }
    private void switchToNextPlayer(){
        playerOrder += 1;
        if(playerOrder > board.getPlayersNumber()) {
            finishUpgradePhase();
            return;
        }
        if(playerOrder > 0){
            nextPlayer.setText("Next Player");
            Player player = getPlayerPriority().get(playerOrder-1);
            showForPlayer(player);
        }
        if(playerOrder ==  board.getPlayersNumber()) nextPlayer.setText("Finish Upgrading");
    }

    public void openShop(Board board, GameController controller){
        this.board = board;
        this.controller = controller;
        createWindow();
        playerOrder = 0;
        switchToNextPlayer();

    }
    private ArrayList<CommandCard> getDeck(){
        if(deck == null){
            deck = new ArrayList<>();
            discarded = new ArrayList<>();
            out = new ArrayList<>();
            controller.fillUpgradeCardDeck(deck);
            shuffle(deck);
        }
        else if(deck.size() == 0){
            for(int i = 0; i < discarded.size(); i++){
                deck.add(discarded.get(i));
            }
            shuffle(deck);
            if(deck.size() == 0){
                System.out.println("No cards in deck or discarded deck");
            }
        }
        return deck;
    }
    private void shuffle(ArrayList<CommandCard> shuffle){
        Random rnd = new Random();
        int switcher;
        for(int i = 0; i < shuffle.size(); i++){
            switcher = rnd.nextInt(0, shuffle.size());
            CommandCard temp = shuffle.get(i);
            shuffle.set(i, shuffle.get(switcher));
            shuffle.set(switcher, temp);
        }
    }
    private CommandCardField getNextCardField(){
        ArrayList<CommandCard> myDeck = getDeck();
        if(myDeck.size() == 0) {
            System.out.println("No cards in upgrade deck");
            return null;
        }
        CommandCard command = myDeck.remove(myDeck.size()-1);
        CommandCard card = myDeck.remove(myDeck.size()-1);
        out.add(card);
        CommandCardField cardField = new CommandCardField(null);
        cardField.setCard(card);
        return cardField;
    }
    private void chooseCardAt(CardFieldView cardFieldView){
        if(nextPlayer.getText().equals("Start Shopping!")) return;
        int freeIndex = -1;
        for(int i = 0; i < playerCards.length; i++){
            if(playerCards[i].getField().getCard() == null){
                freeIndex = i;
                break;
            }
        }
        if(freeIndex != -1){
            if(getPermanent(cardFieldView.getField().getCard().command)){
                int amountOfSame = 0;
                for(int i = 0; i < playerCards.length; i++){
                    if(playerCards[i].getField().getCard() != null){
                        if(getPermanent(cardsToBuy[i].getField().getCard().command)) amountOfSame++;
                    }
                }
                if(amountOfSame > 2) messageLabel.setText("You already have three permanent cards, you can only have three");
                else{
                    if(currentPlayer.getEnergyCubes() >= getPrice(cardFieldView.getField().getCard().command)){
                        playerCards[freeIndex].getField().setCard(cardFieldView.getField().getCard());
                        switchToNextPlayer();
                    }
                    else messageLabel.setText("You cannot afford this item");
                }
            }
            else{
                int amountOfSame = 0;
                for(int i = 0; i < playerCards.length; i++){
                    if(playerCards[i].getField().getCard() != null){
                        if(!getPermanent(playerCards[i].getField().getCard().command)) amountOfSame++;
                    }
                }
                if(amountOfSame > 2) messageLabel.setText("You already have three temporary cards, you can only have three");
                else {
                    if(currentPlayer.getEnergyCubes() >= getPrice(cardFieldView.getField().getCard().command)){
                        playerCards[freeIndex].getField().setCard(cardFieldView.getField().getCard());
                        switchToNextPlayer();
                    }
                    else messageLabel.setText("You cannot afford this item");
                }
            }
        }
        else{
            messageLabel.setText("You dont have space for any more cards, you must first discard one.");
        }
    }
    private void discardCardsAt(CardFieldView cardFieldView){
        cardFieldView.getField().setCard(null);
        playerOrder--;
        switchToNextPlayer();
    }

    private List<Player> getPlayerPriority(){
        List<Player> playerOrder = controller.findPlayerSequence(board.getAntenna());
        return playerOrder;
    }
    private boolean getPermanent(Command command){
        switch(command){

            default: return true;
        }
    }
    private int getPrice(Command command){
        switch(command){

            default: return 1;
        }
    }


}
