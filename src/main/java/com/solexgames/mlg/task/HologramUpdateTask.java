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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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

			for (final String line : Locale.LEADERBOARD_HOLOGRAM.formatLines("{0}", "{1}", "{2}")) {
				if (line.contains("{1}")) {
					final AtomicInteger atomicInteger = new AtomicInteger(1);

					for (Map.Entry<String, Integer> entry : this.leaderboard.getLeaderboard().entrySet()) {
						if (entry != null) {
							holo.appendTextLine(Locale.LEADERBOARD_FORMAT.format(atomicInteger.getAndIncrement(), entry.getKey(), entry.getValue()));
						}
					}
				} else {
					holo.appendTextLine(line
							.replace("{0}", "Top " + this.leaderboard.getAmount() + " " + this.leaderboard.getName())
							.replace("{2}", TimeUtil.secondsToRoundedTime(this.time))
					);
				}
			}
		}

		final TextLine textLine = (TextLine) holo.getLine(holo.size() - 1);

		textLine.setText(CC.GRAY + "Cycling in " + CC.WHITE + TimeUtil.secondsToRoundedTime(this.time) + CC.GRAY + ".");

		this.time--;
	}
}
