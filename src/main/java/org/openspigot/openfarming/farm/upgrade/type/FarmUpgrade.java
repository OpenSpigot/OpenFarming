package org.openspigot.openfarming.farm.upgrade.type;

import com.github.stefvanschie.inventoryframework.GuiItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmGUI;
import org.openspigot.openfarming.util.LangUtils;

public abstract class FarmUpgrade<T, V>{
    protected String configReference;

    public FarmUpgrade(String configReference) {
        this.configReference = configReference;
    }

    public abstract V getRaw(FarmBlock block);
    public abstract void setRaw(FarmBlock block, V value);

    public abstract int getEcoCost(FarmBlock block);
    public abstract int getExpCost(FarmBlock block);

    public abstract T getValue(FarmBlock block);

    public abstract GuiItem getItem(FarmBlock block, FarmGUI gui);

    protected GuiItem toGuiItem(FarmBlock block, FarmGUI gui, ItemStack item) {
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

    public abstract boolean canPurchase(Player player, FarmBlock block);

    public boolean purchaseWithBalance(Player player, FarmBlock block) {
        Economy economy = OpenFarming.getInstance().getEconomy();
        if(economy == null || getEcoCost(block) == 0  || !canPurchase(player, block)) return false;

        if(economy.getBalance(player) < getEcoCost(block)) {
            // They can't afford the upgrade
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        economy.withdrawPlayer(player, getEcoCost(block));
        purchase(player, block);

        return true;
    }

    public boolean purchaseWithExperience(Player player, FarmBlock block) {
        if(getExpCost(block) == 0 || !canPurchase(player, block)) return false;
        if(player.getTotalExperience() < getExpCost(block)) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1, 1);
            player.sendMessage(LangUtils.parse("messages.insufficient_funds", player));
            return false;
        }

        // Complete Purchase
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.setTotalExperience(player.getTotalExperience() - getExpCost(block));
        purchase(player, block);

        return true;
    }

    protected abstract void purchase(Player player, FarmBlock block);

    public static void addEnchantEffect(ItemMeta meta) {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
    }
}
