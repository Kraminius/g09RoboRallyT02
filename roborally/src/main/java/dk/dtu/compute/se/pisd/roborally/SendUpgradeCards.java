package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.model.Command;

public class SendUpgradeCards {

    private Command[] commands;
    private int playerNumber;

    public Command[] getCommands() {
        return commands;
    }

    public void setCommands(Command[] commands) {
        this.commands = commands;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
