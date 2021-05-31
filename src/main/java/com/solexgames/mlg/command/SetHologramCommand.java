package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.handler.HologramHandler;
import com.solexgames.mlg.handler.LocationHandler;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since 30/5/2021 22:18
 */

public class SetHologramCommand extends BaseCommand {

    @CommandAlias("sethologram")
    @CommandPermission("mlgrush.command.sethologram")
    public void execute(Player player) {
        final HologramHandler hologramHandler = CorePlugin.getInstance().getHologramHandler();
        final LocationHandler locationHandler = CorePlugin.getInstance().getLocationHandler();
        final Location location = player.getLocation();

        locationHandler.setHologramLocation(location);
        locationHandler.saveHolo();

        final Hologram rotatingHologram = hologramHandler.getRotatingHologram();

        if (rotatingHologram != null) {
            rotatingHologram.delete();
        }

        hologramHandler.getUpdateTask().cancel();
        hologramHandler.setRotatingHologram(HologramsAPI.createHologram(CorePlugin.getInstance(), location));
        hologramHandler.setupTasks();

        player.sendMessage(Locale.SET_HOLO_SPAWN.format());
    }
}
