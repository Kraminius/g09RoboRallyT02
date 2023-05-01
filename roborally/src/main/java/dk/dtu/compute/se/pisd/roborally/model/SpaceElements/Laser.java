package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

public class Laser {
    private boolean isStart;
    private Heading heading;
    private int damage;

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
