package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

import java.util.ArrayList;

public class Wall {
    private ArrayList<Heading> wallHeadings = new ArrayList<>();

    public ArrayList<Heading> getWallHeadings() {
        return wallHeadings;
    }

    public void setWallHeadings(ArrayList<Heading> wallHeadings) {
        this.wallHeadings = wallHeadings;
    }
}
