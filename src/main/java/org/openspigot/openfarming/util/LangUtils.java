package org.openspigot.openfarming.util;

import org.bukkit.ChatColor;
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
                .replaceAll("\\{FARM-RADIUS}" , String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getValue()))
                .replaceAll("\\{FARM-SPEED}"  , String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getValue()))
                .replaceAll("\\{FARM-TYPE}"   , translateFarmType(farm.getType()))
                .replaceAll("\\{FARM-REPLANT}", toYesNo(farm.isReplant()))

                .replaceAll("\\{NEXT-RADIUS}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getValue()))
                .replaceAll("\\{NEXT-RADIUS-BALCOST}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getEcoCost()))
                .replaceAll("\\{NEXT-RADIUS-EXPCOST}", String.valueOf(FarmUpgrades.RADIUS_UPGRADE.getLevel(farm.getRadius() + 1).getExpCost()))

                .replaceAll("\\{NEXT-SPEED}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getValue()))
                .replaceAll("\\{NEXT-SPEED-BALCOST}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getEcoCost()))
                .replaceAll("\\{NEXT-SPEED-EXPCOST}", String.valueOf(FarmUpgrades.SPEED_UPGRADE.getLevel(farm.getSpeed() + 1).getExpCost()))
        );
    }
}