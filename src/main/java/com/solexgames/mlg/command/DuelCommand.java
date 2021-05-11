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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@CommandAlias("duel|fight|1v1")
public class DuelCommand extends BaseCommand {

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /duel player <playerName>");
    }

    @Subcommand("accept")
    public void onAccept(Player player, String uuid) {
        UUID finalUuid;

        try {
            finalUuid = UUID.fromString(uuid);
        } catch (Exception ignored) {
            player.sendMessage(ChatColor.RED + "The UUID " + Color.SECONDARY + uuid + ChatColor.RED + " is not valid.");
            return;
        }

        final boolean isInArena = CorePlugin.getInstance().getArenaHandler().isInArena(player);

        if (isInArena) {
            player.sendMessage(ChatColor.RED + "You're currently in-game so you cannot accept this duel request.");
            return;
        }

        final DuelRequest duelRequest = CorePlugin.getInstance().getArenaHandler().getIncomingDuelRequest(finalUuid);

        if (duelRequest == null) {
            player.sendMessage(ChatColor.RED + "The duel request with the uuid " + Color.SECONDARY + finalUuid.toString() + ChatColor.RED + " does not exist.");
            return;
        }

        if (duelRequest.getArena().getState().equals(ArenaState.IN_GAME) || duelRequest.getArena().getState().equals(ArenaState.REGENERATING)) {
            player.sendMessage(ChatColor.RED + "The arena with the name " + Color.SECONDARY + duelRequest.getArena().getName() + ChatColor.RED + " is currently busy.");
            return;
        }

        final Player issuer = Bukkit.getPlayer(duelRequest.getIssuer());

        if (issuer == null) {
            player.sendMessage(ChatColor.RED + "The player who sent you that duel request is currently offline.");
            return;
        }

        final boolean isIssuerInArena = CorePlugin.getInstance().getArenaHandler().isInArena(issuer);

        if (isIssuerInArena) {
            player.sendMessage(ChatColor.RED + "The player who sent you that duel request is currently in-game.");
            return;
        }

        player.sendMessage(Color.SECONDARY + "You've accepted " + issuer.getDisplayName() + Color.SECONDARY + "'s duel request!");
        issuer.sendMessage(issuer.getDisplayName() + Color.SECONDARY + " has accepted your duel request!");

        duelRequest.getArena().getAllPlayerList().forEach(player1 -> {
            player1.sendMessage(ChatColor.RED + "You've been kicked from this arena as someone is dueling another person.");

            CorePlugin.getInstance().getArenaHandler().leaveGame(player1, duelRequest.getArena());
        });

        CorePlugin.getInstance().getArenaHandler().addToGame(player, duelRequest.getArena());
        CorePlugin.getInstance().getArenaHandler().addToGame(issuer, duelRequest.getArena());

        CorePlugin.getInstance().getArenaHandler().getDuelRequests().remove(duelRequest);
    }

    @Subcommand("player")
    public void onDuel(Player player, OnlinePlayer toDuel) {
        new DuelArenaSelectMenu(toDuel.getPlayer()).openMenu(player);
    }
}
