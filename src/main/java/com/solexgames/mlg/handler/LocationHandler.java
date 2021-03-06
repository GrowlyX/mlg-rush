package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.LocationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

@Getter
@Setter
@NoArgsConstructor
public class LocationHandler {

    private Location spawnLocation;
    private Location hologramLocation;

    public void loadSpawn() {
        this.spawnLocation = LocationUtil.getLocationFromString(CorePlugin.getInstance().getConfigHandler().getConfig().getString("locations.spawn")).orElse(null);
        this.hologramLocation = LocationUtil.getLocationFromString(CorePlugin.getInstance().getConfigHandler().getConfig().getString("locations.holo")).orElse(null);
    }

    public void saveSpawn() {
        CorePlugin.getInstance().getConfigHandler().getConfig().getConfig().set("locations.spawn", LocationUtil.getStringFromLocation(this.spawnLocation).orElse(null));
        CorePlugin.getInstance().getConfigHandler().getConfig().save();
    }

    public void saveHolo() {
        CorePlugin.getInstance().getConfigHandler().getConfig().getConfig().set("locations.holo", LocationUtil.getStringFromLocation(this.hologramLocation).orElse(null));
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
