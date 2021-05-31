package com.solexgames.mlg.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import com.solexgames.mlg.CorePlugin;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 5/31/2021
 */

@CommandAlias("build")
@CommandPermission("mlgrush.command.build")
public class BuildCommand extends BaseCommand {

    @Default
    public void execute(Player player) {
        final boolean isBuilding = CorePlugin.getInstance().getBuilderHandler().isBuilding(player);

        if (!isBuilding) {
            CorePlugin.getInstance().getBuilderHandler().addBuilder(player);
        } else {
            CorePlugin.getInstance().getBuilderHandler().removeBuilder(player);
        }

        // TODO: 5/31/2021 Move to locale
        player.sendMessage(isBuilding ? "Left build mode" : "Entered build mode");
    }
}
