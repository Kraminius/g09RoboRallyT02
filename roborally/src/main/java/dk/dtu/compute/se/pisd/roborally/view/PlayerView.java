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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.GameClient;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class PlayerView extends Tab implements ViewObserver {

    private Player player;

    private VBox top;
    private StackPane stackPane;

    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;

    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;
    private CardFieldView[] upgradeCardView;

    private HBox buttonPanel;
    private VBox bottomBar;
    private HBox chatWindow;

    private Button finishButton;
    private Button executeButton;
    private Button stepButton;
    private Button openShopButton;
    private VBox playerInteractionPanel;
    private VBox waitingForPlayersPanel;
    private VBox valuesWindow;
    private HBox energyCubes;
    private GridPane upgradeCards;

    private GameController gameController;
    /**
     * @author Tobias Gørlyk - s224271@dtu.dk
     * Sets the colors for the card views.
     *      This method applies a border with a specific color to each card view in the programCardViews,
     *      cardViews, and upgradeCardView lists.
     */
    private void setColors() {
        Border bordor = new Border(new BorderStroke(Color.valueOf(player.getColor()), BorderStrokeStyle.SOLID, null, new BorderWidths(2)));
        for(CardFieldView view : programCardViews){
            view.setBorder(bordor);
        }
        for(CardFieldView view : cardViews){
            view.setBorder(bordor);
        }
        for(CardFieldView view : upgradeCardView){
            view.setBorder(bordor);
        }
    }

    /**
     * @author Ekkart Kindler, Tobias Gørlyk - s224271@dtu.dk, Kenneth Kaiser, s221064@student.dtu.dk
     * Creates a PlayerView object for a specific player.
     * @param gameController The game controller object responsible for controlling the game.
     * @param player The player object associated with this view.
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player) {
        super(player.getName());
        player.setPlayerView(this);
        this.setStyle("-fx-text-base-color: " + player.getColor() + ";");

        top = new VBox();
        stackPane = new StackPane();
        this.setContent(stackPane);

        this.gameController = gameController;
        this.player = player;

        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        // XXX  the following buttons should actually not be on the tabs of the individual
        //      players, but on the PlayersView (view for all players). This should be
        //      refactored.

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction( e -> gameController.finishProgrammingPhase());

        executeButton = new Button("Execute Program");
        executeButton.setOnAction( e-> gameController.executePrograms());

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction( e-> gameController.executeStep());

        openShopButton = new Button("Open Upgrade Shop");
        openShopButton.setOnAction( e-> {
            try {
                gameController.openUpgradeShop();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        VBox buttonsInButtonPanel = new VBox(finishButton, executeButton, stepButton, openShopButton);
        finishButton.setPrefWidth(160);
        executeButton.setPrefWidth(160);
        stepButton.setPrefWidth(160);
        openShopButton.setPrefWidth(160);

        ImageView waitingImage = new ImageView();
        waitingImage.setImage(ImageLoader.get().waiting);
        waitingImage.setFitWidth(110);
        waitingImage.setFitHeight(90);
        waitingForPlayersPanel = new VBox(waitingImage);
        waitingForPlayersPanel.setAlignment(Pos.CENTER);
        buttonPanel = new HBox(buttonsInButtonPanel, waitingForPlayersPanel);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        // programPane.add(buttonPanel, Player.NO_REGISTERS, 0); done in update now

        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);

        bottomBar = new VBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setMinWidth(450);
        valuesWindow = new VBox();
        valuesWindow.setPrefSize(200, 250);
        valuesWindow.setMaxWidth(200);
        valuesWindow.setSpacing(10);
        energyCubes = new HBox();
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
        player.setEnergyCubeLabel(amount);
        valuesWindow.getChildren().add(energyCubes);
        VBox energyCubeWindow = new VBox();
        energyCubeWindow.setAlignment(Pos.CENTER);
        energyCubeWindow.setSpacing(3);
        upgradeCards = new GridPane();
        upgradeCards.setVgap(2.0);
        upgradeCards.setHgap(2.0);
        upgradeCardView = new CardFieldView[Player.NO_UPGRADE_CARDS];
        HBox useButtons = new HBox();
        useButtons.setSpacing(3);
        for (int i = 0; i < Player.NO_UPGRADE_CARDS; i++) {
            CommandCardField cardField = player.getUpgradeCard(i);
            if (cardField != null) {
                StackPane upgradeCard = new StackPane();
                Button button = new Button("Use");
                final int at = i;
                button.setOnAction(e -> gameController.executeUpgradeCommand(player, upgradeCardView[at].getField()));
                button.setStyle("-fx-border-color: #6969d3");
                button.setPrefWidth(65);
                useButtons.getChildren().add(button);
                Button showCard = new Button("");
                upgradeCardView[i] = new CardFieldView(gameController, cardField);
                upgradeCard.setPrefSize(65, 95);
                showCard.setPrefSize(65, 95);
                showCard.setOpacity(0);
                showCard.setOnAction(e ->{
                    String name = cardField.getCard().getName();
                    String text = cardField.getCard().getDescription();
                    Option option = new Option(name, text, 130, 190);
                    option.getOKPressed("");
                });
                upgradeCard.getChildren().add(upgradeCardView[i]);
                upgradeCard.getChildren().add(showCard);
                upgradeCards.add(upgradeCard, i, 0);
            }
        }



        Label upgradeCardsLabel = new Label("Upgrade Cards");
        upgradeCardsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20");
        Label upgradeCardsTempOrPerm = new Label("Permanent Cards                                    Tempoary Cards");
        upgradeCardsTempOrPerm.setStyle("-fx-font-weight: bold");
        energyCubeWindow.getChildren().add(upgradeCardsLabel);
        energyCubeWindow.getChildren().add(upgradeCardsTempOrPerm);
        energyCubeWindow.getChildren().add(useButtons);
        energyCubeWindow.getChildren().add(upgradeCards);
        valuesWindow.getChildren().add(energyCubeWindow);
        bottomBar.getChildren().add(valuesWindow);
        HBox sideBySide = new HBox();
        sideBySide.getChildren().add(top);
        sideBySide.getChildren().add(bottomBar);
        stackPane.getChildren().add(sideBySide);
        chatWindow = new HBox();
        chatWindow.setPadding(new Insets(10, 10, 10, 10));
        sideBySide.getChildren().add(chatWindow);

        player.updateCubeLabel();



        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);


        if (player.board != null) {
            player.board.attach(this);
            update(player.board);
        }
        setColors();
    }

    /**
     * @author Ekkart Kindler, //TODO TOBY, Freja Egelund Grønnemose, s224286@dtu.dk
     * Updates the view based on the changes in the subject.
     * @param subject The subject triggering the update.
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == player.board) {
            showOrHideWaiting(player.board.getIsWaiting());
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                CardFieldView cardFieldView = programCardViews[i];
                if (cardFieldView != null) {
                    if (player.board.getPhase() == Phase.PROGRAMMING ) {
                        cardFieldView.setBackground(CardFieldView.BG_default);
                    } else {
                        if (i < player.board.getStep()) {
                            cardFieldView.setBackground(CardFieldView.BG_DONE);
                        } else if (i == player.board.getStep()) {
                            if (player.board.getCurrentPlayer() == player) {
                                cardFieldView.setBackground(CardFieldView.BG_ACTIVE);
                            } else if (player.board.getPlayerNumber(player.board.getCurrentPlayer()) > player.board.getPlayerNumber(player)) {
                                cardFieldView.setBackground(CardFieldView.BG_DONE);
                            } else {
                                cardFieldView.setBackground(CardFieldView.BG_default);
                            }
                        } else {
                            cardFieldView.setBackground(CardFieldView.BG_default);
                        }
                    }
                }
            }

            if (player.board.getPhase() != Phase.PLAYER_INTERACTION) {
                if (!programPane.getChildren().contains(buttonPanel)) {
                    programPane.getChildren().remove(playerInteractionPanel);
                    programPane.add(buttonPanel, Player.NO_REGISTERS, 0);
                }
                System.out.println(player.board.getPhase());
                switch (player.board.getPhase()) {
                    case INITIALISATION:
                        finishButton.setDisable(true);
                        // XXX just to make sure that there is a way for the player to get
                        //     from the initialization phase to the programming phase somehow!
                        executeButton.setDisable(false);
                        stepButton.setDisable(true);
                        openShopButton.setDisable(true);
                        break;

                    case PROGRAMMING:
                        finishButton.setDisable(false);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        openShopButton.setDisable(true);
                        break;

                    case ACTIVATION:
                        finishButton.setDisable(true);
                        executeButton.setDisable(false);
                        stepButton.setDisable(false);
                        openShopButton.setDisable(true);
                        break;
                    case UPGRADE:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        openShopButton.setDisable(false);
                        break;
                    default:
                        finishButton.setDisable(true);
                        executeButton.setDisable(true);
                        stepButton.setDisable(true);
                        openShopButton.setDisable(true);

                }
            } else {
                if (!programPane.getChildren().contains(playerInteractionPanel)) {
                    programPane.getChildren().remove(buttonPanel);
                    programPane.add(playerInteractionPanel, Player.NO_REGISTERS, 0);
                }
                playerInteractionPanel.getChildren().clear();
                System.out.println(player.board.getCurrentPlayer().getName() + " & " + player.getName());
                if (player.board.getCurrentPlayer() == player) {
                    if(player.getRespawnStatus()){
                        Set<Heading> headings = EnumSet.allOf(Heading.class);
                        for(Heading heading : headings){
                            Button optionButton = new Button(heading.name());
                            optionButton.setOnAction( e -> gameController.respawnPlayer(player, heading));
                            optionButton.setDisable(false);
                            playerInteractionPanel.getChildren().add(optionButton);
                        }
                    }
                    /*Access the interactive command card and creates buttons from the options the command has */
                    CommandCard card = player.getProgramField(gameController.board.getStep()).getCard();
                    List<Command> options = card.command.getOptions();
                    if(GameClient.getPlayerNumber() == player.getId()-1){
                        for (int i = 0; i < options.size(); i++){
                            final Command option = options.get(i);
                            Button optionButton = new Button(options.get(i).displayName);
                            optionButton.setOnAction( e -> interactiveButtonFunctionality(option, player.getId()));
                            optionButton.setDisable(false);
                            playerInteractionPanel.getChildren().add(optionButton);
                        }
                    }
                    else {
                        System.out.println("we will wait for you bby");
                        GameClient.startWaitingForInteractive();
                    }

                }
            }
        }
    }
    /**
     * @author Kenneth Kaiser, s221064@student.dtu.dk
     * Executes the selected command option and continues the game.
     * Also updates the player's heading and notifies the GameClient.
     * @param option       The selected command option to execute.
     * @param playerNumber The player's number.
     */
    public void interactiveButtonFunctionality(Command option, int playerNumber){

        gameController.executeCommandOptionAndContinue(option);

        Heading heading = gameController.board.getPlayer(playerNumber-1).getHeading();

        System.out.println("Our new heading: " + heading);

        try {
            GameClient.setPlayerHeadingInteractive(heading, playerNumber-1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Shows or hides the waiting panel based on the specified boolean value.
     * @param show {@code true} to show the waiting panel, {@code false} to hide it.
     */
    public void showOrHideWaiting(boolean show){
        if(show) waitingForPlayersPanel.setOpacity(1);
        else waitingForPlayersPanel.setOpacity(0);
    }
    /**
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Adds the chat window to the player view if it is not already present.
     */
    public void addChatWindow(){
        VBox chatWindow = RoboRally.getInstance().getChatView();
        if(!this.chatWindow.getChildren().contains(chatWindow)){
            this.chatWindow.getChildren().add(chatWindow);
        }
    }
    /**
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Removes the chat view from the player view if it is currently present.
     */
    public void removeChatView(){
        VBox chatWindow = RoboRally.getInstance().getChatView();
        if(this.chatWindow.getChildren().contains(chatWindow)){
            this.chatWindow.getChildren().remove(chatWindow);
        }
    }

}
