package dk.dtu.compute.se.pisd.roborally.MainMenu;

import dk.dtu.compute.se.pisd.roborally.BuildABoard.BoardBuildHandler;
import dk.dtu.compute.se.pisd.roborally.BuildABoard.CheckLogic;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.JSONHandler;
import dk.dtu.compute.se.pisd.roborally.view.Option;

public class MainMenuHandler {

    private MainMenuController controller;
    private String toDo = "";

    /**
     * @Author Tobias Gørlyk s224271
     * makes sure the viewController isn't null
     * and then shows it
     *
     */
    public String show(){
        if(controller==null){
            controller = new MainMenuController(this);
        }
        controller.showMain();
        controller.show();
        return toDo;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * when user has pressed play game we close the main menu and starts the game
     */
    public void playGame(){
        controller.close();
        toDo = "play";
    }
    /**
     * @Author Tobias Gørlyk s224271
     * show the board menu
     */
    public void showBoardMenu(){
        controller.showBoardBuilderMenu();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * show the current boards on in the files so the user can choose which to edit or delete
     */
    public void showLoadBoardMenu(){
        controller.showBoardLoaderMenu();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * goes back to the main menu
     */
    public void backToMenu(){
        controller.showMain();
    }
    /**
     * @Author Tobias Gørlyk s224271
     * exits the game
     */
    public void exit(){
        System.exit(0);
    }
    /**
     * @Author Tobias Gørlyk s224271
     * starts editing a new board
     */
    public void editNewBoard(){
        controller.close();
        toDo = "createBoard";
    }
    /**
     * @Author Tobias Gørlyk s224271
     * starts editing a board from the files
     * @param name name of file
     */
    public void editLoadedBoard(String name){
        controller.close();
        toDo = "load;" + name;
    }
    /**
     * @Author Tobias Gørlyk s224271
     * Deletes a board from the files
     * @param name the name of the file to delete
     */
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
