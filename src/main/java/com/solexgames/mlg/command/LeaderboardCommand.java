package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @author puugz
 * @since 30/05/2021 20:30
 */
@CommandAlias("leaderboard|top")
public class LeaderboardCommand extends BaseCommand {

	@Default
	public void onDefault(CommandSender sender) {
		sender.sendMessage(CC.GRAY + CC.STRIKE_THROUGH + StringUtils.repeat("-", 20));
		sender.sendMessage(CC.B_PRIMARY + "Top Kills Leaderboard:");

		int i = 1;
		for (Map.Entry<String, Integer> entry : CorePlugin.getInstance().getLeaderboardHandler().getLeaderboards().get(0).getLeaderboard().entrySet()) {
			sender.sendMessage(CC.GRAY + "(" + i + ") " + CC.SECONDARY + entry.getKey() + ": " + CC.PRIMARY + entry.getValue());
			i++;
		}

		sender.sendMessage(CC.GRAY + CC.STRIKE_THROUGH + StringUtils.repeat("-", 20));
	}

	@Subcommand("update")
	@CommandPermission("mlgrush.admin")
	public void onUpdate(CommandSender sender) {
		CorePlugin.getInstance().getLeaderboardHandler().updateLeaderboards();
		sender.sendMessage(CC.GREEN + "Updated all leaderboards.");
	}
}
