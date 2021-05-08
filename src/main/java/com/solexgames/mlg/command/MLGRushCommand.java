package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.util.Color;
import org.bukkit.entity.Player;

public class MLGRushCommand extends BaseCommand {

    @CommandAlias("mlgrush")
    public void joinGame(Player player) {
        player.sendMessage(Color.SECONDARY + "This server is running " + Color.PRIMARY + "MLGRush" + Color.SECONDARY + " created by " + Color.PRIMARY + "GrowlyX#4953" + Color.SECONDARY + "!");
    }
}
