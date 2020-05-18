package io.github.openspigot.openfarming.farms;

import org.bukkit.Location;

import java.util.Random;
import java.util.UUID;

public class Farm {
    private final Random random = new Random();

    private Location location;
    private int level;
    private UUID owner;

    public Farm(Location location, int level, UUID owner) {
        this.location = location;
        this.level = level;
        this.owner = owner;
    }

    public Random getRandom() {
        return random;
    }

    public Location getLocation() {
        return location;
    }

    public int getLevel() {
        return level;
    }

    public UUID getOwner() {
        return owner;
    }
}
