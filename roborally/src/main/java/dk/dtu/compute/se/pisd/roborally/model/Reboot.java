package dk.dtu.compute.se.pisd.roborally.model;

public class Reboot extends Space {

    /**An abstract variation of the class Space. Created the have specific spaces made for respawning players.
     *
     * @param board the board on which the Reboot space exists
     * @param x it's x coordinate
     * @param y it's y coordinate
     * @param wallHeading is NULL on creation - should not be possible to be anything else (must be implemented at some point)
     */
    public Reboot(Board board, int x, int y, Heading[] wallHeading){
        super(board, x, y, wallHeading);
    }
}
