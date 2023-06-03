package dk.dtu.compute.se.pisd.roborally.Server;

import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PlayerProgramService {

    public int numberOfPlayers;
    ArrayList<Integer> players = new ArrayList<>();
    ArrayList<CommandCardField> commandCardFields = new ArrayList<>();
    ArrayList<ArrayList<CommandCardField>> allCommandCardFields = new ArrayList<>();

    ArrayList<Boolean> playersDone = new ArrayList<>();

    public PlayerProgramService(){

    }

    public boolean addProgram(CommandCardField commandCardField, int id){
        allCommandCardFields.get(id).add(commandCardField);
        playersDone.add(id, true);

        //Check to see if all players are done
        for(int i = 0; i < numberOfPlayers; i++){
            if(playersDone.get(i) == false){
                return true;
            }
        }
        //If all are done will we send it to the others
        sendAllPrograms();
        return true;
    }

    //A method sending all CommandCardFields to all player
    public void sendAllPrograms(){
        for(int i = 0; i < numberOfPlayers; i++){
            int id = players.get(i);

        }
    }

}
