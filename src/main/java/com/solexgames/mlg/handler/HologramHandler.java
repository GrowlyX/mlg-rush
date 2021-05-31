package com.solexgames.mlg.handler;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.task.HologramUpdateTask;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

/**
 * @author GrowlyX
 * @since 5/30/2021
 */

@Getter
@NoArgsConstructor
public class HologramHandler {

    @Setter
    private Hologram rotatingHologram;
    private HologramUpdateTask updateTask;

    public void setupHologram() {
        final Location location = CorePlugin.getInstance().getLocationHandler().getHologramLocation();

        if (location != null && location.getWorld() != null) {
            this.rotatingHologram = HologramsAPI.createHologram(CorePlugin.getInstance(), location);
        }

        this.updateTask = new HologramUpdateTask();
        this.updateTask.runTaskTimer(CorePlugin.getInstance(), 20L, 20L);
    }
}
