package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class JSONtoBuild {
    private JSONHandler jsonHandler = new JSONHandler();
    private BoardBuild build;
    private JSONObject obj;
    /**
     * @Author Tobias Gørlyk s224271
     * Constructor that Loads a board with a specific name into a build
     * @param name name of the file to load and the board name
     * @param build the build to load it into
     */
    public JSONtoBuild(String name, BoardBuild build){
        obj = jsonHandler.load(name, "board");
        if(obj != null){
            this.build = build;
            loadBoard();
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * load the board
     */
    private void loadBoard(){
        build.setSize(parseInt((String)obj.get("width")), parseInt((String)obj.get("height"))); //Set the size and changes the build to have all the spaces. It also sets coords.
        for(Object key : obj.keySet()){
            insert(obj.get(key), (String) key);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Inserts an object value into a specific variable for the board build
     * @param value the object value to convert
     * @param varName the name of the variable
     */

    private void insert(Object value, String varName){
        if(value == null) return;
        switch (varName){
            case "name":
                build.setName((String) value);
                break;
            case "checkpoint":
                ArrayList<String> checkpoint = getList((JSONArray) value); //Unsplit
                for(String info : checkpoint){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setCheckpoint(parseInt(split[2]));
                }
                break;
            case "laser":
                ArrayList<String> laser = getList((JSONArray) value); //Unsplit
                for(String info : laser){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setLaserRotation(getRotation(split[2]));
                    element.setLaserStrength(parseInt(split[3]));
                    if(split[4].equals("TRUE")){
                        element.setLaserRay(true);
                        element.setLaserPointer(true);
                    }
                    else{
                        element.setLaserRay(true);
                    }
                }
                break;
            case "antenna":
                ArrayList<String> antenna = getList((JSONArray) value); //Unsplit
                for(String info : antenna){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setAntenna(true);
                }
                break;
            case "push":
                ArrayList<String> push = getList((JSONArray) value); //Unsplit
                for(String info : push){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setPushRotation(getRotation(split[2]));
                    if(split[3].equals("2")) element.setPush(1);
                    else element.setPush(2);
                }
                break;
            case "energyField":
                ArrayList<String> energyField = getList((JSONArray) value); //Unsplit
                for(String info : energyField){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setEnergyField(true);
                }
                break;
            case "gear":
                ArrayList<String> gear = getList((JSONArray) value); //Unsplit
                for(String info : gear){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    if(split[2].equals("LEFT")) element.setGear(1); //Left
                    else element.setGear(2); //Right
                }
                break;
            case "hole":
                ArrayList<String> hole = getList((JSONArray) value); //Unsplit
                for(String info : hole){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setHole(true);
                }
                break;
            case "respawn":
                ArrayList<String> respawn = getList((JSONArray) value); //Unsplit
                for(String info : respawn){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setRespawn(true);
                }
                break;
            case "noField":
                ArrayList<String> noField = getList((JSONArray) value); //Unsplit
                for(String info : noField){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setNoField(true);
                }
                break;
            case "startFields":
                ArrayList<String> startFields = getList((JSONArray) value); //Unsplit
                for(String info : startFields){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setStartField(parseInt(split[2]));
                }
                break;
            case "repair":
                ArrayList<String> repair = getList((JSONArray) value); //Unsplit
                for(String info : repair){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    element.setRepair(true);
                }
                break;
            case "wall":
                ArrayList<String> wall = getList((JSONArray) value); //Unsplit
                for(int x = 0; x < build.getWidth(); x++){
                    for(int y = 0; y < build.getHeight(); y++){
                        BoardBuildElement element = null;
                        ArrayList<String[]> infoOnSpot = new ArrayList<>();
                        for(String info : wall){
                            String[] split = info.split(";");
                            if(parseInt(split[0]) == x && parseInt(split[1]) == y){
                                element = getElementAt(split[0], split[1]);
                                infoOnSpot.add(split);
                            }
                        }
                        if(element != null){
                            if(infoOnSpot.size() == 1){
                                element.setWall(1);
                                element.setWallRotation(getRotation(infoOnSpot.get(0)[2]));
                            }
                            else if(infoOnSpot.size() == 2){
                                int rotation1 = getRotation(infoOnSpot.get(0)[2]);
                                int rotation2 = getRotation(infoOnSpot.get(1)[2]);
                                if(formatInt(rotation2 - rotation1) == 1){ //Corner
                                    element.setWall(2);
                                    element.setWallRotation(getRotation(infoOnSpot.get(0)[2]));
                                }
                                else if(formatInt(rotation2 - rotation1) == 2){ //Opposite
                                    element.setWall(3);
                                    element.setWallRotation(getRotation(infoOnSpot.get(0)[2]));
                                }
                                else System.out.println("2 wall not recognised in loading");
                            }
                            else if(infoOnSpot.size() == 3){
                                element.setWall(4);
                                element.setWallRotation(getRotation(infoOnSpot.get(0)[2]));
                            }
                            else if(infoOnSpot.size() == 4){
                                element.setWall(5);
                                element.setWallRotation(getRotation(infoOnSpot.get(0)[2]));
                            }
                        }
                    }
                }
                break;
            case "belt":
                ArrayList<String> belt = getList((JSONArray) value); //Unsplit
                for(String info : belt){
                    String[] split = info.split(";");
                    BoardBuildElement element = getElementAt(split[0], split[1]);
                    if(parseInt(split[4]) == 1){
                        switch (split[2]){
                            case "null":
                                element.setGreenBelt(1);
                                element.setBeltRotation(formatInt(getRotation(split[3])-1));
                                break;
                            case "LEFT":
                                element.setGreenBelt(2);
                                element.setBeltRotation(formatInt(getRotation(split[3])));
                                break;
                            case "RIGHT":
                                element.setGreenBelt(3);
                                element.setBeltRotation(formatInt(getRotation(split[3])-2));
                                break;
                            case "LEFT_T":
                                element.setGreenBelt(4);
                                element.setBeltRotation(formatInt(getRotation(split[3])-3));
                                break;
                            case "RIGHT_T":
                                element.setGreenBelt(5);
                                element.setBeltRotation(formatInt(getRotation(split[3])-3));
                                break;
                            case "WEAVE":
                                element.setGreenBelt(6);
                                element.setBeltRotation(formatInt(getRotation(split[3])));
                                break;
                        }
                    }
                    else if(parseInt(split[4]) == 2){
                        switch (split[2]){
                            case "null":
                                element.setBlueBelt(1);
                                element.setBeltRotation(formatInt(getRotation(split[3])-1));
                                break;
                            case "LEFT":
                                element.setBlueBelt(2);
                                element.setBeltRotation(formatInt(getRotation(split[3])));
                                break;
                            case "RIGHT":
                                element.setBlueBelt(3);
                                element.setBeltRotation(formatInt(getRotation(split[3])-2));
                                break;
                            case "LEFT_T":
                                element.setBlueBelt(4);
                                element.setBeltRotation(formatInt(getRotation(split[3])-3));
                                break;
                            case "RIGHT_T":
                                element.setBlueBelt(5);
                                element.setBeltRotation(formatInt(getRotation(split[3])-3));
                                break;
                            case "WEAVE":
                                element.setBlueBelt(6);
                                element.setBeltRotation(formatInt(getRotation(split[3])));
                                break;
                        }
                    }
                }
                break;
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Converts a JSON array into a String arrayList.
     * @param arr the JSON array
     * @return the String arrayList
     */
    private ArrayList<String> getList(JSONArray arr){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            list.add((String) arr.get(i));
        }
        return list;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Gets the BoardBuildElement at coordinates x and y
     * @param x x coordinate
     * @param y y coordinate
     * @return the BoardBuildElement at those coordinates
     */
    private BoardBuildElement getElementAt(String x, String y){
            return build.getCurrentBuild().get(parseInt(x)).get(parseInt(y));
    }
    /**
     * @Author Tobias Gørlyk s224271
     * gets an int rotation from a string rotation
     * @param rotation String rotation being either of the following [EAST, SOUTH, WEST, NORTH]
     * @return integer between 0 and 3 [EAST = 0, SOUTH = 1, WEST = 2, NORTH = 3]
     */
    private int getRotation(String rotation){
        switch (rotation){
            case "EAST": return 0;
            case "SOUTH": return 1;
            case "WEST": return 2;
            case "NORTH": return 3;
        }
        return 0;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Formats an integer to be between 0 and 3 and subtracs or adds 4 if its outside.
     * @param toFormat the integer to format
     * @return final integer between 0 and 3
     */
    private int formatInt(int toFormat){
        while(toFormat > 3) toFormat -= 4;
        while(toFormat < 0) toFormat -= 4;
        return toFormat;

    }

}
