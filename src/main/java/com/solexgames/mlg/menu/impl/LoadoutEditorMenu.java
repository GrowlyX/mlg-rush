package com.solexgames.mlg.menu.impl;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.AbstractMenu;
import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class LoadoutEditorMenu extends AbstractMenu {

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

        player.sendMessage(ChatColor.GREEN + "Welcome to the loadout editor!");
        player.sendMessage(ChatColor.GRAY + " * " + Color.SECONDARY + "To save your loadout, close your inventory.");
    }

    @Override
    public void onClose(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());

        for (int i = 0; i <= 8; i++) {
            gamePlayer.getLayout().getItemStacks()[i] = this.getInventory().getItem(i);
        }

        player.sendMessage(new String[]{
                Color.SECONDARY + "You've modified your loadout!",
                ChatColor.GRAY.toString() + ChatColor.ITALIC + "If you need to reset your loadout, try " + Color.SECONDARY + "/resetloadout" + ChatColor.GRAY.toString() + ChatColor.ITALIC + "."
        });

        CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
    }
}
