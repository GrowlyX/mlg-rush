package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.duel.DuelRequest;
import com.solexgames.mlg.menu.impl.DuelArenaSelectMenu;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("duel|fight|1v1")
public class DuelCommand extends BaseCommand {

    @Default
    public void onDuel(Player player, OnlinePlayer target) {
        new DuelArenaSelectMenu(target.getPlayer()).openMenu(player);
    }

    @Subcommand("accept")
    public void onAccept(Player player, String uuid) {
        UUID finalUuid;

        try {
            finalUuid = UUID.fromString(uuid);
        } catch (Exception ignored) {
            player.sendMessage(Locale.UUID_INVALID.format(uuid));
            return;
        }

        final boolean isInArena = CorePlugin.getInstance().getArenaHandler().isInArena(player);

        if (isInArena) {
            player.sendMessage(Locale.CANT_ACCEPT_REQUEST.format());
            return;
        }

        final DuelRequest duelRequest = CorePlugin.getInstance().getArenaHandler().getIncomingDuelRequest(finalUuid);

        if (duelRequest == null) {
            player.sendMessage(Locale.REQUEST_DOESNT_EXIST.format(finalUuid));
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

        final boolean isIssuerInArena = CorePlugin.getInstance().getArenaHandler().isInArena(issuer);

        if (isIssuerInArena) {
            player.sendMessage(Locale.REQUEST_PLAYER_INGAME.format());
            return;
        }

        player.sendMessage(Locale.DUEL_ACCEPTED.format(issuer.getDisplayName()));
        issuer.sendMessage(Locale.DUEL_ACCEPTED_ISSUER.format(issuer.getDisplayName()));

        duelRequest.getArena().getAllPlayerList().forEach(player1 -> {
            player1.sendMessage(Locale.KICKED_FROM_ARENA.format());

            CorePlugin.getInstance().getArenaHandler().leaveGame(player1, duelRequest.getArena());
        });

        CorePlugin.getInstance().getArenaHandler().addToGame(player, duelRequest.getArena());
        CorePlugin.getInstance().getArenaHandler().addToGame(issuer, duelRequest.getArena());

        CorePlugin.getInstance().getArenaHandler().getDuelRequests().remove(duelRequest);
    }
}
