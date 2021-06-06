package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.task.GameStartTask;
import com.solexgames.mlg.util.*;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.clickable.Clickable;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@NoArgsConstructor
public class ArenaHandler {

    private final WeakHashMap<UUID, Arena> arenaWeakHashMap = new WeakHashMap<>();
    private final WeakHashMap<UUID, Arena> spectatorWeakHashMap = new WeakHashMap<>();

    private final List<Arena> allArenas = new ArrayList<>();
    private final List<DuelRequest> duelRequests = new ArrayList<>();

    /**
     * Loads all arenas from arenas.yml
     */
    public void loadArenas() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfigHandler().getArenasConfig().getConfig().getConfigurationSection("arenas");

        configurationSection.getKeys(false).stream().filter(s -> !s.equalsIgnoreCase("test")).forEach(path -> {
            try {
                final Arena arena = new Arena(UUID.randomUUID(), configurationSection.getString(path + ".name"));
                final Cuboid cuboid = Cuboid.getCuboidFromJson(configurationSection.getString(path + ".cuboid"));
                final Cuboid buildableCuboid = Cuboid.getCuboidFromJson(configurationSection.getString(path + ".buildable-cuboid"));

                arena.setCuboid(cuboid);
                arena.setBuildableCuboid(buildableCuboid);
                arena.setTeamSize(configurationSection.getInt(path + ".team-size"));
                arena.setMaxPlayers(configurationSection.getInt(path + ".max-players"));

                arena.setSpawnOne(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-one")).orElse(null));
                arena.setSpawnTwo(LocationUtil.getLocationFromString(configurationSection.getString(path + ".spawn-two")).orElse(null));

                CorePlugin.getInstance().getLogger().info("[Arena] Loaded arena " + arena.getName() + "!");
            } catch (Exception exception) {
                CorePlugin.getInstance().getLogger().info("[Arena] An arena was not loaded because it is corrupted. (" + path + ")");
            }
        });
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
            player.sendMessage(Locale.ARENA_REGENERATING.format());
            return;
        }

        if (this.isInArena(player)) {
            player.sendMessage(Locale.ALREADY_IN_ARENA.format());
            return;
        }

        if (arena.getAllPlayerList().contains(player.getUniqueId())) {
            player.sendMessage(Locale.ALREADY_IN_ARENA.format());
            return;
        }

        if (arena.getTeamSize() == 1) {
            if (arena.getGamePlayerList().size() == 2) {
                player.sendMessage(Locale.ARENA_MAX_PLAYERS.format());
                return;
            }

            this.arenaWeakHashMap.put(player.getUniqueId(), arena);

            arena.getAllPlayerList().add(player.getUniqueId());
            arena.getGamePlayerList().add(new ArenaPlayer(arena, (arena.getGamePlayerList().size() == 0 ? ArenaTeam.BLUE : (arena.getGamePlayerList().get(0).getArenaTeam() == ArenaTeam.BLUE ? ArenaTeam.RED : ArenaTeam.BLUE)), player));
            arena.broadcastMessage(Locale.PLAYER_JOIN_ARENA.format(player.getDisplayName(), arena.getGamePlayerList().size(), arena.getMaxPlayers()));

            PlayerUtil.restorePlayer(player);

            player.teleport(arena.getSpawnFromTeam(arena.getByPlayer(player).getArenaTeam()));

            CorePlugin.getInstance().getHotbarHandler().setupArenaWaitingHotbar(player);

            if (arena.getGamePlayerList().size() >= arena.getMaxPlayers()) {
                new GameStartTask(Arena.LONG_START ? 20 : 5, arena);
            }
        } else {
            player.sendMessage(CC.RED + "MLG Rush Teams mode is currently in development!");
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
        player.teleport(CorePlugin.getInstance().getLocationHandler().getSpawnLocation());

        if (arena == null) {
            return;
        }

        if (arena.getState().equals(ArenaState.AVAILABLE)) {
            this.arenaWeakHashMap.remove(player.getUniqueId());

            arena.broadcastMessage(Locale.PLAYER_LEAVE_ARENA.format(player.getDisplayName(), arena.getGamePlayerList().size(), arena.getMaxPlayers()));
            arena.getAllPlayerList().remove(player.getUniqueId());
            arena.getGamePlayerList().remove(arena.getByPlayer(player));

            CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
        } else {
            arena.end(arena.getOpponentPlayer(player));
        }
    }

