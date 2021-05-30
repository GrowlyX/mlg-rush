package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Locale;
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
                player.sendMessage(Locale.ALREADY_IN_ARENA.format());
                return;
            }
            new MatchSpectateMenu().openMenu(player);
            return;
        }

        if (arena != null) {
            player.sendMessage(Locale.ALREADY_IN_ARENA.format());
            return;
        }

        final Arena targetArena = CorePlugin.getInstance().getArenaHandler().getByPlayer(target.getPlayer());

        if (targetArena == null) {
            player.sendMessage(Locale.PLAYER_NOT_IN_MATCH.format());
            return;
        }

        if (!targetArena.getState().equals(ArenaState.IN_GAME)) {
            player.sendMessage(Locale.MATCH_ERROR.format());
            return;
        }

        CorePlugin.getInstance().getArenaHandler().startSpectating(player, targetArena);
    }
}
