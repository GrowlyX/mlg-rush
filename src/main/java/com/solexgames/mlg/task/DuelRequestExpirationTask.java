package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelRequestExpirationTask extends BukkitRunnable {

    private final DuelRequest duelRequest;

    public DuelRequestExpirationTask(DuelRequest duelRequest) {
        this.duelRequest = duelRequest;

        this.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, 20L);
    }

    @Override
    public void run() {
        final boolean isActive = CorePlugin.getInstance().getArenaHandler().getDuelRequests().contains(this.duelRequest);
        final boolean isExpired = (System.currentTimeMillis() - this.duelRequest.getIssuingTime()) > DuelRequest.EXPIRATION_MILLI;

        if (isExpired && isActive) {
            final Player issuer = Bukkit.getPlayer(this.duelRequest.getIssuer());
            final Player target = Bukkit.getPlayer(this.duelRequest.getTarget());

            if (issuer != null) {
                issuer.sendMessage(Color.SECONDARY + "You're duel request to " + this.duelRequest.getTargetDisplay() + Color.SECONDARY + " has expired.");
            }

            if (target != null) {
                target.sendMessage(Color.SECONDARY + "The duel request from " + this.duelRequest.getIssuerDisplay() + Color.SECONDARY + " has expired.");
            }

            CorePlugin.getInstance().getArenaHandler().getDuelRequests().remove(this.duelRequest);

            this.cancel();
        } else if (!isActive) {
            this.cancel();
        }
    }
}
