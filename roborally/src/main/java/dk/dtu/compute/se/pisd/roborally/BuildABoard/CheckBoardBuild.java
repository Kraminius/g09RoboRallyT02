package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.view.Option;

import java.util.ArrayList;

public class CheckBoardBuild {

    private BoardBuildElement antenna;
    private BoardBuildElement respawn;
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


    public boolean checkBoard(BoardBuild build){

        return false;
    }


    private void showError(String text){
        Option option = new Option(text);
        option.getOKPressed();
    }
    public String getName(){
        return "";
    }
    public int getWidth(){
        return 0;
    }
    public int getHeight(){
        return 0;
    }

    public BoardBuildElement getAntenna(){

        return null;
    }
    public BoardBuildElement getRespawn(){

        return null;
    }
    public ArrayList<BoardBuildElement> getWalls(){

        return null;
    }
    public ArrayList<BoardBuildElement> getBelts(){

        return null;
    }
    public ArrayList<BoardBuildElement> getCheckpoints(){

        return null;
    }
    public ArrayList<BoardBuildElement> getLasers(){

        return null;
    }
    public ArrayList<BoardBuildElement> getPushers(){

        return null;
    }
    public ArrayList<BoardBuildElement> getEnergyFields(){

        return null;
    }
    public ArrayList<BoardBuildElement> getGears(){

        return null;
    }
    public ArrayList<BoardBuildElement> getHoles(){

        return null;
    }
    public ArrayList<BoardBuildElement> getStartFields(){

        return null;
    }
    public ArrayList<BoardBuildElement> getNoFields(){

        return null;
    }
}
