package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class BuildJSON {


    JSONHandler jsonHandler = new JSONHandler();

    public void saveBoard(CheckBoardBuild build){
        jsonHandler.raw.writeJSON(build.getName(), createObjectFromBuild(build), "board");
        jsonHandler.printJSON(build.getName(), "board");
    }

    public static JSONObject createObjectFromBuild(CheckBoardBuild build){
        JSONObject obj = new JSONObject();
        JSONArray antennaArray = new JSONArray();
        antennaArray.add(build.getAntenna().getX() + ";" + build.getAntenna().getY());
        JSONArray respawnArray = new JSONArray();
        antennaArray.add(build.getRespawn().getX() + ";" + build.getRespawn().getY());
        JSONArray wallArray = makeWalls(build);
        JSONArray beltArray = makeBelts(build);


        JSONArray checkpointArray = new JSONArray();
        JSONArray laserArray = new JSONArray();
        JSONArray pushArray = new JSONArray();
        JSONArray energyFieldArray = new JSONArray();
        JSONArray gearArray = new JSONArray();
        JSONArray holeArray = new JSONArray();
        JSONArray noFieldArray = new JSONArray();
        JSONArray startFieldArray = new JSONArray();





        obj.put("name", build.getName());
        obj.put("width", build.getWidth());
        obj.put("height", build.getHeight());
        obj.put("wall", wallArray);
        obj.put("belt", beltArray);
        obj.put("checkpoint", checkpointArray);
        obj.put("laser", laserArray);
        obj.put("antenna", antennaArray);
        obj.put("push", pushArray);
        obj.put("energyField", energyFieldArray);
        obj.put("gear", gearArray);
        obj.put("hole", holeArray);
        obj.put("respawn", respawnArray);
        obj.put("noField", noFieldArray);
        obj.put("startFields", startFieldArray);
        return obj;
    }
    private static String rotationToHeading(int rotation){
        rotation = getTrueRotation(rotation);
        switch (rotation){
            case 0: return"EAST";
            case 1: return "SOUTH";
            case 2: return "WEST";
            case 3: return "NORTH";
        }
        return "null";
    }
    private static int getTrueRotation(int rotation){
        while(rotation > 3){
            rotation -= 4;
        }
        while(rotation < 0){
            rotation += 4;
        }
        return rotation;
    }
    /*
    {
  "name": "Dizzy Highway",
  "width": "13",
  "height": "10",
  "wall":["1;2;NORTH","1;7;SOUTH","2;4;EAST","2;5;EAST", "6;3;NORTH", "6;4;SOUTH", "6;6;WEST", "7;6;EAST", "8;3;WEST", "9;3;EAST", "9;5;NORTH", "9;6;SOUTH"],
  "belt":["2;0;null;EAST;1", "2;9;null;EAST;1","3;7;null;EAST;2", "3;8;null;EAST;2", "4;0;null;SOUTH;2", "4;1;LEFT_T;SOUTH;2","4;2;null;SOUTH;2","4;3;null;SOUTH;2","4;4;null;SOUTH;2","4;5;null;SOUTH;2","4;6;null;SOUTH;2", "4;7;RIGHT_T;SOUTH;2", "4;8;LEFT_T;EAST;2", "5;8;null;EAST;2","6;8;null;EAST;2","7;8;null;EAST;2", "8;8;null;EAST;2", "9;8;null;EAST;2", "10;8;RIGHT_T;EAST;2", "11;8;LEFT_T;NORTH;2", "10;9;null;NORTH;2", "11;9;null;NORTH;2", "5;0;null;SOUTH;2", "5;1;RIGHT_T;WEST;2", "6;1;null;WEST;2", "7;1;null;WEST;2", "8;1;null;WEST;2", "9;1;null;WEST;2", "10;1;null;WEST;2", "11;1;LEFT_T;WEST;2", "12;1;null;WEST;2", "12;2;null;WEST;2", "11;2;RIGHT_T;NORTH;2", "11;3;null;NORTH;2", "11;4;null;NORTH;2","11;5;null;NORTH;2","11;6;null;NORTH;2","11;7;null;NORTH;2","11;9;null;NORTH;2"],
  "checkpoint":["12;3;1"],
  "laser":["6;4;NORTH;1;TRUE", "6;3;NORTH;1;FALSE", "7;6;WEST;1;TRUE","6;6;WEST;1;FALSE", "8;3;WEST;1;FALSE", "9;3;WEST;1;TRUE", "9;5;SOUTH;1;TRUE", "9;6;SOUTH;1;FALSE"],
  "antenna":["0;4"],
  "push": null,
  "energyField":["3;9;1", "5;2;1", "7;5;1", "8;4;1", "10;7;1", "12;0;1"],
  "gear": null,
  "hole": null,
  "respawn": ["7;3"],
  "noField": null,
  "startFields":["1;1;1", "0;3;2", "1;4;3", "1;5;4", "0;6;5", "1;8;6"]
}
     */

    private static JSONArray makeWalls(CheckBoardBuild build){
        JSONArray wallArray = new JSONArray();
        for(BoardBuildElement element : build.getWalls()){
            if(element.getWall() > 0){
                switch (element.getWall()){
                    case 1: //Single East
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation()));
                        break;
                    case 2: //Double Corner East-South
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation()));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 1));
                        break;
                    case 3: //Double Opposite East-West
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation()));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 2));
                        break;
                    case 4: //Tripple END East-South-North
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation()));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 1));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 3));
                        break;
                    case 5: //All Four
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation()));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 1));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 2));
                        wallArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getWallRotation() + 3));
                        break;
                }
            }
        }
        return wallArray;
    }
    private static JSONArray makeBelts(CheckBoardBuild build){
        JSONArray beltArray = new JSONArray();
        for(BoardBuildElement element : build.getBelts()){
            if(element.getWall() > 0){
                switch (element.getGreenBelt()){
                    case 1: //Forward South
                        beltArray.add(element.getX() + ";" + element.getY() + ";null;" + rotationToHeading(element.getBeltRotation()+1) + ";1");
                        break;
                    case 2: //Left East
                        beltArray.add(element.getX() + ";" + element.getY() + ";LEFT;" + rotationToHeading(element.getBeltRotation()) + ";1");
                        break;
                    case 3: //Right West
                        beltArray.add(element.getX() + ";" + element.getY() + ";RIGHT;" + rotationToHeading(element.getBeltRotation()+2) + ";1");
                        break;
                    case 4: //T-Left North
                        beltArray.add(element.getX() + ";" + element.getY() + ";LEFT_T;" + rotationToHeading(element.getBeltRotation()+3) + ";1");
                        break;
                    case 5: //T-Right North
                        beltArray.add(element.getX() + ";" + element.getY() + ";RIGHT_T;" + rotationToHeading(element.getBeltRotation()+3) + ";1");
                        break;
                    case 6: //Interweave East
                        beltArray.add(element.getX() + ";" + element.getY() + ";WEAVE;" + rotationToHeading(element.getBeltRotation()) + ";1");
                        break;
                    case 7: //Interweave 2 West
                        beltArray.add(element.getX() + ";" + element.getY() + ";WEAVE;" + rotationToHeading(element.getBeltRotation()+2) + ";1");
                        break;
                }
                switch (element.getBlueBelt()){
                    case 1: //Forward South
                        beltArray.add(element.getX() + ";" + element.getY() + ";null;" + rotationToHeading(element.getBeltRotation()+1) + ";2");
                        break;
                    case 2: //Left East
                        beltArray.add(element.getX() + ";" + element.getY() + ";LEFT;" + rotationToHeading(element.getBeltRotation()) + ";2");
                        break;
                    case 3: //Right West
                        beltArray.add(element.getX() + ";" + element.getY() + ";RIGHT;" + rotationToHeading(element.getBeltRotation()+2) + ";2");
                        break;
                    case 4: //T-Left North
                        beltArray.add(element.getX() + ";" + element.getY() + ";LEFT_T;" + rotationToHeading(element.getBeltRotation()+3) + ";2");
                        break;
                    case 5: //T-Right North
                        beltArray.add(element.getX() + ";" + element.getY() + ";RIGHT_T;" + rotationToHeading(element.getBeltRotation()+3) + ";2");
                        break;
                    case 6: //Interweave East
                        beltArray.add(element.getX() + ";" + element.getY() + ";WEAVE;" + rotationToHeading(element.getBeltRotation()) + ";2");
                        break;
                    case 7: //Interweave 2 West
                        beltArray.add(element.getX() + ";" + element.getY() + ";WEAVE;" + rotationToHeading(element.getBeltRotation()+2) + ";2");
                        break;
                }
            }
        }
        return beltArray;
    }
}
