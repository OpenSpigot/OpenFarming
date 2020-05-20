package io.github.openspigot.openfarming.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.openspigot.openfarming.database.gson.LocationAdapter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
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

//    private Map<Location, T> blocks = new HashMap<>();
    private Map<Chunk, Map<Location, T>> blockedChunks = new HashMap<>();

    public BlockStore(JavaPlugin plugin, Material material, File dataDirectory) {
        this.dataDirectory = dataDirectory;
        this.material = material;
        this.gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationAdapter()).create();

        new PersistenceListeners(plugin, this);

        load();
    }

    //
    // Chunks
    //
    private File getChunkFile(Chunk chunk) {
        return new File(dataDirectory, chunk.getWorld().getUID().toString() + File.separator + chunk.getX() + "," + chunk.getZ());
    }

    public void loadChunk(Chunk chunk) {
        File chunkFile = getChunkFile(chunk);
        if(!chunkFile.exists()) {
            return;
        }

        chunkFile
    }

    public void unloadChunk(Chunk chunk) {

    }

    //
    // Public
    //
    public T get(Location location) {
        if(!blockedChunks.containsKey(location.getChunk())) {
            return null;
        }

        return blockedChunks.get(location.getChunk()).get(location);
    }
    public T get(Block block) {
        return get(block.getLocation());
    }

    public void add(T block) {
        if(!blockedChunks.containsKey(block.getLocation().getChunk())) {
            loadChunk(block.getLocation().getChunk());
        }

        blockedChunks.get(block.getLocation().getChunk()).put(block.getLocation(), block);
    }

    public T remove(Location location) {
        if(!blockedChunks.containsKey(location.getChunk())) {
            return null;
        }

        return blockedChunks.get(location.getChunk()).remove(location);
    }
    public T remove(T block) { return remove(block.getLocation()); }

    public boolean has(Location location) {
        return blockedChunks.containsKey(location.getChunk()) && blockedChunks.get(location.getChunk()).containsKey(location);
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
    public void save() {
        try {
            FileWriter fileWriter = new FileWriter(dataFile);
            gson.toJson(blocks, fileWriter);
            fileWriter.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void load() {
        if(!dataFile.exists()) {
            return;
        }
        try {
            FileReader fileReader = new FileReader(dataFile);

            Type type = new TypeToken<Map<Location, T>>() {}.getType();
            blocks = gson.fromJson(fileReader, type);

            fileReader.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
}
