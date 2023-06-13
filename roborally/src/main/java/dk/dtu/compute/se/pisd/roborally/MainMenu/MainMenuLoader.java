package dk.dtu.compute.se.pisd.roborally.MainMenu;

import dk.dtu.compute.se.pisd.roborally.BuildABoard.BoardBuildHandler;

public class MainMenuLoader {

    /**
     * @Author Tobias Gørlyk s224271
     * Runs the main menu, depending on the choice done in the menu, when it closes different things should happen.
     * @return false if main menu should open again, true if the game should start.
     */
    public boolean run(){
        BoardBuildHandler buildHandler = new BoardBuildHandler();
        MainMenuHandler mainMenu = new MainMenuHandler();
        String toDo = mainMenu.show();
        switch (toDo){
            case "play": return true;
            case "createBoard":
                buildHandler.createNewBoard();
                return false;
            default:
                String[] arr = toDo.split(";");
                if(arr[0].equals("load")){
                    String name = "";
                    if (arr.length > 2) {
                        String collectedName = "";
                        for (int i = 1; i < arr.length; i++) {
                            collectedName += arr[i];
                        }
                        name = collectedName;
                    }
                    else name = arr[1];
                    buildHandler.loadBoard(name);
                }
                return false;
        }
    }

}
