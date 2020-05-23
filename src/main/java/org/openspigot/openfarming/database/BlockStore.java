package org.openspigot.openfarming.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.openspigot.openfarming.database.driver.PersistenceDriver;
import org.openspigot.openfarming.database.gson.BlockAdapter;
import org.openspigot.openfarming.database.gson.LocationAdapter;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockStore<T extends PersistentBlock> {
    private final PersistenceDriver driver;
    private final String name;
    private final Material material;

    private final Gson gson;
    private final Type gsonType;

    private final Map<Chunk, Map<Location, T>> blockChunks = new HashMap<>();

    public BlockStore(JavaPlugin plugin, Material material, Class<T> type, String name, PersistenceDriver driver) {
        this.driver = driver;
        this.name = name;

        this.material = material;

        this.gsonType = new TypeToken<Map<Location, T>>() {}.getType();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .registerTypeAdapter(this.gsonType, new BlockAdapter<T>(this, type))
                .enableComplexMapKeySerialization()
                .create();

        new PersistenceListeners(plugin, this);

        // Load all of the loaded chunks
        for(World world : Bukkit.getWorlds()) {
            for(Chunk chunk : world.getLoadedChunks()) {
                loadChunk(chunk);
            }
        }
    }

    //
    // Chunks
    //
    @SuppressWarnings("unchecked")
    public void loadChunk(Chunk chunk) {
        try {
            Map<Location, T> blocks = (Map<Location, T>) driver.getChunk(this, chunk);
            for(T block : blocks.values()) {
                if(block.getLocation().getBlock().getType() != this.material) {
                    blocks.remove(block.getLocation());
                }
                block.init();
            }

            blockChunks.put(chunk, blocks);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChunk(Chunk chunk) {
        if(!blockChunks.containsKey(chunk)) {
            return;
        }

        try {
            driver.saveChunk(this, chunk);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        for(Chunk chunk : blockChunks.keySet()) {
            saveChunk(chunk);
        }
    }

    //
    // Public
    //
    public T get(Location location) {
        location = getBlockLocation(location);

        if(!blockChunks.containsKey(location.getChunk())) {
            return null;
        }

        return blockChunks.get(location.getChunk()).get(location);
    }
    public T get(Block block) {
        return get(block.getLocation());
    }

    @SuppressWarnings("deprecation")
    public void add(T block) {
        if(!blockChunks.containsKey(block.getLocation().getChunk())) {
            loadChunk(block.getLocation().getChunk());
        }

        blockChunks.get(block.getLocation().getChunk()).put(block.getLocation(), block);
        block.setStore(this);
        block.init();
    }
    public void add(T block, BlockPlaceEvent event) {
        add(block);
        block.onBlockPlace(event);
    }

    public T remove(Location location) {
        location = getBlockLocation(location);

        if(!blockChunks.containsKey(location.getChunk())) {
            return null;
        }

        return blockChunks.get(location.getChunk()).remove(location);
    }
    public T remove(T block) { return remove(block.getLocation()); }

    public boolean has(Location location) {
        location = getBlockLocation(location);
        return blockChunks.containsKey(location.getChunk()) && blockChunks.get(location.getChunk()).containsKey(location);
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isValidBlock(Block block) {
        return (block.getType() == material && has(block.getLocation()));
    }

    //
    // Utility
    //
    private Location getBlockLocation(Location location) {
        Location blockLocation = location.clone();
        blockLocation.setX(location.getBlockX());
        blockLocation.setY(location.getBlockY());
        blockLocation.setZ(location.getBlockZ());

        return blockLocation;
    }

    //
    // Driver
    //

    public Gson getGson() {
        return gson;
    }

    public Type getGsonType() {
        return gsonType;
    }

    public String getName() {
        return name;
    }

    public Map<Chunk, Map<Location, T>> getBlockChunks() {
        return blockChunks;
    }
}
