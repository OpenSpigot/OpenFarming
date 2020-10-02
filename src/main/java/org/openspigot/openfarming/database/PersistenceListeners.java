package org.openspigot.openfarming.database;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PersistenceListeners implements Listener {
    private final BlockStore<?> store;

    public PersistenceListeners(JavaPlugin plugin, BlockStore<?> store) {
        this.store = store;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    //
    // Listeners - World
    //
    @EventHandler
    public void onSave(WorldSaveEvent event) {
        store.saveAll();
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        // There wont be any custom blocks in a new chunk
        if(event.isNewChunk()) {
            return;
        }

        store.loadChunk(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        store.saveChunk(event.getChunk());
    }

    //
    // Listeners - Blocks
    //
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() != store.getMaterial()) {
            return;
        }

        PersistentBlock block = store.get(event.getBlock().getLocation());
        if(block == null) return;

        if(event.getBlock().getType() != store.getMaterial()) {
            store.remove(block.getLocation());
            return;
        }

        block.onBreak(event);
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(block.getType() != store.getMaterial()) {
                continue;
            }

            PersistentBlock pBlock = store.get(block);
            pBlock.onBlockExplode(event);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(block.getType() != store.getMaterial()) {
                continue;
            }

            PersistentBlock pBlock = store.get(block);
            pBlock.onEntityExplode(event);
        }
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent event) {
        if(event.getToBlock().getType() != store.getMaterial()) return;

        PersistentBlock pBlock = store.get(event.getBlock());
        if(pBlock == null) {
            return;
        }

        pBlock.onBlockChange(event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() != store.getMaterial()) {
            return;
        }

        PersistentBlock block = store.get(event.getClickedBlock());
        if(block == null) {
            return;
        }

        block.onInteract(event);
    }
}