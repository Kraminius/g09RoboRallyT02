package dk.dtu.compute.se.pisd.roborally.model;

public class PowerUps {
    private boolean rammingGear;
    private boolean[] defragGizmo;


    public PowerUps(){
        this.rammingGear = false;
        this.defragGizmo = new boolean[]{false, false};
    }

    public boolean[] getDefragGizmo() {
        return defragGizmo;
    }

    public void setDefragGizmo(boolean[] defragGizmo) {
        this.defragGizmo = defragGizmo;
    }

    public boolean isRammingGear() {
        return rammingGear;
    }

    public void setRammingGear(boolean rammingGear) {
        this.rammingGear = rammingGear;
    }
}
