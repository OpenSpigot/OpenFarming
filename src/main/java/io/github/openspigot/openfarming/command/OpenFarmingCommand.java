package io.github.openspigot.openfarming.command;

import io.github.openspigot.openfarming.OpenFarming;
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

        if(args[0].equalsIgnoreCase("givefarm")) {
            int farmLevel = 1;
            Player targetPlayer = null;

            if(args.length > 1) {
                if(Bukkit.getPlayer(args[1]) == null) {
                    sender.sendMessage("Invalid player");
                    return true;
                }

                targetPlayer = Bukkit.getPlayer(args[1]);
            } else {
                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "You need to be a player to give yourself a farm.");
                    return true;
                }

                targetPlayer = (Player) sender;
            }

            assert targetPlayer != null;
            targetPlayer.getInventory().addItem(plugin.createFarmItem(2));
        }


        return true;
    }
}
