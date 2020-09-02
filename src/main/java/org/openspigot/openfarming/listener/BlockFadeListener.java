package org.openspigot.openfarming.listener;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;

public class BlockFadeListener implements Listener {
    private final OpenFarming plugin;

    public BlockFadeListener(OpenFarming plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        if(event.getBlock().getType() != Material.FARMLAND) {
            return;
        }

        // Don't allow blocks above farmland
        if(event.getBlock().getRelative(BlockFace.UP).getType() != Material.AIR) {
            return;
        }

        FarmBlock farm = plugin.findFarmBlock(event.getBlock().getLocation(), 1);
        event.setCancelled(farm != null);
    }
}
