package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.TimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

@Getter
public class RoundStartTask extends BukkitRunnable {

    private int ticks;
    private final int seconds;

    private final Arena arena;

    public RoundStartTask(int seconds, Arena arena) {
        this.seconds = seconds;

        this.arena = arena;
        this.arena.getAllPlayerList().forEach(uuid -> Bukkit.getPlayer(uuid).setMetadata("frozen", new FixedMetadataValue(CorePlugin.getInstance(), true)));

        this.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        final int finalSeconds = this.seconds - this.ticks;

        switch (finalSeconds) {
            case 5: case 4: case 3: case 2:
            case 1:
                this.arena.broadcastMessage(Locale.ROUND_COUNTDOWN_END.format(TimeUtil.secondsToRoundedTime(finalSeconds)));
                break;
            case 0:
                this.arena.getAllPlayerList().forEach(uuid -> {
                    final Player player = Bukkit.getPlayer(uuid);

                    player.removeMetadata("frozen", CorePlugin.getInstance());
                    CorePlugin.getInstance().getHotbarHandler().setupArenaInGameHotbar(player);
                });
                this.arena.broadcastMessage(Locale.ROUND_COUNTDOWN_END.format());

                this.cancel();
                break;
            default:
                break;
        }

        this.ticks++;
    }
}
