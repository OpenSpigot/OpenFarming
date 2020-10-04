package org.openspigot.openfarming.farm.upgrade;

import org.openspigot.openfarming.farm.upgrade.type.FarmBooleanUpgrade;

public class FarmUpgrades {
    public static final FarmBooleanUpgrade AUTO_REPLANT = new FarmAutoReplantUpgrade();
    public static final FarmRadiusUpgrade RADIUS = new FarmRadiusUpgrade();
    public static final FarmSpeedUpgrade SPEED = new FarmSpeedUpgrade();
}
