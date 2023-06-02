package dk.dtu.compute.se.pisd.roborally.APITests;


import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramCards implements IProgramCards {

    List<Player> players = new ArrayList<>();

    public ProgramCards(Board board) {
        for(int i = 0; i < board.getPlayersNumber(); i++){
            players.add(board.getPlayer(i));}
    }

    @Override
    public List<Command> getComCard(int playerNumb) {
        List list = null;
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
