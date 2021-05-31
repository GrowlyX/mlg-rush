package com.solexgames.mlg.handler;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * @author GrowlyX
 * @since 5/31/2021
 */

public class BuilderHandler {

    private final Set<Player> builders = new HashSet<>();

    public void addBuilder(Player player) {
        this.builders.add(player);
    }

    public void removeBuilder(Player player) {
        this.builders.remove(player);
    }

    public boolean isBuilding(Player player) {
        return this.builders.contains(player);
    }
}
