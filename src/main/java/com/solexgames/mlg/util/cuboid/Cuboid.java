package com.solexgames.mlg.util.cuboid;

import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * https://www.spigotmc.org/threads/region-cuboid.329859/
 */

@Getter
public class Cuboid {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;

    private final double xMinCentered;
    private final double xMaxCentered;
    private final double yMinCentered;
    private final double yMaxCentered;
    private final double zMinCentered;
    private final double zMaxCentered;

    private final Location center;
    private final String world;

    public Cuboid(Location point1, Location point2) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());

        this.world = point1.getWorld().getName();
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;

        this.center = new Location(Bukkit.getWorld(this.world), (this.xMax - this.xMin) / 2 + this.xMin, (this.yMax - this.yMin) / 2 + this.yMin, (this.zMax - this.zMin) / 2 + this.zMin);
    }

    /**
     * Get a {@link Cuboid} from a json string
     * <p>
     *
     * @param json The Gson serialized string
     * @return A cuboid from {@link com.google.gson.Gson} or else null.
     */
    public static Cuboid getCuboidFromJson(String json) {
        return Optional.ofNullable(CorePlugin.GSON.fromJson(json, Cuboid.class)).orElse(null);
    }

    public double getDistance() {
        return this.getPoint1().distance(this.getPoint2());
    }

    public double getDistanceSquared() {
        return this.getPoint1().distanceSquared(this.getPoint2());
    }

    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }

    public Location getPoint1() {
        return new Location(Bukkit.getWorld(this.world), this.xMin, this.yMin, this.zMin);
    }

    public Location getPoint2() {
        return new Location(Bukkit.getWorld(this.world), this.xMax, this.yMax, this.zMax);
    }

    public Location getRandomLocation() {
        final Random random = ThreadLocalRandom.current();

        final int x = random.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = random.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = random.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;

        return new Location(Bukkit.getWorld(this.world), x, y, z);
    }

    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }

    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }

    public boolean isIn(Location loc) {
        return loc.getWorld() == Bukkit.getWorld(this.world) && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
                .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public boolean isIn(Player player) {
        return this.isIn(player.getLocation());
    }

    public boolean isInWithMarge(final Location loc, final double marge) {
        return loc.getWorld() == Bukkit.getWorld(this.world) && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
                .getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
    }

    public String getSerialized() {
        return CorePlugin.GSON.toJson(this);
    }
}
