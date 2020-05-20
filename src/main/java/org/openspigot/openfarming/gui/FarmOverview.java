package org.openspigot.openfarming.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import org.bukkit.entity.HumanEntity;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;

public class FarmOverview extends Gui {
    private final FarmBlock owner;

    public FarmOverview(FarmBlock owner) {
        super(OpenFarming.getInstance(), 6, "Farm Overview");

        this.owner = owner;
    }

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
