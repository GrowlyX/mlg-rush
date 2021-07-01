package com.solexgames.mlg;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.mlg.cache.StatusCache;
import com.solexgames.mlg.command.*;
import com.solexgames.mlg.handler.*;
import com.solexgames.mlg.leaderboard.Leaderboard;
import com.solexgames.mlg.listener.MenuListener;
import com.solexgames.mlg.listener.NPCListener;
import com.solexgames.mlg.listener.PlayerListener;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.scoreboard.ScoreboardAdapter;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.task.DuelExpireTask;
import com.solexgames.mlg.task.GameEndTask;
import com.solexgames.mlg.task.LeaderboardUpdateTask;
import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.CoreConstants;
import com.solexgames.mlg.util.world.VoidWorldGenerator;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
public final class CorePlugin extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    @Getter
    private static CorePlugin instance;

    private NPCHandler npcHandler;
    private ArenaHandler arenaHandler;
    private MongoHandler mongoHandler;
    private BuilderHandler builderHandler;
    private PlayerHandler playerHandler;
    private HotbarHandler hotbarHandler;
    private ConfigHandler configHandler;
    private LocationHandler locationHandler;
    private HologramHandler hologramHandler;
    private LeaderboardHandler leaderboardHandler;

    private String loadingString = ".";
    private boolean hologramsEnabled;

    private final ConversationFactory conversationFactory = new ConversationFactory(this);

    @Override
    public void onEnable() {
        instance = this;

        this.hologramsEnabled = this.getServer().getPluginManager().isPluginEnabled("HolographicDisplays");

        this.configHandler = new ConfigHandler();

        this.locationHandler = new LocationHandler();
        this.locationHandler.loadSpawn();

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.hotbarHandler = new HotbarHandler();
        this.builderHandler = new BuilderHandler();

        this.setupTheming();
        this.createDefaultWorld();

        this.arenaHandler = new ArenaHandler();
        this.arenaHandler.loadArenas();

        this.leaderboardHandler = new LeaderboardHandler();

        if (this.hologramsEnabled) {
            this.hologramHandler = new HologramHandler();
            this.hologramHandler.setupHologram();
        }

        this.getServer().getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.NORMAL);
            world.setTime(1000);

            world.getEntities().stream()
                    .filter(entity -> !(entity instanceof Player))
                    .forEach(Entity::remove);

            this.getLogger().info("[World] Updated world settings for: " + world.getName());
        });

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.getServer().getPluginManager().registerEvents(new NPCListener(), this);

        final PaperCommandManager manager = new PaperCommandManager(this);

        this.registerContexts(manager);
        this.registerCommands(manager);
        this.registerApis(manager);

        new ScoreboardHandler(this, new ScoreboardAdapter(), 5L);

        this.registerTasks();
    }

    private void registerTasks() {
        new StatusCache().runTaskTimerAsynchronously(this, 20L, TimeUnit.SECONDS.toMillis(1L));
        new DuelExpireTask().runTaskTimerAsynchronously(this, 20L, TimeUnit.SECONDS.toMillis(1L));
        new GameEndTask().runTaskTimerAsynchronously(this, 20L, TimeUnit.SECONDS.toMillis(1L));
        new LeaderboardUpdateTask().runTaskTimerAsynchronously(this, 20L, TimeUnit.MINUTES.toMillis(5L));

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> this.playerHandler.getPlayerList().values()
                .stream().filter(player -> player.getPlayer() == null)
                .forEach(gamePlayer -> gamePlayer.savePlayerData(true)), 20L * 60L, 20L * 60L);

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> this.loadingString =
                this.loadingString.equals(".") ? ".." :
                        this.loadingString.equals("..") ? "..." : ".", 10L, 10L);
    }

    private void registerApis(PaperCommandManager manager) {
        manager.enableUnstableAPI("help");
    }

    private void registerCommands(PaperCommandManager manager) {
        manager.registerCommand(new BuildCommand());
        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new JoinGameCommand());
        manager.registerCommand(new MLGRushCommand());
        manager.registerCommand(new LeaveCommand());
        manager.registerCommand(new ResetLoadoutCommand());
        manager.registerCommand(new LayoutCommand());
        manager.registerCommand(new DuelCommand());
        manager.registerCommand(new SpectateCommand());
        manager.registerCommand(new SetSpawnCommand());
        manager.registerCommand(new StatsResetCommand());
        manager.registerCommand(new LeaderboardCommand());
        manager.registerCommand(new SetHologramCommand());
        manager.registerCommand(new StatsCommand());
    }

    private void registerContexts(PaperCommandManager manager) {
        manager.getCommandContexts().registerContext(Arena.class, context -> {
            final String joinedString = String.join(" ", context.getArgs());
            final Arena arena = this.getArenaHandler().getAllArenas()
                    .stream().filter(arena1 -> arena1.getName().equals(joinedString))
                    .findFirst().orElse(null);

            if (arena == null) {
                throw new InvalidCommandArgument("No arena matching " + CC.YELLOW + joinedString + CC.RED + " was found.", false);
            }

            return arena;
        });

        manager.getCommandContexts().registerContext(GamePlayer.class, context -> {
            final String joinedString = String.join(" ", context.getArgs());
            final GamePlayer gamePlayer = this.playerHandler.getByName(joinedString);

            if (gamePlayer == null) {
                throw new InvalidCommandArgument("No player matching " + CC.YELLOW + joinedString + CC.RED + " was found.", false);
            }

            return gamePlayer;
        });

        manager.getCommandContexts().registerContext(Leaderboard.class, context -> {
            final String joinedString = String.join(" ", context.getArgs());
            final Leaderboard gamePlayer = this.leaderboardHandler.getByName(joinedString);

            if (gamePlayer == null) {
                throw new InvalidCommandArgument("No leaderboard matching " + CC.YELLOW + joinedString + CC.RED + " was found.", false);
            }

            return gamePlayer;
        });

        manager.getCommandCompletions().registerAsyncCompletion("leaderboards", context ->
                this.leaderboardHandler.getLeaderboards().stream().map(leaderboard -> leaderboard.getName().toLowerCase()).collect(Collectors.toList())
        );
    }

    private void setupTheming() {
        CC.PRIMARY = ChatColor.valueOf(this.getConfig().getString("language.primary-color").toUpperCase().replace(" ", "_")).toString();
        CC.SECONDARY = ChatColor.valueOf(this.getConfig().getString("language.secondary-color").toUpperCase().replace(" ", "_")).toString();

        CoreConstants.DEFAULT_SCOREBOARD_TITLE = Color.translate(this.getConfigHandler().getScoreboardConfig().getString("DEFAULT_SCOREBOARD_TITLE"));
        CoreConstants.NPC_ENABLED = this.getConfig().getBoolean("settings.enable-npcs");
    }

    private void createDefaultWorld() {
        if (this.getServer().getWorld("mlg") == null) {
            this.getLogger().info("[World] Creating new 'mlg' world...");

            final World world = this.getServer().createWorld(new WorldCreator("mlg").generator(new VoidWorldGenerator()));

            world.setSpawnLocation(0, 62, 0);

            final Block block = world.getBlockAt(0, 61, 0);
            block.setType(Material.BEDROCK);
            block.getState().update();

            this.getLogger().info("[World] Created 'mlg' world.");
        }
    }

    @Override
    public void onDisable() {
        this.arenaHandler.getAllArenas().forEach(arena -> {
            arena.cleanup();
            arena.saveArenaData();
        });
    }
}
