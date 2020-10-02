package org.openspigot.openfarming.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmItem;
import org.openspigot.openfarming.farm.FarmType;

import java.util.HashMap;

@Description("OpenFarming Main Command")
@CommandAlias("openfarming")
public class OpenFarmingCommand extends BaseCommand {
    private final OpenFarming plugin;

    public OpenFarmingCommand(OpenFarming plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    @Syntax("<?command>")
    public void help(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("saveall")
    @Description("Saves all currently placed persistent blocks to the DB")
    @CommandPermission("openfarming.admin.saveall")
    public void saveAll() {
        plugin.getFarmBlockStore().saveAll();
    }

    @Subcommand("givefarm")
    @Description("Gives a player a farm item")
    @Syntax("<type> <radius> <speed> <replant> <?player>")
    @CommandPermission("openfarming.admin.give")
    @CommandCompletion("crop|animal|all * * * *")
    public void onGiveItem(CommandSender sender, @Values("crop|animal|all")FarmType type, int radius, int speed, boolean replant, @Optional OnlinePlayer player) {
        if(!(sender instanceof Player) && player == null) {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command without a player");
            return;
        }

        Player target = (player != null) ? player.getPlayer() : (Player) sender;

        HashMap<Integer, ItemStack> excess = target.getInventory().addItem(new FarmItem(type, radius, speed, replant).build());
        if(excess.size() != 0) {
            sender.sendMessage(ChatColor.RED + "The player has no room in their inventory");
        }
    }
}
