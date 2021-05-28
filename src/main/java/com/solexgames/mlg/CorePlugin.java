package com.solexgames.mlg;

import co.aikar.commands.PaperCommandManager;
import com.solexgames.mlg.cache.StatusCache;
import com.solexgames.mlg.command.*;
import com.solexgames.mlg.handler.*;
import com.solexgames.mlg.listener.MenuListener;
import com.solexgames.mlg.listener.PlayerListener;
import com.solexgames.mlg.scoreboard.ScoreboardAdapter;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.CoreConstants;
import com.solexgames.mlg.world.VoidWorldGenerator;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
public final class CorePlugin extends JavaPlugin {

    @Getter
    private static CorePlugin instance;

    private ArenaHandler arenaHandler;
    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;
    private HotbarHandler hotbarHandler;
    private ConfigHandler configHandler;

    private final ConversationFactory conversationFactory = new ConversationFactory(this);

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        this.setupTheming();

        this.arenaHandler = new ArenaHandler();
        this.arenaHandler.loadArenas();

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.hotbarHandler = new HotbarHandler();
        this.configHandler = new ConfigHandler();

        this.createDefaultWorld();

        this.getServer().getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.NORMAL);
            world.setTime(1000);

            world.getEntities().stream()
                    .filter(entity -> !(entity instanceof Player))
                    .forEach(Entity::remove);

            this.getLogger().info("[World] Updated world settings for: " + world.getName());
        });

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        final PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new JoinGameCommand());
        manager.registerCommand(new MLGRushCommand());
        manager.registerCommand(new LeaveCommand());
        manager.registerCommand(new ResetLoadoutCommand());
        manager.registerCommand(new LoadoutCommand());
        manager.registerCommand(new DuelCommand());
        manager.registerCommand(new SpectateCommand());

        manager.enableUnstableAPI("help");

        new ScoreboardHandler(this, new ScoreboardAdapter(), 5L);
        new StatusCache().runTaskTimer(this, 20L, 20L);
    }

    private void setupTheming() {
        Color.PRIMARY = ChatColor.valueOf(this.getConfig().getString("language.primary-color").toUpperCase().replace(" ", "_"));
        Color.SECONDARY = ChatColor.valueOf(this.getConfig().getString("language.secondary-color").toUpperCase().replace(" ", "_"));

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
