package org.openspigot.openfarming.util;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmType;
import org.openspigot.openfarming.farm.upgrades.FarmUpgrades;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {
    public static String toYesNo(boolean bool) {
        if(bool) return "Yes";
        return "No";
    }

    public static List<String> parseFarmLore(List<String> message, FarmBlock farm) {
        ArrayList<String> result = new ArrayList<>();

        for(String line : message) {
            result.add(parseFarmMessage(line, farm));
        }

        return result;
    }

    public static String translateFarmType(FarmType type) {
        return OpenFarming.getInstance().getConfig().getString("messages.types." + type.toString().toLowerCase(), "");
    }

    public static String parseFarmMessage(String message, FarmBlock farm) {
        return ChatColor.translateAlternateColorCodes('&', message
                .replaceAll("\\{FARM-RADIUS}" , String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius()).getValue()))
                .replaceAll("\\{FARM-SPEED}"  , String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed()).getValue()))
                .replaceAll("\\{FARM-TYPE}"   , translateFarmType(farm.getType()))
                .replaceAll("\\{FARM-REPLANT}", toYesNo(farm.isReplant()))

                .replaceAll("\\{NEXT-RADIUS}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getValue()))
                .replaceAll("\\{NEXT-RADIUS-BALCOST}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getEcoCost()))
                .replaceAll("\\{NEXT-RADIUS-EXPCOST}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getExpCost()))

                .replaceAll("\\{NEXT-SPEED}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getValue()))
                .replaceAll("\\{NEXT-SPEED-BALCOST}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getEcoCost()))
                .replaceAll("\\{NEXT-SPEED-EXPCOST}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getExpCost()))

                .replaceAll("\\{REPLANT-BALCOST}",  String.valueOf(FarmUpgrades.AUTO_REPLANT_UPGRADE.getUpgrade().getEcoCost()))
                .replaceAll("\\{REPLANT-EXPCOST}",  String.valueOf(FarmUpgrades.AUTO_REPLANT_UPGRADE.getUpgrade().getExpCost()))
        );
    }

    public static String parse(String message, Player player) {
        return parse(message, player, ImmutableMap.<String, String>builder().build(), false);
    }

    public static String parse(String message, Player player, ImmutableMap<String, String> placeholders) {
        return parse(message, player, placeholders, false);
    }
    public static String parse(String message, Player player, ImmutableMap<String, String> placeholders, boolean raw) {
        if(!raw) {
            message = OpenFarming.getInstance().getConfig().getString(message);
        }

        placeholders = ImmutableMap.<String, String>builder()
                .putAll(placeholders)
                .put("OPENFARMING-PREFIX", OpenFarming.getInstance().getConfig().getString("messages.prefix", ""))
                .build();

        for(String key : placeholders.keySet()) {
            message = message.replaceAll("\\{" + key + "}", placeholders.get(key));
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}