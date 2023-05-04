package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class PlayerLoader {

    JSONHandler json = new JSONHandler();

    private static PlayerLoader playerLoader;

    public static PlayerLoader getInstance(){
        if(playerLoader == null) playerLoader = new PlayerLoader();
        return playerLoader;
    }


    /** @Author Nicklas Christensen - s224314@dtu.dk
     * Loads a single player from files and inserts each key and their value into the player.
     * @param name JSON file name
     * @param player the player that it should be loaded into
     * @return the Player that is saved with the id
     */
    public boolean loadPlayer(String name, Player player){
        JSONObject obj = json.load(name);
        if(obj == null) return false;
        Board board = player.board;
        for(Object key : obj.keySet()){
            insert(board, player, obj.get(key), (String) key);
        }
        return true;
    }


    /*
    This version didnt use player as parameter and rather placed them in the board given
    public boolean loadPlayer(String name, Board board){
        JSONObject obj = json.load(name);
        if(obj == null) return false;
        String pColor = "Stand in";
        String pName = "Stand in";
        Player player = new Player(board,pColor,pName);
        board.addPlayer(player);
        for(Object key : obj.keySet()){
            insert(board, player, obj.get(key), (String) key);
        }
        return true;
    }
     */

    /**@Author Nicklas Christensen - s224314@dtu.dk
     * Goes through all the keys and adds their values to a space
     * @param p the player that the changes should be made to
     * @param value the Object that comes with the key, this is cast onto different values.
     * @param varName The name of the change we should make.
     */
    private void insert(Board b, Player p, Object value, String varName){

        if(value == null) return;

        switch (varName){
            case "name":
                p.setName((String) value);
                break;
            case "color":
                p.setColor((String) value);
                break;
                //This needs testing
            case "space":
                ArrayList<String> space = getList((JSONArray) value);
                for(int i = 0; i < space.size(); i++){
                    String[] values = space.get(i).split(";");
                    int x = parseInt(values[0]);
                    int y = parseInt(values[1]);
                    p.setSpace(b.spaces[x][y]);
                }
                break;
            case "heading":
                Heading h = getHeading((String) value);
                p.setHeading(h);
                break;
            //These three cases needs testing
            case "program":
                ArrayList<String> program = getList((JSONArray) value);
                for(int i = 0; i < program.size(); i++){
                    String[] values = program.get(i).split(";");
                    //Getting the CommandCard and so
                    p.setProgramField(i, getCommandCardField((String) values[1], p));
                    p.getProgramField(i).setVisible(getBoolean((String)values[0]));
                }
                break;
            case "cards":
                ArrayList<String> cards = getList((JSONArray) value);
                for(int i = 0; i < cards.size(); i++){
                    String[] values = cards.get(i).split(";");
                    //Getting the CommandCard and so
                    p.setCardField(i, getCommandCardField((String) values[1], p));
                    p.getCardField(i).setVisible(getBoolean((String)values[0]));
                }
                break;
            case "checkpointsReadhed":
                ArrayList<String> checkpoints = getList((JSONArray) value);
                for(int i = 0; i < checkpoints.size(); i++){
                    boolean check = getBoolean(checkpoints.get(i));
                    p.setCheckpointReadhed(i, check);
                }
                break;

}}


    public Heading getHeading(String heading){
        switch (heading){
            case "SOUTH":
                return Heading.SOUTH;
            case "WEST":
                return Heading.WEST;
            case "NORTH":
                return Heading.NORTH;
            case "EAST":
                return Heading.EAST;
        }
        return null;
    }

    public CommandCardField getCommandCardField(String commandCardField, Player p){
               CommandCardField c = new CommandCardField(p);
               c.setCard(getCommandCard(commandCardField));
                return c;
    }

    public CommandCard getCommandCard(String commandCard){
                CommandCard c = new CommandCard(getCommand(commandCard));
                return c;
    }

    public Command getCommand(String command){
        switch(command){
            //Might have to switch for displayName
            case "FORWARD":
                return Command.FORWARD;
            case "RIGHT":
                return Command.RIGHT;
            case "LEFT":
                    return Command.LEFT;
            case "FAST_FORWARD":
                    return Command.FAST_FORWARD;
            case "U_TURN":
                    return Command.U_TURN;
            case "BACK_UP":
                    return Command.BACK_UP;
            case "AGAIN":
                    return Command.AGAIN;
            case "OPTION_LEFT_RIGHT":
                    return Command.OPTION_LEFT_RIGHT;
        }
        return null;
    }

    public boolean getBoolean(String check){
        switch (check){
            case "true":
                return true;
            case "false":
                return false;
        }
        return false;
    }

    private ArrayList<String> getList(JSONArray arr){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < arr.size(); i++){
            list.add((String) arr.get(i));
        }
        return list;
    }
    //This testfunktion has not been used
    public void testLoad(String name, Player player){
        ReaderAndWriter raw = new ReaderAndWriter();
        JSONHandler jsonHandler = new JSONHandler();

        System.out.println("We First have: "+player.getName()+ ". " + player.getColor()+ ". "
        +player.getSpace()+". "+player.getHeading()+". "+player.getProgramField(0)+". "
        +player.getCardField(0)+". "+player.getCheckpointReadhed());
        //Now we run load
        loadPlayer(name, player);
        System.out.println("We First have: "+player.getName()+ ". " + player.getColor()+ ". "
                +player.getSpace()+". "+player.getHeading()+". "+player.getProgramField(0)+". "
                +player.getCardField(0)+". "+player.getCheckpointReadhed());
        loadPlayer(name, player);
    }

}
