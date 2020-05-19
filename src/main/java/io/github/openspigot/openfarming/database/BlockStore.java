package io.github.openspigot.openfarming.database;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.openspigot.openfarming.database.gson.LocationAdapter;
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
    private final File dataFile;
    private final Material material;

    private Map<Location, T> blocks = new HashMap<>();

    public BlockStore(JavaPlugin plugin, Material material, File dataFile) {
        this.dataFile = dataFile;
        this.material = material;
        this.gson = new GsonBuilder().registerTypeAdapter(Location.class, new LocationAdapter()).create();

        new PersistenceListeners(plugin, this);

        load();
    }


    //
    // Public
    //
    public T get(Location location) {
        return blocks.get(location);
    }
    public T get(Block block) {
        return get(block.getLocation());
    }

    public void add(T block) {
        blocks.put(block.getLocation(), block);
    }

    public T remove(Location location) {
        return blocks.remove(location);
    }
    public T remove(T block) { return blocks.remove(block.getLocation()); }

    public boolean has(Location location) {
        return blocks.containsKey(location);
    }

    public Material getMaterial() {
        return material;
    }

    public boolean isValidBlock(Block block) {
        return (block.getType() == material && blocks.get(block.getLocation()) != null);
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
