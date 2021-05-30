package com.solexgames.mlg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 3/4/2021
 */

@UtilityClass
public class Color {

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
