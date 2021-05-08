package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
public class HotbarHandler {

    private final ItemStack[] defaultInventory;

    private final ItemStack joinGameItem;
    private final ItemStack profileItem;
    private final ItemStack leaveGameItem;

    private final ItemStack knockbackStick;
    private final ItemStack sandStoneStack;
    private final ItemStack pickaxe;

    public HotbarHandler() {
        this.joinGameItem = new ItemBuilder(Material.COMPASS)
                .setDisplayName(Color.PRIMARY + "Join an Arena")
                .create();
        this.profileItem = new ItemBuilder(Material.EMERALD)
                .setDisplayName(Color.PRIMARY + "Profile")
                .create();
        this.leaveGameItem = new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Leave Arena")
                .create();
        this.sandStoneStack = new ItemBuilder(Material.SANDSTONE)
                .setAmount(64)
                .create();
        this.knockbackStick = new ItemBuilder(Material.STICK)
                .setEnchant(Enchantment.KNOCKBACK, 1)
                .setUnbreakable(true)
                .create();
        this.pickaxe = new ItemBuilder(Material.GOLD_PICKAXE)
                .setUnbreakable(true)
                .create();

        this.defaultInventory = new ItemStack[] {
                this.knockbackStick,
                null,
                null,
                null,
                this.sandStoneStack,
                null,
                null,
                null,
                this.pickaxe,
        };
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

    public void setupArenaInGameHotbar(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());

        gamePlayer.getLayout().applyInventory(player);
    }
}
