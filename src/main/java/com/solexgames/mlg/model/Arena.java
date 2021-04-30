package com.solexgames.mlg.model;

import com.google.gson.annotations.SerializedName;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.state.StateBasedModel;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.LocationUtil;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Arena extends StateBasedModel<ArenaState> {

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
     * @param uuid Arena specified UUID
     * @param name Arena specified Name
     */
    public Arena(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        CorePlugin.getInstance().getArenaHandler().getAllArenas().add(this);
    }

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

    public ArenaTeam getByArenaPlayer(Player player) {
        return this.getGamePlayerList().stream()
                .filter(arenaPlayer1 -> arenaPlayer1.getPlayer().equals(player))
                .map(ArenaPlayer::getArenaTeam)
                .findFirst().orElse(null);
    }

    public ArenaPlayer getByPlayer(Player player) {
        return this.getGamePlayerList().stream()
                .filter(arenaPlayer1 -> arenaPlayer1.getPlayer().equals(player))
                .findFirst().orElse(null);
    }

    public void broadcastMessage(String message) {
        this.getGamePlayerList().forEach(arenaPlayer -> arenaPlayer.getPlayer().sendMessage(Color.translate(message)));
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public ArenaState getState() {
        return this.arenaState;
    }

    @Override
    public void cleanup() {

    }
}
