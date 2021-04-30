package com.solexgames.mlg.command;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.prompt.ArenaNamePrompt;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            CorePlugin.getInstance().getConversationFactory()
                    .withFirstPrompt(new ArenaNamePrompt((Player) sender))
                    .withLocalEcho(false)
                    .buildConversation((Player) sender)
                    .begin();
        }

        return false;
    }
}
