package com.solexgames.mlg.menu.button.impl;

import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.menu.paginated.AbstractPaginatedMenu;
import com.solexgames.mlg.menu.impl.ViewAllPagesMenu;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class ViewPageButton extends Button {

    private final int mod;
    private final AbstractPaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (!this.hasNext(player)) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .setDurability(7)
                    .setDisplayName(" ")
                    .create();
        }

        return new ItemBuilder(this.mod > 0 ? Material.SPECKLED_MELON : Material.MELON)
                .setDisplayName((this.mod > 0 ? ChatColor.GREEN + "Next page" : ChatColor.RED + "Previous page") + ChatColor.GRAY + " (" + Color.PRIMARY + (menu.getPage() + mod) + ChatColor.GRAY + "/" + Color.PRIMARY + menu.getPages(player) + ChatColor.GRAY + ")")
                .addLore(
                        "&7Right Click to view all pages!"
                ).create();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType.equals(ClickType.RIGHT) && !this.hasNext(player)) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
        } else {
            if (this.hasNext(player)) {
                this.menu.modPage(player, this.mod);
            } else {
                this.menu.reset(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
}
