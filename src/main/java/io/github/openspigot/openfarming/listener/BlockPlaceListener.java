package io.github.openspigot.openfarming.listener;

import io.github.openspigot.openfarming.OpenFarming;
import io.github.openspigot.openfarming.farms.Farm;
import org.bukkit.Location;
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
        if(event.getItemInHand().getType() != OpenFarming.FARM_MATERIAL || !plugin.isFarmItem(event.getItemInHand())) {
            return;
        }

        Location blockLocation = event.getBlockPlaced().getLocation();

        Farm farm = new Farm(blockLocation, plugin.getFarmLevel(event.getItemInHand()), event.getPlayer().getUniqueId());
        plugin.getFarmManager().addFarm(farm);

        event.getPlayer().sendMessage("Farm placed.");
    }
}
