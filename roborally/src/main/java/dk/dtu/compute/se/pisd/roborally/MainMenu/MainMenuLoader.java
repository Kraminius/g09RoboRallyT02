package dk.dtu.compute.se.pisd.roborally.MainMenu;

import dk.dtu.compute.se.pisd.roborally.BuildABoard.BoardBuildHandler;

public class MainMenuLoader {

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
                    buildHandler.loadBoard(name);
                }
                return false;
        }
    }

}
