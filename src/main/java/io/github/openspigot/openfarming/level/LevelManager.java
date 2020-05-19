package io.github.openspigot.openfarming.level;

import java.util.HashMap;
import java.util.Map;

public class LevelManager {
    private Map<Integer, FarmLevel> levels = new HashMap<>();

    private int maxRadius = -1, lowestLevel = -1, highestLevel = -1;

    public LevelManager() {
        addLevel(new FarmLevel(1, 1, 25, 25, 1, true));
        addLevel(new FarmLevel(2, 2, 25, 25, 1, true));
        addLevel(new FarmLevel(3, 3, 25, 25, 1, true));
        addLevel(new FarmLevel(4, 4, 25, 25, 1, true));
    }

    public FarmLevel getLevel(int level) {
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

    public int getMaxRadius() {
        return maxRadius;
    }
}
