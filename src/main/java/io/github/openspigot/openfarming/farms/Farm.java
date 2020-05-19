//package io.github.openspigot.openfarming.farms;
//
//import io.github.openspigot.openfarming.OpenFarming;
//import io.github.openspigot.openfarming.gui.FarmGUI;
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.Sound;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//
//import java.util.Random;
//import java.util.UUID;
//
//public class Farm {
//    private final Random random = new Random();
//
//    private Location location;
//    private int level;
//    private UUID owner;
//
//    private FarmType type;
//    private FarmGUI gui;
//
//    //
//    // Constructor
//    //
//    public Farm(Location location, int level, UUID owner, FarmType type) {
//        this.location = location;
//        this.level = level;
//        this.owner = owner;
//        this.type = type;
//
//        this.gui = new FarmGUI(OpenFarming.getPlugin(OpenFarming.class), this);
//    }
//
//    //
//    // Public
//    //
//
//    public void tillLand() {
//        // Prevent tilling if not a crop type
//        if(type == FarmType.ANIMAL) {
//            return;
//        }
//
//        int radius = 4;
//
//        for(int lx = -radius; lx <= radius; lx++) {
//            for(int lz = -radius; lz <= radius; lz++) {
//                Block lBlock = location.getWorld().getBlockAt(location.getBlockX() + lx, location.getBlockY() - 1, location.getBlockZ() + lz);
//
//                if(lBlock.getType() != Material.DIRT && lBlock.getType() != Material.GRASS_BLOCK) {
//                    continue;
//                }
//
//                Bukkit.getScheduler().runTaskLater(OpenFarming.getPlugin(OpenFarming.class), () -> {
//                    if(lBlock.getRelative(BlockFace.UP).getType() != Material.AIR) {
//                        return;
//                    }
//
//                    lBlock.setType(Material.FARMLAND);
//                    lBlock.getWorld().playSound(lBlock.getLocation(), Sound.ITEM_HOE_TILL, 10, 15);
//                }, (Math.abs(lx) + Math.abs(lz)) * 2);
//            }
//        }
//    }
//
//
//    //
//    // Getters
//    //
//
//    public Location getLocation() {
//        return location;
//    }
//
//    public int getLevel() {
//        return level;
//    }
//
//    public UUID getOwner() {
//        return owner;
//    }
//
//    public FarmType getType() {
//        return type;
//    }
//
//    public FarmGUI getGui() {
//        return gui;
//    }
//}
