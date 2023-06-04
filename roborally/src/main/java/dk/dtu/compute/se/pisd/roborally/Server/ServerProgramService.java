package dk.dtu.compute.se.pisd.roborally.Server;

import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ServerProgramService {

    //We need a way to save players and their URI and the number of players
    public int numberOfPlayers = 2;
    ArrayList<Integer> players = new ArrayList<>();
    ArrayList<String> playersURI = new ArrayList<>();
    ArrayList<CommandCardField> commandCardFields = new ArrayList<>();
    ArrayList<ArrayList<CommandCardField>> allCommandCardFields = new ArrayList<>();

    ArrayList<Boolean> playersDone = new ArrayList<>();

    public ServerProgramService(){

    }

    /**
     * Adds the players programCardField to the array of them all
     * @param commandCardField all the programCards of a single player(not all players)
     * @param id which player we have recieved cards from
     * @return  Wether we recieved them and all is good or not
     */
    public boolean addProgram(ArrayList<CommandCardField> commandCardField, int id){
        for(int i = 0; i < commandCardField.size(); i++){
            allCommandCardFields.get(id).add(commandCardField.get(i));
        }
        players.add(id);
        //Vi kan gøre så den add true på den specifike id
        //Så skal den næste for også bare ændres da id starter fra 1 ikke 0. (i=1 og i<=numberOfPlayers)
        playersDone.add(true);

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
            ServerSend.sendProgram(allCommandCardFields, id, "dummy" /* playersURI.get(i)*/);
        }
    }

    /*
    public static void goActivate(){
        for(int i = 0; i < numberOfPlayers)
    }

     */

}
