package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.leaderboard.Leaderboard;
import com.solexgames.mlg.task.HologramUpdateTask;
import com.solexgames.mlg.util.Locale;
import org.bukkit.command.CommandSender;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author puugz
 * @since 30/05/2021 20:30
 */
@CommandAlias("leaderboard|top")
public class LeaderboardCommand extends BaseCommand {

	@Default
	@CommandCompletion("@leaderboards")
	public void onDefault(CommandSender sender, @Name("type") @Optional Leaderboard leaderboard) {
		this.sendMessage(sender, leaderboard == null ? CorePlugin.getInstance().getLeaderboardHandler().getLeaderboards().get(0) : leaderboard);
	}

	@Subcommand("update")
	@CommandPermission("mlgrush.admin")
	public void onUpdate(CommandSender sender) {
		CorePlugin.getInstance().getLeaderboardHandler().updateLeaderboards();
		sender.sendMessage(Locale.LEADERBOARD_UPDATED.format());
	}

	public void sendMessage(CommandSender sender, Leaderboard leaderboard) {
		for (String line : Locale.LEADERBOARD_MESSAGE.formatLines(leaderboard.getAmount(), leaderboard.getName(), "{2}")) {
			if (line.contains("{2}")) {
				final AtomicInteger atomicInteger = new AtomicInteger(1);

				leaderboard.getLeaderboard().entrySet().stream()
						.filter(Objects::nonNull)
						.sorted(HologramUpdateTask.ENTRY_COMPARATOR)
						.forEachOrdered(stringIntegerEntry -> sender.sendMessage(Locale.LEADERBOARD_FORMAT.format(atomicInteger.getAndIncrement(), stringIntegerEntry.getKey(), stringIntegerEntry.getValue())));
			} else {
				sender.sendMessage(line);
			}
		}
	}
}
