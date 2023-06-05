package dk.dtu.compute.se.pisd.roborally.MainMenu;

import dk.dtu.compute.se.pisd.roborally.BuildABoard.BoardBuildHandler;
import dk.dtu.compute.se.pisd.roborally.BuildABoard.CheckLogic;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.view.Option;

public class MainMenuHandler {

    private MainMenuController controller;
    private BoardBuildHandler buildHandler;
    private int toDo = 0;

    public MainMenuHandler(BoardBuildHandler buildHandler) {
        this.buildHandler = buildHandler;
    }

    public int show(){
        if(controller==null){
            controller = new MainMenuController(this);
        }
        controller.showMain();
        controller.show();
        return toDo;
    }
    public void playGame(){
        controller.close();
    }
    public void showBoardMenu(){
        controller.showBoardBuilderMenu();
    }
    public void showLoadBoardMenu(){
        controller.showBoardLoaderMenu();
    }
    public void backToMenu(){
        controller.showMain();
    }
    public void exit(){
        System.exit(0);
    }
    public void editNewBoard(){
        controller.close();
        buildHandler.createNewBoard();
        show();
    }
    public void editLoadedBoard(String name){
        controller.close();

        show();
    }
    public void deleteLoadedBoard(String name){
        if(CheckLogic.isOriginal(name) != null) {
            Option option = new Option("Error");
            option.getOKPressed("You cannot delete an original map.");
            return;
        }
        Option option = new Option("Delete " + name + "?");
        if(option.getYESNO("Are you certain you want to delete the map: " + name + "?")){
            JSONHandler jsonHandler = new JSONHandler();
            jsonHandler.deleteFile(name, "board");
        }
    }


}
