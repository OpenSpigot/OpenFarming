package org.openspigot.openfarming.farm.upgrades;

import org.bukkit.entity.Player;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.util.LangUtils;

public class FarmAutoReplantUpgrade extends FarmUpgradeBoolean {
    public FarmAutoReplantUpgrade() {
        super("autoReplant");
    }

    @Override
    public Boolean getValue(FarmBlock block) {
        return block.isReplant();
    }

    @Override
    public void setValue(FarmBlock block, boolean value) {
        block.setReplant(value);
    }

    @Override
    protected void purchase(Player player, FarmBlock block) {
        super.purchase(player, block);

        player.sendMessage(LangUtils.parse("upgrades.autoReplant.purchased", player));
    }
}
