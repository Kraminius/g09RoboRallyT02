package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class PlayerSaver {

    JSONHandler json = new JSONHandler();
    ReaderAndWriter raw = new ReaderAndWriter();

    private static PlayerSaver playerLoader;

    public static PlayerSaver getInstance(){
        if(playerLoader == null) playerLoader = new PlayerSaver();
        return playerLoader;
    }


    /** @Author Nicklas Christensen - s224314@dtu.dk
     * Saves a single player to a file and inserts each key and their value into the JSON.
     * @param name JSON file name
     * @param player the player that it saves
     * @return Wether it was a succes or not
     */
    public boolean savePlayer(String name, Player player){
        JSONObject obj = new JSONObject();
        Board board = player.board;
        obj.put("name", player.getName());
        obj.put("color", player.getColor());
        String space = (player.getSpace().x + ";" + player.getSpace().y);
        obj.put("space", space);
        String heading = getHeading(player.getHeading());
        obj.put("heading", heading);
        JSONArray programs = new JSONArray();
        for(int i = 0; i < 5; i++) {
            String visible = getBoolean(player.getProgramField(i).isVisible());
            String command = getCommand(player.getProgramField(i).getCard().command);
            String addition = visible +";"+command;
            programs.add(addition);
        }
        obj.put("program", programs);
        JSONArray cards = new JSONArray();
        for(int i = 0; i < 8; i++) {
            String visible = getBoolean(player.getProgramField(i).isVisible());
            String command = getCommand(player.getProgramField(i).getCard().command);
            String addition = visible +";"+command;
            cards.add(addition);
        }
        obj.put("cards", cards);
        JSONArray checkpoints = new JSONArray();
        for(int i = 0; i < player.board.getCheckPointSpaces().size(); i++) {
            String unlocked = getBoolean(player.getCheckpointReadhed()[i]);
            cards.add(unlocked);
        }
        obj.put("checkpointsReadhed", checkpoints);

        raw.writeJSON(name, obj);

        //Tester
        JSONObject obj2 = json.load(name);
        if(obj == null) return false;
        return true;
    }


    /*
    This version didnt use player as parameter and rather placed them in the board given
    public boolean loadPlayer(String name, Board board){
        JSONObject obj = json.load(name);
        if(obj == null) return false;
        String pColor = "Stand in";
        String pName = "Stand in";
        Player player = new Player(board,pColor,pName);
        board.addPlayer(player);
        for(Object key : obj.keySet()){
            insert(board, player, obj.get(key), (String) key);
        }
        return true;
    }
     */
    public String getHeading(Heading heading){
        switch (heading){
            case SOUTH:
                return "SOUTH";
            case WEST:
                return "WEST";
            case NORTH:
                return "NORTH";
            case EAST:
                return "EAST";
        }
        return null;
    }

    public String getCommand(Command command){
        switch(command){
            //Might have to switch for displayName
            case FORWARD:
                return "FORWARD";
            case RIGHT:
                return "RIGHT";
            case LEFT:
                return "LEFT";
            case FAST_FORWARD:
                return "FAST_FORWARD";
            case U_TURN:
                return "U_TURN";
            case BACK_UP:
                return "BACK_UP";
            case AGAIN:
                return "AGAIN";
            case OPTION_LEFT_RIGHT:
                return "OPTION_LEFT_RIGHT";
        }
        return null;
    }

    public String getBoolean(boolean check){
        if (check) {
            return "true";
        } else {
            return "false";
        }
    }

}
