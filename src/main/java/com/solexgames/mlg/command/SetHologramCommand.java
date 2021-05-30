package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.solexgames.mlg.CorePlugin;
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
        final LocationHandler locationHandler = CorePlugin.getInstance().getLocationHandler();
        final Location location = player.getLocation();

        locationHandler.setHologramLocation(location);
        locationHandler.saveHolo();

        if (CorePlugin.getInstance().getHologramHandler().getRotatingHologram() == null) {
            CorePlugin.getInstance().getHologramHandler().setRotatingHologram(HologramsAPI.createHologram(CorePlugin.getInstance(), location));
        }

        player.sendMessage(Locale.SET_HOLO_SPAWN.format());
    }
}
