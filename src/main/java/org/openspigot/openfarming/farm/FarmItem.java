package org.openspigot.openfarming.farm;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;

public class FarmItem {
    private FarmType type;
    private int radius;
    private int speed;
    private boolean replant;

    public FarmItem(FarmType type, int radius, int speed, boolean replant) {
        this.type = type;
        this.radius = radius;
        this.speed = speed;
        this.replant = replant;
    }

    //
    // Generate
    //
    public ItemStack build() {
        ItemStack item = new ItemStack(OpenFarming.FARM_MATERIAL);

        // Set Meta
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Automated &a&l" + type.toString() + " &r&7Farm"));
        item.setItemMeta(meta);

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("radius", radius);
        nbtItem.setInteger("speed", speed);
        nbtItem.setBoolean("replant", replant);
        nbtItem.setString("farmType", type.toString());

        return nbtItem.getItem();
    }

    //
    // Getters
    //
    public int getRadius() {
        return radius;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isReplant() {
        return replant;
    }

    public FarmType getType() {
        return type;
    }

    //
    // Setters
    //
    public FarmItem type(FarmType type) {
        this.type = type;
        return this;
    }

    public FarmItem radius(int radius) {
        this.radius = radius;
        return this;
    }

    public FarmItem speed(int speed) {
        this.speed = speed;
        return this;
    }

    public FarmItem replant(boolean replant) {
        this.replant = replant;
        return this;
    }

    //
    // Utility
    //
    public static FarmItem from(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);

        return new FarmItem(
            FarmType.valueOf(nbtItem.getString("farmType")),
            nbtItem.getInteger("radius"),
            nbtItem.getInteger("speed"),
            nbtItem.getBoolean("replant")
        );
    }

    public static boolean is(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);

        return nbtItem.hasKey("farmType") && nbtItem.hasKey("radius") && nbtItem.hasKey("speed") && nbtItem.hasKey("replant");
    }
}
