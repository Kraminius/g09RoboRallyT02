package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

public class CheckBoardBuild {

    private String name;
    private int width;
    private int height;
    private ArrayList<BoardBuildElement> antenna;
    private ArrayList<BoardBuildElement> respawn;
    private ArrayList<BoardBuildElement> walls;
    private ArrayList<BoardBuildElement> belts;
    private ArrayList<BoardBuildElement> checkpoints;
    private ArrayList<BoardBuildElement> lasers;
    private ArrayList<BoardBuildElement> pushers;
    private ArrayList<BoardBuildElement> energyfields;
    private ArrayList<BoardBuildElement> gears;
    private ArrayList<BoardBuildElement> holes;
    private ArrayList<BoardBuildElement> noFields;
    private ArrayList<BoardBuildElement> startFields;
    private ArrayList<BoardBuildElement> repair;


    public String checkBoard(BoardBuild build){
        CheckLogic logic = new CheckLogic(build, getSortedBoardBuild(build));
        return logic.checkBoard();
    }

    public CheckBoardBuild getSortedBoardBuild(BoardBuild build){
        name = build.getName();
        width = build.getWidth();
        height = build.getHeight();
        walls = new ArrayList<>();
        belts = new ArrayList<>();
        checkpoints = new ArrayList<>();
        lasers = new ArrayList<>();
        pushers = new ArrayList<>();
        energyfields = new ArrayList<>();
        gears = new ArrayList<>();
        holes = new ArrayList<>();
        noFields = new ArrayList<>();
        startFields = new ArrayList<>();
        repair = new ArrayList<>();
        respawn = new ArrayList<>();
        antenna = new ArrayList<>();

        for(ArrayList<BoardBuildElement> arr: build.getCurrentBuild()){
            for(BoardBuildElement element: arr){
                if(element.isAntenna()) antenna.add(element);
                if(element.isRespawn()) respawn.add(element);
                if(element.isLaserRay()) lasers.add(element);
                if(element.isHole()) holes.add(element);
                if(element.isNoField()) noFields.add(element);
                if(element.isRepair()) repair.add(element);
                if(element.isEnergyField()) energyfields.add(element);
                if(element.getWall() > 0) walls.add(element);
                if(element.getGreenBelt() > 0) belts.add(element);
                if(element.getBlueBelt() > 0) belts.add(element);
                if(element.getCheckpoint() > 0) checkpoints.add(element);
                if(element.getPush() > 0) pushers.add(element);
                if(element.getGear() > 0) gears.add(element);
                if(element.getStartField() > 0) startFields.add(element);
            }
        }
        return this;
    }
    public String getName(){
        return name;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

    public ArrayList<BoardBuildElement> getAntenna(){

        return antenna;
    }
    public ArrayList<BoardBuildElement> getRespawn(){
        return respawn;
    }
    public ArrayList<BoardBuildElement> getWalls(){

        return walls;
    }
    public ArrayList<BoardBuildElement> getBelts(){

        return belts;
    }
    public ArrayList<BoardBuildElement> getCheckpoints(){

        return checkpoints;
    }
    public ArrayList<BoardBuildElement> getLasers(){

        return lasers;
    }
    public ArrayList<BoardBuildElement> getPushers(){

        return pushers;
    }
    public ArrayList<BoardBuildElement> getEnergyFields(){

        return energyfields;
    }
    public ArrayList<BoardBuildElement> getGears(){

        return gears;
    }
    public ArrayList<BoardBuildElement> getHoles(){

        return holes;
    }
    public ArrayList<BoardBuildElement> getStartFields(){

        return startFields;
    }
    public ArrayList<BoardBuildElement> getNoFields(){

        return noFields;
    }
    public ArrayList<BoardBuildElement> getRepair() {
        return repair;
    }
}
