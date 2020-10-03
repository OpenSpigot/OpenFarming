package org.openspigot.openfarming.farm.upgrades;

import com.github.stefvanschie.inventoryframework.GuiItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmGUI;
import org.openspigot.openfarming.util.LangUtils;

import java.util.ArrayList;

public abstract class FarmUpgrade<T> implements IFarmUpgrade<T> {
    private final String configReference;
    private final ArrayList<FarmUpgradeLevel<T>> levels = new ArrayList<>();

    public FarmUpgrade(String configReference) {
        this.configReference = configReference;
        ConfigurationSection configLevels =  OpenFarming.getInstance().getConfig().getConfigurationSection("upgrades." + configReference + ".levels");

        for(String key : configLevels.getKeys(false)) {
            ConfigurationSection levelConfig = configLevels.getConfigurationSection(key);
            if(levelConfig == null) continue;

            levels.add(new FarmUpgradeLevel<T>(Integer.parseInt(key), (T)levelConfig.get("value"), levelConfig.getInt("ecoCost"), levelConfig.getInt("expCost")));
        }
    }

    // Expected Overrides
    public abstract int getRawLevel(FarmBlock block);
    public abstract void setRawLevel(FarmBlock block, int level);
    protected void upgrade(Player player, FarmBlock block) {
        setRawLevel(block, getRawLevel(block) + 1);
    }

    public GuiItem createItem(FarmBlock block, FarmGUI gui, Material material) {
        OpenFarming plugin = OpenFarming.getInstance();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("gui." + configReference + "Upgrade");

        if(isMaxLevel(getRawLevel(block))) {
            meta.setDisplayName(LangUtils.parseFarmMessage(section.getString("nameMaxLevel"), block));
            meta.setLore(LangUtils.parseFarmLore(section.getStringList("loreMaxLevel"), block));

            // Add enchant effect
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        } else {
            meta.setDisplayName(LangUtils.parseFarmMessage(section.getString("name"), block));
            meta.setLore(LangUtils.parseFarmLore(section.getStringList("lore"), block));
        }

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
            if(!(inventoryClickEvent.getWhoClicked() instanceof Player)) return;

            if(inventoryClickEvent.isLeftClick()) {
                if(purchaseWithBalance((Player) inventoryClickEvent.getWhoClicked(), block)) gui.refreshUpgrades();
            } else if(inventoryClickEvent.isRightClick()) {
                if(purchaseWithExperience((Player) inventoryClickEvent.getWhoClicked(), block)) gui.refreshUpgrades();
            }
        }));
    }

    //
    // Public
    //
    public FarmUpgradeLevel<T> getLevel(int level) {
        // Get last available if too high of a level.
        if(level >= levels.size()) {
            return levels.get(levels.size() - 1);
        }

        return levels.get(level);
    }
    public FarmUpgradeLevel<T> getLevel(FarmBlock block) {
        return getLevel(getRawLevel(block));
    }


    //
    // Utility Getters
    public T getValue(FarmBlock block) {
        return getLevel(block).getValue();
    }

    public int getMaxLevel() {
        return levels.size() - 1;
    }

    public boolean isMaxLevel(int level) {
        return level >= getMaxLevel();
    }

    //
    // Purchasing Upgrade
    public boolean purchaseWithBalance(Player player, FarmBlock block) {
        int currentLevel = getRawLevel(block);

        // Check if it's already the max level
        if(isMaxLevel(currentLevel)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        Economy economy = OpenFarming.getInstance().getEconomy();
        if(economy == null) return false;
        int cost = getLevel(currentLevel + 1).getEcoCost();
        if(cost == 0) return false;

        // Check if the player has enough money
        if(economy.getBalance(player) < cost) {
            // They can't afford the upgrade
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        economy.withdrawPlayer(player, cost);
        upgrade(player, block);
        return true;
    }

    public boolean purchaseWithExperience(Player player, FarmBlock block) {
        int currentLevel = getRawLevel(block);

        // Check if it's already the max level
        if(isMaxLevel(currentLevel)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        int cost = getLevel(currentLevel + 1).getExpCost();
        if(cost == 0) return false;

        if(player.getTotalExperience() < cost) {
            // They can't afford the upgrade
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.setTotalExperience(player.getTotalExperience() - cost);
        upgrade(player, block);

        return true;
    }
}
