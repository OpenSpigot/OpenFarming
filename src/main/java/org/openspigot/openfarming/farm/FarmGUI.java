package org.openspigot.openfarming.farm;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.util.LangUtils;

public class FarmGUI extends Gui {
    private final FarmBlock owner;

    public FarmGUI(FarmBlock owner) {
        super(OpenFarming.getInstance(), 6, "Farm Overview");
        this.owner = owner;

        // UI
        StaticPane headerPane = new StaticPane(0, 0, 9, 3);
        PaginatedPane inventoryPane = new PaginatedPane(0, 2, 9, 3);

        //
        // Configure Panes
        //
        headerPane.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        headerPane.addItem(createOverviewItem(), 1, 1);

        headerPane.addItem(createReplantUpgradeItem(), 5, 1);
        headerPane.addItem(createUpgradeRadiusItem(), 6, 1);
        headerPane.addItem(createUpgradeSpeedItem(), 7, 1);

        addPane(headerPane);
        addPane(inventoryPane);
        update();
    }

    @Override
    public void show(HumanEntity humanEntity) {
        // Only show the UI to one user at a time
        if(getViewers().size() < 1) {
            super.show(humanEntity);
            return;
        }

        humanEntity.sendMessage("This farm is already being accessed.");
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

    private GuiItem createReplantUpgradeItem() {
        OpenFarming plugin = OpenFarming.getInstance();

        ItemStack item = new ItemStack(OpenFarming.UPGRADE_REPLANT_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        if(owner.isReplant()) {
            meta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.autoReplant.purchased_name", ""), owner));
            meta.setLore(LangUtils.parseFarmLore(plugin.getConfig().getStringList("gui.autoReplant.purchased_lore"), owner));

            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        } else {
            meta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.autoReplant.name", ""), owner));
            meta.setLore(LangUtils.parseFarmLore(plugin.getConfig().getStringList("gui.autoReplant.lore"), owner));
        }

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> inventoryClickEvent.setCancelled(true)));
    }

    private GuiItem createUpgradeRadiusItem() {
        OpenFarming plugin = OpenFarming.getInstance();

        ItemStack item = new ItemStack(OpenFarming.UPGRADE_RADIUS_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.radiusUpgrade.name", ""), owner));
        meta.setLore(LangUtils.parseFarmLore(plugin.getConfig().getStringList("gui.radiusUpgrade.lore"), owner));

        if(owner.isMaxRadius()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        }

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> inventoryClickEvent.setCancelled(true)));
    }

    private GuiItem createUpgradeSpeedItem() {
        OpenFarming plugin = OpenFarming.getInstance();

        ItemStack item = new ItemStack(OpenFarming.UPGRADE_SPEED_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.speedUpgrade.name", ""), owner));
        meta.setLore(LangUtils.parseFarmLore(plugin.getConfig().getStringList("gui.speedUpgrade.lore"), owner));

        if(owner.isMaxSpeed()) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        }

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> inventoryClickEvent.setCancelled(true)));
    }
}
