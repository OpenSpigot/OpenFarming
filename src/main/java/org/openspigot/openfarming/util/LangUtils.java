package org.openspigot.openfarming.util;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.openspigot.openfarming.OpenFarming;
import org.openspigot.openfarming.farm.FarmType;

import java.util.ArrayList;
import java.util.List;

public class LangUtils {
    public static String toYesNo(boolean bool) {
        if(bool) return parse("messages.b_yes", null);
        return parse("messages.b_no", null);
    }

    public static String translateFarmType(FarmType type) {
        return OpenFarming.getInstance().getConfig().getString("messages.types." + type.toString().toLowerCase(), "");
    }

    //
    // Lore Parsing
    //
    public static List<String> parseLore(List<String> message, Player player, ImmutableMap<String, String> placeholders) {
        ArrayList<String> result = new ArrayList<>();

        for(String line : message) {
            result.add(parse(line, player, placeholders, true));
        }

        return result;
    }
    public static List<String> parseLore(List<String> message) {
        return parseLore(message, null, ImmutableMap.<String, String>builder().build());
    }
    public static List<String> parseLore(String path, Player player, ImmutableMap<String, String> placeholders) {
        return parseLore(OpenFarming.getInstance().getConfig().getStringList(path), player, placeholders);
    }

    //
    // Message Parsing
    //
    public static String parse(String message, Player player, ImmutableMap<String, String> placeholders, boolean raw) {
        // Check if the message is a path or raw text
        if(!raw) {
            message = OpenFarming.getInstance().getConfig().getString(message, "INVALID MESSAGE PATH");
        }

        // Global Placeholders
        ImmutableMap.Builder<String, String> placeholdersBuilder = ImmutableMap.<String, String>builder()
                .put("OPENFARMING-PREFIX", OpenFarming.getInstance().getConfig().getString("messages.prefix", ""));

        // Add additional placeholders
        if(placeholders != null) {
            placeholdersBuilder.putAll(placeholders);
        }

        // Build it!
        placeholders = placeholdersBuilder.build();

        // Replace all the placeholders in the final message
        for(String key : placeholders.keySet()) {
            message = message.replaceAll("\\{" + key + "}", placeholders.get(key));
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String parse(String message, Player player, ImmutableMap<String, String> placeholders) {
        return parse(message, player, placeholders, false);
    }

    public static String parse(String message, Player player) {
        return parse(message, player, ImmutableMap.<String, String>builder().build(), false);
    }

    public static String parse(String message) {
        return parse(message, null);
    }
}