package dk.dtu.compute.se.pisd.roborally.model.UpgradeCards;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public class AdminPrivilege implements UpgradeCard {

    //this is just an example

    //Is permanent or no?
    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public void applyUpgrade(Player player) {
    cardLogic(player);
    }

    private void cardLogic(Player player){

    }
}
