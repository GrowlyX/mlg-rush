package com.solexgames.mlg.command;

import com.solexgames.mlg.menu.impl.SelectGameMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            new SelectGameMenu().openMenu(((Player) sender));
        }

        return false;
    }
}
