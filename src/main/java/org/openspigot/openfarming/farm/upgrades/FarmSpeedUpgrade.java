package org.openspigot.openfarming.farm.upgrades;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.util.LangUtils;

public class FarmSpeedUpgrade extends FarmUpgrade<Float> {
    public FarmSpeedUpgrade() {
        super("speed");
    }

    @Override
    public int getRawLevel(FarmBlock block) {
        return block.getSpeed();
    }

    @Override
    public void setRawLevel(FarmBlock block, int level) {
        block.setSpeed(level);
    }

    protected void upgrade(Player player, FarmBlock block) {
        super.upgrade(player, block);

        // Inform the player that it has been upgraded.
        ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                .put("VALUE", String.valueOf(getLevel(getRawLevel(block) + 1).getValue()))
                .build();
        player.sendMessage(LangUtils.parse("upgrades.speed.purchased", player, placeholders));
    }
}
