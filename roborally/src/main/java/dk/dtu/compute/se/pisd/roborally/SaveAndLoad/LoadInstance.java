package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.CommandCard;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.view.UpgradeShop;

import java.util.ArrayList;

public class LoadInstance {
    public static GameController load(AppController appController, Load load){
        Board board = createBoard(appController, load);
        GameController gameController = initGameController(appController, board);
        initUpgradeShopAndCards(gameController, load);
        loadPlayer(load, gameController);
        loadCurrentState(load, board);






        return gameController;
    }

    private static Board createBoard(AppController appController, Load load){
        Board board;
        try{
            board = new Board(load.getBoard()); //Creates new board by loading the name of the board that was loaded last time.
            return board;
        }
        catch (Exception e){
            System.out.println("Board not recognised: " + load.getBoard()); //If player dont have that board loaded on their pc it cannot load the game.
            appController.stopGame(); //Stops the game as this cannot be loaded.
            return null; //Should not reach this point
        }
    }
    private static GameController initGameController(AppController appController, Board board){
        GameController gameController = new GameController(board); //Creates new gamecontroller with the new board.
        appController.roboRally.createBoardView(gameController); //register the new gamecontroller in the appcontroller
        return gameController;
    }
    private static void initUpgradeShopAndCards(GameController gameController, Load load){
        gameController.upgradeShop = new UpgradeShop();
        ArrayList<CommandCard> deck = new ArrayList<>();
        ArrayList<CommandCard> out = new ArrayList<>();
        ArrayList<CommandCard> discarded = new ArrayList<>();
        Command[] arrDeck = load.getUpgradeCardsDeck();
        Command[] arrOut = load.getUpgradeOutDeck();
        Command[] arrDiscard = load.getUpgradeDiscardDeck();
        for(int i = 0; i < arrDeck.length; i++){
            deck.add(new CommandCard(arrDeck[i])); //Creates each card for the deck
        }
        for(int i = 0; i < arrOut.length; i++){
            out.add(new CommandCard(arrOut[i])); //Creates each card for the out
        }
        for(int i = 0; i < arrDiscard.length; i++){
            discarded.add(new CommandCard(arrDiscard[i])); //Creates each card that has been discarded
        }
        gameController.upgradeShop.setDeck(deck); //Sets the decks in the upgrade shop to be these decks.
        gameController.upgradeShop.setOut(out);
        gameController.upgradeShop.setDiscarded(discarded);
    }

    private static void loadPlayer(Load load, GameController gameController){
        Board board = gameController.board;
        for (int i = 0; i < load.getPlayerAmount(); i++) {
            Player player = new Player(board, load.getPlayerColors()[i], load.getPlayerNames()[i], i+1); //Makes that has this board, the color and name they used to.
            gameController.fillStartDeck(player.getCardDeck());//Fill their start deck with cards so there are some to change.
            player.setSpace(board.getSpace(load.getX()[i], load.getY()[i])); //Set players position
            player.setHeading(load.getPlayerHeadings()[i]); //sets player heading
            int checkpointReached = load.getPlayerCheckPoints()[i]; //Sees how many checkpoints player has reached in total
            for(int j = 0; j < checkpointReached; j++){ //Checks all checkpoints for that player up to that point.
                player.setCheckpointReached(j, true);
            }

            board.addPlayer(player); //Adds player to board
        }

        for(int i = 0; i < board.getPlayersNumber(); i++){ //Sets current player
            Player player = board.getPlayer(i);
            if(player.getName().equals(load.getCurrentPlayer())){
                board.setCurrentPlayer(player);
                break;
            }
        }
        Command[][] playerProgrammingDeck;
        Command[][] playerCurrentProgram;
        Command[][] playerDiscardPile;
        Command[][] playerUpgradeCards;
    }
    private static void loadCardsForPlayer(Player player, Load load){

    }
    private static void loadCurrentState(Load load, Board board){
        board.setStepMode(load.isStepmode()); //Set if we are running in stepmode
        board.setStep(load.getStep()); //sets the step
        board.setPhase(load.getPhase()); //sets the phase
    }

}
