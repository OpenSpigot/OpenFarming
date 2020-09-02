package org.openspigot.openfarming;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.openspigot.openfarming.command.OpenFarmingCommand;
import org.openspigot.openfarming.database.BlockStore;
import org.openspigot.openfarming.database.driver.FileDriver;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.listener.BlockFadeListener;
import org.openspigot.openfarming.listener.BlockGrowListener;
import org.openspigot.openfarming.listener.BlockPlaceListener;
import org.openspigot.openfarming.util.ConfigWrapper;

import java.io.File;

public final class OpenFarming extends JavaPlugin {
    //
    // Constants
    public static final Material FARM_MATERIAL = Material.END_ROD;
    public static final Material UPGRADE_RADIUS_MATERIAL = Material.COMPASS;
    public static final Material UPGRADE_SPEED_MATERIAL = Material.SUGAR;
    public static final Material UPGRADE_REPLANT_MATERIAL = Material.SUNFLOWER;

    //
    // Instance Variables
    private static OpenFarming instance;

    private ConfigWrapper config;
    private BlockStore<FarmBlock> farmBlockStore;

    //
    // Enable
    //
    @Override
    public void onEnable() {
        OpenFarming.instance = this;
        getDataFolder().mkdirs();

        // Load Config
        this.config = new ConfigWrapper(this, "config.yml");

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

    //
    // Public
    //
    public FarmBlock findFarmBlock(Location location, int yOffset) {
        int maxRadius = 5;

        for (int lx = -maxRadius; lx <= maxRadius; lx++) {
            for (int lz = -maxRadius; lz <= maxRadius; lz++) {

                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() + yOffset, location.getBlockZ() + lz);
                if (farmBlockStore.isValidBlock(lBlock)) {
                    FarmBlock checkBlock = farmBlockStore.get(lBlock);

                    if (Math.abs(lx) <= checkBlock.getRadius() && Math.abs(lz) <= checkBlock.getRadius()) {
                        return checkBlock;
                    }
                }

            }
        }

        return null;
    }

    public int maxUpgradeAmount(String name) {
        int balLen = (getConfig().isBoolean(name + ".balCost")) ? 0 : getConfig().getIntegerList(name + ".balCost").size();
        int expLen = (getConfig().isBoolean(name + ".expCost")) ? 0 : getConfig().getIntegerList(name + ".expCost").size();

        return Math.max(balLen, expLen);
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

    @Override
    public FileConfiguration getConfig() {
        return config.getRaw();
    }
}
