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
@SuppressWarnings("all")
public class HotbarHandler {

    private final ItemStack[] defaultInventory;

    private final ItemStack joinGameItem;
    private final ItemStack leaveGameItem;
    private final ItemStack layoutEditorItem;
    private final ItemStack startSpectateItem;
    private final ItemStack stopSpectateItem;

    private final ItemStack knockbackStick;
    private final ItemStack sandStoneStack;
    private final ItemStack pickaxe;

    private final ItemStack placeholder;

    public HotbarHandler() {
        this.joinGameItem = new ItemBuilder(Material.COMPASS)
                .setDisplayName(Color.PRIMARY + "Join an Arena")
                .create();
        this.layoutEditorItem = new ItemBuilder(Material.BOOK)
                .setDisplayName(Color.PRIMARY + "Edit layout")
                .create();
        this.startSpectateItem = new ItemBuilder(Material.ENDER_CHEST)
                .setDisplayName(Color.PRIMARY + "Spectate a match")
                .create();
        this.leaveGameItem = new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Leave Arena")
                .create();
        this.stopSpectateItem = new ItemBuilder(Material.BED)
                .setDisplayName(ChatColor.RED + ChatColor.BOLD.toString() + "Stop Spectating")
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
        this.placeholder = new ItemStack(Material.AIR);

        this.defaultInventory = new ItemStack[] {
                this.knockbackStick,
                this.placeholder,
                this.placeholder,
                this.placeholder,
                this.sandStoneStack,
                this.placeholder,
                this.placeholder,
                this.placeholder,
                this.pickaxe,
        };
    }

    public void setupLobbyHotbar(Player player) {
        player.getInventory().clear();

        player.getInventory().setItem(0, this.joinGameItem);
        player.getInventory().setItem(1, this.startSpectateItem);
        player.getInventory().setItem(8, this.layoutEditorItem);

        player.updateInventory();
    }

    public void setupArenaWaitingHotbar(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(8, this.leaveGameItem);

        player.updateInventory();
    }

    public void setupArenaInGameHotbar(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());

        try {
            gamePlayer.getLayout().applyInventory(player);
        } catch (Exception ignored) {
            player.getInventory().clear();

            for (int i = 0; i <= 8; i++) {
                final ItemStack itemStack = CorePlugin.getInstance().getHotbarHandler().getDefaultInventory()[i];

                if (itemStack == null) {
                    player.getInventory().setItem(i, this.placeholder);
                } else {
                    player.getInventory().setItem(i, CorePlugin.getInstance().getHotbarHandler().getDefaultInventory()[i]);
                }
            }

            player.updateInventory();
        }
    }

    public void setupSpectatorHotbar(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(8, this.stopSpectateItem);

        player.updateInventory();
    }
}
