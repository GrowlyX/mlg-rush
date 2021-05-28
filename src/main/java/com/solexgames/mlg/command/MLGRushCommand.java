package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.util.Color;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MLGRushCommand extends BaseCommand {

    private final static String[] AUTHORS = new String[]{
            "GrowlyX#4953",
            "puugz#4877",
    };

    @CommandAlias("mlgrush")
    public void mlgRush(Player player) {
        player.sendMessage(Color.SECONDARY + "This server is running " + Color.PRIMARY + "MLGRush" + Color.SECONDARY + " created by " + Color.PRIMARY + String.join(Color.SECONDARY + ", " + Color.PRIMARY, AUTHORS) + Color.SECONDARY + ".");
    }
}
