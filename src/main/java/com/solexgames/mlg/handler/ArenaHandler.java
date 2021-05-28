package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.task.DuelRequestExpirationTask;
import com.solexgames.mlg.task.GameStartTask;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.LocationUtil;
import com.solexgames.mlg.util.PlayerUtil;
import com.solexgames.mlg.util.clickable.Clickable;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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
    private final List<DuelRequest> duelRequests = new ArrayList<>();

    /**
     * Loads all arenas from the config.yml
     */
    public void loadArenas() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("arenas");

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
            player.teleport(Bukkit.getWorld("mlg").getSpawnLocation());
            player.sendMessage(ChatColor.RED + "You aren't currently in an arena.");
            return;
        }

        if (arena.getState().equals(ArenaState.AVAILABLE)) {
            arena.getAllPlayerList().remove(player);
            arena.getGamePlayerList().remove(arena.getByPlayer(player));
            arena.broadcastMessage(Color.PRIMARY + player.getName() + Color.SECONDARY + " has left the arena. " + ChatColor.GRAY + "(" + arena.getGamePlayerList().size() + "/" + arena.getMaxPlayers() + ")");

            player.teleport(Bukkit.getWorld("mlg").getSpawnLocation());

            CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
        } else {
            arena.end(arena.getOpponentPlayer(player));
        }
    }

    public void startSpectating(Player player, Arena arena) {
        arena.broadcastMessage(Color.PRIMARY + player.getDisplayName() + Color.SECONDARY + " has started spectating the match.");
        arena.getSpectatorList().add(player);

        PlayerUtil.resetPlayer(player);
        CorePlugin.getInstance().getHotbarHandler().setupSpectatorHotbar(player);

        player.setAllowFlight(true);
        player.setFlying(true);

        arena.getAllPlayerList().forEach(player1 -> player1.hidePlayer(player));

        player.teleport(arena.getAllPlayerList().get(0).getLocation().add(0.0D, 2.0D, 0.0D));
    }

    public void stopSpectating(Player player, Arena arena) {
        arena.broadcastMessage(Color.PRIMARY + player.getDisplayName() + Color.SECONDARY + " has stopped spectating the match.");
        arena.getSpectatorList().remove(player);

        player.teleport(Bukkit.getWorld("mlg").getSpawnLocation());

        PlayerUtil.resetPlayer(player);

        arena.getAllPlayerList().forEach(player1 -> player1.showPlayer(player));

        CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
    }

    public void sendDuelRequest(Player issuer, Player target, Arena selectedArena) {
        final DuelRequest duelRequest = new DuelRequest(UUID.randomUUID(),
                issuer.getUniqueId(), target.getUniqueId(),
                System.currentTimeMillis(), issuer.getDisplayName(),
                target.getDisplayName(), selectedArena);

        issuer.sendMessage(Color.SECONDARY + "You've sent out a duel request to " + target.getDisplayName() + Color.SECONDARY + " on the map " + Color.PRIMARY + selectedArena.getName() + Color.SECONDARY + "!");

        final Clickable clickable = new Clickable("");

        clickable.add(Color.SECONDARY + "You've received a duel request from " + issuer.getDisplayName() + Color.SECONDARY + "! ");
        clickable.add(ChatColor.GREEN + ChatColor.BOLD.toString() + "[Accept]", ChatColor.GREEN + "Click to accept " + issuer.getDisplayName() + ChatColor.GREEN + "'s duel request.", "/duel accept " + duelRequest.getId().toString(), ClickEvent.Action.RUN_COMMAND);

        target.spigot().sendMessage(clickable.asComponents());

        this.duelRequests.add(duelRequest);

        new DuelRequestExpirationTask(duelRequest);
    }

    /**
     * Sends the end display title to a player
     * <p></p>
     *
     * @param player Player to send the title to
     * @param winner If the title should be the victory title or not
     */
    public void sendEndTitle(Player player, boolean winner) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;

        PacketPlayOutTitle packetPlayOutTitle;
        PacketPlayOutTitle packetPlayOutSubtitle;

        if (winner) {
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + "VICTORY" + "\",color:" + ChatColor.BOLD.name().toLowerCase() + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}")
            );
            packetPlayOutSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + "You've won the game!" + "\",color:" + ChatColor.GRAY.name().toLowerCase() + "}")
            );

        } else {
            packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + "YOU LOST" + "\",color:" + ChatColor.BOLD.name().toLowerCase() + "\",color:" + ChatColor.RED.name().toLowerCase() + "}")
            );
            packetPlayOutSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer
                    .a("{\"text\": \"" + "You lost the game!" + "\",color:" + ChatColor.GRAY.name().toLowerCase() + "}")
            );
        }

        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutTitle);
        craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutSubtitle);
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

    /**
     * Checks if a player is spectating a game or not
     * <p></p>
     *
     * @param player Player to check the status of
     * @return if the player is in the arena or not
     */
    public boolean isSpectating(Player player) {
        return this.allArenas.stream()
                .filter(kit -> kit.getSpectatorList().contains(player))
                .findFirst().orElse(null) != null;
    }

    /**
     * Gets a player's spectating arena
     * <p></p>
     *
     * @param player Player to find an arena from
     * @return An arena a player is in, or null
     */
    public Arena getSpectating(Player player) {
        return this.allArenas.stream()
                .filter(kit -> kit.getSpectatorList().contains(player))
                .findFirst().orElse(null);
    }

    /**
     * Gets a player's arena
     * <p></p>
     *
     * @param player Player to find an arena from
     * @return An arena a player is in, or null
     */
    public Arena getByPlayer(Player player) {
        return this.allArenas.stream()
                .filter(kit -> kit.getAllPlayerList().contains(player))
                .findFirst().orElse(null);
    }

    /**
     * Gets a player's incoming duel request
     * <p></p>
     *
     * @param uuid UUID of a duel request
     * @return A duel request from a player, or else null
     */
    public DuelRequest getIncomingDuelRequest(UUID uuid) {
        return this.duelRequests.stream()
                .filter(duelRequest -> duelRequest.getId().equals(uuid))
                .findFirst().orElse(null);
    }
}
