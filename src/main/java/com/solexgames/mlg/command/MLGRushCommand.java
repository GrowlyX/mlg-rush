package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
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
        player.sendMessage(CC.SECONDARY + "This server is running " + CC.PRIMARY + "MLGRush" + CC.SECONDARY + " created by " + CC.PRIMARY + String.join(CC.SECONDARY + ", " + CC.PRIMARY, AUTHORS) + CC.SECONDARY + ".");
    }

    @Subcommand("reload")
    @CommandPermission("mlgrush.admin")
    public void reload(CommandSender sender) {
        CorePlugin.getInstance().getConfigHandler().reload();

        sender.sendMessage(Locale.PLUGIN_RELOADED.format());
    }
}
