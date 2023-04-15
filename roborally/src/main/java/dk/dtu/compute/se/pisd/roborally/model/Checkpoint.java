package dk.dtu.compute.se.pisd.roborally.model;

/**@author Nicklas Christensen    s224314@dtu.dk
 *
 * This class if for storing information for the checkpoints on a given space, it has numerical order
 * in which it must be collected
 */
public class Checkpoint {
    public int number;

    public Checkpoint(int number){
        this.number = number;
    }
}
