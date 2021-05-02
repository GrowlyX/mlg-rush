package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinGameCommand extends BaseCommand {

    @CommandAlias("joingame|join")
    public void joinGame(Player player) {
        new SelectGameMenu().openMenu(player);
    }
}
