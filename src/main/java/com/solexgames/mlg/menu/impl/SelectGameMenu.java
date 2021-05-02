package com.solexgames.mlg.menu.impl;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.menu.paginated.AbstractPaginatedMenu;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SelectGameMenu extends AbstractPaginatedMenu {

    public SelectGameMenu() {
        super(18);
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        return null;
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Available arenas";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> integerButtonMap = new HashMap<>();

        int i = 0;
        for (Arena arena : CorePlugin.getInstance().getArenaHandler().getAllArenas()) {
            integerButtonMap.put(i++, new ArenaButton(arena));
        }

        return integerButtonMap;
    }

    @RequiredArgsConstructor
    private static class ArenaButton extends Button {

        private final Arena arena;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WOOL)
                    .setDurability(this.getDurability())
                    .setDisplayName(Color.PRIMARY + ChatColor.BOLD.toString() + this.arena.getName())
                    .addLore(
                            ChatColor.GRAY + "State: " + this.getStateString(),
                            ChatColor.GRAY + "Players: " + Color.PRIMARY + this.arena.getGamePlayerList().size() + "/" + this.arena.getMaxPlayers(),
                            "",
                            this.getJoinabilityString()
                    )
                    .create();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            CorePlugin.getInstance().getArenaHandler().addToGame(player, this.arena);
            player.closeInventory();
        }

        private int getDurability() {
            switch (this.arena.getArenaState()) {
                case IN_GAME:
                    return 1;
                case AVAILABLE:
                    return 5;
                default:
                    return 14;
            }
        }

        private String getStateString() {
            switch (this.arena.getArenaState()) {
                case IN_GAME:
                    return ChatColor.YELLOW + "In-game";
                case AVAILABLE:
                    return ChatColor.GREEN + "Available";
                case REGENERATING:
                    return ChatColor.GOLD + "Regenerating";
                default:
                    return ChatColor.RED + "Offline";
            }
        }

        private String getJoinabilityString() {
            if (this.arena.getArenaState() == ArenaState.AVAILABLE) {
                return ChatColor.GREEN + "[Click to join this game]";
            }
            return ChatColor.RED + "[Cannot join right now]";
        }
    }
}
