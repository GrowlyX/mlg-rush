package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Optional;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Locale;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since 01/07/2021 21:47
 */

@CommandAlias("stats|statistics")
public class StatsCommand extends BaseCommand {

	@Default
	public void execute(Player player, @Name("player") @Optional GamePlayer target) {
		if (target != null) {
			this.sendStatsMessage(player, target);
		} else {
			final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());

			this.sendStatsMessage(player, gamePlayer);
		}
	}

	private void sendStatsMessage(Player player, GamePlayer target) {
		for (String line : Locale.STATS.formatLines(target.getName(), target.getKills(), target.getDeaths(), target.getWins(), target.getLosses(), target.getGamesPlayed())) {
			player.sendMessage(line);
		}
	}
}
