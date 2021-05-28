package com.solexgames.mlg.command;

import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.handler.LocationHandler;
import com.solexgames.mlg.util.Color;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

@CommandAlias("setspawn")
@CommandPermission("mlgrush.command.setspawn")
public class SetSpawnCommand {

    public void execute(Player player) {
        final LocationHandler locationHandler = CorePlugin.getInstance().getLocationHandler();

        locationHandler.setSpawnLocation(player.getLocation());
        locationHandler.saveSpawn();

        player.sendMessage(Color.SECONDARY + "You've set the world spawn!");
    }
}
