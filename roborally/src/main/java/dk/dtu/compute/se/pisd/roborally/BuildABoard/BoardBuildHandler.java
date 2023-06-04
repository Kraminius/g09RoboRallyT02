package dk.dtu.compute.se.pisd.roborally.BuildABoard;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BoardBuildHandler {

    private BoardBuild boardBuild;
    private BuildABearViewController view;



     public BoardBuildHandler(){
         boardBuild = new BoardBuild();
         view = new BuildABearViewController(this);
         updateBoard();
         view.show();
     }

     public void showElementMenuFor(int x, int y){
         removeMarkers();
         boardBuild.getCurrentBuild().get(x).get(y).setShowing(true);
         updateBoard();
         view.updateElementGrid(boardBuild.getCurrentBuild().get(x).get(y));
     }
     public void elementClicked(int index){

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
