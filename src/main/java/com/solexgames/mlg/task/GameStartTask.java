package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.TimeUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
public class GameStartTask extends BukkitRunnable {

    private int ticks;

    private final int seconds;
    private final Arena arena;

    public GameStartTask(int seconds, Arena arena) {
        this.seconds = seconds;
        this.arena = arena;

        this.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        final int finalSeconds = this.seconds - this.ticks;

        if (this.arena.getGamePlayerList().size() < this.arena.getMaxPlayers()) {
            this.arena.broadcastMessage(Locale.NOT_ENOUGH_PLAYERS.format());
            this.cancel();
            return;
        }

        switch (finalSeconds) {
            case 20: case 15: case 10: case 5: case 4: case 3: case 2:
            case 1:
                this.arena.broadcastMessage(Locale.GAME_COUNTDOWN.format(TimeUtil.secondsToRoundedTime(finalSeconds)), Sound.NOTE_STICKS);
                break;
            case 0:
                this.arena.broadcastMessage(Locale.GAME_COUNTDOWN_END.format(), Sound.NOTE_PLING, 2f);
                this.arena.start();
                break;
            default:
                break;
        }

        ticks++;
    }
}
