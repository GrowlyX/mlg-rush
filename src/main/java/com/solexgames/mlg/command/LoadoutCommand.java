package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LoadoutCommand extends BaseCommand {

    @CommandAlias("layout")
    public void execute(Player player) {
        if (CorePlugin.getInstance().getArenaHandler().getByPlayer(player) == null) {
            new LoadoutEditorMenu().openMenu(player);
        } else {
            player.sendMessage(ChatColor.RED + "You cannot edit layouts during games.");
        }
    }
}
