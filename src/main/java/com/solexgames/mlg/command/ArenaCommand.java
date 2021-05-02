package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.prompt.ArenaNamePrompt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

@CommandAlias("arena")
@CommandPermission("mlgrush.command.arena")
public class ArenaCommand extends BaseCommand {

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /arena <create|delete>");
    }

    @Subcommand("create|arenabot")
    @CommandPermission("mlgrush.command.arena.subcommand.create")
    public void arenaCreate(Player player) {
        player.sendMessage(Color.SECONDARY + "Starting " + Color.PRIMARY + "ArenaBot v1.0" + Color.SECONDARY + "...");

        CorePlugin.getInstance().getConversationFactory()
                .withFirstPrompt(new ArenaNamePrompt(player))
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    @Subcommand("delete|remove")
    @CommandPermission("mlgrush.command.arena.subcommand.delete")
    public void arenaDelete(Player player) {
        player.sendMessage(Color.SECONDARY + "Starting " + Color.PRIMARY + "ArenaBot v1.0" + Color.SECONDARY + "...");

        Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> CorePlugin.getInstance().getConversationFactory()
                .withFirstPrompt(new ArenaNamePrompt(player))
                .withLocalEcho(false)
                .buildConversation(player)
                .begin(), 40L);
    }
}
