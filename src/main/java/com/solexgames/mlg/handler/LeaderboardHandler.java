package com.solexgames.mlg.handler;

import com.solexgames.mlg.leaderboard.Leaderboard;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author puugz
 * @since 30/05/2021 20:24
 */
@Getter
public class LeaderboardHandler {

	private final List<Leaderboard> leaderboards;

	public LeaderboardHandler() {
		this.leaderboards = new ArrayList<>();
		this.leaderboards.add(new Leaderboard("Kills", "kills", 5));
		this.leaderboards.add(new Leaderboard("Deaths", "deaths", 5));
	}

	public void updateLeaderboards() {
		this.leaderboards.forEach(Leaderboard::update);
	}
}
