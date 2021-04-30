package com.solexgames.mlg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

/**
 * @author GrowlyX
 * @since 3/4/2021
 */

@UtilityClass
public final class LocationUtil {

    private static final String SEPARATOR = "_S_";

    public static Optional<String> getStringFromLocation(Location location) {
        if (location != null) {
            return Optional.of(location.getWorld().getName() + SEPARATOR + location.getX() + SEPARATOR + location.getY() + SEPARATOR + location.getZ() + SEPARATOR + location.getYaw() + SEPARATOR + location.getPitch());
        }

        return Optional.empty();
    }

    public static Optional<Location> getLocationFromString(String string) {
        if (string != null && !string.trim().equals("")) {
            final String[] args = string.split(SEPARATOR);

            if (args.length == 6) {
                final World world = Bukkit.getServer().getWorld(args[0]);

                final double xCoord = Double.parseDouble(args[1]);
                final double yCoord = Double.parseDouble(args[2]);
                final double zCoord = Double.parseDouble(args[3]);
                final float yawFloat = Float.parseFloat(args[4]);
                final float pitchFloat = Float.parseFloat(args[5]);

                return Optional.of(new Location(world, xCoord, yCoord, zCoord, yawFloat, pitchFloat));
            }
        }
        return Optional.empty();
    }
}
