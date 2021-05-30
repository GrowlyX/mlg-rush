package com.solexgames.mlg.task;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.leaderboard.Leaderboard;
import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.TimeUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 5/30/2021
 */

public class HologramUpdateTask extends BukkitRunnable {

	private Leaderboard leaderboard;
	private final int leaderboardsCount;

	private int time = 0;
	private int stage = 1;

	public HologramUpdateTask() {
		final List<Leaderboard> leaderboards = CorePlugin.getInstance().getLeaderboardHandler().getLeaderboards();

		this.leaderboard = leaderboards.get(0);
		this.leaderboardsCount = leaderboards.size();
	}

	@Override
	public void run() {
		final Hologram holo = CorePlugin.getInstance().getHologramHandler().getRotatingHologram();

		if (holo == null) {
			return;
		}

		if (this.time <= 0) {
			this.stage++;

			if (this.stage > this.leaderboardsCount) {
				this.stage = 1;
			}

			this.time = 10;
			this.leaderboard = CorePlugin.getInstance().getLeaderboardHandler().getLeaderboards().get(this.stage - 1);
			holo.clearLines();

			holo.appendTextLine(CC.B_PRIMARY + "MLG Rush");
			holo.appendTextLine(CC.B_PRIMARY + "Leaderboards");
			holo.appendTextLine("");
			holo.appendTextLine(CC.PRIMARY + "Top " + leaderboard.getAmount() + " " + leaderboard.getName());
			holo.appendTextLine("");
			int i = 1;
			for (Map.Entry<String, Integer> entry : leaderboard.getLeaderboard().entrySet()) {
				try {
					holo.appendTextLine(Locale.LEADERBOARD_FORMAT.format(i, entry.getKey(), entry.getValue()));
				} catch (NullPointerException ex) {
					holo.appendTextLine(CC.RED + "N/A");
				}
				i++;
			}
			holo.appendTextLine("");
		}

		((TextLine)holo.getLine(holo.size() - 1)).setText(CC.GRAY + "Cycling in " + TimeUtil.secondsToRoundedTime(this.time) + ".");

//		for (String s : Locale.LEADERBOARD_HOLOGRAM.formatLines("{0}", "{1}", "{2}")) {
//			if (s.contains("{1}")) {
//				int i = 1;
//				for (Map.Entry<String, Integer> entry : leaderboard.getLeaderboard().entrySet()) {
//					holo.appendTextLine(Locale.LEADERBOARD_FORMAT.format(i, entry.getKey(), entry.getValue()));
//					i++;
//				}
//			} else {
//				holo.appendTextLine(s
//						.replace("{0}", "Top " + leaderboard.getAmount() + " " + leaderboard.getName())
//						.replace("{2}", TimeUtil.secondsToRoundedTime(this.time)));
//			}
//		}

		this.time--;
	}
}
