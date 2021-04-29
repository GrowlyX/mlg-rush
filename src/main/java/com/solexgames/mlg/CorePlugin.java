package com.solexgames.mlg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.solexgames.mlg.adapter.DateTypeAdapter;
import com.solexgames.mlg.adapter.LocationTypeAdapter;
import com.solexgames.mlg.adapter.PotionEffectTypeAdapter;
import com.solexgames.mlg.handler.KitHandler;
import com.solexgames.mlg.handler.MongoHandler;
import com.solexgames.mlg.handler.PlayerHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.Date;

@Getter
public final class CorePlugin extends JavaPlugin {

    public static Gson GSON;

    @Getter
    private static CorePlugin instance;

    private KitHandler kitHandler;
    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;

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
        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
