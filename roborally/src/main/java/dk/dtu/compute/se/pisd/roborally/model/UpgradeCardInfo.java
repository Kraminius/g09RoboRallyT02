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
            default: return 1;
        }
    }
}
