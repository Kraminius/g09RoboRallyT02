package dk.dtu.compute.se.pisd.roborally.BuildABoard;

public class BoardBuildHandler {

    private BoardBuild boardBuild;
    private BuildABearViewController view;



     public BoardBuildHandler(){
         boardBuild = new BoardBuild();
         view = new BuildABearViewController(this);
         view.show();
     }


    public void updateBoard(){

    }
}
