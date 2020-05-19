package io.github.openspigot.openfarming.listener;

import io.github.openspigot.openfarming.OpenFarming;
import io.github.openspigot.openfarming.farms.FarmBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final OpenFarming plugin;

    public BlockPlaceListener(OpenFarming plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(plugin.isFarmItem(event.getItemInHand())) {
            int level = plugin.getFarmLevel(event.getItemInHand());

            FarmBlock farm = new FarmBlock(event.getBlock().getLocation(), level, plugin.getFarmType(event.getItemInHand()), event.getPlayer().getUniqueId());
            plugin.getFarmStore().add(farm);
        }
    }


}
