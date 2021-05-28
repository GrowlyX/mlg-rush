package com.solexgames.mlg.cache;

import com.solexgames.mlg.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class StatusCache extends BukkitRunnable {

    public static int PLAYING = 0;
    public static int LOBBY = 0;

    @Override
    public void run() {
        StatusCache.PLAYING = CorePlugin.getInstance().getArenaHandler().getArenaWeakHashMap().size();
        StatusCache.LOBBY = Bukkit.getOnlinePlayers().size() - StatusCache.PLAYING;
    }
}
