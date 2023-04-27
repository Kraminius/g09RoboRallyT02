package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PlayerStatLoader {


    JSONHandler json = new JSONHandler();

    /** Loads the information stored in the players.
     *
     * @param id of the status requested
     * @return amount and info of players
     */
    //Need work or tests (Player[]?)
    public void getPlayerStat(int id, Board board){
        JSONObject obj = json.load("PlayerStat" + id);
        int AmountOfPlayers = board.getPlayersNumber();

        for(int i = 0; i < AmountOfPlayers; i++){
        Player player = new Player(board, null, "Stand in name");
        //Board boardf = new Board(0, 0);
        for(Object key : obj.keySet()){
            insert(player, obj.get(key), (String) key);
        }
        }
    }

/*

This is an attempt at making some JSONArray in the start to find the number of players

    public void getPlayerStat(int id, Board board){
        JSONObject obj = json.load("PlayerStat" + id);

        ArrayList<Player> players = getList((JSONArray) obj);

        for(int i = 0; i < players.size(); i++){
            Player player = new Player(board, null, "Stand in name");
            //Board boardf = new Board(0, 0);
            for(Object key : obj.keySet()){
                insert(player, obj.get(key), (String) key);
            }
        }
    }

 */


/*
    public Board getBoard(int id){
        JSONObject obj = json.load("board_" + id);
        Board board = new Board(0, 0);
        for(Object key : obj.keySet()){
            insert(board, obj.get(key), (String) key);
        }
    }
 */
    //Insert
    private void insert(Player p, Object value, String varName){
        switch (varName){
            //new
            case "name":
                p.setName((String)value);
                break;
            case "color":
                p.setColor((String)value);
                break;
            case "space":
                p.setSpace((Space)value);
                break;
            case "heading":
                p.setHeading((Heading)value);
                break;
            //Well see if checkpoints work
            case "checkpoint":

                ArrayList<Boolean> checkpoints = getList((JSONArray) value);
                for(int i = 0; i < checkpoints.size(); i++){
                    System.out.println("Vi har arraylist checkpoint: " + i + " som: " + checkpoints.get(i));
                    p.setCheckpointReadhed(i,checkpoints.get(i));
                }


                /*
                ArrayList<String> checkpoints = getList((JSONArray) value);
                for(int i = 0; i < checkpoints.size(); i++){
                String[] values = checkpoints.get(i).split(";");
                if(i%2 == 0) {
                    p.setCheckpointReadhed((int)values[i],(boolean)values[i+1]);
                }
                }
                */
                break;

            /*old
            case "width":
                b.width = (int)value;
                break;
            case "height":
                b.height = (int)value;
                b.loadSpaces();
                break;
            case "name":
                b.boardName = (String) value;
                break;
            case "wall":
                ArrayList<String> walls = getList((JSONArray) value);
                Space[][] spaces = new Space[b.width][b.height];

                for(int i = 0; i < walls.size(); i++){
                    String[] values = walls.get(i).split(";");
                    spaces[0][1].wall.wallHeadings
                }

             */
        }

    }

    //This method brings the players in a Player-array
    private ArrayList<Player> getPlayers(JSONArray pl){
        ArrayList<Player> list = new ArrayList<>();
        for(int i = 0; i < pl.size(); i++){
            list.add((Player)  pl.get(i));
        }
        return list;
    }
    private ArrayList<Boolean> getList(JSONArray arr){
        ArrayList<Boolean> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            list.add((Boolean) arr.get(i));
        }
        return list;
    }


    /* board_x.json file structure
    {
        "varName":"stringValue",
        "varName": intValue,
        "varName":["stringValue1", "stringValue2", "stringValue3"],
        "varName":[intValue1, intValue2, intValue3],
        "wall":["0;0;EAST;WEST", "2;3;NORTH;EAST"]
    }
     */
}
