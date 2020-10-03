package org.openspigot.openfarming.farm.upgrades;

import com.github.stefvanschie.inventoryframework.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmGUI;

public interface IFarmUpgrade<T> {
    T getValue(FarmBlock block);
    GuiItem createItem(FarmBlock block, FarmGUI gui, Material material);
    boolean purchaseWithBalance(Player player, FarmBlock block);
    boolean purchaseWithExperience(Player player, FarmBlock block);
}
