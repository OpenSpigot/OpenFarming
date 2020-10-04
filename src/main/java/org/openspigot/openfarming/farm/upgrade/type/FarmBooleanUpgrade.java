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

public abstract class FarmBooleanUpgrade extends FarmUpgrade<Boolean, Boolean> {
    public FarmBooleanUpgrade(String configReference) {
        super(configReference);
    }

    private ConfigurationSection getSection() {
        return OpenFarming.getInstance().getConfig().getConfigurationSection("upgrades." + configReference);
    }

    @Override
    public Boolean getValue(FarmBlock block) {
        return getRaw(block);
    }

    //
    // Purchases
    //
    @Override
    public int getEcoCost(FarmBlock block) {
        return getSection().getInt("ecoCost");
    }

    @Override
    public int getExpCost(FarmBlock block) {
        return getSection().getInt("expCost");
    }

    @Override
    public boolean canPurchase(Player player, FarmBlock block) {
        return !getValue(block);
    }

    @Override
    protected void purchase(Player player, FarmBlock block) {
        setRaw(block, true);
        player.sendMessage(LangUtils.parse(getSection().getString("purchased"), player, null, true));
    }

    //
    // GUI Item
    //
    @Override
    public GuiItem getItem(FarmBlock block, FarmGUI gui) {
        ItemStack item = new ItemStack(Material.valueOf(getSection().getString("gui.material")));
        ItemMeta meta = item.getItemMeta();

        if(getValue(block)) {
            // Unlocked
            meta.setDisplayName(LangUtils.parse(getSection().getString("gui.unlocked.name"), null, null, true));
            meta.setLore(LangUtils.parseLore(getSection().getStringList("gui.unlocked.lore")));
            addEnchantEffect(meta);
        } else {
            ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                    .put("ECO", String.valueOf(getEcoCost(block)))
                    .put("EXP", String.valueOf(getExpCost(block)))
                    .build();

            meta.setDisplayName(LangUtils.parse(getSection().getString("gui.locked.name"), null, placeholders, true));
            meta.setLore(LangUtils.parseLore(getSection().getStringList("gui.locked.lore"), null, placeholders));
        }

        item.setItemMeta(meta);
        return toGuiItem(block, gui, item);
    }
}
