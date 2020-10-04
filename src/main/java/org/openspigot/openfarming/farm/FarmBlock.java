package org.openspigot.openfarming.farm;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.database.PersistentBlock;
import org.openspigot.openfarming.farm.upgrade.FarmUpgrades;
import org.openspigot.openfarming.util.LangUtils;

import java.util.UUID;

public class FarmBlock extends PersistentBlock {
    private int radius;
    private int speed;
    private boolean replant;

    private final UUID owner;
    private final FarmType type;

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

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getPlayer().sendMessage(LangUtils.parse("messages.placed", event.getPlayer()));
    }

    @Override
    public void onBreak(BlockBreakEvent event) {
        // Prevent breaking if they're not the owner.
        if(!event.getPlayer().getUniqueId().equals(owner)) {
            event.getPlayer().sendMessage(LangUtils.parse("messages.locked", event.getPlayer()));
            event.setCancelled(true);
        }

        event.getPlayer().sendMessage(LangUtils.parse("messages.broken", event.getPlayer()));
        event.setDropItems(false);
        location.getWorld().dropItemNaturally(location, new FarmItem(type, radius, speed, replant).build());

        store.remove(location);
    }


    //
    // Public
    //
    public void tillLand() {
        if(type == FarmType.ANIMAL) {
            return;
        }

        int radius = FarmUpgrades.RADIUS.getValue(this);

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

    //
    // Getters
    //
    public UUID getOwner() {
        return owner;
    }

    public FarmType getType() {
        return type;
    }

    public int getRadius() {
        return radius;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isReplant() {
        return replant;
    }

    //
    // Setters
    //
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setReplant(boolean replant) {
        this.replant = replant;
    }
}
