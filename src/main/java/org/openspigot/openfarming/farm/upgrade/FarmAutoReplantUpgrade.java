package org.openspigot.openfarming.farm.upgrade;

import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.upgrade.type.FarmBooleanUpgrade;

public class FarmAutoReplantUpgrade extends FarmBooleanUpgrade {
    public FarmAutoReplantUpgrade() {
        super("autoReplant");
    }

    @Override
    public Boolean getRaw(FarmBlock block) {
        return block.isReplant();
    }

    @Override
    public void setRaw(FarmBlock block, Boolean value) {
        block.setReplant(value);
    }
}
