package dk.dtu.compute.se.pisd.roborally.BuildABoard;

public class BoardBuildElement {
    private boolean antenna;
    private boolean energyField;
    private boolean hole;
    private boolean laserPointer;
    private boolean laserRay;
    private boolean repair;
    private boolean respawn;
    private int startField;
    private int checkpoint;
    private int wall;
    private int push;
    private int gear;
    private int greenBelt;
    private int blueBelt;


    public boolean isAntenna() {
        return antenna;
    }

    public void setAntenna(boolean antenna) {
        this.antenna = antenna;
    }

    public boolean isEnergyField() {
        return energyField;
    }

    public void setEnergyField(boolean energyField) {
        this.energyField = energyField;
    }

    public boolean isHole() {
        return hole;
    }

    public void setHole(boolean hole) {
        this.hole = hole;
    }

    public boolean isLaserPointer() {
        return laserPointer;
    }

    public void setLaserPointer(boolean laserPointer) {
        this.laserPointer = laserPointer;
    }

    public boolean isLaserRay() {
        return laserRay;
    }

    public void setLaserRay(boolean laserRay) {
        this.laserRay = laserRay;
    }

    public boolean isRepair() {
        return repair;
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public int getStartField() {
        return startField;
    }

    public void setStartField(int startField) {
        this.startField = startField;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(int checkpoint) {
        this.checkpoint = checkpoint;
    }

    public int getWall() {
        return wall;
    }

    public void setWall(int wall) {
        this.wall = wall;
    }

    public int getPush() {
        return push;
    }

    public void setPush(int push) {
        this.push = push;
    }

    public int getGreenBelt() {
        return greenBelt;
    }

    public void setGreenBelt(int greenBelt) {
        this.greenBelt = greenBelt;
    }

    public int getBlueBelt() {
        return blueBelt;
    }

    public void setBlueBelt(int blueBelt) {
        this.blueBelt = blueBelt;
    }

    public int getGear() {
        return gear;
    }

    public void setGear(int gear) {
        this.gear = gear;
    }
}
