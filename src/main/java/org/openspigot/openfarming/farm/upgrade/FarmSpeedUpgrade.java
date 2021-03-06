package org.openspigot.openfarming.farm.upgrade;

import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.upgrade.type.FarmLevelUpgrade;

public class FarmSpeedUpgrade extends FarmLevelUpgrade<Float> {
    public FarmSpeedUpgrade() {
        super("speed");
    }

    @Override
    public Integer getRaw(FarmBlock block) {
        return block.getSpeed();
    }

    @Override
    public void setRaw(FarmBlock block, Integer value) {
        block.setSpeed(value);
    }
}
