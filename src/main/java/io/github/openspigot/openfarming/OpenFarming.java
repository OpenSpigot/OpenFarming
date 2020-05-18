package io.github.openspigot.openfarming;

import de.tr7zw.nbtapi.NBTItem;
import io.github.openspigot.openfarming.command.OpenFarmingCommand;
import io.github.openspigot.openfarming.farms.FarmManager;
import io.github.openspigot.openfarming.listener.BlockBreakListener;
import io.github.openspigot.openfarming.listener.BlockPlaceListener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenFarming extends JavaPlugin {
    public static final Material FARM_MATERIAL = Material.END_ROD;

    private FarmManager farmManager = new FarmManager();


    @Override
    public void onEnable() {
        getCommand("openfarming").setExecutor(new OpenFarmingCommand(this));

        new BlockPlaceListener(this);
        new BlockBreakListener(this);
    }

    //
    // Farm Item
    //
    public ItemStack createFarmItem(int level) {
        ItemStack item = new ItemStack(FARM_MATERIAL);

        // Meta
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Level &a&l" + level + " &r&7Farm"));
        item.setItemMeta(itemMeta);

        // NBT
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("farmLevel", level);

        return nbtItem.getItem();
    }

    public boolean isFarmItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getInteger("farmLevel") != null;
    }

    public int getFarmLevel(ItemStack item) {
        return new NBTItem(item).getInteger("farmLevel");
    }

    //
    // Getters
    //

    public FarmManager getFarmManager() {
        return farmManager;
    }
}
