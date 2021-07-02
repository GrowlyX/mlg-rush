package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/29/2021
 */

@CommandAlias("statsreset|resetstats")
@CommandPermission("mlgrush.command.statsreset")
public class StatsResetCommand extends BaseCommand {

    @Default
    public void execute(CommandSender sender, @Name("player") GamePlayer player) {
        player.setKills(0);
        player.setDeaths(0);
        player.setWins(0);
        player.setLosses(0);
        player.setGamesPlayed(0);

        final Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());

        sender.sendMessage(Locale.STATS_RESET.format(bukkitPlayer != null ? bukkitPlayer.getDisplayName() : player.getName()));
    }
}
