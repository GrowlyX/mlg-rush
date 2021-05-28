package com.solexgames.mlg.scoreboard;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.CoreConstants;
import io.github.nosequel.scoreboard.element.ScoreboardElement;
import io.github.nosequel.scoreboard.element.ScoreboardElementHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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

        element.setTitle("&6&lMLG Rush");

        if (gamePlayer == null) {
            return element;
        }

        Arena arena;
        ScoreboardType boardType = ScoreboardType.LOBBY;

        if (this.isInArena(player)) {
            arena = this.getArena(player);

            switch (arena.getState()) {
                case AVAILABLE:
                    boardType = arena.getMaxPlayers() - arena.getAllPlayerList().size() == 0 ? ScoreboardType.GAME_STARTING : ScoreboardType.GAME_WAITING;
                    break;
                case IN_GAME:
                    boardType = ScoreboardType.IN_GAME;
                    break;
            }
        }



        return element;
    }

    private String placeholder(Arena arena, Player player, String input, ScoreboardType boardType) {
        boolean inGame = boardType.equals(ScoreboardType.IN_GAME);

        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());
        final ArenaPlayer arenaPlayer = arena != null ? arena.getByPlayer(player) : null;

        return input
                .replace("%player%", player.getName())
                .replace("%displayname%", player.getDisplayName())
                .replace("%wins%", gamePlayer.getWins() + "")
                .replace("%losses%", gamePlayer.getLosses() + "")
                .replace("%kills%", (inGame && arena != null ? arenaPlayer.getKills() : gamePlayer.getKills()) + "")
                .replace("%deaths%", (inGame && arena != null ? arenaPlayer.getDeaths() : gamePlayer.getDeaths()) + "")
                .replace("%points%", inGame && arena != null ? arenaPlayer.getPoints() + "" : "%points%")
                .replace("%kdr%", String.valueOf((gamePlayer.getKills() == 0 || gamePlayer.getDeaths() == 0) ? "0.0" : (gamePlayer.getKills() / gamePlayer.getDeaths())))
                .replace("%more%", boardType.equals(ScoreboardType.GAME_WAITING) ?
                        String.valueOf(arena.getMaxPlayers() - arena.getAllPlayerList().size()) : "%more%")
//                .replace("%lobby%", gamePlayer.getWins() + "")
//                .replace("%playing%", gamePlayer.getWins() + "") i have to pull
                ;
    }

    private boolean isInArena(Player player) {
        return CorePlugin.getInstance().getArenaHandler().isInArena(player);
    }

    private Arena getArena(Player player) {
        return CorePlugin.getInstance().getArenaHandler().getByPlayer(player);
    }
}
