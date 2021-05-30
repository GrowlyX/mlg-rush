package com.solexgames.mlg.menu.impl;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.menu.paginated.AbstractPaginatedMenu;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DuelArenaSelectMenu extends AbstractPaginatedMenu {

    private final Player target;

    public DuelArenaSelectMenu(Player target) {
        super(45);

        this.target = target;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Select a map";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttonMap = new HashMap<>();
        final AtomicInteger atomicInteger = new AtomicInteger();

        CorePlugin.getInstance().getArenaHandler().getAllArenas().stream().filter(arena -> arena.getState().equals(ArenaState.AVAILABLE)).forEach(arena -> buttonMap.put(atomicInteger.get(), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.WOOL)
                        .setDisplayName(CC.PRIMARY + arena.getName())
                        .addLore(
                                CC.SECONDARY + "[Click to select this map]"
                        )
                        .setDurability(5)
                        .create();
            }

            @Override
            public void clicked(Player player, ClickType clickType) {
                player.closeInventory();

                CorePlugin.getInstance().getArenaHandler().sendDuelRequest(player, DuelArenaSelectMenu.this.target, arena);
            }
        }));

        return buttonMap;
    }
}
