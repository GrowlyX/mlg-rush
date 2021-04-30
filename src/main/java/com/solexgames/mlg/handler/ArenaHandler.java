package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ArenaHandler {

    private final List<Arena> allArenas = new ArrayList<>();

    public ArenaHandler() {
        this.loadArenas();
    }

    private void loadArenas() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("arenas");

        if (configurationSection.getKeys(true) == null) {
            return;
        }

        try {
            configurationSection.getKeys(true).forEach(path -> {
                final Arena arena = new Arena(UUID.fromString(configurationSection.getString(path + ".uuid")), configurationSection.getString(path + ".name"));
                final Cuboid cuboid = Cuboid.getCuboidFromJson(configurationSection.getString(path + ".cuboid"));

                arena.setCuboid(cuboid);
                arena.setTeamSize(configurationSection.getInt(path + ".team-size"));
                arena.setMaxPlayers(configurationSection.getInt(path + ".max-players"));

                CorePlugin.getInstance().getLogger().info("[Arena] Loaded arena " + arena.getName() + "!");
            });
        } catch (Exception ignored) {
            CorePlugin.getInstance().getLogger().info("There aren't any arenas.");
        }
    }
    /**
     * Filters through all available kits and finds a kit with the same name as {@param name}
     *
     * @param name Name parameter
     * @return A kit with the name {@param name}
     */
    public Arena getByName(String name) {
        return this.allArenas.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters through all available kits and finds a kit with the same {@link UUID} as {@param uuid}
     *
     * @param uuid UUID parameter
     * @return A kit with the uuid {@param uuid}
     */
    public Arena getByUuid(UUID uuid) {
        return this.allArenas.stream()
                .filter(kit -> kit.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}
