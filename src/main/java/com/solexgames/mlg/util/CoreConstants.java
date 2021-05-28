package com.solexgames.mlg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class CoreConstants {

    public static String PLAYER_DATA_LOAD = ChatColor.RED + "An error occurred while trying to load your data.\n" + ChatColor.RED + "Please try again later or contact a staff member.";
    public static String DEFAULT_SCOREBOARD_TITLE = ChatColor.GOLD + ChatColor.BOLD.toString() + "MLG Rush";

    public static boolean NPC_ENABLED = true;

}
