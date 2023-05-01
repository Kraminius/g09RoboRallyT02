package dk.dtu.compute.se.pisd.roborally.model.SpaceElements;

public class SpaceElement {

    private boolean isAntenna;
    private boolean isRespawn;
    private boolean isHole;
    private boolean isSpace = true;
    private boolean isRepair;

    private Belt belt = null;
    private Checkpoint checkpoint = null;
    private EnergyField energyField;
    private Gear gear;
    private Laser laser;
    private Push push;
    private StartField startField;
    private Wall wall;


    public boolean isAntenna() {
        return isAntenna;
    }

    public void setAntenna(boolean antenna) {
        isAntenna = antenna;
    }

    public boolean isRespawn() {
        return isRespawn;
    }

    public void setRespawn(boolean respawn) {
        isRespawn = respawn;
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean hole) {
        isHole = hole;
    }

    public boolean isSpace() {
        return isSpace;
    }

    public void setSpace(boolean space) {
        isSpace = space;
    }

    public boolean isRepair() {
        return isRepair;
    }

    public void setRepair(boolean repair) {
        isRepair = repair;
    }

    public Belt getBelt() {
        return belt;
    }

    public void setBelt(Belt belt) {
        this.belt = belt;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
    }

    public EnergyField getEnergyField() {
        return energyField;
    }

    public void setEnergyField(EnergyField energyField) {
        this.energyField = energyField;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public Laser getLaser() {
        return laser;
    }

    public void setLaser(Laser laser) {
        this.laser = laser;
    }

    public Push getPush() {
        return push;
    }

    public void setPush(Push push) {
        this.push = push;
    }

    public StartField getStartField() {
        return startField;
    }

    public void setStartField(StartField startField) {
        this.startField = startField;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }
}
