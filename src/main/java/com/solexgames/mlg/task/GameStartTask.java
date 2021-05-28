package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Color;
import lombok.Getter;
import org.bukkit.ChatColor;
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
            this.arena.broadcastMessage(ChatColor.RED + "The game starting state has been cancelled as there aren't enough players to start the game!");
            this.cancel();
            return;
        }

        switch (finalSeconds) {
            case 20: case 15: case 10: case 5: case 4: case 3: case 2:
            case 1:
                this.arena.broadcastMessage(Color.SECONDARY + "The game will be starting in " + Color.PRIMARY + finalSeconds + Color.SECONDARY + " " + (finalSeconds == 1 ? "second" : "seconds") + "!");
                break;
            case 0:
                this.arena.broadcastMessage(Color.PRIMARY + "The game has started! " + ChatColor.GREEN + "Good luck and have fun!");
                this.arena.start();
                break;
            default:
                break;
        }

        ticks++;
    }
}
