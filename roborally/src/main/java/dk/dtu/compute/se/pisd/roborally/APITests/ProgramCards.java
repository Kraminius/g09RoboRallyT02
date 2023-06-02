package dk.dtu.compute.se.pisd.roborally.APITests;


import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.*;
import dk.dtu.compute.se.pisd.roborally.model.SpaceElements.SpaceElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static dk.dtu.compute.se.pisd.roborally.model.Command.FORWARD;
import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

@Service
public class ProgramCards implements IProgramCards {

    List<Player> players = new ArrayList<>();
    Activator activator;
    GameController gameController;

    public ProgramCards() {


        activator = new Activator();
        //programCards = new ProgramCards(gameController.board);

        Board board = new Board();
        board.width = 8;
        board.height = 8;
        board.spaces = new Space[board.width][board.height];
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                board.spaces[x][y] = new Space(board, x, y);
            }
        }


        SpaceElement respawn = new SpaceElement();
        respawn.setRespawn(true);
        board.getSpace(4, 4).setElement(respawn);

        gameController = new GameController(board);

        Player player1 = new Player(board, "red", "First", 0);
        Player player2 = new Player(board, "yellow", "Second", 1);
        Player player3 = new Player(board, "blue", "Third", 2);

        players.add(player1);
        players.add(player2);
        players.add(player3);


        board.setCurrentPlayer(player1);

        CommandCardField commandCardField = new CommandCardField(player1);
        CommandCard commandCard = new CommandCard(FORWARD);
        commandCardField.setCard(commandCard);
        player1.setProgramField(0,commandCardField);

        //board.getPlayer(0).setProgramField(0, new CommandCardField(board.getPlayer(0)).setCard(new CommandCard(FORWARD)));

    }

    @Override
    public List<Command> getComCard(int playerNumb) {
        List<Command> list = new ArrayList<>();
        for(int i = 0; i < players.get(playerNumb).getProgram().length; i++) {
            list.add(players.get(i).getProgram()[i].getCard().command);
        }
        return list;
    }

    @Override
    public Command getComCardSpecific(int playerNumb, int number){
        return players.get(playerNumb).getProgram()[number].getCard().command;
    }


}
