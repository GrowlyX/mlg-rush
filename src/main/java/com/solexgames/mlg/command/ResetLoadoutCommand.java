package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ResetLoadoutCommand extends BaseCommand {

    @CommandAlias("resetloadout")
    public void execute(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());
        gamePlayer.getLayout().resetLayout();

        player.sendMessage(Locale.LAYOUT_RESET.format());
    }
}
