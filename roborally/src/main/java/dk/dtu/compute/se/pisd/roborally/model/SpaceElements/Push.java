package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

import java.util.ArrayList;

public class Push {
    private ArrayList<Integer> activateRounds = new ArrayList<>();
    private Heading heading;

    public ArrayList<Integer> getActivateRounds() {
        return activateRounds;
    }

    public void setActivateRounds(ArrayList<Integer> activateRounds) {
        this.activateRounds = activateRounds;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }
}
