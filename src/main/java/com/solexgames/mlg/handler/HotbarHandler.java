package com.solexgames.mlg.handler;

import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class HotbarHandler {

    private final ItemStack joinGameItem;
    private final ItemStack profileItem;
    private final ItemStack leaveGameItem;

    public HotbarHandler() {
        this.joinGameItem = new ItemBuilder(Material.COMPASS)
                .setDisplayName(Color.PRIMARY + ChatColor.BOLD.toString() + "Join an Arena" + ChatColor.GRAY + " (Right-click)")
                .create();
        this.profileItem = new ItemBuilder(Material.EMERALD)
                .setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString() + "Profile" + ChatColor.GRAY + " (Right-click)")
                .create();
        this.leaveGameItem = new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Leave Arena" + ChatColor.GRAY + " (Right-click)")
                .create();
    }

    public void setupLobbyHotbar(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(0, this.joinGameItem);
        player.getInventory().setItem(4, this.profileItem);

        player.updateInventory();
    }

    public void setupArenaWaitingHotbar(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(8, this.leaveGameItem);

        player.updateInventory();
    }
}