    public void startSpectating(Player player, Arena arena) {
        arena.broadcastMessage(Locale.STARTED_SPECTATING.format(player.getDisplayName()));
        player.sendMessage(Locale.STARTED_SPECTATING.format(player.getDisplayName()));

        arena.getSpectatorList().add(player.getUniqueId());

        this.spectatorWeakHashMap.put(player.getUniqueId(), arena);

        PlayerUtil.resetPlayer(player);
        CorePlugin.getInstance().getHotbarHandler().setupSpectatorHotbar(player);

        player.setAllowFlight(true);
        player.setFlying(true);

        arena.getAllPlayerList().forEach(uuid1 -> {
            final Player player1 = Bukkit.getPlayer(uuid1);

            player1.hidePlayer(player);
        });

        player.teleport(Bukkit.getPlayer(arena.getAllPlayerList().get(0)).getLocation().add(0.0D, 2.0D, 0.0D));
    }

    public void stopSpectating(Player player, Arena arena) {
        arena.broadcastMessage(Locale.STOPPED_SPECTATING.format(player.getDisplayName()));
        player.sendMessage(Locale.STOPPED_SPECTATING.format(player.getDisplayName()));


        arena.getSpectatorList().remove(player.getUniqueId());

        player.teleport(CorePlugin.getInstance().getLocationHandler().getSpawnLocation());

        this.spectatorWeakHashMap.remove(player.getUniqueId());

        PlayerUtil.resetPlayer(player);

        arena.getAllPlayerList().forEach(uuid1 -> {
            final Player player1 = Bukkit.getPlayer(uuid1);

            player1.showPlayer(player);
        });

        CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
    }

    public void sendDuelRequest(Player issuer, Player target, Arena selectedArena) {
        final DuelRequest duelRequest = new DuelRequest(
                issuer.getUniqueId(), target.getUniqueId(),
                System.currentTimeMillis(), issuer.getDisplayName(),
                target.getDisplayName(), selectedArena);

        issuer.sendMessage(Locale.REQUEST_SENT.format(target.getDisplayName(), selectedArena.getName()));

        final Clickable clickable = new Clickable("");

        clickable.add(CC.SECONDARY + "You've received a duel request from " + issuer.getDisplayName() + CC.SECONDARY + "! ");
        clickable.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "[Click to Accept]", ChatColor.GREEN + "Click to accept " + issuer.getDisplayName() + ChatColor.GREEN + "'s duel request.", "/duel accept " + issuer.getName(), ClickEvent.Action.RUN_COMMAND);

        target.spigot().sendMessage(clickable.asComponents());

        this.duelRequests.add(duelRequest);
    }

    /**
     * Sends the end display title to a player
     * <p></p>
     *
     * @param player Player to send the title to
     * @param winner If the title should be the victory title or not
     */
    public void sendEndTitle(Player player, boolean winner) {
        if (winner) {
            PlayerUtil.sendTitle(player, Locale.WINNER_TITLE.format(), Locale.WINNER_SUBTITLE.format());
        } else {
            PlayerUtil.sendTitle(player, Locale.LOSER_TITLE.format(), Locale.LOSER_SUBTITLE.format());
        }
    }

    /**
     * Checks if a player is in an arena or not
     * <p></p>
     *
     * @param player Player to check the status of
     *
     * @return if the player is in the arena or not
     */
    public boolean isInArena(Player player) {
        return this.getByPlayer(player) != null;
    }

    /**
     * Checks if a player is spectating a game or not
     * <p></p>
     *
     * @param player Player to check the status of
     *
     * @return if the player is in the arena or not
     */
    public boolean isSpectating(Player player) {
        return this.getSpectating(player) != null;
    }

    /**
     * Gets a player's spectating arena
     * <p></p>
     *
     * @param player Player to find an arena from
     *
     * @return An arena a player is in, or null
     */
    public Arena getSpectating(Player player) {
        return this.spectatorWeakHashMap.getOrDefault(player.getUniqueId(), null);
    }

    /**
     * Gets a player's arena
     * <p></p>
     *
     * @param player Player to find an arena from
     *
     * @return An arena a player is in, or null
     */
    public Arena getByPlayer(Player player) {
        return this.arenaWeakHashMap.getOrDefault(player.getUniqueId(), null);
    }

    /**
     * Gets a player's incoming duel request
     * <p></p>
     *
     * @param uuid UUID of a duel request
     *
     * @return A duel request from a player, or else null
     */
    public DuelRequest getIncomingDuelRequest(UUID uuid) {
        return this.duelRequests.stream()
                .filter(duelRequest -> duelRequest.getIssuer().equals(uuid))
                .findFirst().orElse(null);
    }
}
