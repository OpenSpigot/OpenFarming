package org.openspigot.openfarming.farm;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.upgrade.FarmUpgrades;
import org.openspigot.openfarming.util.LangUtils;

public class FarmGUI extends Gui {
    private final FarmBlock owner;
    StaticPane headerPane;

    public FarmGUI(FarmBlock owner) {
        super(6, LangUtils.parse("gui.title"));
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
        meta.setDisplayName(LangUtils.parse("gui.previous.title"));
        previousPage.setItemMeta(meta);
        headerPane.addItem(new GuiItem(previousPage), 0, 2);

        ItemStack nextPage = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        meta = nextPage.getItemMeta();
        meta.setDisplayName(LangUtils.parse("gui.next.title"));
        nextPage.setItemMeta(meta);

        headerPane.addItem(new GuiItem(nextPage), 8, 2);

        addPane(headerPane);
        addPane(inventoryPane);
        update();
    }

    public void refreshUpgrades() {
        headerPane.addItem(createOverviewItem(), 1, 1);
        headerPane.addItem(FarmUpgrades.AUTO_REPLANT.getItem(owner, this), 5, 1);
        headerPane.addItem(FarmUpgrades.RADIUS.getItem(owner, this), 6, 1);
        headerPane.addItem(FarmUpgrades.SPEED.getItem(owner, this), 7, 1);
        update();
    }

    //
    // Private
    //
    private GuiItem createOverviewItem() {
        ItemStack item = new ItemStack(OpenFarming.FARM_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        ImmutableMap<String, String> placeholders = ImmutableMap.<String, String>builder()
                .put("TYPE", LangUtils.translateFarmType(owner.getType()))
                .put("RADIUS", String.valueOf(FarmUpgrades.RADIUS.getValue(owner)))
                .put("SPEED", String.valueOf(FarmUpgrades.SPEED.getValue(owner)))
                .put("REPLANT", LangUtils.toYesNo(FarmUpgrades.AUTO_REPLANT.getValue(owner)))
                .build();

        meta.setDisplayName(LangUtils.parse("gui.overview.title", null, placeholders));
        meta.setLore(LangUtils.parseLore("gui.overview.lore", null, placeholders));

        item.setItemMeta(meta);

        return new GuiItem(item, (inventoryClickEvent -> inventoryClickEvent.setCancelled(true)));
    }
}
