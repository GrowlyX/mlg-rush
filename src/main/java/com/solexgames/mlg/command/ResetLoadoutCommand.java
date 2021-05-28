package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ResetLoadoutCommand extends BaseCommand {

    @CommandAlias("resetloadout")
    public void execute(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());
        gamePlayer.getLayout().resetLayout();

        player.sendMessage(ChatColor.GREEN + "You've reset your layout!");
    }
}
