package dk.dtu.compute.se.pisd.roborally.model;

public class PowerUps {
    private boolean rammingGear;

    private boolean barrelLaser;


    public PowerUps(){
        this.rammingGear = false;
        this.barrelLaser = false;
    }


    public boolean isRammingGear() {
        return rammingGear;
    }

    public void setRammingGear(boolean rammingGear) {
        this.rammingGear = rammingGear;
    }

    public boolean isBarrelLaser() {
        return barrelLaser;
    }

    public void setBarrelLaser(boolean barrelLaser) {
        this.barrelLaser = barrelLaser;
    }
}
