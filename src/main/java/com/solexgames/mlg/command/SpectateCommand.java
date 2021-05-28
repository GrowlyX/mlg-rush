package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class SpectateCommand extends BaseCommand {

    @CommandAlias("spectate|spec")
    public void joinGame(Player player) {
        new MatchSpectateMenu().openMenu(player);
    }
}
