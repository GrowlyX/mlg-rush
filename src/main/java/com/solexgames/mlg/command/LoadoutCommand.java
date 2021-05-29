package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import com.solexgames.mlg.state.impl.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LoadoutCommand extends BaseCommand {

    @CommandAlias("layout")
    public void execute(Player player) {
        final Arena arena = CorePlugin.getInstance().getArenaHandler().getByPlayer(player);

        if (arena != null) {
            player.sendMessage(ChatColor.RED + "You cannot edit layouts during games.");
            return;
        }

        final Arena spectating = CorePlugin.getInstance().getArenaHandler().getSpectating(player);

        if (spectating != null) {
            player.sendMessage(ChatColor.RED + "You cannot edit layouts while spectating.");
            return;
        }

        new LoadoutEditorMenu().openMenu(player);
    }
}
