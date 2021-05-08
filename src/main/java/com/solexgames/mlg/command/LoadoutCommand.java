package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import org.bukkit.entity.Player;

public class LoadoutCommand extends BaseCommand {

    @CommandAlias("loadout")
    public void joinGame(Player player) {
        new LoadoutEditorMenu().openMenu(player);
    }
}
