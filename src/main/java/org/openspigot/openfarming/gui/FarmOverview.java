package org.openspigot.openfarming.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.util.LangUtils;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;

import java.util.ArrayList;

public class FarmOverview extends Gui {
    private final FarmBlock owner;

    private final StaticPane headerPane;
    private final PaginatedPane inventoryPane;

    private GuiItem overview, ecoUpgrade, expUpgrade;

    public FarmOverview(FarmBlock owner) {
        super(OpenFarming.getInstance(), 6, "Farm Overview");

        this.owner = owner;

        this.headerPane = new StaticPane(0, 0, 9, 3);
        this.inventoryPane = new PaginatedPane(0, 2, 9, 3);

        initItems();
        initPanes();

        addPane(headerPane);
        addPane(inventoryPane);
    }

    //
    // Private
    //
    private void initItems() {
        OpenFarming plugin = OpenFarming.getInstance();

        // Overview
        ItemStack overviewStack = new ItemStack(OpenFarming.FARM_MATERIAL);
        ItemMeta overviewMeta = overviewStack.getItemMeta();

        overviewMeta.setDisplayName(LangUtils.parseFarmMessage(plugin.getConfig().getString("gui.overview.title", ""), owner));

        ArrayList<String> lore = new ArrayList<>();
        if(owner.isMaxLevel()) {
            lore.addAll(plugin.getConfig().getStringList("gui.overview.loreMaxLevel"));
        } else {
            lore.addAll(plugin.getConfig().getStringList("gui.overview.lore"));
        }

        overviewMeta.setLore(LangUtils.parseFarmLore(lore, owner));
        overviewStack.setItemMeta(overviewMeta);

        overview = new GuiItem(overviewStack, (inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
        }));


        // Upgrades
        if(!owner.isMaxLevel()) {
            // Exp
            ItemStack expUpgradeStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
            ItemMeta expUpgradeMeta = expUpgradeStack.getItemMeta();
            expUpgradeMeta.setDisplayName("&e");

            expUpgradeStack.setItemMeta(expUpgradeMeta);
        }
    }

    private void initPanes() {
        headerPane.fillWith(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
        });

        headerPane.addItem(overview, 4, 1);

        update();
    }

    //
    // Overrides
    //
    @Override
    public void show(HumanEntity humanEntity) {
        // Only show the UI to 1 viewer at a time
        if(getViewers().size() < 1) {
            super.show(humanEntity);
            return;
        }

        // Send a message if it's already accessed.
        // TODO: Make message configurable
        humanEntity.sendMessage("This farm is already being accessed.");
    }
}
