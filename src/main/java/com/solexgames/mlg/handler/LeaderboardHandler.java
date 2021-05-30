package com.solexgames.mlg.handler;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.solexgames.mlg.CorePlugin;
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

        this.leaderboards.add(new Leaderboard("Kills", "kills", 10));
        this.leaderboards.add(new Leaderboard("Deaths", "deaths", 10));
        this.leaderboards.add(new Leaderboard("Wins", "totalWins", 10));
        this.leaderboards.add(new Leaderboard("Losses", "totalLosses", 10));
    }

    public void updateLeaderboards() {
        this.leaderboards.forEach(Leaderboard::update);
    }

    public Leaderboard getByName(String name) {
        return this.leaderboards.stream()
                .filter(leaderboard -> leaderboard.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
