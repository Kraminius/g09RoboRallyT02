package dk.dtu.compute.se.pisd.roborally.model;

public class Belt {

    /**@author Tobias Gørlyk     s224271@dtu.dk
     *
     * This class is used to store information about a conveyor belt on a given Space, each space has one, but it can be null meaning no belt.
     * It saves a heading of the belt, if it's turning and it's speed.
     */
    public Heading heading;
    public String turn = ""; //"Left", "Right", ""

    public int speed;

    public Belt(Heading heading, String turn, int speed){
        this.heading = heading;
        this.turn = turn;
        this.speed = speed;
    }
}
