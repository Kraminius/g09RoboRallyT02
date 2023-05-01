package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

/**@author Nicklas Christensen    s224314@dtu.dk
 *
 * This class if for storing information for the checkpoints on a given space, it has numerical order
 * in which it must be collected
 */
public class PriorityAntenna {

    //The antenna should not be reachable
    public Heading[] wallHeadings = {NORTH, EAST, WEST, SOUTH};
}
