package com.solexgames.mlg.menu.impl;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.AbstractMenu;
import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
import com.solexgames.mlg.util.builder.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class LoadoutEditorMenu extends AbstractMenu {

    private static final ItemStack RED_GLASS = new ItemBuilder(Material.STAINED_GLASS_PANE)
            .setDurability(14)
            .setDisplayName(ChatColor.RED + "Do not touch!")
            .addLore(
                    "&7You shouldn't be able",
                    "&7to add items to your",
                    "&7own inventory while",
                    "&7editing loadouts!"
            )
            .create();

    @Override
    public String getTitle(Player player) {
        return "Editing Loadout";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());
        final ItemStack[] itemStacks = gamePlayer.getLayout().getItemStacks() == null ? CorePlugin.getInstance().getHotbarHandler().getDefaultInventory().clone() : gamePlayer.getLayout().getItemStacks();

        for (int i = 0; i <= 8; i++) {
            final int finalSlot = i;

            buttonMap.put(i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    final ItemStack itemStack = itemStacks[finalSlot];

                    if (itemStack != null) {
                        return itemStacks[finalSlot];
                    } else {
                        return new ItemStack(Material.AIR);
                    }
                }
            });
        }

        return buttonMap;
    }

    @Override
    public void onOpen(Player player) {
        player.getInventory().clear();
        player.sendMessage(Locale.LAYOUT_OPEN_EDITOR.formatLinesArray());

        while (player.getInventory().firstEmpty() != -1) {
            final int firstEmpty = player.getInventory().firstEmpty();

            player.getInventory().setItem(firstEmpty, LoadoutEditorMenu.RED_GLASS);
        }
    }

    @Override
    public void onClose(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());

        for (int i = 0; i <= 8; i++) {
            gamePlayer.getLayout().getItemStacks()[i] = this.getInventory().getItem(i);
        }

        player.sendMessage(Locale.LAYOUT_MODIFIED.formatLinesArray());

        new BukkitRunnable() {
            @Override
            public void run() {
                CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
            }
        }.runTaskLater(plugin, 5L);
    }
}
