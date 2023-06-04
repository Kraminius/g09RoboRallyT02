package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BoardBuildHandler {

    private BoardBuild boardBuild;
    private BuildABoardViewController view;



    public BoardBuildHandler(){
        boardBuild = new BoardBuild();
        view = new BuildABoardViewController(this);
        updateBoard();
        view.show();
    }

    public void showElementMenuFor(int x, int y){
        removeMarkers();
        boardBuild.getCurrentBuild().get(x).get(y).setShowing(true);
        updateBoard();
        view.updateElementGrid(boardBuild.getCurrentBuild().get(x).get(y));
    }

    public void elementClicked(int type, int x, int y){
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        BoardBuildElement element = build.get(x).get(y);
        BoardBuildLogic.addIfNotExistent(type, element);
        updateBoard();
        if(BoardBuildLogic.shouldShowTurns(type)){
            view.showLeftRight(BoardBuildLogic.shouldShowTurns(type));
        }
        if(BoardBuildLogic.hasVariant(type)) view.updateElementVariants(element, type);
        else showElementMenuFor(x, y);
    }
    public void elementVariantClicked(int type, int value, int x, int y){
        System.out.println(type + "    " + value + " [" + x + ", " + y + "]");
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        BoardBuildElement element = build.get(x).get(y);
        BoardBuildLogic.changeElementVariant(element, type, value);
        updateBoard();
        view.updateElementVariants(element, type);
    }
    public void turn(boolean direction, BoardBuildElement element, int type){
        BoardBuildLogic.turn(element, type, direction);
        updateBoard();
    }
    private void removeMarkers(){
        ArrayList<ArrayList<BoardBuildElement>> build = boardBuild.getCurrentBuild();
        for(ArrayList<BoardBuildElement> arrayList : build){
            for(BoardBuildElement element : arrayList){
                element.setShowing(false);
            }
        }
    }

    public void updateBoard(){
        view.updateGrid(boardBuild.getCurrentBuild());
    }
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
}
