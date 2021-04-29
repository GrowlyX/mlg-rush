package com.solexgames.mlg.menu.button.impl;

import com.cryptomorin.xseries.XMaterial;
import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.menu.paginated.AbstractPaginatedMenu;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SelectPageButton extends Button {

    private final int page;
    private final AbstractPaginatedMenu menu;
    private final boolean current;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> stringList = new ArrayList<>();

        stringList.add(ChatColor.GRAY + "Click to switch to this menu!");

        if (this.current) {
            stringList.add("  ");
            stringList.add(ChatColor.GREEN + "This is the current page.");
        }

        return new ItemBuilder(this.current ? XMaterial.ENCHANTED_BOOK.parseMaterial() : XMaterial.BOOK.parseMaterial())
                .addLore(stringList)
                .setDisplayName(ChatColor.YELLOW + "Page " + this.page)
                .create();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        this.menu.modPage(player, this.page - this.menu.getPage());
        Button.playNeutral(player);
    }
}
