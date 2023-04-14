package dk.dtu.compute.se.pisd.roborally.model;

public class Belt {

    public Heading heading;
    public String turn = ""; //"Left", "Right", ""

    public int speed;

    public Belt(Heading heading, String turn, int speed){
        this.heading = heading;
        this.turn = turn;
        this.speed = speed;
    }
}
