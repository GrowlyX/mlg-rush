package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Locale;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LayoutCommand extends BaseCommand {

    @CommandAlias("layout|loadout")
    public void execute(Player player) {
        final Arena arena = CorePlugin.getInstance().getArenaHandler().getByPlayer(player);

        if (arena != null) {
            player.sendMessage(Locale.CANT_EDIT_INGAME.format());
            return;
        }

        final Arena spectating = CorePlugin.getInstance().getArenaHandler().getSpectating(player);

        if (spectating != null) {
            player.sendMessage(Locale.CANT_EDIT_SPEC.format());
            return;
        }

        new LoadoutEditorMenu().openMenu(player);
    }
}
