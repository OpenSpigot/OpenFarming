package org.openspigot.openfarming.database.provider;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.openspigot.openfarming.database.PersistentBlock;

import java.util.Map;

public interface PersistenceDriver<T extends PersistentBlock> {
    public Map<Location, T> getChunk(Chunk chunk);

    public void saveChunk(Chunk chunk);
}
