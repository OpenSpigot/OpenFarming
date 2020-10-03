package org.openspigot.openfarming.farm.upgrades;

public class FarmUpgrades {
    public static final FarmUpgrade<Integer> RADIUS_UPGRADE = new FarmRadiusUpgrade();
    public static final FarmUpgrade<Float> SPEED_UPGRADE = new FarmSpeedUpgrade();
    public static final FarmUpgradeBoolean AUTO_REPLANT_UPGRADE = new FarmAutoReplantUpgrade();
}
