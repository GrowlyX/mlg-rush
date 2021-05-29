package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

@CommandAlias("mlgrush")
public class MLGRushCommand extends BaseCommand {

    private final static String[] AUTHORS = new String[]{
            "GrowlyX#4953",
            "puugz#4877",
    };

    @Default
    public void onDefault(Player player) {
        player.sendMessage(Color.SECONDARY + "This server is running " + Color.PRIMARY + "MLGRush" + Color.SECONDARY + " created by " + Color.PRIMARY + String.join(Color.SECONDARY + ", " + Color.PRIMARY, AUTHORS) + Color.SECONDARY + ".");
    }

    @Subcommand("reload")
    @CommandPermission("mlgrush.admin")
    public void reload(CommandSender sender) {
        CorePlugin.getInstance().getConfigHandler().reload();

        sender.sendMessage(Color.PRIMARY + "MLGRush " + Color.SECONDARY + " has been reloaded!");
    }
}
