package org.openspigot.openfarming.util;

import org.bukkit.ChatColor;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmBlock;
import org.openspigot.openfarming.farm.FarmType;

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
                .replaceAll("\\{FARM-RADIUS}" , String.valueOf(farm.getRadius()))
                .replaceAll("\\{FARM-SPEED}"  , String.valueOf(farm.getSpeed()))
                .replaceAll("\\{FARM-TYPE}"   , translateFarmType(farm.getType()))
                .replaceAll("\\{FARM-REPLANT}", toYesNo(farm.isReplant()))
        );
    }
}