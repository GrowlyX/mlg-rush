package com.solexgames.mlg.handler;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.task.HologramUpdateTask;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author GrowlyX
 * @since 5/30/2021
 */

@Getter
@NoArgsConstructor
public class HologramHandler {

    private Hologram rotatingHologram;
    private HologramUpdateTask updateTask;

    public void setupHologram() {
        this.rotatingHologram = HologramsAPI.getHolograms(CorePlugin.getInstance()).stream()
                .findFirst().orElse(null);

        this.updateTask = new HologramUpdateTask();
        this.updateTask.runTaskTimerAsynchronously(CorePlugin.getInstance(), 20L, TimeUnit.SECONDS.toMillis(10L));
    }
}
