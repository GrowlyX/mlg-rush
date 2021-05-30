package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.CC;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author puugz
 * @since 30/05/2021 20:26
 */
public class LeaderboardUpdateTask extends BukkitRunnable {

	@Override
	public void run() {
		CorePlugin.getInstance().getLeaderboardHandler().updateLeaderboards();
	}
}
