package org.openspigot.openfarming.farm.upgrades;

import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.util.LangUtils;

public class FarmRadiusUpgrade extends FarmUpgrade<Integer> {
    public FarmRadiusUpgrade() {
        super("radius");
    }

    @Override
    public int getRawLevel(FarmBlock block) {
        return block.getRadius();
    }

    @Override
    public void setRawLevel(FarmBlock block, int level) {
        block.setRadius(level);
    }

    @Override
    protected void upgrade(Player player, FarmBlock block) {
        super.upgrade(player, block);

        block.tillLand(); // Re-till the land (since it's a radius upgrade)

        // Inform the player that it has been upgraded.
        ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                .put("VALUE", String.valueOf(getLevel(block.getRadius()).getValue()))
                .build();

        player.sendMessage(LangUtils.parse("upgrades.radius.purchased", player, placeholders));
    }
}
