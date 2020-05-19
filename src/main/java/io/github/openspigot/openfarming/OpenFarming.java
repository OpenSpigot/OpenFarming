package io.github.openspigot.openfarming;

import de.tr7zw.nbtapi.NBTItem;
import io.github.openspigot.openfarming.command.OpenFarmingCommand;
import io.github.openspigot.openfarming.database.BlockStore;
import io.github.openspigot.openfarming.farms.FarmBlock;
import io.github.openspigot.openfarming.farms.FarmType;
import io.github.openspigot.openfarming.level.LevelManager;
import io.github.openspigot.openfarming.listener.BlockFadeListener;
import io.github.openspigot.openfarming.listener.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class OpenFarming extends JavaPlugin {
    // Constants
    public static final Material FARM_MATERIAL = Material.END_ROD;

    private static OpenFarming instance;

    private final LevelManager levelManager = new LevelManager();
    private BlockStore<FarmBlock> farmStore;

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();

        // Register Commands
        getCommand("openfarming").setExecutor(new OpenFarmingCommand(this));

        this.farmStore = new BlockStore<>(this, FARM_MATERIAL, new File(getDataFolder(), "farms.json"));

        // Register Listeners
        new BlockPlaceListener(this);
        new BlockFadeListener(this);
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

    public static OpenFarming getInstance() {
        return instance;
    }
}
