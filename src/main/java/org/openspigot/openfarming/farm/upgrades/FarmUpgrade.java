package org.openspigot.openfarming.farm.upgrades;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.OpenFarming;

import java.util.ArrayList;

public class FarmUpgrade<T> {
    private final String configReference;
    private final ArrayList<FarmUpgradeLevel<T>> levels = new ArrayList<>();

    public FarmUpgrade(String configReference) {
        this.configReference = configReference;

        ConfigurationSection configLevels = getConfig().getConfigurationSection("levels");
        for(String key : getConfig().getConfigurationSection("levels").getKeys(false)) {
            ConfigurationSection levelConfig = configLevels.getConfigurationSection(key);
            if(levelConfig == null) continue;

            levels.add(new FarmUpgradeLevel<T>(Integer.parseInt(key), (T)levelConfig.get("value"), levelConfig.getInt("ecoCost"), levelConfig.getInt("expCost")));
        }
    }

    //
    // Public
    //
    public ConfigurationSection getConfig() {
        return OpenFarming.getInstance().getConfig().getConfigurationSection("upgrades." + configReference);
    }

    public FarmUpgradeLevel<T> getLevel(int level) {
        // Get last available if too high of a level.
        if(level >= levels.size()) {
            return levels.get(levels.size() - 1);
        }

        return levels.get(level);
    }

    //
    // Utility Getters
    public T getValue(int level) {
        return getLevel(level).getValue();
    }

    public int getMaxLevel() {
        return levels.size() - 1;
    }

    public boolean isMaxLevel(int level) {
        return level == getMaxLevel();
    }

    //
    // Purchasing Upgrade
    public boolean purchaseWithBalance(Player player, int level) {
        Economy economy = OpenFarming.getInstance().getEconomy();
        if(economy == null) return false;

        int cost = getLevel(level).getEcoCost();
        if(economy.getBalance(player) < cost) {
            return false;
        }

        economy.withdrawPlayer(player, cost);
        return true;
    }
}
