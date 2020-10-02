package org.openspigot.openfarming.database;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class PersistentBlock {
    protected Location location;
    protected boolean unbreakable = false;

    protected transient BlockStore<?> store;

    public PersistentBlock(Location location) {
        this.location = location;
    }
    public PersistentBlock(Location location, boolean unbreakable) {
        this.location = location;
        this.unbreakable = unbreakable;
    }

    @Deprecated
    public void setStore(BlockStore<?> store) {
        this.store = store;
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
        if(isUnbreakable()) {
            event.setCancelled(true);
            return;
        }

        store.remove(location);
    };

    public void onEntityExplode(EntityExplodeEvent event) {
        if(isUnbreakable()) {
            event.blockList().remove(event.getLocation().getWorld().getBlockAt(location));
            return;
        }

        store.remove(location);
    };

    public void onBlockExplode(BlockExplodeEvent event) {
        if(isUnbreakable()) {
            event.blockList().remove(event.getBlock().getWorld().getBlockAt(location));
            return;
        }

        store.remove(location);
    };

    public void onBlockChange(BlockFromToEvent event) {
        if(isUnbreakable()) {
            event.setCancelled(true);
            return;
        }

        store.remove(location);
    }


    public void onInteract(PlayerInteractEvent event) { };

    public void onBlockPlace(BlockPlaceEvent event) { };

    public void init() { }
}