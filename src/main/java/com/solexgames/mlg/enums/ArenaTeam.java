package com.solexgames.mlg.enums;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@RequiredArgsConstructor
public enum ArenaTeam {

    RED(ChatColor.RED + "Red"),
    BLUE(ChatColor.BLUE + "Blue");

    private final String coloredName;

}
