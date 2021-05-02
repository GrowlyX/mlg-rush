package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

public class JoinGameCommand extends BaseCommand {

    @CommandAlias("joingame|join")
    public void joinGame(Player player) {
        new SelectGameMenu().openMenu(player);
    }
}
