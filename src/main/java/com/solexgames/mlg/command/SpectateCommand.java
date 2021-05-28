package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

@CommandAlias("spectate|spec")
public class SpectateCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        new MatchSpectateMenu().openMenu(player);
    }

    public void execute(Player player, OnlinePlayer target) {
        final Arena arena = CorePlugin.getInstance().getArenaHandler().getByPlayer(target.getPlayer());

        if (arena == null) {
            player.sendMessage(ChatColor.RED + "Error: That player is not in a match.");
            return;
        }

        if (!arena.getState().equals(ArenaState.IN_GAME)) {
            player.sendMessage(ChatColor.RED + "Error: That match has not started yet or the arena binded to it is regenerating.");
            return;
        }

        CorePlugin.getInstance().getArenaHandler().startSpectating(player, arena);
    }
}
