package com.solexgames.mlg.adapter;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
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

        element.add(Color.SB_LINE);
        element.add(Color.SECONDARY + "Wins: " + Color.PRIMARY + gamePlayer.getWins());
        element.add(Color.SECONDARY + "Losses: " + Color.PRIMARY + gamePlayer.getLosses());
        element.add("  ");
        element.add(Color.SECONDARY + "Kills: " + ChatColor.GREEN + gamePlayer.getKills());
        element.add(Color.SECONDARY + "Deaths: " + ChatColor.GOLD + gamePlayer.getDeaths());
        element.add(Color.SECONDARY + "KD/R: " + ChatColor.LIGHT_PURPLE + gamePlayer.getKdr());
        element.add("  ");
        element.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "www.mlgrush.com");
        element.add(Color.SB_LINE);

        return element;
    }
}
