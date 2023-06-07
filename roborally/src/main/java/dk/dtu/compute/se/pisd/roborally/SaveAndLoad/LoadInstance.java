package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.UpgradeShop;

import java.util.ArrayList;

public class LoadInstance {
    public static GameController load(AppController appController, Load load){
        Board board = createBoard(appController, load);
        GameController gameController = new GameController(board); //Creates new gamecontroller with the new board.
        initUpgradeShopAndCards(gameController, load);
        loadPlayer(load, gameController);
        loadCurrentState(load, board);
        initBoardView(appController, gameController);
        loadCubesOnBoard(load, board);




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
    private static void loadCubesOnBoard(Load load, Board board){
        int[] pos = load.getMapCubePositions();
        for (Space space : board.getEnergyFieldSpaces()) {
            space.getElement().getEnergyField().setCubes(0);
        }
        for(int i = 0; i < pos.length; i+=3){
            board.getSpace(pos[i], pos[i+1]).getElement().getEnergyField().setCubes(pos[i+2]);
        }
    }
    private static void initBoardView(AppController appController, GameController gameController){
        appController.roboRally.createBoardView(gameController); //register the new gamecontroller in the appcontroller
    }
    private static void initUpgradeShopAndCards(GameController gameController, Load load){
        gameController.upgradeShop = new UpgradeShop();
        ArrayList<CommandCard> deck = new ArrayList<>();
        ArrayList<CommandCard> out = new ArrayList<>();
        ArrayList<CommandCard> discarded = new ArrayList<>();
        ArrayList<CommandCardField> loadedCards = new ArrayList<>();
        Command[] arrDeck = load.getUpgradeCardsDeck();
        Command[] arrOut = load.getUpgradeOutDeck();
        Command[] arrDiscard = load.getUpgradeDiscardDeck();
        Command[] arrLoaded = load.getUpgradeDiscardDeck();
        for(int i = 0; i < arrDeck.length; i++){
            deck.add(new CommandCard(arrDeck[i])); //Creates each card for the deck
        }
        for(int i = 0; i < arrOut.length; i++){
            out.add(new CommandCard(arrOut[i])); //Creates each card for the out
        }
        for(int i = 0; i < arrDiscard.length; i++){
            discarded.add(new CommandCard(arrDiscard[i])); //Creates each card that has been discarded
        }
        for(int i = 0; i < arrLoaded.length; i++){
            CommandCardField field = new CommandCardField(null);
            field.setCard(new CommandCard(arrLoaded[i]));
            loadedCards.add(field); //Creates each card that has been loaded
        }
        gameController.upgradeShop.setDeck(deck); //Sets the decks in the upgrade shop to be these decks.
        gameController.upgradeShop.setOut(out);
        gameController.upgradeShop.setDiscarded(discarded);
        gameController.upgradeShop.setLoadedCards(loadedCards);
    }

    private static void loadPlayer(Load load, GameController gameController){
        Board board = gameController.board;
        for (int i = 0; i < load.getPlayerAmount(); i++) {
            Player player = new Player(board, load.getPlayerColors()[i], load.getPlayerNames()[i], i+1); //Makes that has this board, the color and name they used to.
            board.addPlayer(player); //Adds player to board
            gameController.fillStartDeck(player.getCardDeck());//Fill their start deck with cards so there are some to change.
            player.setSpace(board.getSpace(load.getX()[i], load.getY()[i])); //Set players position
            player.setHeading(load.getPlayerHeadings()[i]); //sets player heading
            int checkpointReached = load.getPlayerCheckPoints()[i]; //Sees how many checkpoints player has reached in total
            for(int j = 0; j < checkpointReached; j++){ //Checks all checkpoints for that player up to that point.
                player.setCheckpointReached(j, true);
            }
            player.setEnergyCubes(load.getPlayerEnergyCubes()[i]);
            loadCardsForPlayer(player, load, i);
        }

        for(int i = 0; i < board.getPlayersNumber(); i++){ //Sets current player
            Player player = board.getPlayer(i);
            if(player.getName().equals(load.getCurrentPlayer())){
                board.setCurrentPlayer(player);
                break;
            }
        }
    }
    private static void loadCardsForPlayer(Player player, Load load, int index){
        Command[][] playerProgrammingDeck = load.getPlayerProgrammingDeck();
        Command[][] playerCurrentProgram = load.getPlayerCurrentProgram();
        Command[][] playerDiscardPile = load.getPlayerDiscardPile();
        Command[][] playerUpgradeCards = load.getPlayerUpgradeCards();
        Command[][] playerPulledCards = load.getPlayersPulledCards();

        ArrayList<CommandCard> deck = player.getCardDeck();
        ArrayList<CommandCard> discard = player.getDiscardPile();
        CommandCardField[] upgradeCards = player.getUpgradeCards();
        CommandCardField[] currentProgram = player.getProgram();
        CommandCardField[] pulledCards = player.getCards();


        if(playerProgrammingDeck[index] != null){
            deck.clear();
            for(int i = 0; i < playerProgrammingDeck[index].length; i++){
                deck.add(new CommandCard(playerProgrammingDeck[index][i]));
            }
        }
        if(playerDiscardPile[index] != null){
            discard.clear();
            for(int i = 0; i < playerDiscardPile[index].length; i++){
                discard.add(new CommandCard(playerDiscardPile[index][i]));
            }
        }
        if(playerCurrentProgram[index] != null) {
            for (int i = 0; i < playerCurrentProgram[index].length; i++) {
                currentProgram[i] = new CommandCardField(player);
                currentProgram[i].setCard(new CommandCard(playerCurrentProgram[index][i]));
            }
        }
        if(playerPulledCards[index] != null){
            for(int i = 0; i < playerPulledCards[index].length; i++){
                pulledCards[i] = new CommandCardField (player);
                pulledCards[i].setCard(new CommandCard(playerPulledCards[index][i]));
            }
        }
        if(playerUpgradeCards[index] != null){
            int extraUpgrade = 0;
            for(int i = 0; i < playerUpgradeCards[index].length; i++){
                if(!UpgradeCardInfo.getPermanent(playerUpgradeCards[index][i]) && i < player.NO_UPGRADE_CARDS/2) extraUpgrade = player.NO_UPGRADE_CARDS/2; //If it's permanent I have to move it a bit to the right in the array.
                upgradeCards[i+extraUpgrade] = new CommandCardField (player);
                upgradeCards[i+extraUpgrade].setCard(new CommandCard(playerUpgradeCards[index][i]));
            }
        }



    }
    private static void loadCurrentState(Load load, Board board){
        board.setStepMode(load.isStepmode()); //Set if we are running in stepmode
        board.setStep(load.getStep()); //sets the step
        board.setPhase(load.getPhase()); //sets the phase
    }

}
