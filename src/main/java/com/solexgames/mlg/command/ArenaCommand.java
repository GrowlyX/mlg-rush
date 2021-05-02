package com.solexgames.mlg.command;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.prompt.ArenaNamePrompt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("die");
            return false;
        }

        final Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Color.SECONDARY + "Usage: " + Color.PRIMARY + "/arena " + ChatColor.WHITE + "<create|delete>");
        }
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "create":
                    player.sendMessage(Color.SECONDARY + "Starting " + Color.PRIMARY + "ArenaBot v1.0" + Color.SECONDARY + "...");

                    Bukkit.getScheduler().runTaskLater(CorePlugin.getInstance(), () -> CorePlugin.getInstance().getConversationFactory()
                            .withFirstPrompt(new ArenaNamePrompt(player))
                            .withLocalEcho(false)
                            .buildConversation(player)
                            .begin(), 40L);
                    break;
                case "delete":
                    break;
            }
        }

        return false;
    }
}
