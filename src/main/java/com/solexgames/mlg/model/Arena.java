package com.solexgames.mlg.model;

import com.google.gson.annotations.SerializedName;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.state.StateBasedModel;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.task.RoundStartTask;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.LocationUtil;
import com.solexgames.mlg.util.PlayerUtil;
import com.solexgames.mlg.util.builder.ItemBuilder;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@Setter
public class Arena extends StateBasedModel<ArenaState, ArenaPlayer> {

    public static final ItemStack[] RED_ITEM_STACK_ARRAY = new ItemStack[]{new ItemBuilder(Material.LEATHER_BOOTS).setColor(org.bukkit.Color.RED).create(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setColor(org.bukkit.Color.RED).create(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(org.bukkit.Color.RED).create(),
            new ItemBuilder(Material.LEATHER_HELMET).setColor(org.bukkit.Color.RED).create()};

    public static final ItemStack[] BLUE_ITEM_STACK_ARRAY = new ItemStack[]{new ItemBuilder(Material.LEATHER_BOOTS).setColor(org.bukkit.Color.BLUE).create(),
            new ItemBuilder(Material.LEATHER_LEGGINGS).setColor(org.bukkit.Color.BLUE).create(),
            new ItemBuilder(Material.LEATHER_CHESTPLATE).setColor(org.bukkit.Color.BLUE).create(),
            new ItemBuilder(Material.LEATHER_HELMET).setColor(org.bukkit.Color.BLUE).create()};

    public static final int WINNER_POINT_AMOUNT = 5;

    private final List<Location> blockLocationList = new ArrayList<>();
    private final List<ArenaPlayer> gamePlayerList = new ArrayList<>();
    private final List<Player> allPlayerList = new ArrayList<>();

    @SerializedName("_id")
    private final UUID uuid;

    private String name;
    private String configPath;
    private Cuboid cuboid;

    private int teamSize;
    private int maxPlayers;

    private Location spawnOne;
    private Location spawnTwo;

    private ArenaState arenaState = ArenaState.AVAILABLE;

    /**
     * Creates a new instance of {@link Arena}
     * <p>
     *
     * @param uuid Arena UUID
     * @param name Arena Name
     */
    public Arena(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        CorePlugin.getInstance().getArenaHandler().getAllArenas().add(this);
    }

    /**
     * Saves the arena's data to the main config
     */
    public void saveArenaData() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("arenas");

        try {
            configurationSection.set(this.name + ".uuid", this.uuid.toString());
            configurationSection.set(this.name + ".name", this.name);
            configurationSection.set(this.name + ".team-size", this.teamSize);
            configurationSection.set(this.name + ".max-players", this.maxPlayers);
            configurationSection.set(this.name + ".cuboid", this.cuboid.getSerialized());
            configurationSection.set(this.name + ".spawn-one", LocationUtil.getStringFromLocation(this.spawnOne).orElse(null));
            configurationSection.set(this.name + ".spawn-two", LocationUtil.getStringFromLocation(this.spawnTwo).orElse(null));
        } catch (Exception exception) {
            CorePlugin.getInstance().getLogger().severe("[Arena] Couldn't save the arena " + this.name + ": " + exception.getMessage());
        }

        CorePlugin.getInstance().saveConfig();
    }

    /**
     * Gets an ArenaPlayer from a Player instance
     * <p></p>
     *
     * @param player Player to find an ArenaPlayer from
     *
     * @return an ArenaPlayer
     */
    public ArenaPlayer getByPlayer(Player player) {
        return this.getGamePlayerList().stream()
                .filter(arenaPlayer1 -> arenaPlayer1.getPlayer().equals(player))
                .findFirst().orElse(null);
    }

    /**
     * Broadcasts a message to the whole arena
     * <p></p>
     *
     * @param message Message to broadcast
     */
    public void broadcastMessage(String message) {
        this.getGamePlayerList().forEach(arenaPlayer -> arenaPlayer.getPlayer().sendMessage(Color.translate(message)));
    }

    /**
     * Adds a point to the {@param player} parameter and starts a new round
     * <p></p>
     *
     * @param player Player to add a point to
     */
    public void incrementPointAndStartRound(Player player) {
        final ArenaPlayer arenaPlayer = this.getByPlayer(player);

        if (arenaPlayer.getPoints() == Arena.WINNER_POINT_AMOUNT) {
            this.end(this.getByPlayer(player));
            return;
        }

        arenaPlayer.setPoints(arenaPlayer.getPoints() + 1);

        this.broadcastMessage(Color.PRIMARY + player.getName() + Color.SECONDARY + " has scored a point! " + ChatColor.GRAY + "(" + ChatColor.BLUE + this.getPoints(ArenaTeam.BLUE) + ChatColor.GRAY + "/" + ChatColor.RED + this.getPoints(ArenaTeam.RED) + ChatColor.GRAY + ")");
        this.resetAndSetupGameSystem();
    }

    /**
     * Gets the total sum of points a team has
     * <p></p>
     *
     * @param arenaTeam Arena team to get points of
     *
     * @return the amount of total points a team has
     */
    public int getPoints(ArenaTeam arenaTeam) {
        return this.getGamePlayerList().stream()
                .filter(arenaPlayer -> arenaPlayer.getArenaTeam().equals(arenaTeam))
                .mapToInt(ArenaPlayer::getPoints).sum();
    }

    /**
     * Cleans up arena, resets players, and schedules a new round
     */
    public void resetAndSetupGameSystem() {
        this.cleanup();

        // running on the main thread because bukkit thinks this is ran async for some reason
        Bukkit.getScheduler().runTask(CorePlugin.getInstance(), () -> this.getGamePlayerList().forEach(arenaPlayer -> {
            PlayerUtil.resetPlayer(arenaPlayer.getPlayer());

            if (arenaPlayer.getArenaTeam().equals(ArenaTeam.BLUE)) {
                arenaPlayer.getPlayer().teleport(this.spawnOne);
                arenaPlayer.getPlayer().getInventory().setArmorContents(Arena.BLUE_ITEM_STACK_ARRAY);
            } else {
                arenaPlayer.getPlayer().teleport(this.spawnTwo);
                arenaPlayer.getPlayer().getInventory().setArmorContents(Arena.RED_ITEM_STACK_ARRAY);
            }

            arenaPlayer.getPlayer().setMetadata("frozen", new FixedMetadataValue(CorePlugin.getInstance(), true));
            arenaPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10000, 10));
        }));

