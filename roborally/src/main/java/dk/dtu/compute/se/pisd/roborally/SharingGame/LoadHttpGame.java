package dk.dtu.compute.se.pisd.roborally.SharingGame;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.LoadInstance;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;

public class LoadHttpGame {

    AppController appController;

    public LoadHttpGame(AppController appController){
        this.appController = appController;
    }

    public boolean LoadRetrievedGame(Load load){
        LoadInstance.load(appController, load);
        return true;
    }

}
