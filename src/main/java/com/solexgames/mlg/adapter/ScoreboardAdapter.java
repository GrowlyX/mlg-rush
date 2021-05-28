package com.solexgames.mlg.adapter;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.model.Arena;
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

        element.setTitle(Color.PRIMARY + ChatColor.BOLD.toString() + "MLG Rush");

        if (gamePlayer == null) {
            return element;
        }

        element.add(Color.SB_LINE);

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);

            switch (arena.getState()) {
                case AVAILABLE:
                    if ((arena.getMaxPlayers() - arena.getAllPlayerList().size() == 0)) {
                        element.add(Color.SECONDARY + "Starting match...");
                    } else {
                        element.add(Color.SECONDARY + "Waiting for players...");
                        element.add(Color.PRIMARY + "" + (arena.getMaxPlayers() - arena.getAllPlayerList().size()) + Color.SECONDARY + " players.");
                    }
                    break;
                case IN_GAME:
                    element.add(Color.SECONDARY + "Points: " + Color.PRIMARY + arena.getByPlayer(player).getPoints());
                    element.add(Color.SECONDARY + "Kills: " + ChatColor.GREEN + arena.getByPlayer(player).getKills());
                    element.add(Color.SECONDARY + "Deaths: " + ChatColor.GOLD + arena.getByPlayer(player).getDeaths());
                    break;
            }
        } else {
            element.add(Color.SECONDARY + "Wins: " + Color.PRIMARY + gamePlayer.getWins());
            element.add(Color.SECONDARY + "Losses: " + Color.PRIMARY + gamePlayer.getLosses());
            element.add("  ");
            element.add(Color.SECONDARY + "Kills: " + ChatColor.GREEN + gamePlayer.getKills());
            element.add(Color.SECONDARY + "Deaths: " + ChatColor.GOLD + gamePlayer.getDeaths());
            element.add(Color.SECONDARY + "KD/R: " + ChatColor.LIGHT_PURPLE + ((gamePlayer.getKills() == 0 || gamePlayer.getDeaths() == 0) ? "0.0" : (gamePlayer.getKills() / gamePlayer.getDeaths())));
        }

        element.add("  ");
        element.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + CoreConstants.SERVER_IP);
        element.add(Color.SB_LINE);

        return element;
    }

    private boolean isInArena(Player player) {
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

        return arenaHandler.isInArena(player);
    }

    private Arena getArena(Player player) {
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

        return arenaHandler.getByPlayer(player);
    }
}
