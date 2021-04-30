package com.solexgames.mlg.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 3/4/2021
 */

public final class Color {

    public static ChatColor PRIMARY = ChatColor.AQUA;
    public static ChatColor SECONDARY = ChatColor.YELLOW;

    public static String SB_LINE = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-------------------------";

    public static String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> translate(List<String> text) {
        final List<String> newList = new ArrayList<>();

        for (String string : text) {
            newList.add(Color.translate(string));
        }

        return newList;
    }
}
