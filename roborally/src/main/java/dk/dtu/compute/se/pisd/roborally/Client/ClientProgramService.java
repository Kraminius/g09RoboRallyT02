package dk.dtu.compute.se.pisd.roborally.Client;

import dk.dtu.compute.se.pisd.roborally.Server.ServerSend;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClientProgramService {

    public static boolean goActivate = false;

    public static ArrayList<ArrayList<CommandCardField>> allCommandCardFiels;

    //We need a way to save players and their URI
    public int numberOfPlayers;
    ArrayList<Integer> players = new ArrayList<>();

    public ClientProgramService(){

    }

    /**
     * Here we change to variables so that the player can recieve all CommandCardFiels
     * and know that they are allowed to proceed to activate
     * @param commandCardField
     * @return
     */
    public boolean addAllProgram(ArrayList<ArrayList<CommandCardField>> commandCardField){
        allCommandCardFiels = commandCardField;
        goActivate = true;
        return true;
    }

    public static ArrayList<ArrayList<CommandCardField>> getCommandCards(){
        return allCommandCardFiels;
    }

    public static boolean proceedToActivate(){
        return goActivate;
    }


}
