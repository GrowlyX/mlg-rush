package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.task.GameStartTask;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.LocationUtil;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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

        try {
            configurationSection.getKeys(true).forEach(path -> {
                final Arena arena = new Arena(UUID.fromString(configurationSection.getString(path + ".uuid")), configurationSection.getString(path + ".name"));
                final Cuboid cuboid = Cuboid.getCuboidFromJson(configurationSection.getString(path + ".cuboid"));

                arena.setCuboid(cuboid);
                arena.setTeamSize(configurationSection.getInt(path + ".team-size"));
                arena.setMaxPlayers(configurationSection.getInt(path + ".max-players"));

                arena.setSpawnOne(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-one")).orElse(null));
                arena.setSpawnTwo(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-two")).orElse(null));

                CorePlugin.getInstance().getLogger().info("[Arena] Loaded arena " + arena.getName() + "!");
            });
        } catch (Exception ignored) {
            CorePlugin.getInstance().getLogger().info("There aren't any arenas.");
        }
    }

    public void addToGame(Player player, Arena arena) {
        if (arena.getState().equals(ArenaState.IN_GAME) || arena.getState().equals(ArenaState.REGENERATING)) {
            player.sendMessage(ChatColor.RED + "You cannot join this arena at the moment.");
            return;
        }

        if (arena.getTeamSize() == 1) {
            if (arena.getGamePlayerList().size() == 2) {
                player.sendMessage(ChatColor.RED + "This arena is currently at max capacity!");
                return;
            }

            arena.getGamePlayerList().add(new ArenaPlayer(arena, (arena.getGamePlayerList().size() == 0 ? ArenaTeam.BLUE : (arena.getGamePlayerList().get(0).getArenaTeam() == ArenaTeam.BLUE ? ArenaTeam.RED : ArenaTeam.BLUE)), player));
            arena.broadcastMessage(Color.PRIMARY + player.getName() + Color.SECONDARY + " has joined the arena. " + ChatColor.GRAY + "(" + arena.getGamePlayerList().size() + "/" + arena.getMaxPlayers() + ")");

            player.teleport((arena.getByArenaPlayer(player) == ArenaTeam.BLUE) ? arena.getSpawnOne() : arena.getSpawnTwo());

            if (arena.getGamePlayerList().size() >= arena.getMaxPlayers()) {
                new GameStartTask(20, arena);
            }
        } else {
            player.sendMessage(ChatColor.RED + "MLG Rush Teams mode is currently in development!");
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
