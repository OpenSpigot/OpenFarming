package org.openspigot.openfarming.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmItem;

public class BlockPlaceListener implements Listener {
    private final OpenFarming plugin;

    public BlockPlaceListener(OpenFarming plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(FarmItem.is(event.getItemInHand())) {
            FarmBlock farm = new FarmBlock(event.getBlock().getLocation(), event.getPlayer(), FarmItem.from(event.getItemInHand()));
            plugin.getFarmBlockStore().add(farm, event);
        }
    }
}
