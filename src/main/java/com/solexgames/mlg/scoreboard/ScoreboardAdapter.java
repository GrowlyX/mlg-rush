package com.solexgames.mlg.scoreboard;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.cache.StatusCache;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.CoreConstants;
import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import javafx.util.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ScoreboardAdapter implements ScoreboardElementHandler {

    private final CorePlugin plugin = CorePlugin.getInstance();

    /**
     * Get the scoreboard element of a player
     *
     * @param player the player
     * @return the element
     */
    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();
        final GamePlayer gamePlayer = this.plugin.getPlayerHandler().getByName(player.getName());

        element.setTitle(CoreConstants.DEFAULT_SCOREBOARD_TITLE);

        if (gamePlayer == null) {
            return element;
        }

        ScoreboardType boardType = ScoreboardType.LOBBY;

        if (this.plugin.getArenaHandler().isInArena(player)) {
            final Arena arena = this.plugin.getArenaHandler().getByPlayer(player);

            switch (arena.getState()) {
                case AVAILABLE:
                    boardType = arena.getMaxPlayers() - arena.getAllPlayerList().size() == 0 ? ScoreboardType.GAME_STARTING : ScoreboardType.GAME_WAITING;
                    break;
                case IN_GAME:
                    boardType = ScoreboardType.IN_GAME;
                    break;
            }
        } else if (this.plugin.getArenaHandler().isSpectating(player)) {
            boardType = ScoreboardType.SPECTATING;
        }

        final Map<String, Pair<String, List<String>>> scoreboardMap = CorePlugin.getInstance().getConfigHandler().getScoreboardMap();

        element.setTitle(scoreboardMap.get(boardType.toString()).getKey());

        for (final String line : scoreboardMap.get(boardType.toString()).getValue()) {
            element.add(this.placeholder(player, line, boardType));
        }

        return element;
    }

    private String placeholder(Player player, String input, ScoreboardType boardType) {
        final boolean inGame = boardType.equals(ScoreboardType.IN_GAME);

        final Arena arena = this.plugin.getArenaHandler().getByPlayer(player);
        final Arena spectatingArena = this.plugin.getArenaHandler().getSpectating(player);

        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());
        final ArenaPlayer arenaPlayer = arena != null ? arena.getByPlayer(player) : null;

        return input.replace("%player%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%fancy%", arenaPlayer != null ? this.getFancyPoints(arenaPlayer) : "%fancy%")
                .replace("%player1%", spectatingArena != null ? spectatingArena.getGamePlayerList().get(0).getPlayer().getDisplayName() : "%player1%")
                .replace("%player2%", spectatingArena != null ? spectatingArena.getGamePlayerList().get(1).getPlayer().getDisplayName() : "%player2%")
                .replace("%arena%", spectatingArena != null ? spectatingArena.getName() : "%arena%")
                .replace("%wins%", gamePlayer.getWins() + "")
                .replace("%losses%", gamePlayer.getLosses() + "")
                .replace("%kills%", (inGame && arena != null ? arenaPlayer.getKills() : gamePlayer.getKills()) + "")
                .replace("%deaths%", (inGame && arena != null ? arenaPlayer.getDeaths() : gamePlayer.getDeaths()) + "")
                .replace("%points%", inGame && arena != null ? arenaPlayer.getPoints() + "" : "%points%")
                .replace("%kdr%", String.valueOf((gamePlayer.getKills() == 0 || gamePlayer.getDeaths() == 0) ? "0.0" : Math.abs(gamePlayer.getKills() / gamePlayer.getDeaths())))
                .replace("%more%", boardType.equals(ScoreboardType.GAME_WAITING) && arena != null ? arena.getMaxPlayers() - arena.getAllPlayerList().size() + "" : "%more%")
                .replace("%lobby%", StatusCache.LOBBY + "")
                .replace("%playing%", StatusCache.PLAYING + "");
    }

    private String getFancyPoints(ArenaPlayer arenaPlayer) {
        final StringJoiner joiner = new StringJoiner("");

        for (int i = 0; i < arenaPlayer.getPoints(); i++) {
            joiner.add((arenaPlayer.getArenaTeam().equals(ArenaTeam.BLUE) ? ChatColor.BLUE : ChatColor.RED) + "⬤");
        }

        for (int i = 0; i < Arena.WINNER_POINT_AMOUNT - arenaPlayer.getPoints(); i++) {
            joiner.add(ChatColor.GRAY + "⬤"); //■
        }

        return joiner.toString();
    }
}
