package org.openspigot.openfarming.database.driver;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.openspigot.openfarming.database.BlockStore;

import java.util.Map;

public interface PersistenceDriver {
    public Map<Location, ?> getChunk(BlockStore<?> store, Chunk chunk) throws Exception;

    public void saveChunk(BlockStore<?> store, Chunk chunk) throws Exception;
}
