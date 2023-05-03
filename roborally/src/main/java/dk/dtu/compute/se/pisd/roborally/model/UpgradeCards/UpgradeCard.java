package dk.dtu.compute.se.pisd.roborally.model.UpgradeCards;

import dk.dtu.compute.se.pisd.roborally.model.Player;

public interface UpgradeCard {
    boolean isPermanent();
    void applyUpgrade(Player player);
}
