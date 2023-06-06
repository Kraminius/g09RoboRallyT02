/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.MyClient;
import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameLoader;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.Load;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.LoadInstance;
import dk.dtu.compute.se.pisd.roborally.UpdateInstance;
import dk.dtu.compute.se.pisd.roborally.view.BoardLoadWindow;
import dk.dtu.compute.se.pisd.roborally.SaveAndLoad.GameSave;
import dk.dtu.compute.se.pisd.roborally.model.*;

import dk.dtu.compute.se.pisd.roborally.view.LoadGameWindow;
import dk.dtu.compute.se.pisd.roborally.view.Option;
import dk.dtu.compute.se.pisd.roborally.view.StartPositionWindow;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final public List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final public RoboRally roboRally;

    private GameController gameController;

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();




        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }
            BoardLoadWindow boardLoadWindow = new BoardLoadWindow();
            String boardInput = boardLoadWindow.getBoardInput();
            Board board = new Board(boardInput);
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1), i+1);
                player.setEnergyCubes(5);
                gameController.fillStartDeck(player.getCardDeck());
                board.addPlayer(player);


                //player.setSpace(board.getSpace(i % board.width, i));
            }

            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            //gameController.startProgrammingPhase();
            board.setCurrentPlayer(board.getPlayer(0));
            roboRally.createBoardView(gameController);
            StartPositionWindow positionWindow = new StartPositionWindow();
            positionWindow.getStartSpaces(board);


            gameController.startUpgradePhase();

        }
    }



    public void saveGame() {
        // XXX needs to be implemented eventually
        String saveName = "";
        boolean nameChecksOut;
        do{
            Option option = new Option("Write the name of the save.");
            saveName = option.getPromptedAnswer("eg. mySaveFile");
            if(saveName.equals("")) nameChecksOut = false;
            else nameChecksOut = true;
        }while(!nameChecksOut);

        System.out.println("Saving game under name: " + saveName);

        GameSave gameSave = new GameSave();
        gameSave.saveGame(this.gameController, saveName);

    }

    public void loadGame() {
        // XXX needs to be implemented eventually
        // for now, we just create a new game

        LoadGameWindow load = new LoadGameWindow();
        String saveName = load.getLoadInput();
        System.out.println("Loading " + saveName);
        gameController = GameLoader.loadGame(saveName, this);

        if(gameController == null) {
            newGame();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {
            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }


    public void exit() {
        if (gameController != null) {
            Option option = new Option("Are you sure you want to exit RoboRally?");
            String[] options = new String[3];
            options[0] = "Save and Exit";
            options[1] = "Exit without Saving";
            options[2] = "Cancel";
            String answer = option.getChoice(options);

            switch (answer){
                case "Save and Exit":
                    saveGame();
                    break;
                case "Exit without Saving":
                    stopGame();
                    break;
                case "Cancel":
                    //Do nothing, menu closes
                    return;
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }

    //Instantiate a gameState
    public void instantiateGame(){
        //we need to make a Load
        GameSave gameSave = new GameSave();
        Load loadGame = GameLoader.loadData(gameSave.jsonGame(gameController));
        try {
            MyClient.instantiateGame(loadGame);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Updating game
    public void updateGame(){

        try {
            LoadInstance.load(this,MyClient.update());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
