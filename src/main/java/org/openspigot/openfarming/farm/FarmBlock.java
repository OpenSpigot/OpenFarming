package org.openspigot.openfarming.farm;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.database.PersistentBlock;

import java.util.UUID;

public class FarmBlock extends PersistentBlock {
    private int radius;
    private int speed;
    private boolean replant;

    private UUID owner;
    private FarmType type;

    private transient FarmGUI gui;

    //
    // Constructor
    //
    public FarmBlock(Location location, Player owner, FarmType type, int radius, int speed, boolean replant) {
        super(location, true);

        this.radius = radius;
        this.speed = speed;
        this.replant = replant;

        this.owner = owner.getUniqueId();
        this.type = type;
    }
    public FarmBlock(Location location, Player owner, FarmItem item) {
        this(location, owner, item.getType(), item.getRadius(), item.getSpeed(), item.isReplant());
    }

    //
    // Events
    //
    @Override
    public void init() {
        this.gui = new FarmGUI(this);
        tillLand();
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if(event.getHand() == EquipmentSlot.OFF_HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        event.setCancelled(true);
        gui.show(event.getPlayer());
    }

    //
    // Public
    //
    public void tillLand() {
        if(type == FarmType.ANIMAL) {
            return;
        }

        for(int lx = -radius; lx <= radius; lx++) {
            for(int lz = -radius; lz <= radius; lz++) {
                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() - 1, location.getBlockZ() + lz);

                if(lBlock.getType() != Material.DIRT && lBlock.getType() != Material.GRASS_BLOCK) {
                    continue;
                }

                if(lBlock.getRelative(BlockFace.UP).getType() != Material.AIR) {
                    continue;
                }

                Bukkit.getScheduler().runTaskLater(OpenFarming.getInstance(), () -> {
                    lBlock.setType(Material.FARMLAND);
                    lBlock.getWorld().playSound(lBlock.getLocation(), Sound.ITEM_HOE_TILL, 10, 15);
                }, (Math.abs(lx) + Math.abs(lz)) * 2);
            }
        }
    }

    public void harvest(Block block) {
        block.setType(Material.AIR);
    }

    public boolean isMaxRadius() {
        return radius >= OpenFarming.getInstance().maxUpgradeAmount("gui.radiusUpgrade");
    }
    public boolean isMaxSpeed() {
        return speed >= OpenFarming.getInstance().maxUpgradeAmount("gui.speedUpgrade");
    }

    //
    // Getters
    //
    public int getRadius() {
        if(isMaxRadius()) {
            // Lock to the max radius achievable in the config
            return OpenFarming.getInstance().maxUpgradeAmount("gui.radiusUpgrade");
        }

        return radius;
    }

    public int getSpeed() {
        if(isMaxSpeed()) {
            // Lock to the max speed achievable in the config
            return OpenFarming.getInstance().maxUpgradeAmount("gui.speedUpgrade");
        }

        return speed;
    }

    public boolean isReplant() {
        return replant;
    }

    public UUID getOwner() {
        return owner;
    }

    public FarmType getType() {
        return type;
    }
}
