package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BuildJSON {


    static JSONHandler jsonHandler = new JSONHandler();

    public static void saveBoard(CheckBoardBuild build){
        jsonHandler.raw.writeJSON(build.getName(), saveBuildToJSON(build), "board");
        jsonHandler.printJSON(build.getName(), "board");
    }

    public static JSONObject saveBuildToJSON(CheckBoardBuild build){
        JSONObject obj = new JSONObject();
        JSONArray antennaArray = new JSONArray();
        antennaArray.add(build.getAntenna().get(0).getX() + ";" + build.getAntenna().get(0).getY());
        JSONArray respawnArray = new JSONArray();
        respawnArray.add(build.getRespawn().get(0).getX() + ";" + build.getRespawn().get(0).getY());
        JSONArray wallArray = makeWalls(build);
        JSONArray beltArray = makeBelts(build);
        JSONArray checkpointArray = makeCheckPoints(build);
        JSONArray laserArray = makeLasers(build);
        JSONArray pushArray = makePush(build);
        JSONArray energyFieldArray  = makeEnergyFields(build);
        JSONArray gearArray = makeGear(build);
        JSONArray holeArray  = makeHole(build);
        JSONArray noFieldArray  = makeNoFields(build);
        JSONArray startFieldArray  = makeStartFields(build);
        JSONArray repairArray  = makeRepair(build);
        obj.put("name", build.getName()+"");
        obj.put("width", build.getWidth()+"");
        obj.put("height", build.getHeight()+"");
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
        obj.put("repair", repairArray);
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
            if(element.getGreenBelt() > 0){
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
                }
            }
            if(element.getBlueBelt() > 0){
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
                }
            }
        }
        return beltArray;
    }
    private static JSONArray makeCheckPoints(CheckBoardBuild build){
        JSONArray checkpointArray = new JSONArray();
        for(BoardBuildElement element : build.getCheckpoints()){
            if(element.getCheckpoint() > 0){
                checkpointArray.add(element.getX() + ";" + element.getY() + ";" + element.getCheckpoint());
            }
        }
        return checkpointArray;
    }
    private static JSONArray makeLasers(CheckBoardBuild build){
        JSONArray laserArray = new JSONArray();
        for(BoardBuildElement element : build.getLasers()){
            if(element.isLaserRay()){
                if(element.isLaserPointer()){
                    laserArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getLaserRotation()) + ";" + element.getLaserStrength() + ";TRUE");
                }
                else{
                    laserArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getLaserRotation()) + ";" + element.getLaserStrength() + ";FALSE");
                }
            }
        }
        return laserArray;
    }
    private static JSONArray makePush(CheckBoardBuild build){
        JSONArray pushArray = new JSONArray();
        for(BoardBuildElement element : build.getPushers()){
            if(element.getPush() > 0){
                if(element.getPush() == 1){
                    pushArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getPushRotation()) + ";2;4");
                }
                if(element.getPush() == 2){
                    pushArray.add(element.getX() + ";" + element.getY() + ";" + rotationToHeading(element.getPushRotation()) + ";1;3;5");
                }
            }
        }
        return pushArray;
    }
    private static JSONArray makeEnergyFields(CheckBoardBuild build){
        JSONArray energyFieldArray = new JSONArray();
        for(BoardBuildElement element : build.getEnergyFields()){
            if(element.isEnergyField()){
                energyFieldArray.add(element.getX() + ";" + element.getY() + ";1" );
            }
        }
        return energyFieldArray;
    }
    private static JSONArray makeRepair(CheckBoardBuild build){
        JSONArray repairArray = new JSONArray();
        for(BoardBuildElement element : build.getRepair()){
            if(element.isRepair()){
                repairArray.add(element.getX() + ";" + element.getY());
            }
        }
        return repairArray;
    }
    private static JSONArray makeNoFields(CheckBoardBuild build){
        JSONArray noFieldArray = new JSONArray();
        for(BoardBuildElement element : build.getNoFields()){
            if(element.isNoField()){
                noFieldArray.add(element.getX() + ";" + element.getY());
            }
        }
        return noFieldArray;
    }
    private static JSONArray makeHole(CheckBoardBuild build){
        JSONArray holeArray = new JSONArray();
        for(BoardBuildElement element : build.getHoles()){
            if(element.isHole()){
                holeArray.add(element.getX() + ";" + element.getY());
            }
        }
        return holeArray;
    }
    private static JSONArray makeGear(CheckBoardBuild build){
        JSONArray gearArray = new JSONArray();
        for(BoardBuildElement element : build.getGears()){
            if(element.getGear() == 1){
                gearArray.add(element.getX() + ";" + element.getY() + ";LEFT");
            }
            if(element.getGear() == 2){
                gearArray.add(element.getX() + ";" + element.getY() + ";RIGHT");
            }
        }
        return gearArray;
    }
    private static JSONArray makeStartFields(CheckBoardBuild build){
        JSONArray startFieldArray = new JSONArray();
        for(BoardBuildElement element : build.getStartFields()){
            if(element.getStartField() > 0){
                startFieldArray.add(element.getX() + ";" + element.getY() + ";" + element.getStartField());
            }
        }
        return startFieldArray;
    }

    //Gear, Hole, NoField

}
