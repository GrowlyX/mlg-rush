package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/28/2021
 */

public class SpectateCommand extends BaseCommand {

    @CommandAlias("spectate|spec")
    public void execute(Player player, @Optional OnlinePlayer target) {
        final Arena arena = CorePlugin.getInstance().getArenaHandler().getByPlayer(player);

        if (target == null) {
            if (arena != null) {
                player.sendMessage(ChatColor.RED + "Error: You are already in an arena.");
                return;
            }
            new MatchSpectateMenu().openMenu(player);
            return;
        }

        if (arena != null) {
            player.sendMessage(ChatColor.RED + "Error: You are already in an arena.");
            return;
        }

        final Arena targetArena = CorePlugin.getInstance().getArenaHandler().getByPlayer(target.getPlayer());

        if (targetArena == null) {
            player.sendMessage(ChatColor.RED + "Error: That player is not in a match.");
            return;
        }

        if (!targetArena.getState().equals(ArenaState.IN_GAME)) {
            player.sendMessage(ChatColor.RED + "Error: That match has not started yet or the arena bound to it is regenerating.");
            return;
        }

        CorePlugin.getInstance().getArenaHandler().startSpectating(player, targetArena);
    }
}
