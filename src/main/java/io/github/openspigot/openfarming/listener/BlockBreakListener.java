package io.github.openspigot.openfarming.listener;

import io.github.openspigot.openfarming.OpenFarming;
import io.github.openspigot.openfarming.farms.Farm;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {
    private final OpenFarming plugin;

    public BlockBreakListener(OpenFarming plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

        handleEvent(event);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(!isFarmBlock(block)) continue;

            Farm farm = plugin.getFarmManager().removeFarm(block.getLocation());
            ItemStack farmDrop = plugin.createFarmItem(farm.getLevel());

            event.blockList().remove(block);
            block.setType(Material.AIR);
            block.getLocation().getWorld().dropItemNaturally(block.getLocation().add(.5, .5, .5), farmDrop);
        }
    }

    // Prevent destruction by water.
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockChange(BlockFromToEvent event) {
        if(!isFarmBlock(event.getToBlock())) {
            return;
        }

        event.setCancelled(true);
    }

    //
    // Private
    //
    private void handleEvent(BlockEvent event) {
        if(!isFarmBlock(event.getBlock())) {
            return;
        }

        Location blockLocation = event.getBlock().getLocation();
        Farm farm = plugin.getFarmManager().removeFarm(blockLocation);

        // Cancel the normal block destroy and create our own.
        ((Cancellable) event).setCancelled(true);

        // Create a new drop with the correct level from the farm
        ItemStack farmDrop = plugin.createFarmItem(farm.getLevel());

        Block block = event.getBlock();
        block.setType(Material.AIR); // Remove the actual block
        block.getLocation().getWorld().dropItemNaturally(blockLocation.add(.5, .5, .5), farmDrop); // Drop the item
    }

    private boolean isFarmBlock(Block block) {
        return (block.getType() == OpenFarming.FARM_MATERIAL && plugin.getFarmManager().getFarm(block.getLocation()) != null);
    }
}
