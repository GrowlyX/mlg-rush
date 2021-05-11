package com.solexgames.mlg;

import co.aikar.commands.PaperCommandManager;
import com.solexgames.mlg.adapter.ScoreboardAdapter;
import com.solexgames.mlg.command.*;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.handler.HotbarHandler;
import com.solexgames.mlg.handler.MongoHandler;
import com.solexgames.mlg.handler.PlayerHandler;
import com.solexgames.mlg.listener.MenuListener;
import com.solexgames.mlg.listener.PlayerListener;
import com.solexgames.mlg.util.Color;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.conversations.ConversationFactory;
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

    private final ConversationFactory conversationFactory = new ConversationFactory(this);

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();

        Color.PRIMARY = ChatColor.valueOf(this.getConfig().getString("language.primary-color"));
        Color.SECONDARY = ChatColor.valueOf(this.getConfig().getString("language.secondary-color"));

        this.arenaHandler = new ArenaHandler();
        this.arenaHandler.loadArenas();

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.hotbarHandler = new HotbarHandler();

        this.getServer().getWorlds().forEach(world -> {
            world.setDifficulty(Difficulty.NORMAL);
            world.setTime(1000);

            this.getLogger().info("[World] Updated world settings for: " + world.getName());
        });

        final PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new JoinGameCommand());
        manager.registerCommand(new MLGRushCommand());
        manager.registerCommand(new LeaveCommand());
        manager.registerCommand(new ResetLoadoutCommand());
        manager.registerCommand(new LoadoutCommand());
        manager.registerCommand(new DuelCommand());

        manager.enableUnstableAPI("help");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);

        new ScoreboardHandler(this, new ScoreboardAdapter(), 5L);
    }

    @Override
    public void onDisable() {
        this.arenaHandler.getAllArenas().forEach(arena -> {
            arena.cleanup();
            arena.saveArenaData();
        });
    }
}
