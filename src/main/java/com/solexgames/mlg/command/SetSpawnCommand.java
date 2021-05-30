package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.handler.LocationHandler;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class SetSpawnCommand extends BaseCommand {

    @CommandAlias("setspawn")
    @CommandPermission("mlgrush.command.setspawn")
    public void execute(Player player) {
        final LocationHandler locationHandler = CorePlugin.getInstance().getLocationHandler();

        locationHandler.setSpawnLocation(player.getLocation());
        locationHandler.saveSpawn();

        player.sendMessage(Locale.SET_WORLD_SPAWN.format());
    }
}
