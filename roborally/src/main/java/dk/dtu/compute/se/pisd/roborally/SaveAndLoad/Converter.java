package dk.dtu.compute.se.pisd.roborally.SaveAndLoad;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;

public class Converter {
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
        switch (command){
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
        }
        /*

    //SPAM CARDS

    SPAM ("Spam"),

    WORM ("Worm"),

    TROJAN_HORSE("Trojan horse"),

    VIRUS("Virus"),

    //UPGRADE CARDS
    RAMMING_GEAR_PUPG("Ramming Gear\nDeal one SPAM damage card when you push a robot"),
    SPAM_BLOCKER_TUPG("Spam Blocker \nDiscard all spam cards in your hand"),
    ENERGY_ROUTINE_TUPG("Energy Routine \nAdd the Energy Routine Programming card to your discard pile"),
    SPAM_FOLDER_TUPG("Spam Folder\nAdd the Spam Folder card to your discard pile"),
    SPAM_FOLDER("Spam folder\nPermamently discard one spam card from your discard pile"),
    SANDBOX("Sandbox Routine", FORWARD, FAST_FORWARD, SPRINT_FORWARD, BACK_UP, LEFT, RIGHT, U_TURN),
    WEASEL("Weasel routine", LEFT, RIGHT, U_TURN),
    SANDBOX_UPG("Sandbox Routine","Add the Sandbox routine card to your discard pile"),
    //Bug with this card. If you turn right, then use this card. You will move 3 steps forward and 3 steps downwards. ISSUE
    SPEED("Speed routine","Move 3"),
    SPEED_TUPG("Speed Routine","Add the speed routine to your discard pile"),
    ENERGY("Energy","Gain 1 energy"),
    RECOMPILE_TUPG("Recompile ","Discard your entire hand. Draw a new one"),
    RECHARGE_TUPG("Recharge ","Gain three energy"),
    HACK_TUPG("Hack ","Execute current register again"),
    ZOOP_TUPG("Zoop ","Rotate to face any direction"),
    REPEAT_ROUTINE_TUPG("Repeat Routine","Add an again card to your discard pile"),
    DEFRAG_GIZMO_PUPG("Defrag Gizmo","Permanently discards a damage card from your hand"),
    BOINK_TUPG("Boink","Move to an adjacent space. Do not change direction"),
    DOUBLE_BARREL_LASER_PUGB("Double Barrel Laser","Deal one additional SPAM damage card to any robot you shoot.");
         */

        return null;
    }
    public static Command[] getCommands(String[] commands){
        Command[] toReturn = new Command[commands.length];
        for(int i = 0; i < commands.length; i++){
            toReturn[i] = getCommand(commands[i]);
        }
        return toReturn;
    }



}
