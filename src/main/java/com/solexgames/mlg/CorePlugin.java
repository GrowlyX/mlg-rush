package com.solexgames.mlg;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.mlg.adapter.type.LocationTypeAdapter;
import com.solexgames.mlg.adapter.ScoreboardAdapter;
import com.solexgames.mlg.command.ArenaCommand;
import com.solexgames.mlg.command.JoinGameCommand;
import com.solexgames.mlg.handler.*;
import com.solexgames.mlg.listener.PaginationListener;
import com.solexgames.mlg.listener.PlayerListener;
import com.solexgames.mlg.model.Arena;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.Date;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
public final class CorePlugin extends JavaPlugin {

    public static Gson GSON;

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

        GSON = new GsonBuilder()
                .registerTypeAdapter(Location.class, new LocationTypeAdapter())
                .disableHtmlEscaping()
                .create();

        this.saveDefaultConfig();

        this.arenaHandler = new ArenaHandler();
        this.arenaHandler.loadArenas();

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.hotbarHandler = new HotbarHandler();

        final PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ArenaCommand());
        manager.registerCommand(new JoinGameCommand());

        manager.enableUnstableAPI("help");

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PaginationListener(), this);

        new ScoreboardHandler(this, new ScoreboardAdapter(), 20L);
    }

    @Override
    public void onDisable() {
        this.arenaHandler.getAllArenas().forEach(Arena::cleanup);
        this.arenaHandler.getAllArenas().forEach(Arena::saveArenaData);
    }
}
