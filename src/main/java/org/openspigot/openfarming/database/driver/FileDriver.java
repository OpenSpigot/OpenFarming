package org.openspigot.openfarming.database.driver;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.openspigot.openfarming.database.BlockStore;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class FileDriver implements PersistenceDriver {
    private final File saveLocation;

    public FileDriver(File saveLocation) {
        this.saveLocation = saveLocation;
    }

    @Override
    public Map<Location, ?> getChunk(BlockStore<?> store, Chunk chunk) throws Exception {
        File chunkFile = getChunkFile(store, chunk);
        if(!chunkFile.exists()) {
            return new HashMap<>();
        }

        FileReader reader = new FileReader(chunkFile);
        HashMap<Location, ?> blockMap = store.getGson().fromJson(reader, store.getGsonType());
        reader.close();

        return blockMap;
    }

    @Override
    public void saveChunk(BlockStore<?> store, Chunk chunk) throws Exception {
        File chunkFile = getChunkFile(store, chunk);
        Map<Location, ?> blocks = store.getBlockChunks().get(chunk);

        if(blocks == null || blocks.size() <= 0) {
            if(chunkFile.exists()) chunkFile.delete();
            return;
        }

        FileWriter fileWriter = new FileWriter(chunkFile);
        store.getGson().toJson(blocks, store.getGsonType(), fileWriter);
        fileWriter.close();
    }

    //
    // Private
    //
    private File getChunkFile(BlockStore<?> store, Chunk chunk) {
        File worldFolder = new File(saveLocation, chunk.getWorld().getUID().toString());
        if(!worldFolder.exists()) {
            worldFolder.mkdirs();
        }

        return new File(worldFolder, chunk.getX() + "," + chunk.getZ() + "-" + store.getName() + ".json");
    }
}