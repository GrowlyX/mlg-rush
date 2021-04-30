package com.solexgames.mlg.enums;

import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public enum ArenaTeam {

    RED(ChatColor.RED + "Red"),
    BLUE(ChatColor.BLUE + "Blue");

    private final String coloredName;

}
