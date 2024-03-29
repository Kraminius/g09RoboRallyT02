package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.GameClient;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class GameSave {


    JSONHandler json = new JSONHandler();
    private String phaseToString(Phase phase){
        switch (phase){
            case UPGRADE : return "UPGRADE";
            case ACTIVATION : return "ACTIVATION";
            case PROGRAMMING : return "PROGRAMMING";
            case INITIALISATION : return "INITIALISATION";
            case PLAYER_INTERACTION : return "PLAYER_INTERACTION";
        }
        return null;
    }
    private String headingToString(Heading heading){
        switch (heading){
            case NORTH: return "NORTH";
            case EAST: return "EAST";
            case SOUTH: return "SOUTH";
            case WEST: return "WEST";
        }
        return null;
    }
    private String booleanToString(boolean bool){
        if(bool) return "TRUE";
        else return "FALSE";
    }

    public boolean saveGame(GameController controller, String name){
        JSONObject obj = new JSONObject();
        Board board = controller.board;
        obj.put("board", board.boardName); //String
        obj.put("step", board.getStep()); //Int
        obj.put("playerAmount", board.getPlayersNumber()); //Int
        obj.put("currentPlayer", board.getCurrentPlayer().getName()); //String
        obj.put("phase", phaseToString(board.getPhase())); //String
        obj.put("isStepMode", booleanToString(board.isStepMode())); //String

        JSONArray playersName = new JSONArray();
        JSONArray playersColor = new JSONArray();
        JSONArray playersX = new JSONArray();
        JSONArray playersY = new JSONArray();
        JSONArray playersHeading = new JSONArray();
        JSONArray playersCheckpoints = new JSONArray();
        JSONArray playersProgrammingDeck = new JSONArray();
        JSONArray playersPulledCards = new JSONArray();
        JSONArray playersProgram = new JSONArray();
        JSONArray playersDiscardCards = new JSONArray();
        JSONArray playerUpgradeCards = new JSONArray();
        JSONArray playerEnergyCubes = new JSONArray();
        JSONArray mapEnergyCubes = new JSONArray();
        for(int i = 0; i < board.getPlayersNumber(); i++){
            Player player = board.getPlayer(i);
            playersName.add(player.getName());
            playersColor.add(player.getColor());
            playerEnergyCubes.add(player.getEnergyCubes());
            playersX.add(player.getSpace().x);
            playersY.add(player.getSpace().y);
            playersHeading.add(headingToString(player.getHeading()));
            playersCheckpoints.add(getCheckpointReached(player.getCheckpointReadhed()));
            CommandCardField[] program = player.getProgram();
            CommandCardField[] pulled = player.getCards();
            CommandCardField[] upgradeCards = player.getUpgradeCards();
            ArrayList<CommandCard> discardPile = player.getDiscardPile();
            ArrayList<CommandCard> programmingDeck = player.getCardDeck();


            playersProgram.add("#");
            for(int j = 0; j < program.length; j++){
                if(program[j].getCard() != null) playersProgram.add(program[j].getCard().command.toString());
            }

            playersPulledCards.add("#");
            for(int j = 0; j < pulled.length; j++){
                if(pulled[j].getCard() != null) playersPulledCards.add(pulled[j].getCard().command.toString());
            }
            playerUpgradeCards.add("#");
            for(int j = 0; j < upgradeCards.length; j++){
                if(upgradeCards[j].getCard() != null) playerUpgradeCards.add(upgradeCards[j].getCard().command.toString());
            }
            playersDiscardCards.add("#");
            for(int j = 0; j < discardPile.size(); j++){
                if(discardPile.get(j) != null) playersDiscardCards.add(discardPile.get(j).command.toString());
            }
            playersProgrammingDeck.add("#");
            for(int j = 0; j < programmingDeck.size(); j++){
                if(programmingDeck.get(j) != null) playersProgrammingDeck.add(programmingDeck.get(j).command.toString());
            }
        }
        JSONArray upgradeCardsDeck = new JSONArray();
        JSONArray upgradeDiscardDeck = new JSONArray();
        JSONArray upgradeOutDeck = new JSONArray();
        JSONArray upgradeLoadDeck = new JSONArray();
        if(controller.upgradeShop != null){
            ArrayList<CommandCard> outUpgradeCards = controller.upgradeShop.getOut();
            ArrayList<CommandCard> discardUpgradeCards = controller.upgradeShop.getDiscarded();
            ArrayList<CommandCard> upgradeDeck = controller.upgradeShop.getDeck();
            ArrayList<CommandCardField> loadDeck = controller.upgradeShop.getLoadedCards();


            for(CommandCard c : outUpgradeCards){
                upgradeOutDeck.add(c.command.toString());
            }
            for(CommandCard c : discardUpgradeCards){
                upgradeDiscardDeck.add(c.command.toString());
            }
            for(CommandCard c : upgradeDeck){
                upgradeCardsDeck.add(c.command.toString());
            }
            for(CommandCardField c : loadDeck){
                upgradeLoadDeck.add(c.getCard().command.toString());
            }
        }

        saveCubes(mapEnergyCubes, board);



        obj.put("playersName", playersName);
        obj.put("playerCubes", playerEnergyCubes);
        obj.put("playerColor", playersColor);
        obj.put("playersX", playersX);
        obj.put("playersY", playersY);
        obj.put("playersHeading", playersHeading);
        obj.put("playersCheckpoints", playersCheckpoints);
        obj.put("playersProgrammingDeck", playersProgrammingDeck);
        obj.put("playersPulledCards", playersPulledCards);
        obj.put("playersProgram", playersProgram);
        obj.put("playersDiscardCards", playersDiscardCards);
        obj.put("playerUpgradeCards", playerUpgradeCards);
        obj.put("upgradeCardsDeck", upgradeCardsDeck);
        obj.put("upgradeDiscardDeck", upgradeDiscardDeck);
        obj.put("upgradeOutDeck", upgradeOutDeck);
        obj.put("mapCubes", mapEnergyCubes);

        json.save(name, obj, "game");
        return true;
    }
    private void saveCubes(JSONArray mapEnergyCubes, Board board){
        ArrayList<Space> energyCubeSpaces = board.getEnergyFieldSpaces();
        for(int i = 0; i < energyCubeSpaces.size(); i++){
            mapEnergyCubes.add(energyCubeSpaces.get(i).x);
            mapEnergyCubes.add(energyCubeSpaces.get(i).y);
            mapEnergyCubes.add(energyCubeSpaces.get(i).getElement().getEnergyField().getCubes());
        }
    }
    private int getCheckpointReached(boolean[] arr){
        int reached = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i])  reached++;
        }
        return reached;
    }


    //Used when using POST game to server
    public JSONObject jsonGame(GameController controller){
        JSONObject obj = new JSONObject();
        Board board = controller.board;
        obj.put("board", board.boardName); //String
        obj.put("step", board.getStep()); //Int
        obj.put("playerAmount", board.getPlayersNumber()); //Int
        obj.put("currentPlayer", board.getCurrentPlayer().getName()); //String
        obj.put("phase", phaseToString(board.getPhase())); //String
        obj.put("isStepMode", booleanToString(board.isStepMode())); //String

        JSONArray playersName = new JSONArray();
        JSONArray playersColor = new JSONArray();
        JSONArray playersX = new JSONArray();
        JSONArray playersY = new JSONArray();
        JSONArray playersHeading = new JSONArray();
        JSONArray playersCheckpoints = new JSONArray();
        JSONArray playersProgrammingDeck = new JSONArray();
        JSONArray playersPulledCards = new JSONArray();
        JSONArray playersProgram = new JSONArray();
        JSONArray playersDiscardCards = new JSONArray();
        JSONArray playerUpgradeCards = new JSONArray();
        JSONArray playerEnergyCubes = new JSONArray();
        JSONArray mapEnergyCubes = new JSONArray();
        //for(int i = 0; i < board.getPlayersNumber(); i++){
        int playerNum = GameClient.getPlayerNumber();
        Player player = board.getPlayer(playerNum);
        playersName.add(player.getName());
        playersColor.add(player.getColor());
        playerEnergyCubes.add(player.getEnergyCubes());
        playersX.add(player.getSpace().x);
        playersY.add(player.getSpace().y);
        playersHeading.add(headingToString(player.getHeading()));
        playersCheckpoints.add(getCheckpointReached(player.getCheckpointReadhed()));
        CommandCardField[] program = player.getProgram();
        CommandCardField[] pulled = player.getCards();
        CommandCardField[] upgradeCards = player.getUpgradeCards();
        ArrayList<CommandCard> discardPile = player.getDiscardPile();
        ArrayList<CommandCard> programmingDeck = player.getCardDeck();


        playersProgram.add("#");
        for(int j = 0; j < program.length; j++){
            if(program[j].getCard() != null) playersProgram.add(program[j].getCard().command.toString());
        }

        playersPulledCards.add("#");
        for(int j = 0; j < pulled.length; j++){
            if(pulled[j].getCard() != null) playersPulledCards.add(pulled[j].getCard().command.toString());
        }
        playerUpgradeCards.add("#");
        for(int j = 0; j < upgradeCards.length; j++){
            if(upgradeCards[j].getCard() != null) playerUpgradeCards.add(upgradeCards[j].getCard().command.toString());
        }
        playersDiscardCards.add("#");
        for(int j = 0; j < discardPile.size(); j++){
            if(discardPile.get(j) != null) playersDiscardCards.add(discardPile.get(j).command.toString());
        }
        playersProgrammingDeck.add("#");
        for(int j = 0; j < programmingDeck.size(); j++){
            if(programmingDeck.get(j) != null) playersProgrammingDeck.add(programmingDeck.get(j).command.toString());
        }
        //}
        JSONArray upgradeCardsDeck = new JSONArray();
        JSONArray upgradeDiscardDeck = new JSONArray();
        JSONArray upgradeOutDeck = new JSONArray();
        if(controller.upgradeShop != null){
            ArrayList<CommandCard> outUpgradeCards = controller.upgradeShop.getOut();
            ArrayList<CommandCard> discardUpgradeCards = controller.upgradeShop.getDiscarded();
            ArrayList<CommandCard> upgradeDeck = controller.upgradeShop.getDeck();


            for(CommandCard c : outUpgradeCards){
                upgradeOutDeck.add(c.command.toString());
            }
            for(CommandCard c : discardUpgradeCards){
                upgradeDiscardDeck.add(c.command.toString());
            }
            for(CommandCard c : upgradeDeck){
                upgradeCardsDeck.add(c.command.toString());
            }
        }

        saveCubes(mapEnergyCubes, board);



        obj.put("playersName", playersName);
        obj.put("playerCubes", playerEnergyCubes);
        obj.put("playerColor", playersColor);
        obj.put("playersX", playersX);
        obj.put("playersY", playersY);
        obj.put("playersHeading", playersHeading);
        obj.put("playersCheckpoints", playersCheckpoints);
        obj.put("playersProgrammingDeck", playersProgrammingDeck);
        obj.put("playersPulledCards", playersPulledCards);
        obj.put("playersProgram", playersProgram);
        obj.put("playersDiscardCards", playersDiscardCards);
        obj.put("playerUpgradeCards", playerUpgradeCards);
        obj.put("upgradeCardsDeck", upgradeCardsDeck);
        obj.put("upgradeDiscardDeck", upgradeDiscardDeck);
        obj.put("upgradeOutDeck", upgradeOutDeck);
        obj.put("mapCubes", mapEnergyCubes);

        //json.save(name, obj, "game");
        return obj;
    }
}

