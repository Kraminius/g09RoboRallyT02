package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import org.json.simple.JSONArray;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class Converter {
    /**
     * @param bool a String boolean that can be either: TRUE, True, true, FALSE, False or false. They are then converted from string into either true or false.
     * @Author Tobias Gørlyk - s224271@dtu.dk
     * @return returns true or false depending on input. If input is unrecognised false is also returned.
     */
    public static boolean getBool(String bool){
        switch (bool){
            case "TRUE":
            case "True":
            case "true": return true;
            case "FALSE":
            case "False":
            case "false": return false;
        }
        return false;
    }

    public static Heading getHeading(String heading){
        switch (heading){
            case "SOUTH": return Heading.SOUTH;
            case "WEST": return Heading.WEST;
            case "NORTH": return Heading.NORTH;
            case "EAST": return Heading.EAST;
        }
        return null;
    }
    public static Phase getPhase(String phase){
        switch (phase){
            case "PROGRAMMING": return Phase.PROGRAMMING;
            case "PLAYER_INTERACTION": return Phase.PLAYER_INTERACTION;
            case "ACTIVATION": return Phase.ACTIVATION;
            case "UPGRADE": return Phase.UPGRADE;
            case "INITIALISATION": return Phase.INITIALISATION;
        }
        return null;
    }
    public static Command getCommand(String command){
        switch (command.toUpperCase()){
            //programming cards
            case "AGAIN": return Command.AGAIN;
            case "BACK_UP": return Command.BACK_UP;
            case "LEFT": return Command.LEFT;
            case "RIGHT": return Command.RIGHT;
            case "FORWARD": return Command.FORWARD;
            case "FAST_FORWARD": return Command.FAST_FORWARD;
            case "SPRINT_FORWARD": return Command.SPRINT_FORWARD;
            case "U_TURN": return Command.U_TURN;
            case "MOVE_LEFT": return Command.MOVE_LEFT;
            case "MOVE_RIGHT": return Command.MOVE_RIGHT;
            case "POWER_UP": return Command.POWER_UP;
            case "OPTION_LEFT_RIGHT": return Command.OPTION_LEFT_RIGHT;
            //damage cards
            case "SPAM": return Command.SPAM;
            case "WORM": return Command.WORM;
            case "VIRUS": return Command.VIRUS;
            case "TROJAN_HORSE": return Command.TROJAN_HORSE;
            //upgrade cards
            case "RAMMING_GEAR_PUPG": return Command.RAMMING_GEAR_PUPG;
            case "SPAM_BLOCKER_TUPG": return Command.SPAM_BLOCKER_TUPG;
            case "ENERGY_ROUTINE_TUPG": return Command.ENERGY_ROUTINE_TUPG;
            case "SPAM_FOLDER_TUPG": return Command.SPAM_FOLDER_TUPG;
            case "SPAM_FOLDER": return Command.SPAM_FOLDER;
            case "SANDBOX": return Command.SANDBOX;
            case "WEASEL": return Command.WEASEL;
            case "SANDBOX_UPG": return Command.SANDBOX_UPG;
            case "SPEED": return Command.SPEED;
            case "SPEED_TUPG": return Command.SPEED_TUPG;
            case "ENERGY": return Command.ENERGY;
            case "RECOMPILE_TUPG": return Command.RECOMPILE_TUPG;
            case "RECHARGE_TUPG": return Command.RECHARGE_TUPG;
            case "HACK_TUPG": return Command.HACK_TUPG;
            case "ZOOP_TUPG": return Command.ZOOP_TUPG;
            case "REPEAT_ROUTINE_TUPG": return Command.REPEAT_ROUTINE_TUPG;
            case "DEFRAG_GIZMO_PUPG": return Command.DEFRAG_GIZMO_PUPG;
            case "BOINK_TUPG": return Command.BOINK_TUPG;
            case "DOUBLE_BARREL_LASER_PUGB": return Command.DOUBLE_BARREL_LASER_PUGB;
        }
        return null;
    }
    public static Command[] getCommands(String[] commands){
        if(commands == null){
            return null;
        }
        Command[] toReturn = new Command[commands.length];
        for(int i = 0; i < commands.length; i++){
            toReturn[i] = getCommand(commands[i]);
        }
        return toReturn;
    }
    public static String[][] splitSeries(String[] arr, String splitter){
        //Count splitters + 1
        int amount = 0;
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(splitter)) amount++;
        }
        String[][] toReturn = new String[amount][];
        ArrayList<String> list = new ArrayList<>();
        amount = 0;
        for(int i = 1; i < arr.length; i++){
            if(arr[i].equals(splitter)){
                toReturn[amount] = list.toArray(new String[list.size()]);
                amount++;
                list.clear();
            }
            else if(i == arr.length-1){
                list.add(arr[i]);
                toReturn[amount] = list.toArray(new String[list.size()]);
            }
            else{
                list.add(arr[i]);
            }
        }
        return toReturn;
    }

    public static int[] jsonArrToInt(JSONArray jsonArray){
        int[] toReturn = new int[jsonArray.size()];
        for(int i = 0; i < jsonArray.size(); i++){
            toReturn[i] = parseInt(String.valueOf(jsonArray.get(i)));
        }
        return toReturn;
    }
    public static String[] jsonArrToString(JSONArray jsonArray){
        String[] toReturn = new String[jsonArray.size()];
        for(int i = 0; i < jsonArray.size(); i++){
            toReturn[i] = String.valueOf(jsonArray.get(i));
        }
        return toReturn;
    }


}
