package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**@author Nicklas Christensen    s224314@dtu.dk
 *
 * This class if for storing information for the checkpoints on a given space, it has numerical order
 * in which it must be collected
 */
public class Checkpoint {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
