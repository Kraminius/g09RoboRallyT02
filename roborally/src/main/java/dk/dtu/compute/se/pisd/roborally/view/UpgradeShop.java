package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.GameClient;
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
    private VBox cardHolder;
    private Button finishButton;

    private Button close;
    private FlowPane buyCards;
    private Label messageLabel;
    private int playerOrder;
    private Player currentPlayer;

    private ArrayList<CommandCard> deck;
    private ArrayList<CommandCard> discarded;
    private ArrayList<CommandCard> out;
    private ArrayList<CommandCardField> loadedCards;


    private AntennaHandler antennaHandler = new AntennaHandler();


    /**
     * @author Tobias - s224271@dtu.dk
     * Asserts the values of the parameters to the UpgradeShop class so it can get the values it needs from board and gamecontroller.
     * Afterward it opens the shop with a window and gets ready to switch to next players.
     * @param board the board we are opening the shop for.
     * @param controller the controller we are using currently.
     */
    public UpgradeShop(GameController controller, Board board){
        this.controller = controller;
        this.board = board;
    }

    public void openShopFor(int player){
        createWindow();
        switchToPlayer(player);
        currentPlayer = board.getPlayer(player);
        stage.showAndWait();
    }


    private boolean checkIfLoad(){
        if(loadedCards.size() != 0) return true;
        else return false;
    }
    public CommandCardField[] getCards(int amount){
        if(checkIfLoad()){
            CommandCardField[] cards = new CommandCardField[loadedCards.size()];
            for(int i = 0; i < loadedCards.size(); i++){
                cards[i] = loadedCards.get(i);
            }
            return cards;
        }
        else{
            CommandCardField[] cards = new CommandCardField[amount];
            for(int i = 0; i < cards.length; i++){
                cards[i] = getNextCardField();
            }
            return cards;
        }

    }

    public void setCardsForRound(CommandCardField[] cards){
        cardsToBuy = new CardFieldView[cards.length];
        for(int i = 0; i < cards.length; i++){
            cardsToBuy[i] = new CardFieldView(controller, cards[i]);
        }
    }
    //region javaFX
    /**
     * @author Tobias - s224271@dtu.dk
     *  We create an upgrade shop window that pops up on screen.
     *  We also make it so that you cannot close the window by normal means and must use the buttons within along with not being able to edit other windows of the game.
     */
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
        finishButton = new Button("Exit Upgrade Shop");
        finishButton.setOnAction(e -> {stage.close();});
        finishButton.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        close = new Button("Close");
        close.setOnAction(e -> {
            try {
                closeShop();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        close.setStyle("-fx-font-size: 13; -fx-font-weight: bold");
        cardHolder = new VBox();
        cardHolder.setAlignment(Pos.CENTER);

        buyCards = new FlowPane();
        buyCards.setPadding(new Insets(100, 10, 100, 10));
        buyCards.setHgap(80);
        buyCards.setVgap(130);
        buyCards.setAlignment(Pos.TOP_CENTER);

        updateShopCards();
        shop.getChildren().add(label);
        shop.getChildren().add(messageLabel);
        shop.getChildren().add(cardHolder);
        shop.getChildren().add(finishButton);
        Scene scene = new Scene(window, 900, 800);
        stage = new Stage();
        stage.setTitle("Uprade Shop");
        stage.setScene(scene);
        stage.setX(300);
        stage.setY(5);
        stage.initModality(Modality.APPLICATION_MODAL); //Make other window useless.
        stage.setOnCloseRequest(Event::consume);
    }
    /**
     * @author Tobias - s224271@dtu.dk
     *  We update the cards that are in the shop, by showing the ones that have not been sold yet.
     */
    private void updateShopCards(){
        buyCards.getChildren().clear();
        cardHolder.getChildren().clear();
        for (int i = 0; i < cardsToBuy.length; i++) {
            CommandCardField cardField = cardsToBuy[i].getField();
            if (cardField.getCard() != null) {
                VBox card = new VBox();
                StackPane cardStackPane = new StackPane();
                card.getChildren().add(cardStackPane);
                cardsToBuy[i] = new CardFieldView(controller, cardField);
                cardStackPane.getChildren().add(cardsToBuy[i]);
                Button button = new Button("Buy");
                button.setPrefSize(65, 15);
                button.setStyle("-fx-border-color: #6969d3; -fx-font-weight: bold; -fx-font-size: 8;");
                final CardFieldView cardView = cardsToBuy[i];
                button.setOnAction(e -> buyCard(cardView));
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
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * We show the name, energy cubes and upgrade cards of a player so they can make decisions off of that along with a button to discard cards.
     */
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
                button.setOnAction(e -> discardCard(cardFieldView.getField()));
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
        Label amount = new Label("" + currentPlayer.getEnergyCubes());
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


    //endregion

    /**
     * @author Tobias - s224271@dtu.dk
     * finish upgrade phase first puts the remaining cards into the discarded upgrade cards pile.
     * Afterward it closes the page.
     */
    public void finishUpgradePhase(CommandCard[] cardsToDiscard){
        if(cardsToDiscard == null) return;
        if(cardsToDiscard.length < 1) return;
        for(int i = 0; i < cardsToDiscard.length; i++){
            discarded.add(cardsToDiscard[i]);
        }
    }

    private void closeShop() throws Exception {

        for(int i = 0; i < cardsToBuy.length; i++){
            if(cardsToBuy[i].getField().getCard() != null){
                discarded.add(cardsToBuy[i].getField().getCard());
            }
        }

        Command[] temp = new Command[cardsToBuy.length];

        for (int i = 0; i < cardsToBuy.length; i++) {
            temp[i] = cardsToBuy[i].getField().getCard().command;
        }


        stage.close();
    }

    /**
     * @author Tobias - s224271@dtu.dk
     * adds one to the playerOrder wich determines who's turn it is to buy.
     * If all people have already had their turn the playerOrder gets above the player number and closes.
     * As long as it isn't the order is above 0 we set a buttons text to display "Next Player", as this button changes it look. The button also calls this method.
     * we update the shop cards, by showing or removing cards that should be shown or be discarded.
     * The showForPlayer shows the players upgrade cards and how many energy cubes they have.
     * If the playerOrder is equal to the amount of players, they are the last player, and the button changes its text to display "finish upgrading", as there are no more players after.
     */
    private void switchToPlayer(int playerNum){
        finishButton.setText("Finish Upgrade Phase");
        Player player = getPlayerPriority().get(playerNum);
        updateShopCards();
        showForPlayer(player);
    }

    /**
     * @author Tobias - s224271@dtu.dk
     * A method to get the deck of upgrade cards.
     * if there is no deck at all, we initialise all decks and fills a deck with the GameController
     * if there is a deck, but it is empty we check the discarded pile and fills it into the normal deck and shuffle.
     * if for some reason there is nothing in the deck or in the discard pile, but it has been initialised we create a new deck. This should not happen.
     *
     * @return the deck of cards
     */
    public ArrayList<CommandCard> getDeck(){
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
                deck = null;
                deck = getDeck();
            }
        }
        return deck;
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * a simple shuffle for the cards
     * @param shuffle the deck that needs shuffling.
     */
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
    /**
     * @author Tobias - s224271@dtu.dk
     * A method to get the next upgrade card in the deck, we make it a CommandCardField with no player attached and then return it.
     *
     * @return the next upgrade card in the deck
     */
    private CommandCardField getNextCardField(){
        ArrayList<CommandCard> myDeck = getDeck();
        if(myDeck.size() == 0) {
            System.out.println("No cards in upgrade deck");
            return null;
        }
        CommandCard card = myDeck.remove(myDeck.size()-1);
        CommandCardField cardField = new CommandCardField(null);
        cardField.setCard(card);
        return cardField;
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * discards a specific card, by nulling it from the view and adding it to the discarded pile.
     * @param cardField the card that needs to be discarded.
     */
    public void discardCard(CommandCardField cardField){
        CommandCard card = cardField.getCard();
        discarded.add(card);
        cardField.setCard(null);
        switchToPlayer(currentPlayer.getId());
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * we get the order the players should buyy from the gamecontroller using the antenna.
     * @return returns a list of players which is the order
     */
    private List<Player> getPlayerPriority(){
        List<Player> playerOrder = antennaHandler.findPlayerSequence(board.getAntenna(), board);
        return playerOrder;
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * We check if an upgrade card is a permanent upgrade card.
     * We use the UpgraceCardInfo class for this info.
     * @param command the command we want checked.
     * @return if it is permanent or not
     */
    private boolean getPermanent(Command command){
        return UpgradeCardInfo.getPermanent(command);
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * We recieve the price of an upgrade card.
     * We use the UpgraceCardInfo class for this info.
     * @param command the command we want checked.
     * @return the price
     */
    private int getPrice(Command command){
        return UpgradeCardInfo.getPrice(command);
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * We check wether or not a player can buy a card. If they can, we add it to their upgrade cards, if not we send a message, saying they cant.
     * If they buy something the turn moves to the next player, if not they must press next player themselves.
     * We check if they have the money, we check if they have any space for more cards at all.
     * We check if they have 3 or more of either permanent or not upgrade cards as you can only have the three.
     * At last we add it to the player and the out pile and updates the view.
     * @param cardFieldView the card that the player has clicked buy at.
     */
    private void buyCard(CardFieldView cardFieldView){
        int freeIndex = -1;
        for(int i = 0; i < playerCards.length; i++){
            if(playerCards[i].getField().getCard() == null){
                freeIndex = i;
                break;
            }
        }
        if(freeIndex != -1){
            if(currentPlayer.getEnergyCubes() >= getPrice(cardFieldView.getField().getCard().command)){
                int amountOfSame = 0;
                if(getPermanent(cardFieldView.getField().getCard().command)){
                    for(int i = 0; i < playerCards.length; i++){
                        if(playerCards[i].getField().getCard() != null){
                            if(getPermanent(playerCards[i].getField().getCard().command)) amountOfSame++;
                        }
                    }
                    if(amountOfSame > 2) {
                        messageLabel.setText("You already have three permanent cards, you can only have three");
                        return;
                    }
                }
                else {
                    for (int i = 0; i < playerCards.length; i++) {
                        if (playerCards[i].getField().getCard() != null) {
                            if (!getPermanent(playerCards[i].getField().getCard().command)) amountOfSame++;
                        }
                    }
                    if (amountOfSame > 2){
                        messageLabel.setText("You already have three temporary cards, you can only have three");
                        return;
                    }
                }
                playerCards[freeIndex].getField().setCard(cardFieldView.getField().getCard());
                currentPlayer.setEnergyCubes(currentPlayer.getEnergyCubes() - getPrice(cardFieldView.getField().getCard().command));
                currentPlayer.updateUpgradeCardView();
                out.add(cardFieldView.getField().getCard());
                removeInstanceFromCardsToBuy(cardFieldView);
                cardFieldView.getField().setCard(null);
                updatePowerUps(currentPlayer);
                stage.close();
            }
            else messageLabel.setText("You cannot afford this item");
        }
        else{
            messageLabel.setText("You dont have space for any more cards, you must first discard one.");
        }
    }


    public void updatePowerUps(Player player){
        CommandCardField[] cards = player.getUpgradeCards();
        for(int i = 0; i < cards.length; i++){

            if(cards[i].getCard() != null){
                Command currentCommand = cards[i].getCard().command;
                boolean[] boughtUpgrade = {true, false};

                switch (currentCommand){
                    case DEFRAG_GIZMO_PUPG -> player.getPowerUps().setDefragGizmo(boughtUpgrade);
                    case RAMMING_GEAR_PUPG -> player.getPowerUps().setRammingGear(true);
                    case DOUBLE_BARREL_LASER_PUGB -> player.getPowerUps().setBarrelLaser(true);
                }

            }
        }
    }

    public void removeInstanceFromCardsToBuy(CardFieldView commandCardField){
        int newSize = cardsToBuy.length - 1;
        CardFieldView[] newCardsToBuy = new CardFieldView[newSize];


        for (int i = 0, j = 0; i < cardsToBuy.length; i++) {
            if (cardsToBuy[i] != commandCardField) {
                newCardsToBuy[j++] = cardsToBuy[i];
            }
        }
        cardsToBuy = newCardsToBuy;
    }

    public void setDeck(ArrayList<CommandCard> deck) {
        this.deck = deck;
    }

    public ArrayList<CommandCard> getDiscarded() {
        return discarded;
    }

    public void setDiscarded(ArrayList<CommandCard> discarded) {
        this.discarded = discarded;
    }

    public ArrayList<CommandCard> getOut() {
        return out;
    }

    public void setOut(ArrayList<CommandCard> out) {
        this.out = out;
    }
    public void setLoadedCards(ArrayList<CommandCardField> cards){
        loadedCards = cards;
    }
    public ArrayList<CommandCardField> getLoadedCards(){
        return loadedCards;
    }

    public CardFieldView[] getCardsToBuy() {
        return cardsToBuy;
    }

    public void setCardsToBuy(CardFieldView[] cardsToBuy) {
        this.cardsToBuy = cardsToBuy;
    }
}

