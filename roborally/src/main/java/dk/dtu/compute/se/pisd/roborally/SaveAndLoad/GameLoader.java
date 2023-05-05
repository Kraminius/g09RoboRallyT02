package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
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
        load(appController, obj);

        for (Object key : obj.keySet()) {
            insert(gameController, obj.get(key), (String) key);
        }
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
    private static void load(AppController appController, JSONObject obj){
        Board board = new Board((String)obj.get("board"));
        gameController = new GameController(board);
        appController.roboRally.createBoardView(gameController);
        int no = (int) obj.get("playerAmount");
        for (int i = 0; i < no; i++) {
            Player player = new Player(board, appController.PLAYER_COLORS.get(i), (String)((JSONArray) obj.get("playersName")).get(i), i+1);
            gameController.fillStartDeck(player.getCardDeck());
            player.setSpace(board.getSpace((int)((JSONArray) obj.get("playersX")).get(i), (int)((JSONArray) obj.get("playersY")).get(i)));
            player.setHeading(getHeading((String)((JSONArray) obj.get("playersHeading")).get(i)));
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

    private static Heading getHeading(String heading){
        switch (heading){
            case "SOUTH":
                return Heading.SOUTH;
            case "WEST":
                return Heading.WEST;
            case "NORTH":
                return Heading.NORTH;
            case "EAST":
                return Heading.EAST;
        }
        return null;
    }
}
