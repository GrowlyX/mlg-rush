package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import org.bukkit.entity.Player;

public class LeaveCommand extends BaseCommand {

    @CommandAlias("leave|leavegame|quit|l|spawn")
    public void execute(Player player) {
        CorePlugin.getInstance().getArenaHandler().leaveGame(player, CorePlugin.getInstance().getArenaHandler().getByPlayer(player));
    }
}
