package org.openspigot.openfarming.listener;

import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;

public class BlockGrowListener implements Listener {

    private final OpenFarming plugin;

    public BlockGrowListener(OpenFarming plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        FarmBlock block = plugin.findFarm(event.getBlock().getLocation(), 0);
        if(block == null) {
            return;
        }

        if(event.getNewState().getBlockData() instanceof Ageable) {
            Ageable newData = (Ageable) event.getNewState().getBlockData();
            System.out.println(newData.getAge());
            System.out.println(newData.getMaximumAge());

            if(newData.getMaximumAge() == newData.getAge()) {
                block.harvest(event.getBlock());
                event.setCancelled(true);
            }
        }
    }
}
