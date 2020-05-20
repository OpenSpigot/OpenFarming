package org.openspigot.openfarming.farm;

import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.database.PersistentBlock;
import org.openspigot.openfarming.gui.FarmOverview;
import org.openspigot.openfarming.level.FarmLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.UUID;

public class FarmBlock extends PersistentBlock {

    private int level;
    private FarmType type;
    private UUID owner;

    private transient FarmOverview farmOverview = new FarmOverview(this);

    //
    // Constructors
    //
    public FarmBlock(Location location, int level, FarmType type, UUID owner) {
        super(location);

        this.level = level;
        this.type = type;
        this.owner = owner;

        till();
    }

    public FarmBlock(Location location, FarmLevel level, FarmType type, UUID owner) {
        this(location, level.getLevel(), type, owner);
    }

    //
    // Public
    //
    public void till() {
        if(type == FarmType.ANIMAL) {
            return;
        }

        int radius = getFarmLevel().getRadius();

        for(int lx = -radius; lx <= radius; lx++) {
            for(int lz = -radius; lz <= radius; lz++) {
                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() - 1, location.getBlockZ() + lz);

                if(lBlock.getType() != Material.DIRT && lBlock.getType() != Material.GRASS_BLOCK) {
                    continue;
                }

                Bukkit.getScheduler().runTaskLater(OpenFarming.getInstance(), () -> {
                    if(lBlock.getRelative(BlockFace.UP).getType() != Material.AIR) {
                        return;
                    }

                    lBlock.setType(Material.FARMLAND);
                    lBlock.getWorld().playSound(lBlock.getLocation(), Sound.ITEM_HOE_TILL, 10, 15);
                }, (Math.abs(lx) + Math.abs(lz)) * 2);
            }
        }

    }

    //
    // Events
    //
    @Override
    public void init() {
        this.farmOverview = new FarmOverview(this);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        farmOverview.show(event.getPlayer());
    }


    //
    // Getters
    //
    public int getLevel() {
        return level;
    }

    public FarmLevel getFarmLevel() {
        return OpenFarming.getInstance().getLevelManager().getLevel(level);
    }

    public FarmType getType() {
        return type;
    }

    public UUID getOwner() {
        return owner;
    }
}
