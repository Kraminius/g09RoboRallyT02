package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BoardLoader {


    JSONHandler json = new JSONHandler();

    private static BoardLoader boardLoader;

    public static BoardLoader getInstance(){
        if(boardLoader == null) boardLoader = new BoardLoader();
        return boardLoader;
    }




    /** Loads a board from files and inserts each key and their value into the board.
     *
     * @param name board name
     * @param board the board that it should be loaded into
     * @return the board that is saved with the id
     */
    public boolean loadBoard(String name, Board board){
        JSONObject obj = json.load(name);
        if(obj == null) return false;
        board.width = parseInt((String) obj.get("width"));
        board.height = parseInt((String) obj.get("height"));
        board.spaces = new Space[board.width][board.height];
        for(int x = 0; x < board.width; x++){
            for(int y = 0; y < board.height; y++){
                board.spaces[x][y] = new Space(board, x, y);
            }
        }
        for(Object key : obj.keySet()){
            insert(board, obj.get(key), (String) key);
        }
        return true;
    }
    /**@Author Tobias Gørlyk - s224271@dtu.dk
     * Goes through all the keys and adds their values to a space
     * @param b the board that the changes should be made to
     * @param value the Object that comes with the key, this is cast onto different values.
     * @param varName The name of the change we should make.
     */
    private void insert(Board b, Object value, String varName){

        if(value == null) return;

        switch (varName){
            case "name":
                b.boardName = (String) value;
                break;
            case "wall":
                ArrayList<String> walls = getList((JSONArray) value);
                for(int i = 0; i < walls.size(); i++){
                    String[] values = walls.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    for(int j = 2; j < values.length; j++){
                        Wall wall;
                        if(b.spaces[x][y].getElement().getWall() == null) wall = new Wall();
                        else wall = b.spaces[x][y].getElement().getWall();
                        wall.getWallHeadings().add(getHeading(values[j]));
                        b.spaces[x][y].getElement().setWall(wall);
                    }
                    }
                break;
            case "belt":
                ArrayList<String> belts = getList((JSONArray) value);
                for(int i = 0; i < belts.size(); i++){
                    String[] values = belts.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    Belt belt = new Belt();
                    if(values[2].equals("null")) belt.setTurn(""); //LEFT/RIGHT/null
                    else belt.setTurn(values[2]);
                    belt.setHeading(getHeading(values[3]));
                    belt.setSpeed(parseInt(values[4]));
                    b.spaces[x][y].getElement().setBelt(belt);

                }
                break;
            case "checkpoint":
                ArrayList<String> checkpoints = getList((JSONArray) value);
                for(int i = 0; i < checkpoints.size(); i++){
                    String[] values = checkpoints.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    Checkpoint checkpoint = new Checkpoint();
                    checkpoint.setNumber(parseInt(values[2]));
                    b.spaces[x][y].getElement().setCheckpoint(checkpoint);
                }
                break;
            case "laser":
                ArrayList<String> lasers = getList((JSONArray) value);
                for(int i = 0; i < lasers.size(); i++){
                    String[] values = lasers.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    Laser laser = new Laser();
                    laser.setHeading(getHeading(values[2]));
                    laser.setDamage(parseInt(values[3]));
                    if(values[4].equals("TRUE")) laser.setStart(true);
                    b.spaces[x][y].getElement().setLaser(laser);
                }
                break;
            case "antenna":
                ArrayList<String> antennas = getList((JSONArray) value);
                for(int i = 0; i < antennas.size(); i++){
                    String[] values = antennas.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    b.spaces[x][y].getElement().setAntenna(true);
                    b.setAntenna(b.spaces[x][y]);

                }
                break;
            case "push":
                ArrayList<String> pushers = getList((JSONArray) value);
                for(int i = 0; i < pushers.size(); i++){
                    String[] values = pushers.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    Push push = new Push();
                    push.setHeading(getHeading(values[2]));
                    for(int j = 3; j < values.length; j++){
                        push.getActivateRounds().add(parseInt(values[j]));
                    }
                    b.spaces[x][y].getElement().setPush(push);
                }
                break;
            case "energyField":
                ArrayList<String> energyFields = getList((JSONArray) value);
                for(int i = 0; i < energyFields.size(); i++){
                    String[] values = energyFields.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    EnergyField energyField = new EnergyField();
                    energyField.setCubes(parseInt(values[2]));
                    b.spaces[x][y].getElement().setEnergyField(energyField);
                }
                break;
            case "gear":
                ArrayList<String> gears = getList((JSONArray) value);
                for(int i = 0; i < gears.size(); i++){
                    String[] values = gears.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    Gear gear = new Gear();
                    gear.setRotation(values[2]); //LEFT/RIGHT
                    b.spaces[x][y].getElement().setGear(gear);
                }
                break;
            case "hole":
                ArrayList<String> holes = getList((JSONArray) value);
                for(int i = 0; i < holes.size(); i++){
                    String[] values = holes.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    b.spaces[x][y].getElement().setHole(true);
                }
                break;
            case "respawn":
                ArrayList<String> respawns = getList((JSONArray) value);
                for(int i = 0; i < respawns.size(); i++){
                    String[] values = respawns.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    b.spaces[x][y].getElement().setRespawn(true);
                }
                break;
            case "noField":
                ArrayList<String> noFields = getList((JSONArray) value);
                for(int i = 0; i < noFields.size(); i++){
                    String[] values = noFields.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    b.spaces[x][y].getElement().setSpace(false);
                }
                break;
            case "startFields":
                ArrayList<String> startFields = getList((JSONArray) value);
                for(int i = 0; i < startFields.size(); i++){
                    String[] values = startFields.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    StartField startField = new StartField();
                    startField.setId(parseInt(values[2]));
                    b.spaces[x][y].getElement().setStartField(startField);
                }
                break;
            case "repair":
                ArrayList<String> repairFields = getList((JSONArray) value);
                for(int i = 0; i < repairFields.size(); i++){
                    String[] values = repairFields.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    b.spaces[x][y].getElement().setRepair(true);
                }
                break;
        }
    }

    public Heading getHeading(String heading){
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
    private ArrayList<String> getList(JSONArray arr){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            list.add((String) arr.get(i));
        }
        return list;
    }
}
