package org.openspigot.openfarming.level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    private Map<Integer, FarmLevel> levels = new HashMap<>();

    private int maxRadius = -1, lowestLevel = -1, highestLevel = -1;

    public LevelManager() {
        addLevel(new FarmLevel(1, 1, 1, 25, 25, 1, true));
        addLevel(new FarmLevel(2, 2, 1.25, 25, 25, 1, true));
        addLevel(new FarmLevel(3, 3, 1.5, 25, 25, 1, true));
        addLevel(new FarmLevel(4, 4, 1.75, 25, 25, 1, true));
    }

    public void loadLevels(ConfigurationSection levelsConfig) {

    }

    public FarmLevel getLevel(int level) {
        if(level > highestLevel) {
            return getHighestLevel();
        }
        if(level < lowestLevel) {
            return getLowestLevel();
        }

        return levels.get(level);
    }

    public void addLevel(FarmLevel level) {
        if(level.getLevel() <= 0) {
            return;
        }

        if(level.getLevel() < lowestLevel || lowestLevel == -1) {
            lowestLevel = level.getLevel();
        }
        if(level.getLevel() > highestLevel || highestLevel == -1) {
            highestLevel = level.getLevel();
        }

        if(level.getRadius() > maxRadius || maxRadius == -1) {
            maxRadius = level.getRadius();
        }

        levels.put(level.getLevel(), level);
    }

    //
    // Getters
    //
    public FarmLevel getLowestLevel() {
        return levels.get(lowestLevel);
    }

    public FarmLevel getHighestLevel() {
        return levels.get(highestLevel);
    }

    public Map<Integer, FarmLevel> getLevels() {
        return levels;
    }

    public int getMaxRadius() {
        return maxRadius;
    }
}
