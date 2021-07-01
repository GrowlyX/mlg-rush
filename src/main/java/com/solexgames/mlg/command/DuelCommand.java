package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.menu.impl.DuelArenaSelectMenu;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("duel|fight|1v1")
public class DuelCommand extends BaseCommand {

    @Default
    public void onDuel(Player player, @Name("player") OnlinePlayer target) {
        new DuelArenaSelectMenu(target.getPlayer()).openMenu(player);
    }

    @Subcommand("accept")
    public void onAccept(Player player, @Name("player") OnlinePlayer target) {
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();
        final boolean isInArena = arenaHandler.isInArena(player);

        if (isInArena) {
            player.sendMessage(Locale.CANT_ACCEPT_REQUEST.format());
            return;
        }

        final DuelRequest duelRequest = arenaHandler.getIncomingDuelRequest(target.getPlayer().getUniqueId());

        if (duelRequest == null) {
            player.sendMessage(Locale.REQUEST_DOESNT_EXIST.format(target.getPlayer().getName()));
            return;
        }

        if (duelRequest.getArena().getState().equals(ArenaState.IN_GAME) || duelRequest.getArena().getState().equals(ArenaState.REGENERATING)) {
            player.sendMessage(Locale.ARENA_CURRENTLY_BUSY.format(duelRequest.getArena().getName()));
            return;
        }

        final Player issuer = Bukkit.getPlayer(duelRequest.getIssuer());

        if (issuer == null) {
            player.sendMessage(Locale.REQUEST_PLAYER_OFFLINE.format());
            return;
        }

        final boolean isIssuerInArena = arenaHandler.isInArena(issuer);

        if (isIssuerInArena) {
            player.sendMessage(Locale.REQUEST_PLAYER_INGAME.format());
            return;
        }

        player.sendMessage(Locale.DUEL_ACCEPTED.format(issuer.getDisplayName()));
        issuer.sendMessage(Locale.DUEL_ACCEPTED_ISSUER.format(issuer.getDisplayName()));

        duelRequest.getArena().getAllPlayerList().forEach(uuid1 -> {
            final Player player1 = Bukkit.getPlayer(uuid1);

            player1.sendMessage(Locale.KICKED_FROM_ARENA.format());

            arenaHandler.leaveGame(player1, duelRequest.getArena());
        });

        arenaHandler.addToGame(player, duelRequest.getArena());
        arenaHandler.addToGame(issuer, duelRequest.getArena());

        arenaHandler.getDuelRequests().remove(duelRequest);
    }
}
