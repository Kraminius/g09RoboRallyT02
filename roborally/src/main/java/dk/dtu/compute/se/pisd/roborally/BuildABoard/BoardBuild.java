package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

public class BoardBuild {
    private String name;
    private int width = 10;
    private int height = 10;
    private ArrayList<ArrayList<BoardBuildElement>> currentBuild;

    /**
     * @Author Tobias Gørlyk s224271
     * We get the build as it is currently. If there are no build we create a new build with the createBoard method.
     * @return ArrayList<ArrayList<BoardBuildElement>> - a Two-Dimensional ArrayList of BoardBuildElements which holds the different elements of the board.
     */
    public ArrayList<ArrayList<BoardBuildElement>> getCurrentBuild(){
        if(currentBuild == null) createBoard();
        return currentBuild;
    }

    /**
     * @Author Tobias Gørlyk s224271
     * creates a new Two-Dimensional ArrayList of BoardBuildElements which holds the data of the board and works like a grid with x and y values.
     * it ends by setting the coordinates to the board so we can use them.
     */
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
        setCoords();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * This method loads a board by sending itself to a new JSONtoBuild along with the name of the board that needs to be loaded.
     * The JSONtoBuild resizes the build, names it and adds the elements that were on the loaded board so we can edit this one.
     * @param name the name of the board that needs to be loaded, at this point it should already have been checked to see if it's an existing board.
     */
    public void loadBoard(String name){
        System.out.println("Loading: " + name);
        JSONtoBuild jsonToBuild = new JSONtoBuild(name, this);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * This methods goes through the current build and makes sure all elements has their correct x and y coordinates,
     * which we need to know when saving or placing some of the elements.
     */
    private void setCoords(){
        for(int x = 0; x < currentBuild.size(); x++){
            for(int y = 0; y < currentBuild.get(0).size(); y++){
                currentBuild.get(x).get(y).setX(x);
                currentBuild.get(x).get(y).setY(y);
            }
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * This method is a recursive method that resizes the board. It does this by check if it's the right amount of columns, and rows.
     * If there are too many rows or columns one row or column is removed and the method is called again.
     * If there are too few rows or columns one row or column is added and the method is called again.
     * The method stops the recursive loop once the amount of columns and rows add up with the width and height of the board.
     * By doing it this way it keeps the original values of the current array and we only need to delete data that is outside the bounds of the board.
     * This way we can resize the board while data is stored on it.
     */
    private void resizeBoard(){
        if(currentBuild.size() != width){
            if(currentBuild.size() > width){
                currentBuild.remove(currentBuild.size()-1); //Removes the last arrayList
                resizeBoard();
            }
            else if(currentBuild.size() < width){
                ArrayList<BoardBuildElement> line = new ArrayList<>();
                currentBuild.add(line);
                for(int y = 0; y < height; y++){
                    line.add(new BoardBuildElement()); //Adds a line of elements
                }
                resizeBoard();
            }
        }
        else if(currentBuild.get(0).size() != height){
            if(currentBuild.get(0).size() > height){
                for(ArrayList<BoardBuildElement> arr : currentBuild){
                    arr.remove(arr.size()-1); //Removes the last element in each arrayList
                }
                resizeBoard();
            }
            else if(currentBuild.get(0).size() < height){
                for(ArrayList<BoardBuildElement> arr : currentBuild){
                    arr.add(new BoardBuildElement()); //Adds an element in each arrayList
                }
                resizeBoard();
            }
        }
        setCoords();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Sets the size of the board by changing the values of width and height and calling the method resizeBoard which enlarges or reduces the size of the board.
     * @param width the chosen width.
     * @param height the chosen height.
     */
    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
        resizeBoard();
    }


    /**
     * @Author Tobias Gørlyk s224271
     * The following few methods are all getters or setters for some of the private variables contained in this class.
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
