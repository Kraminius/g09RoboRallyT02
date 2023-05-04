package dk.dtu.compute.se.pisd.roborally.model;

public class PowerUps {
    private boolean rammingGear;


    public PowerUps(){
        this.rammingGear = false;
    }


    public boolean isRammingGear() {
        return rammingGear;
    }

    public void setRammingGear(boolean rammingGear) {
        this.rammingGear = rammingGear;
    }
}
