package io.github.openspigot.openfarming;

import de.tr7zw.nbtapi.NBTItem;
import io.github.openspigot.openfarming.command.OpenFarmingCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpenFarming extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("openfarming").setExecutor(new OpenFarmingCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack createFarmItem(int level) {
        ItemStack item = new ItemStack(Material.END_ROD);

        // Meta
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Level &a&l" + level + " &r&7Farm"));
        item.setItemMeta(itemMeta);

        // NBT
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("farmingLevel", level);

        return nbtItem.getItem();
    }
}
