package org.openspigot.openfarming;

import co.aikar.commands.BukkitCommandManager;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.openspigot.openfarming.command.OpenFarmingCommand;
import org.openspigot.openfarming.database.BlockStore;
import org.openspigot.openfarming.database.driver.FileDriver;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmType;
import org.openspigot.openfarming.level.LevelManager;
import org.openspigot.openfarming.listener.BlockFadeListener;
import org.openspigot.openfarming.listener.BlockGrowListener;
import org.openspigot.openfarming.listener.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.openspigot.openfarming.util.ConfigWrapper;

import java.io.File;
import java.util.HashSet;
import java.util.stream.Collectors;

public final class OpenFarming extends JavaPlugin {
    // Constants
    public static final Material FARM_MATERIAL = Material.END_ROD;

    private static OpenFarming instance;

    private ConfigWrapper config;
    private LevelManager levelManager;
    private BlockStore<FarmBlock> farmStore;

    @Override
    public void onEnable() {
        instance = this;

        getDataFolder().mkdirs();
        this.config = new ConfigWrapper(this, "config.yml");

        // Load Levels
        this.levelManager = new LevelManager();
        this.levelManager.loadLevels(getConfig().getConfigurationSection("levels"));

        // Setup block persistence
        FileDriver fileDriver = new FileDriver(new File(getDataFolder(), "blockstore"));
        this.farmStore = new BlockStore<>(this, FARM_MATERIAL, FarmBlock.class, "farms", fileDriver);

        // Register Commands
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.getCommandCompletions().registerAsyncCompletion("level", c ->
                levelManager.getLevels().keySet().stream().map(String::valueOf).collect(Collectors.toCollection(HashSet::new))
        );

        commandManager.registerCommand(new OpenFarmingCommand(this));

        // Register Listeners
        new BlockPlaceListener(this);
        new BlockFadeListener(this);
        new BlockGrowListener(this);
    }

    @Override
    public void onDisable() {
        farmStore.saveAll();
    }

    //
    // Farm Item
    //
    public ItemStack createFarmItem(int level, FarmType type) {
        ItemStack item = new ItemStack(FARM_MATERIAL);

        // Meta
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Level &a&l" + level + " " + type.toString() + " &r&7Farm"));
        item.setItemMeta(itemMeta);

        // NBT
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("farmLevel", level);
        nbtItem.setString("farmType", type.toString());

        return nbtItem.getItem();
    }

    public boolean isFarmItem(ItemStack item) {
        if(item.getType() != FARM_MATERIAL) {
            return false;
        }

        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getInteger("farmLevel") != null && nbtItem.getString("farmType") != null;
    }

    public int getFarmLevel(ItemStack item) {
        return new NBTItem(item).getInteger("farmLevel");
    }

    public FarmType getFarmType(ItemStack item) {
        return FarmType.valueOf(new NBTItem(item).getString("farmType"));
    }

    //
    // Farm Block
    //
    public FarmBlock findFarm(Location location, int yOffset) {
        int maxRadius = levelManager.getMaxRadius();

        for(int lx = -maxRadius; lx <= maxRadius; lx++) {
            for(int lz = -maxRadius; lz <= maxRadius; lz++) {
                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() + yOffset, location.getBlockZ() + lz);
                if(farmStore.isValidBlock(lBlock)) {
                    FarmBlock checkBlock = farmStore.get(lBlock);

                    if(Math.abs(lx) <= checkBlock.getFarmLevel().getRadius() && Math.abs(lz) <= checkBlock.getFarmLevel().getRadius()) {
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


    public LevelManager getLevelManager() {
        return levelManager;
    }

    public BlockStore<FarmBlock> getFarmStore() {
        return farmStore;
    }

    @Override
    public FileConfiguration getConfig() {
        return config.getRaw();
    }

    public static OpenFarming getInstance() {
        return instance;
    }

}
