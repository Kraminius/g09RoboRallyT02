package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import com.beust.ah.A;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class BoardLoader {


    JSONHandler json = new JSONHandler();

    /** Loads a board from files and inserts each key and their value into the board.
     *
     * @param id board id
     * @return the board that is saved with the id
     */
    public Board getBoard(int id){
        JSONObject obj = json.load("board_" + id);
        Board board = new Board(0, 0);
        for(Object key : obj.keySet()){
            insert(board, obj.get(key), (String) key);
        }
    }

    private void insert(Board b, Object value, String varName){
        switch (varName){
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

        }
    }

    private ArrayList<String> getList(JSONArray arr){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            list.add((String) arr.get(i));
        }
        return list;
    }


    /* board_x.json file structure


    {
        "varName":"stringValue",
        "varName": intValue,
        "varName":["stringValue1", "stringValue2", "stringValue3"],
        "varName":[intValue1, intValue2, intValue3],
        "wall":["x;y;EAST;WEST", "x;y;NORTH;EAST"],
        "belt":["x;y;LEFT/RIGHT/null;HEADING;SPEED"],
        "checkpoint":["x;y;checkpointNumber"],
        "laser":["x;y;HEADING;range;amount"], //amount = amount of lasers = amount of damage cards taken.
        "antenna":["x;y"],
        "push":["x;y;heading;1"] // number after heading = activation on turn.
        "energyField":["x;y;energyCubes"],
        "gear": ["x;y;LEFT/RIGHT"],
        "hole": ["x;y"],
        "respawn": ["x;y;spawnHeading"],
        "noField": ["x;y"];
        "startFields":["x;y;id];

    }
    {

    }
     */
}
