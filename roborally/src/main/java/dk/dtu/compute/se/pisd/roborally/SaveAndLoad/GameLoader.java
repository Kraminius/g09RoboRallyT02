package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.view.StartPositionWindow;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static java.lang.Integer.parseInt;

public class GameLoader {

    private static GameController gameController;

    private static JSONHandler handler = new JSONHandler();

    public static GameController loadGame(String name, AppController appController) {
        JSONObject obj = handler.load(name, "game");
        if (obj == null) {
            System.out.println("Error in loading");
            return null;
        }
        load(appController, loadData(obj));

        return gameController;
    }

    /**
     * @param controller the controller that the changes should be made to
     * @param value      the Object that comes with the key, this is cast onto different values.
     * @param varName    The name of the change we should make.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * Goes through all the keys and adds their values to a game
     */
    private static void insert(GameController controller, Object value, String varName) {

    }
    private static Load loadData(JSONObject obj){
        Load load = new Load();
        load.setBoard((String)obj.get("board"));
        load.setStep((int)obj.get("step"));
        load.setCurrentPlayer((String)obj.get("currentPlayer"));
        load.setStepmode(Converter.getBool((String)obj.get("isStepMode")));
        load.setPhase(Converter.getPhase ((String) obj.get("phase")));
        load.setPlayerAmount((int)obj.get("playerAmount"));

        int amount = load.getPlayerAmount();
        load.setPlayerNames(new String[amount]);
        load.setPlayerColors(new String[amount]);
        load.setPlayersXPosition(new int[amount]);
        load.setPlayersYPosition(new int[amount]);
        load.setPlayerHeadings(new Heading[amount]);
        load.setPlayerProgrammingDeck(new Command[amount][]);
        load.setPlayerCurrentProgram(new Command[amount][]);
        load.setPlayerDiscardPile(new Command[amount][]);
        load.setPlayerUpgradeCards(new Command[amount][]);
        for(int i = 0; i < amount; i++){
            load.getPlayerNames()[i] = ((String[])obj.get("playersName"))[i];
            load.getPlayerColors()[i] = ((String[])obj.get("playerColor"))[i];
            load.getPlayersXPosition()[i] = ((int[])obj.get("playersX"))[i];
            load.getPlayersYPosition()[i] = ((int[])obj.get("playersY"))[i];
            load.getPlayerHeadings()[i] = Converter.getHeading (((String[])obj.get("playersHeading"))[i]);
        }
        /*
  "upgradeDiscardDeck":["SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG"],
  "upgradeOutDeck":["SPAM_FOLDER_TUPG"],
  "upgradeCardsDeck":["SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG","SPAM_FOLDER_TUPG"],
  "playersProgrammingDeck":["FAST_FORWARD","FORWARD","POWER_UP","LEFT","FORWARD","RIGHT","U_TURN","BACK_UP","AGAIN","AGAIN","SPRINT_FORWARD","RIGHT","POWER_UP","FORWARD","LEFT","FORWARD","RIGHT","AGAIN"],

  "playersProgram":[],
  "playersCheckpoints":[0,0],
  "playerUpgradeCards":[],
  "playersDiscardCards":[],
  "step":0,"board":"Chop Shop Challenge"}
         */

        return load;
    }
    private static void load(AppController appController, Load load){
        Board board = new Board((String)obj.get("board"));
        gameController = new GameController(board);
        appController.roboRally.createBoardView(gameController);
        int no = (int) obj.get("playerAmount");
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, appController.PLAYER_COLORS.get(i), (String)((JSONArray) obj.get("playersName")).get(i), i+1);
            gameController.fillStartDeck(player.getCardDeck());
            player.setSpace(board.getSpace((int)((JSONArray) obj.get("playersX")).get(i), (int)((JSONArray) obj.get("playersY")).get(i)));
            player.setHeading(Converter.getHeading((String)((JSONArray) obj.get("playersHeading")).get(i)));
            int checkpointReached = ((int)((JSONArray) obj.get("playersCheckpoints")).get(i));
            for(int j = 0; j < checkpointReached; j++){
                player.setCheckpointReadhed(j, true);
            }
            board.addPlayer(player);

            //player.setSpace(board.getSpace(i % board.width, i));
        }

        // XXX: V2
        // board.setCurrentPlayer(board.getPlayer(0));
        //gameController.startProgrammingPhase();
        board.setCurrentPlayer(board.getPlayer(0));
        StartPositionWindow positionWindow = new StartPositionWindow();
        positionWindow.getStartSpaces(board);

        gameController.startUpgradePhase();
    }


    private Command getCommand(String command){
        switch (command){
            case "STEP": return Command.AGAIN
        }
    }
}
