package dk.dtu.compute.se.pisd.roborally.model.UpgradeCards;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public class TestUpgradeCardPerm implements UpgradeCard {

    //this is just an example

    //Is permanent or no?
    @Override
    public boolean isPermanent() {
        return true;
    }

    @Override
    public void applyUpgrade(Player player) {
        // Gives player 5 cubes
        //player.setTokens(player.getTokens + 5);
    }
}
