package com.solexgames.mlg.listener;

import com.solexgames.mlg.CoreConstants;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import io.github.nosequel.scoreboard.ScoreboardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPreLoginHigh(AsyncPlayerPreLoginEvent event) {
        CorePlugin.getInstance().getPlayerHandler().setupPlayer(event.getUniqueId(), event.getName());

        event.allow();
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onAsyncPreLoginLow(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(event.getUniqueId());

            if (gamePlayer != null) {
                event.allow();
            } else {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CoreConstants.PLAYER_DATA_LOAD);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDisconnect(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());

        if (gamePlayer != null) {
            gamePlayer.savePlayerData();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onConnect(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Location spawn = Bukkit.getWorld("lobby-world").getSpawnLocation();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(event.getPlayer().getUniqueId());

        if (gamePlayer != null) {
            if (spawn != null) {
                player.teleport(spawn);
            }

            new ScoreboardAdapter();
        } else {
            event.getPlayer().kickPlayer(CoreConstants.PLAYER_DATA_LOAD);
        }
    }
}
