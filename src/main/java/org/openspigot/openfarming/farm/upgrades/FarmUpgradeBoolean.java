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

public abstract class FarmUpgradeBoolean implements IFarmUpgrade<Boolean> {
    private final String configReference;
    private final FarmUpgradeLevel<Boolean> upgrade;

    public FarmUpgradeBoolean(String configReference) {
        this.configReference = configReference;

        ConfigurationSection section = OpenFarming.getInstance().getConfig().getConfigurationSection("upgrades." + configReference);
        this.upgrade = new FarmUpgradeLevel<>(1, true, section.getInt("ecoCost"), section.getInt("expCost"));
    }

    @Override
    public abstract Boolean getValue(FarmBlock block);
    public abstract void setValue(FarmBlock block, boolean value);

    @Override
    public GuiItem createItem(FarmBlock block, FarmGUI gui, Material material) {
        OpenFarming plugin = OpenFarming.getInstance();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("gui." + configReference + "Upgrade");
        if(getValue(block)) {
            meta.setDisplayName(LangUtils.parseFarmMessage(section.getString("purchased_name"), block));
            meta.setLore(LangUtils.parseFarmLore(section.getStringList("purchased_lore"), block));

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

    @Override
    public boolean purchaseWithBalance(Player player, FarmBlock block) {
        if(getValue(block)) {
            // Already Upgraded
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        if(upgrade.getEcoCost() == 0) return false;

        Economy economy = OpenFarming.getInstance().getEconomy();
        if(economy.getBalance(player) < upgrade.getEcoCost()) {
            // They can't afford the upgrade
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        economy.withdrawPlayer(player, upgrade.getEcoCost());
        purchase(player, block);

        return true;
    }

    @Override
    public boolean purchaseWithExperience(Player player, FarmBlock block) {
        if(getValue(block)) {
            // Already Upgraded
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            return false;
        }

        if(upgrade.getExpCost() == 0) return false;

        if(player.getTotalExperience() < upgrade.getExpCost()) {
            // They can't afford the upgrade
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.setTotalExperience(player.getTotalExperience() - upgrade.getExpCost());
        purchase(player, block);

        return true;
    }

    protected void purchase(Player player, FarmBlock block) {
        setValue(block, true);
    }

    //
    // Getters
    //
    public FarmUpgradeLevel<Boolean> getUpgrade() {
        return upgrade;
    }
}
