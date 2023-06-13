package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import dk.dtu.compute.se.pisd.roborally.view.Option;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BoardBuildHandler {

    private BoardBuild boardBuild;
    private BuildABoardView view;

    /**
     * @Author Tobias Gørlyk s224271
     * Creates a new BoardBuild and a new View, we then update the board and tell the view to show it.
     */
    public void createNewBoard(){
        boardBuild = new BoardBuild();
        view = new BuildABoardView(this);
        updateBoard();
        view.show();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Creates a new BoardBuild initiates the board, then loads a board into it and creates the view along with setting the name and size of the board,
     * to what is in the loaded board. Finishing off with showing the board.
     * @param name the name of the board to load.
     */
    public void loadBoard(String name){
        boardBuild = new BoardBuild();
        boardBuild.getCurrentBuild();
        boardBuild.loadBoard(name);
        view = new BuildABoardView(this);
        updateBoard();
        view.setBoardName(name);
        view.setSizeText(boardBuild.getWidth(), boardBuild.getHeight());
        view.show();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Saves the board under a name by first checking the name if it's available and under correct format throwing an error to the player if done wrong.
     * Otherwise, it checks the board if it is formatted correctly and at last saves the board.
     * @param name Name of the save
     */
    public void saveBoard(String name){
        String nameError = CheckLogic.checkName(name);
        if(nameError == null){
            boardBuild.setName(name);
            CheckBoardBuild checkBoardBuild = new CheckBoardBuild();
            String error = checkBoardBuild.checkBoard(boardBuild);
            if(error == null){
                checkBoardBuild = checkBoardBuild.getSortedBoardBuild(boardBuild);
                BuildJSON.saveBoard(checkBoardBuild);
            }
            else{
                view.showError(error);
            }
        }
        else{
            view.showError(nameError);
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * shows the different elements the player can put onto a field, by opening a small menu on the right side of the screen.
     * it needs to know which element of the build that it should edit so we take the x and y coordinates of the field.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    public void showElementMenuFor(int x, int y){
        removeMarkers();
        boardBuild.getCurrentBuild().get(x).get(y).setShowing(true);
        updateBoard();
        view.updateElementGrid(boardBuild.getCurrentBuild().get(x).get(y));
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Changes the field at the coordinates to add the element type to that field.
     * Example: Adding a laser to a field that might already contain something like a wall.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param type the type of element
     */
    public void elementClicked(int type, int x, int y){
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        BoardBuildElement element = build.get(x).get(y);
        BoardBuildLogic.addIfNotExistent(type, element, boardBuild);
        updateBoard();
        if(BoardBuildLogic.isTurnAble(type)){
            view.showLeftRight(BoardBuildLogic.isTurnAble(type));
        }
        else view.showLeftRight(false);
        if(BoardBuildLogic.hasVariant(type)) view.updateElementVariants(element, type);
        else showElementMenuFor(x, y);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Changes the field at the coordinates to change the element of the same type to that field.
     * Example: Changing the green belt type to be a right-turning belt instead of left turning.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param type the type of element
     * @param value the type variant
     */
    public void elementVariantClicked(int type, int value, int x, int y){
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        BoardBuildElement element = build.get(x).get(y);
        BoardBuildLogic.changeElementVariant(element, type, value);
        updateBoard();
        view.updateElementVariants(element, type);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Turns the type on the element specifically either left or right.
     * Example: Turns the wall on the element 90 degrees to the right, but the belt on the same element still faces forward.
     * @param type type of element
     * @param direction true = Right, false = left
     * @param element the element that has the type on it.
     */
    public void turn(boolean direction, BoardBuildElement element, int type){
        BoardBuildLogic.turn(element, type, direction);
        updateBoard();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Remove the marker that is coloring the edge of the chosen field, to choose a new field.
     */
    private void removeMarkers(){
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        for(ArrayList<BoardBuildElement> arrayList : build){
            for(BoardBuildElement element : arrayList){
                element.setShowing(false);
            }
        }
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Updates the grid on the board with the current build
     */
    public void updateBoard(){
        view.updateGrid(boardBuild.getCurrentBuild());
    }
    /**
     * @Author Tobias Gørlyk s224271
     * checks the string input to see if they are numbers and if they are of the right size, throws error if they are not.
     * Otherwise it parses the string and resizes the build and finishes by updating the board.
     * @param input a String array that must have two elements, and x and y value. 1 < x < 25, 1 < y < 16.
     */
    public void resizeBoard(String[] input){

        String error = BoardBuildLogic.checkSizeInput(input);
        if(error != null){
            view.showError(error);
            return;
        }
        int x = parseInt(input[0]);
        int y = parseInt(input[1]);

        boardBuild.setSize(x, y);
        updateBoard();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * creates the option for yes or no when exit is pressed or when a close request has happened on the board window.
     * If they choose yes then the window closes.
     */
    public void exit(){
        Option option = new Option("Exit Board Builder?");
        if(option.getYESNO("Are you sure you want to exit the board builder? \nUnsaved work will be lost.")){
            view.close();
        }
    }
}
