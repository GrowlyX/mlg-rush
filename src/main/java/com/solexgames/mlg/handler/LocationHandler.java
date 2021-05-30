package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.LocationUtil;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

@Setter
@NoArgsConstructor
public class LocationHandler {

    private Location spawnLocation;

    public void loadSpawn() {
        this.spawnLocation = LocationUtil.getLocationFromString(CorePlugin.getInstance().getConfigHandler().getConfig().getString("locations.spawn"))
                .orElse(null);
    }

    public void saveSpawn() {
        CorePlugin.getInstance().getConfigHandler().getConfig().getConfig().set("locations.spawn", LocationUtil.getStringFromLocation(this.spawnLocation).orElse(null));
        CorePlugin.getInstance().getConfigHandler().getConfig().save();
    }

    public Location getSpawnLocation() {
        if (this.spawnLocation == null) {
            return Bukkit.getWorld("mlg").getSpawnLocation();
        } else {
            return this.spawnLocation;
        }
    }
}
