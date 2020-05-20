package org.openspigot.openfarming.command;

import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenFarmingCommand implements CommandExecutor {
    private final OpenFarming plugin;

    public OpenFarmingCommand(OpenFarming plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            return true;
        }
        //              0        1      2       3
        // /openfarming givefarm <type> <level> <?player>

        if(args[0].equalsIgnoreCase("givefarm")) {
            Player targetPlayer;

            if(args.length > 3) {
                if(Bukkit.getPlayer(args[3]) == null) {
                    sender.sendMessage("Invalid player");
                    return true;
                }

                targetPlayer = Bukkit.getPlayer(args[3]);
            } else {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "You need to be a player to give yourself a farm.");
                    return true;
                }

                targetPlayer = (Player) sender;
            }

            int farmLevel;
            try {
                farmLevel = Integer.parseInt(args[2]);
            } catch (Exception e) {
                sender.sendMessage("Invalid Integer (Level)");
                return true;
            }

            FarmType type = FarmType.valueOf(args[1].toUpperCase());

            assert targetPlayer != null;
            targetPlayer.getInventory().addItem(plugin.createFarmItem(farmLevel, type));
        }

        if(args[0].equalsIgnoreCase("loc")) {
            Player player = (Player) sender;

            player.sendMessage("X: " + player.getLocation().getChunk().getX() + ", Z: " + player.getLocation().getChunk().getZ());
            return true;
        }

        if(args[0].equalsIgnoreCase("saveall")) {
            plugin.getFarmStore().saveAll();
        }


        return true;
    }
}
