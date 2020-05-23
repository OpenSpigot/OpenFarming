package org.openspigot.openfarming.listener;

import org.bukkit.block.BlockFace;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

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

        FarmBlock farm = plugin.findFarm(event.getBlock().getLocation(), 1);
        if(farm == null) {
            return;
        }

        event.setCancelled(true);
    }
}
