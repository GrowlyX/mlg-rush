package com.solexgames.mlg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.mlg.adapter.DateTypeAdapter;
import com.solexgames.mlg.adapter.LocationTypeAdapter;
import com.solexgames.mlg.adapter.PotionEffectTypeAdapter;
import com.solexgames.mlg.adapter.ScoreboardAdapter;
import com.solexgames.mlg.command.ArenaCommand;
import com.solexgames.mlg.command.TestCommand;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.handler.KitHandler;
import com.solexgames.mlg.handler.MongoHandler;
import com.solexgames.mlg.handler.PlayerHandler;
import com.solexgames.mlg.listener.PaginationListener;
import com.solexgames.mlg.listener.PlayerListener;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.model.Kit;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.Date;

@Getter
public final class CorePlugin extends JavaPlugin {

    public static Gson GSON;

    @Getter
    private static CorePlugin instance;

    private KitHandler kitHandler;
    private ArenaHandler arenaHandler;
    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;

    private final ConversationFactory conversationFactory = new ConversationFactory(this);

    @Override
    public void onEnable() {
        instance = this;

        GSON = new GsonBuilder()
                .registerTypeAdapter(PotionEffect.class, new PotionEffectTypeAdapter())
                .registerTypeAdapter(Location.class, new LocationTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .disableHtmlEscaping()
                .create();

        this.saveDefaultConfig();

        this.kitHandler = new KitHandler();
        this.arenaHandler = new ArenaHandler();
        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();

        this.getCommand("arena").setExecutor(new ArenaCommand());
        this.getCommand("test").setExecutor(new TestCommand());

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new PaginationListener(), this);

        new ScoreboardHandler(this, new ScoreboardAdapter(), 20L);
    }

    @Override
    public void onDisable() {
        this.kitHandler.getAllKits().forEach(Kit::saveKitData);
        this.arenaHandler.getAllArenas().forEach(Arena::saveArenaData);
    }
}
