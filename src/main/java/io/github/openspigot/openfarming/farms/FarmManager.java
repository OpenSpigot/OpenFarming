package io.github.openspigot.openfarming.farms;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class FarmManager {

    private final Map<Location, Farm> farms = new HashMap<>();

    public void addFarm(Farm farm) {
        this.farms.put(farm.getLocation(), farm);
    }

    public Farm removeFarm(Location location) {
        return this.farms.remove(getBlockLocation(location));
    }

    public Farm getFarm(Location location) {
        return this.farms.get(getBlockLocation(location));
    }

    public Map<Location, Farm> getFarms() {
        return farms;
    }

    //
    // Private
    //
    private Location getBlockLocation(Location location) {
        Location blockLocation = location.clone();
        blockLocation.setX(location.getBlockX());
        blockLocation.setY(location.getBlockY());
        blockLocation.setZ(location.getBlockZ());

        return blockLocation;
    }
}
