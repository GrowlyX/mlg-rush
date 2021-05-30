package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class GameEndTask extends BukkitRunnable {

    public static final long FIFTEEN_MINUTE = TimeUnit.MINUTES.toMillis(15L);

    @Override
    public void run() {
        final List<Arena> arenas = CorePlugin.getInstance().getArenaHandler().getAllArenas();
        final List<Arena> finalArenas = new ArrayList<>(arenas);

        finalArenas.stream()
                .filter(arena -> arena.getArenaState().equals(ArenaState.IN_GAME) && arena.getStart() + GameEndTask.FIFTEEN_MINUTE <= System.currentTimeMillis())
                .forEach(arena -> {
                    arena.broadcastMessage(Locale.FORCE_ENDED.format());

                    Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> arena.end(arena.getGamePlayerList().get(0)));
                });
    }
}
