package dk.dtu.compute.se.pisd.roborally.model;

public class UpgradeCardInfo {


    /**
     * @author Tobias, Mikkel - s224271@dtu.dk, s224279@dtu.dk
     * Checks the different commands to see if they are permanent upgrade cards.
     * @param command the command for the card you want to check if it is a permanent upgrade card.
     * @return if it is a permanent upgrade card or not.
     */
    public static boolean getPermanent(Command command){
        switch(command){
            case DEFRAG_GIZMO_PUPG:
                return true;
            case RAMMING_GEAR_PUPG:
                return true;
            case SPAM_BLOCKER_TUPG:
                return false;
            case SPAM_FOLDER_TUPG:
                return false;
            case SANDBOX_UPG:
                return false;
            case RECOMPILE_TUPG:
                return false;
            case RECHARGE_TUPG:
                return false;
            case HACK_TUPG:
                return false;
            case ZOOP_TUPG:
                return false;
            case SPEED_TUPG:
                return false;
            case REPEAT_ROUTINE_TUPG:
                return false;
            case BOINK_TUPG:
                return false;
            case DOUBLE_BARREL_LASER_PUGB:
                return true;

            default: return false;
        }
    }
    /**
     * @author Tobias, Mikkel - s224271@dtu.dk, s224279@dtu.dk
     * Checks the different commands to see the prices of the upgrade cards.
     * @param command the command for the card you want to get the price of.
     * @return The price of the card.
     */
    public static int getPrice(Command command){
        switch(command){
            case DEFRAG_GIZMO_PUPG:
                return 5;
            case RAMMING_GEAR_PUPG:
                return 2;
            case SPAM_BLOCKER_TUPG:
                return 3;
            case SPAM_FOLDER_TUPG:
                return 2;
            case SANDBOX_UPG:
                return 5;
            case RECOMPILE_TUPG:
                return 1;
            case RECHARGE_TUPG:
                return 0;
            case HACK_TUPG:
                return 1;
            case ZOOP_TUPG:
                return 1;
            case SPEED_TUPG:
                return 3;
            case REPEAT_ROUTINE_TUPG:
                return 3;
            case BOINK_TUPG:
                return 1;
            case DOUBLE_BARREL_LASER_PUGB:
                return 2;
            default: return 1;
        }
    }
}
