package io.github.openspigot.openfarming.database;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class PersistentBlock {
    protected Location location;
    protected boolean unbreakable = false;

    public PersistentBlock(Location location) {
        this.location = location;
    }
    public PersistentBlock(Location location, boolean unbreakable) {
        this.location = location;
        this.unbreakable = unbreakable;
    }

    //
    // Getters
    //
    public Location getLocation() {
        return location;
    }

    public boolean isUnbreakable() {
        return unbreakable;
    }

    //
    // Events
    //
    public void onBreak(BlockBreakEvent event) {
        if(!isUnbreakable()) return;

        event.setCancelled(true);
    };

    public void onEntityExplode(EntityExplodeEvent event) {
        if(!isUnbreakable()) return;
        event.blockList().remove(event.getLocation().getWorld().getBlockAt(location));
    };

    public void onBlockExplode(BlockExplodeEvent event) {
        if(!isUnbreakable()) return;
        event.blockList().remove(event.getBlock().getWorld().getBlockAt(location));
    };

    public void onBlockChange(BlockFromToEvent event) {
        if(!isUnbreakable()) return;
        event.setCancelled(true);
    }

    public void onInteract(PlayerInteractEvent event) { };
}
