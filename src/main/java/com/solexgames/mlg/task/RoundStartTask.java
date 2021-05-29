package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Color;
import lombok.Getter;
import org.bukkit.ChatColor;
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
        this.arena.getAllPlayerList().forEach(player -> player.setMetadata("frozen", new FixedMetadataValue(CorePlugin.getInstance(), true)));

        this.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        final int finalSeconds = this.seconds - this.ticks;

        switch (finalSeconds) {
            case 5: case 4: case 3: case 2:
            case 1:
                this.arena.broadcastMessage(Color.SECONDARY + "The round will be starting in " + Color.PRIMARY + finalSeconds + Color.SECONDARY + " " + (finalSeconds == 1 ? "second" : "seconds") + "!");
                break;
            case 0:
                this.arena.getAllPlayerList().forEach(player -> {
                    player.removeMetadata("frozen", CorePlugin.getInstance());
                    CorePlugin.getInstance().getHotbarHandler().setupArenaInGameHotbar(player);
                });
                this.arena.broadcastMessage(Color.PRIMARY + "The round has started! " + ChatColor.GREEN + "Good luck and have fun!");

                this.cancel();
                break;
            default:
                break;
        }

        ticks++;
    }
}
