package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

public class Belt {

    /**@author Tobias Gørlyk     s224271@dtu.dk
     *
     * This class is used to store information about a conveyor belt on a given Space, each space has one, but it can be null meaning no belt.
     * It saves a heading of the belt, if it's turning and it's speed.
     */
    private Heading heading;
    private String turn = ""; //"LEFT", "RIGHT", LEFT_T, RIGHT_T, ""

    private int speed;

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
