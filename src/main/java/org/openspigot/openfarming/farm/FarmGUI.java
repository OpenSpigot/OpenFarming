package org.openspigot.openfarming.farm;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.upgrades.FarmUpgrades;
import org.openspigot.openfarming.util.LangUtils;

public class FarmGUI extends Gui {
    private final FarmBlock owner;
    StaticPane headerPane;

    public FarmGUI(FarmBlock owner) {
        super(6, LangUtils.parse("gui.title", null));
        this.owner = owner;

        // UI
        headerPane = new StaticPane(0, 0, 9, 3);
        PaginatedPane inventoryPane = new PaginatedPane(0, 2, 9, 3);

        //
        // Configure Panes
        //
        headerPane.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        refreshUpgrades();

        ItemMeta meta;
        ItemStack previousPage = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        meta = previousPage.getItemMeta();
        meta.setDisplayName(LangUtils.parse("gui.previous.title", null));
        previousPage.setItemMeta(meta);
        headerPane.addItem(new GuiItem(previousPage), 0, 2);

        ItemStack nextPage = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        meta = nextPage.getItemMeta();
        meta.setDisplayName(LangUtils.parse("gui.next.title", null));
        nextPage.setItemMeta(meta);

        headerPane.addItem(new GuiItem(nextPage), 8, 2);

        addPane(headerPane);
        addPane(inventoryPane);
        update();
    }

    public void refreshUpgrades() {
        headerPane.addItem(createOverviewItem(), 1, 1);
        headerPane.addItem(FarmUpgrades.AUTO_REPLANT_UPGRADE.createItem(owner, this, OpenFarming.UPGRADE_REPLANT_MATERIAL), 5, 1);
        headerPane.addItem(FarmUpgrades.RADIUS_UPGRADE.createItem(owner, this, OpenFarming.UPGRADE_RADIUS_MATERIAL), 6, 1);
        headerPane.addItem(FarmUpgrades.SPEED_UPGRADE.createItem(owner, this, OpenFarming.UPGRADE_SPEED_MATERIAL), 7, 1);
        update();
    }

    //
    // Private
    //
    private GuiItem createOverviewItem() {
        OpenFarming plugin = OpenFarming.getInstance();

        ItemStack item = new ItemStack(OpenFarming.FARM_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.overview.title", ""), owner));
        meta.setLore(LangUtils.parseFarmLore(plugin.getConfig().getStringList("gui.overview.lore"), owner));

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> inventoryClickEvent.setCancelled(true)));
    }

    //
    // Utility
    //
    private static void addEnchantEffect(ItemMeta meta) {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
    }
}
