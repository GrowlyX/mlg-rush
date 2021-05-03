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
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@NoArgsConstructor
public class ArenaHandler {

    private final List<Arena> allArenas = new ArrayList<>();

    /**
     * Loads all arenas from the config.yml
     */
    public void loadArenas() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("arenas");

        try {
            configurationSection.getKeys(false).stream().filter(s -> !s.equalsIgnoreCase("test")).forEach(path -> {
                final Arena arena = new Arena(UUID.randomUUID(), configurationSection.getString(path + ".name"));
                final Cuboid cuboid = Cuboid.getCuboidFromJson(configurationSection.getString(path + ".cuboid"));

                arena.setCuboid(cuboid);
                arena.setTeamSize(configurationSection.getInt(path + ".team-size"));
                arena.setMaxPlayers(configurationSection.getInt(path + ".max-players"));

                arena.setSpawnOne(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-one")).orElse(null));
                arena.setSpawnTwo(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-two")).orElse(null));

                CorePlugin.getInstance().getLogger().info("[Arena] Loaded arena " + arena.getName() + "!");
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Adds a player to a specific arena
     * <p></p>
     *
     * @param player Player to add to the arena
     * @param arena  Arena to add the player to
     */
    public void addToGame(Player player, Arena arena) {
        if (arena.getState().equals(ArenaState.IN_GAME) || arena.getState().equals(ArenaState.REGENERATING)) {
            player.sendMessage(ChatColor.RED + "You cannot join this arena at the moment.");
            return;
        }

        if (this.isInArena(player)) {
            player.sendMessage(ChatColor.RED + "You're already in an arena!");
            return;
        }

        if (arena.getAllPlayerList().contains(player)) {
            player.sendMessage(ChatColor.RED + "You're already in an arena!");
            return;
        }

        if (arena.getTeamSize() == 1) {
            if (arena.getGamePlayerList().size() == 2) {
                player.sendMessage(ChatColor.RED + "This arena is currently at max capacity!");
                return;
            }

            arena.getAllPlayerList().add(player);
            arena.getGamePlayerList().add(new ArenaPlayer(arena, (arena.getGamePlayerList().size() == 0 ? ArenaTeam.BLUE : (arena.getGamePlayerList().get(0).getArenaTeam() == ArenaTeam.BLUE ? ArenaTeam.RED : ArenaTeam.BLUE)), player));
            arena.broadcastMessage(Color.PRIMARY + player.getName() + Color.SECONDARY + " has joined the arena. " + ChatColor.GRAY + "(" + arena.getGamePlayerList().size() + "/" + arena.getMaxPlayers() + ")");

            player.teleport(arena.getSpawnFromTeam(arena.getByPlayer(player).getArenaTeam()));

            CorePlugin.getInstance().getHotbarHandler().setupArenaWaitingHotbar(player);

            if (arena.getGamePlayerList().size() >= arena.getMaxPlayers()) {
                new GameStartTask(Arena.LONG_START ? 20 : 5, arena);
            }
        } else {
            player.sendMessage(ChatColor.RED + "MLG Rush Teams mode is currently in development!");
        }
    }

    /**
     * Removes a player from an arena and sends them to spawn
     * <p></p>
     *
     * @param player Player to remove from the arena
     * @param arena  Arena to remove the player from
     */
    public void leaveGame(Player player, Arena arena) {
        if (arena == null) {
            player.sendMessage(ChatColor.RED + "You aren't currently in an arena.");
            return;
        }

        if (arena.getState().equals(ArenaState.AVAILABLE)) {
            arena.getAllPlayerList().remove(player);
            arena.getGamePlayerList().remove(arena.getByPlayer(player));
            arena.broadcastMessage(Color.PRIMARY + player.getName() + Color.SECONDARY + " has left the arena. " + ChatColor.GRAY + "(" + arena.getGamePlayerList().size() + "/" + arena.getMaxPlayers() + ")");

            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());

            CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
        } else {
            arena.end(arena.getOpponentPlayer(player));
        }
    }

    /**
     * Checks if a player is in an arena or not
     * <p></p>
     *
     * @param player Player to check the status of
     * @return if the player is in the arena or not
     */
    public boolean isInArena(Player player) {
        return this.getByPlayer(player) != null;
    }

    public Arena getByPlayer(Player player) {
        return this.allArenas.stream()
                .filter(kit -> kit.getAllPlayerList().contains(player))
                .findFirst().orElse(null);
    }
}
