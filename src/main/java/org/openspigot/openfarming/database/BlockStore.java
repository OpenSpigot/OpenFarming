package org.openspigot.openfarming.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.event.block.BlockPlaceEvent;
import org.openspigot.openfarming.database.gson.BlockAdapter;
import org.openspigot.openfarming.database.gson.LocationAdapter;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BlockStore<T extends PersistentBlock> {
    private final Gson gson;
    private final File dataDirectory;
    private final Material material;
    private final Type gsonType;

    private Map<Chunk, Map<Location, T>> blockChunks = new HashMap<>();

    public BlockStore(JavaPlugin plugin, Material material, Class<T> type, File dataDirectory) {
        this.gsonType = new TypeToken<Map<Location, T>>() {}.getType();

        this.dataDirectory = dataDirectory;
        this.material = material;
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
    private File getChunkFile(Chunk chunk) {
        File worldFolder = new File(dataDirectory, chunk.getWorld().getUID().toString());
        if(!worldFolder.exists()) {
            worldFolder.mkdirs();
        }

        return new File(worldFolder, chunk.getX() + "," + chunk.getZ() + ".json");
    }

    public void loadChunk(Chunk chunk) {
        File chunkFile = getChunkFile(chunk);
        if(!chunkFile.exists()) {
            blockChunks.put(chunk, new HashMap<>());
            return;
        }

        try {
            FileReader fileReader = new FileReader(chunkFile);
            Map<Location, T> blocks = gson.fromJson(fileReader, gsonType);
            fileReader.close();

            blockChunks.put(chunk, blocks);
            for(T block : blocks.values()) {
                block.init();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveChunk(Chunk chunk) {
        if(!blockChunks.containsKey(chunk)) {
            return;
        }

        Map<Location, T> blocks = blockChunks.get(chunk);

        File chunkFile = getChunkFile(chunk);
        if(!chunkFile.exists() && (blocks == null || blocks.size() <= 0)) {
            return;
        }

        if(blocks == null || blocks.size() <= 0) {
            chunkFile.delete();
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter(chunkFile);
            gson.toJson(blocks, gsonType, fileWriter);
            fileWriter.close();
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
        if(!blockChunks.containsKey(location.getChunk())) {
            return null;
        }

        return blockChunks.get(location.getChunk()).remove(location);
    }
    public T remove(T block) { return remove(block.getLocation()); }

    public boolean has(Location location) {
        return blockChunks.containsKey(location.getChunk()) && blockChunks.get(location.getChunk()).containsKey(location);
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isValidBlock(Block block) {
        return (block.getType() == material && has(block.getLocation()));
    }

    //
    // File Handling
    //
//    public void save() {
//        try {
//            FileWriter fileWriter = new FileWriter(dataFile);
//            gson.toJson(blocks, fileWriter);
//            fileWriter.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void load() {
//        if(!dataFile.exists()) {
//            return;
//        }
//        try {
//            FileReader fileReader = new FileReader(dataFile);
//
//            Type type = new TypeToken<Map<Location, T>>() {}.getType();
//            blocks = gson.fromJson(fileReader, type);
//
//            fileReader.close();
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

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
}
