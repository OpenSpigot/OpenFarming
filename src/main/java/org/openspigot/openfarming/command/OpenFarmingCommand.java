package org.openspigot.openfarming.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openspigot.openfarming.OpenFarming;
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

    @Subcommand("givefarm")
    @Description("Gives a player a farm item")
    @Syntax("<type> <level> <?player>")
    @CommandPermission("openfarming.admin.give")
    @CommandCompletion("crop|animal|all @level *")
    public void onGiveItem(CommandSender sender, @Values("crop|animal|all") FarmType type, int level, @Optional OnlinePlayer player) {
        if(!(sender instanceof Player) && player == null) {
            sender.sendMessage("&cYou must be a player to run this command without a player");
            return;
        }

        Player targetPlayer = (player != null) ? player.getPlayer() : (Player) sender;

        HashMap<Integer, ItemStack> didntFit = targetPlayer.getInventory().addItem(plugin.createFarmItem(level, type));
        if(didntFit.size() != 0) {
            sender.sendMessage("The player has no room in their inventory");
        }
    }

    @Subcommand("saveall")
    @Description("Saves all currently placed persistent blocks to the db")
    @CommandPermission("openfarming.admin.saveall")
    public void saveAll() {
        plugin.getFarmStore().saveAll();
    }

    @Subcommand("reload")
    @Description("Reloads OpenFarming and all configs")
    @CommandPermission("openfarming.admin.reload")
    public void reload() {

    }

}
