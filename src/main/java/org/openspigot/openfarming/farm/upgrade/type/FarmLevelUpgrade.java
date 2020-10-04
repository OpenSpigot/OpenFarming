package org.openspigot.openfarming.farm.upgrade.type;

import com.github.stefvanschie.inventoryframework.GuiItem;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmGUI;
import org.openspigot.openfarming.util.LangUtils;

import java.util.ArrayList;

public abstract class FarmLevelUpgrade<T> extends FarmUpgrade<T, Integer> {
    private final ArrayList<FarmUpgradeLevel<T>> levels = new ArrayList<>();

    public FarmLevelUpgrade(String configReference) {
        super(configReference);

        ConfigurationSection section = getSection().getConfigurationSection("levels");
        for(String key : section.getKeys(false)) {
            System.out.println(key);
            ConfigurationSection levelConfig = section.getConfigurationSection(key);
            if(levelConfig == null) continue;

            levels.add(new FarmUpgradeLevel<T>(Integer.parseInt(key), (T)levelConfig.get("value"), levelConfig.getInt("ecoCost"), levelConfig.getInt("expCost")));
        }
    }

    private ConfigurationSection getSection() {
        return OpenFarming.getInstance().getConfig().getConfigurationSection("upgrades." + configReference);
    }

    @Override
    public T getValue(FarmBlock block) {
        return getLevel(block).getValue();
    }

    //
    // Purchases
    //
    @Override
    public int getExpCost(FarmBlock block) {
        return getLevel(getRaw(block) + 1).getExpCost();
    }
    @Override
    public int getEcoCost(FarmBlock block) {
        return getLevel(getRaw(block) + 1).getEcoCost();
    }

    @Override
    public boolean canPurchase(Player player, FarmBlock block) {
        return !isMaxLevel(block);
    }

    @Override
    protected void purchase(Player player, FarmBlock block) {
        setRaw(block, getRaw(block) + 1);
        ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
            .put("VALUE", String.valueOf(getLevel(block).getValue()))
            .build();
        player.sendMessage(LangUtils.parse(getSection().getString("purchased"), player, placeholders, true));
    }

    //
    // Levels
    //
    public FarmUpgradeLevel<T> getLevel(int level) {
        if(level >= levels.size()) {
            return levels.get(getMaxLevel());
        }
        return levels.get(level);
    }

    public FarmUpgradeLevel<T> getLevel(FarmBlock block) {
        return getLevel(getRaw(block));
    }

    public int getMaxLevel() {
        return levels.size() - 1;
    }

    public boolean isMaxLevel(FarmBlock block) {
        return getRaw(block) >= getMaxLevel();
    }

    //
    // GUI Item
    //
    @Override
    public GuiItem getItem(FarmBlock block, FarmGUI gui) {
        ItemStack item = new ItemStack(Material.valueOf(getSection().getString("gui.material")));
        ItemMeta meta = item.getItemMeta();

        if(isMaxLevel(block)) {
            ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                    .put("VALUE", String.valueOf(getValue(block)))
                    .build();

            meta.setDisplayName(LangUtils.parse(getSection().getString("gui.max.name"), null, placeholders, true));
            meta.setLore(LangUtils.parseLore(getSection().getStringList("gui.max.lore"), null, placeholders));
            addEnchantEffect(meta);
        } else {
            ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                    .put("NEXTVALUE", String.valueOf(getValue(block)))
                    .put("ECO", String.valueOf(getEcoCost(block)))
                    .put("EXP", String.valueOf(getExpCost(block)))
                    .build();

            meta.setDisplayName(LangUtils.parse(getSection().getString("gui.purchasable.name"), null, placeholders, true));
            meta.setLore(LangUtils.parseLore(getSection().getStringList("gui.purchasable.lore"), null, placeholders));
        }

        item.setItemMeta(meta);
        return toGuiItem(block, gui, item);
    }
}
