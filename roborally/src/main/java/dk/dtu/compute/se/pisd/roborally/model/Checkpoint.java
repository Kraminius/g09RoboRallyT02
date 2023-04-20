package dk.dtu.compute.se.pisd.roborally.model;

/**@author Nicklas Christensen    s224314@dtu.dk
 *
 * This abstract class if for storing information for the checkpoints on a given space, it has numerical order
 * in which it must be collected
 */
public class Checkpoint extends Space {
    public int number;

    public Checkpoint(Board board, int x, int y, Heading[] wallHeading, int number){
        super(board, x, y, wallHeading);
        this.number = number;
    }

    public int getCheckpointNumber(){
        return number;
    }
}
