package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

public class BoardBuild {
    private int width = 10;
    private int height = 10;
    private ArrayList<ArrayList<BoardBuildElement>> currentBuild;


    public ArrayList<ArrayList<BoardBuildElement>> getCurrentBuild(){
        if(currentBuild == null) createBoard();
        return currentBuild;
    }
    private void createBoard(){
        currentBuild = new ArrayList<>();
        for(int x = 0; x < width; x++){
            ArrayList<BoardBuildElement> line = new ArrayList<>();
            currentBuild.add(line);
            for(int y = 0; y < height; y++){
                BoardBuildElement element = new BoardBuildElement();
                line.add(element);
            }
        }
    }
    private boolean resizeBoard(){
        if(currentBuild.size() != width){
            if(currentBuild.size() > width){
                currentBuild.remove(currentBuild.size()-1); //Removes the last arrayList
                return false;
            }
            else if(currentBuild.size() < width){
                ArrayList<BoardBuildElement> line = new ArrayList<>();
                currentBuild.add(line);
                for(int y = 0; y < height; y++){
                    line.add(new BoardBuildElement()); //Adds a line of elements
                }
                return false;
            }
        }
        else if(currentBuild.get(0).size() != height){
            if(currentBuild.get(0).size() > height){
                for(ArrayList<BoardBuildElement> arr : currentBuild){
                    arr.remove(arr.size()-1); //Removes the last element in each arrayList
                }
                return false;
            }
            else if(currentBuild.get(0).size() < height){
                for(ArrayList<BoardBuildElement> arr : currentBuild){
                    arr.add(new BoardBuildElement()); //Adds an element in each arrayList
                }
                return false;
            }
        }
        return true;
    }
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        resizeBoard();
    }

}
