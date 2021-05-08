package com.solexgames.mlg.model;

import com.google.gson.annotations.SerializedName;
import com.solexgames.mlg.CorePlugin;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Layout {

    @SerializedName("boundPlayer")
    private final UUID playerUuid;

    @SerializedName("layout")
    private ItemStack[] itemStacks;

    public void setupDefaultInventory() {
        if (this.itemStacks == null) {
            this.itemStacks = CorePlugin.getInstance().getHotbarHandler().getDefaultInventory();
        }
    }

    public void applyInventory(Player player) {
        player.getInventory().clear();

        if (this.itemStacks == null) {
            this.itemStacks = CorePlugin.getInstance().getHotbarHandler().getDefaultInventory();
        }

        for (int i = 0; i <= 8; i++) {
            player.getInventory().setItem(i, this.itemStacks[i]);
        }

        player.updateInventory();
    }

    public void resetLayout() {
        this.itemStacks = CorePlugin.getInstance().getHotbarHandler().getDefaultInventory();
    }
}
