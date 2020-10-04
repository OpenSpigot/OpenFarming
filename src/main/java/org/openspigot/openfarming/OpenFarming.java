package org.openspigot.openfarming;

import co.aikar.commands.PaperCommandManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.openspigot.openfarming.command.OpenFarmingCommand;
import org.openspigot.openfarming.database.BlockStore;
import org.openspigot.openfarming.database.driver.FileDriver;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.upgrade.FarmUpgrades;
import org.openspigot.openfarming.listener.BlockFadeListener;
import org.openspigot.openfarming.listener.BlockGrowListener;
import org.openspigot.openfarming.listener.BlockPlaceListener;
import org.openspigot.openfarming.util.ConfigWrapper;

import java.io.File;
import java.util.logging.Level;

public final class OpenFarming extends JavaPlugin {
    //
    // Constants
    public static final Material FARM_MATERIAL = Material.END_ROD;

    //
    // Instance Variables
    private static OpenFarming instance;

    private ConfigWrapper config;
    private BlockStore<FarmBlock> farmBlockStore;
    private Economy economy = null;

    //
    // Enable
    //
    @Override
    public void onEnable() {
        OpenFarming.instance = this;
        getDataFolder().mkdirs();

        // Load Config
        this.config = new ConfigWrapper(this, "config.yml");

        // Vault Hook
        if(!setupEconomy()) {
            getLogger().log(Level.WARNING, "Vault isn't installed, economy will not be supported.");
        }

        // Setup Block Persistence
        FileDriver fileDriver = new FileDriver(new File(getDataFolder(), "farms"));
        this.farmBlockStore = new BlockStore<>(this, FARM_MATERIAL, FarmBlock.class, "farms", fileDriver);

        // Register Commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new OpenFarmingCommand(this));

        // Register Listeners
        new BlockPlaceListener(this);
        new BlockFadeListener(this);
        new BlockGrowListener(this);
    }

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) return false;

        this.economy = rsp.getProvider();
        return true;
    }

    //
    // Disable
    //
    @Override
    public void onDisable() {
        farmBlockStore.saveAll();
    }

    //
    // Public
    //
    public FarmBlock findFarmBlock(Location location, int yOffset) {
        int maxRadius = FarmUpgrades.RADIUS.getLevel(FarmUpgrades.RADIUS.getMaxLevel()).getValue();

        for (int lx = -maxRadius; lx <= maxRadius; lx++) {
            for (int lz = -maxRadius; lz <= maxRadius; lz++) {

                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() + yOffset, location.getBlockZ() + lz);
                if (farmBlockStore.isValidBlock(lBlock)) {
                    FarmBlock checkBlock = farmBlockStore.get(lBlock);
                    int radius = FarmUpgrades.RADIUS.getValue(checkBlock);

                    if(Math.abs(lx) <= radius && Math.abs(lz) <= radius) {
                        return checkBlock;
                    }
                }

            }
        }

        return null;
    }

    //
    // Getters
    //
    public BlockStore<FarmBlock> getFarmBlockStore() {
        return farmBlockStore;
    }

    public static OpenFarming getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public FileConfiguration getConfig() {
        return config.getRaw();
    }
}
