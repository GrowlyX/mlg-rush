package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Config;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.prompt.ArenaNamePrompt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/1/2021
 */

@CommandAlias("arena")
@CommandPermission("mlgrush.command.arena")
public class ArenaCommand extends BaseCommand {

    @HelpCommand
    public void doHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("create|arenabot")
    @CommandPermission("mlgrush.command.arena.subcommand.create")
    public void arenaCreate(Player player) {
        player.sendMessage(Locale.STARTING_ARENA_BOT.format());

        CorePlugin.getInstance().getConversationFactory()
                .withFirstPrompt(new ArenaNamePrompt(player))
                .withLocalEcho(false)
                .buildConversation(player)
                .begin();
    }

    @Subcommand("delete")
    @CommandPermission("mlgrush.command.arena.subcommand.delete")
    public void arenaDelete(Player player, Arena arena) {
        final Config config = CorePlugin.getInstance().getConfigHandler().getArenasConfig();

        config.getConfig().set("arenas." + arena.getConfigPath(), null);
        config.save();

        if (arena.getState().equals(ArenaState.IN_GAME)) {
            arena.broadcastMessage(Locale.GAME_SHUTDOWN.format());
            arena.end(arena.getGamePlayerList().get(0));
            arena.cleanup();
        }

        player.sendMessage(Locale.ARENA_DELETED.format(arena.getName()));

        CorePlugin.getInstance().getArenaHandler().getAllArenas().remove(arena);
    }
}
