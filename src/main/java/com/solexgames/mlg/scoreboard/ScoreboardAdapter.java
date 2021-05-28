package com.solexgames.mlg.scoreboard;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.cache.StatusCache;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.CoreConstants;
import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import javafx.util.Pair;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class ScoreboardAdapter implements ScoreboardElementHandler {

    /**
     * Get the scoreboard element of a player
     *
     * @param player the player
     * @return the element
     */
    @Override
    public ScoreboardElement getElement(Player player) {
        final ScoreboardElement element = new ScoreboardElement();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());

        element.setTitle(CoreConstants.DEFAULT_SCOREBOARD_TITLE);

        if (gamePlayer == null) {
            return element;
        }

        ScoreboardType boardType = ScoreboardType.LOBBY;

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);

            switch (arena.getState()) {
                case AVAILABLE:
                    boardType = arena.getMaxPlayers() - arena.getAllPlayerList().size() == 0 ? ScoreboardType.GAME_STARTING : ScoreboardType.GAME_WAITING;
                    break;
                case IN_GAME:
                    boardType = ScoreboardType.IN_GAME;
                    break;
            }
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
        final Arena arena = this.getArena(player);

        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());
        final ArenaPlayer arenaPlayer = arena != null ? arena.getByPlayer(player) : null;

        return input.replace("%player%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%wins%", gamePlayer.getWins() + "")
                .replace("%losses%", gamePlayer.getLosses() + "")
                .replace("%kills%", (inGame && arena != null ? arenaPlayer.getKills() : gamePlayer.getKills()) + "")
                .replace("%deaths%", (inGame && arena != null ? arenaPlayer.getDeaths() : gamePlayer.getDeaths()) + "")
                .replace("%points%", inGame && arena != null ? arenaPlayer.getPoints() + "" : "%points%")
                .replace("%kdr%", String.valueOf((gamePlayer.getKills() == 0 || gamePlayer.getDeaths() == 0) ? "0.0" : (gamePlayer.getKills() / gamePlayer.getDeaths())))
                .replace("%more%", boardType.equals(ScoreboardType.GAME_WAITING) && arena != null ? arena.getMaxPlayers() - arena.getAllPlayerList().size() + "" : "%more%")
                .replace("%lobby%", StatusCache.LOBBY + "")
                .replace("%playing%", StatusCache.PLAYING + "");
    }

    private boolean isInArena(Player player) {
        return CorePlugin.getInstance().getArenaHandler().isInArena(player);
    }

    private Arena getArena(Player player) {
        return CorePlugin.getInstance().getArenaHandler().getByPlayer(player);
    }
}