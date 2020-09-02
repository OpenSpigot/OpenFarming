package org.openspigot.openfarming;

import org.bukkit.plugin.java.JavaPlugin;

public final class OpenFarming extends JavaPlugin {
    private static OpenFarming instance;

    //
    // Enable/Disable
    //
    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    //
    // Getters
    //
    public static OpenFarming getInstance() {
        return instance;
    }

}
