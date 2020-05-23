package org.openspigot.openfarming.util;

import org.bukkit.ChatColor;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmType;
import org.openspigot.openfarming.level.FarmLevel;

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
        FarmLevel currentLevel = farm.getFarmLevel();
        FarmLevel nextLevel = OpenFarming.getInstance().getLevelManager().getLevel(farm.getLevel() + 1);

        return ChatColor.translateAlternateColorCodes('&', message
                .replaceAll("\\{FARM-LEVEL}"  , String.valueOf(currentLevel.getLevel()))
                .replaceAll("\\{FARM-RADIUS}" , String.valueOf(currentLevel.getRadius()))
                .replaceAll("\\{FARM-SPEED}"  , String.valueOf(currentLevel.getSpeed()))
                .replaceAll("\\{FARM-EXPCOST}", String.valueOf(currentLevel.getExpCost()))
                .replaceAll("\\{FARM-ECOCOST}", String.valueOf(currentLevel.getEcoCost()))
                .replaceAll("\\{FARM-PAGES}"  , String.valueOf(currentLevel.getPages()))
                .replaceAll("\\{FARM-TYPE}"   , translateFarmType(farm.getType()))
                .replaceAll("\\{FARM-REPLANT}", toYesNo(currentLevel.isReplant()))

                .replaceAll("\\{FARM-NEXTLEVEL}"        , String.valueOf(nextLevel.getLevel()))
                .replaceAll("\\{FARM-NEXTLEVEL-RADIUS}" , String.valueOf(nextLevel.getRadius()))
                .replaceAll("\\{FARM-NEXTLEVEL-SPEED}"  , String.valueOf(nextLevel.getSpeed()))
                .replaceAll("\\{FARM-NEXTLEVEL-EXPCOST}", String.valueOf(nextLevel.getExpCost()))
                .replaceAll("\\{FARM-NEXTLEVEL-ECOCOST}", String.valueOf(nextLevel.getEcoCost()))
                .replaceAll("\\{FARM-NEXTLEVEL-PAGES}"  , String.valueOf(nextLevel.getPages()))
                .replaceAll("\\{FARM-NEXTLEVEL-REPLANT}", toYesNo(nextLevel.isReplant()))
        );
    }
}
