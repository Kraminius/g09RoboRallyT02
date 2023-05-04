package dk.dtu.compute.se.pisd.roborally.model;

public class UpgradeCardInfo {


    /**
     * @author Tobias - s224271@dtu.dk
     * Checks the different commands to see if they are permanent upgrade cards.
     * @param command the command for the card you want to check if it is a permanent upgrade card.
     * @return if it is a permanent upgrade card or not.
     */
    public boolean getPermanent(Command command){
        switch(command){

            default: return false;
        }
    }
    /**
     * @author Tobias - s224271@dtu.dk
     * Checks the different commands to see the prices of the upgrade cards.
     * @param command the command for the card you want to get the price of.
     * @return The price of the card.
     */
    public int getPrice(Command command){
        switch(command){

            default: return 1;
        }
    }
}