        new RoundStartTask(5, this);
    }

    /**
     * Resets the arena & its players
     */
    public void resetAndStop() {
        this.arenaState = ArenaState.REGENERATING;

        this.cleanup();

        this.getGamePlayerList().forEach(arenaPlayer -> {
            PlayerUtil.resetPlayer(arenaPlayer.getPlayer());
            CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(arenaPlayer.getPlayer());

            Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> arenaPlayer.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation()), 50L);
        });

        this.gamePlayerList.clear();
        this.allPlayerList.clear();

        Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), ()-> this.arenaState = ArenaState.AVAILABLE, 60L);
    }

    public boolean isTeamsBed(Location location, ArenaTeam arenaTeam) {
        final Location spawn = arenaTeam == ArenaTeam.BLUE ? this.spawnOne : this.spawnTwo;

        return location.getBlock().getLocation().distance(spawn) <= 3.0D;
    }

    public boolean isCloseToSpawn(Location location, ArenaTeam arenaTeam) {
        final Location spawn = arenaTeam == ArenaTeam.BLUE ? this.spawnOne : this.spawnTwo;

        return location.getBlock().getLocation().distance(spawn) <= 2.0D;
    }

    public ArenaTeam getOpposingTeam(ArenaPlayer arenaPlayer) {
        return arenaPlayer.getArenaTeam() == ArenaTeam.BLUE ? ArenaTeam.RED : ArenaTeam.BLUE;
    }

    public Location getSpawnFromTeam(ArenaTeam arenaTeam) {
        return arenaTeam == ArenaTeam.BLUE ? this.spawnOne : spawnTwo;
    }

    public ArenaPlayer getOpponentPlayer(Player player) {
        return this.gamePlayerList.stream()
                .filter(arenaPlayer -> !arenaPlayer.getArenaTeam().equals(this.getByPlayer(player).getArenaTeam()))
                .findFirst().orElse(null);
    }

    @Override
    public void start() {
        this.arenaState = ArenaState.IN_GAME;

        this.resetAndSetupGameSystem();
    }

    @Override
    public void end(ArenaPlayer profile) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(profile.getPlayer().getName());

        if (gamePlayer != null) {
            gamePlayer.setWins(gamePlayer.getWins() + 1);

            this.broadcastMessage(Color.PRIMARY + profile.getPlayer().getName() + Color.SECONDARY + " has won the game!");
        }

        this.resetAndStop();
    }

    @Override
    public ArenaState getState() {
        return this.arenaState;
    }

    @Override
    public void cleanup() {
        for (Location location : this.getBlockLocationList()) {
            final Block block = location.getBlock();

            if (block != null) {
                block.setType(Material.AIR);
            }
        }
    }
}
