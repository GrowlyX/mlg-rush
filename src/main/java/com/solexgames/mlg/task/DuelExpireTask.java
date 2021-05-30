package com.solexgames.mlg.task;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class DuelExpireTask extends BukkitRunnable {

    @Override
    public void run() {
        final List<DuelRequest> requests = CorePlugin.getInstance().getArenaHandler().getDuelRequests();
        final List<DuelRequest> finalRequests = new ArrayList<>(requests);

        finalRequests.stream()
                .filter(duelRequest -> requests.contains(duelRequest) && (System.currentTimeMillis() - duelRequest.getIssuingTime()) > DuelRequest.EXPIRATION_MILLI)
                .forEach(duelRequest -> {
                    final Player issuer = Bukkit.getPlayer(duelRequest.getIssuer());
                    final Player target = Bukkit.getPlayer(duelRequest.getTarget());

                    if (issuer != null) {
                        issuer.sendMessage(Locale.REQUEST_TO_EXPIRED.format(duelRequest.getTargetDisplay()));
                    }

                    if (target != null) {
                        target.sendMessage(Locale.REQUEST_FROM_EXPIRED.format(duelRequest.getIssuerDisplay()));
                    }

                    CorePlugin.getInstance().getArenaHandler().getDuelRequests().remove(duelRequest);
                });
    }
}
