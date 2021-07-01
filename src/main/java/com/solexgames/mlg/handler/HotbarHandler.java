package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.builder.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@SuppressWarnings("all")
public class HotbarHandler {

    private final ItemStack[] defaultInventory;

    private ItemStack[] lobbyHotbar;
    private final Map<Integer, List<HotbarItemCommand>> hotbarCommandMap;

//    private final ItemStack joinGameItem;
//    private final ItemStack layoutEditorItem;
//    private final ItemStack startSpectateItem;
    private final ItemStack stopSpectateItem;
    private final ItemStack leaveGameItem;

    private final ItemStack knockbackStick;
    private final ItemStack sandStoneStack;
    private final ItemStack pickaxe;

    private final ItemStack placeholder;

    public HotbarHandler() {
//        this.joinGameItem = new ItemBuilder(Material.COMPASS)
//                .setDisplayName(CC.PRIMARY + "Join an Arena")
//                .create();
//        this.layoutEditorItem = new ItemBuilder(Material.BOOK)
//                .setDisplayName(CC.PRIMARY + "Edit layout")
//                .create();
//        this.startSpectateItem = new ItemBuilder(Material.ENDER_CHEST)
//                .setDisplayName(CC.PRIMARY + "Spectate a match")
//                .create();
        this.leaveGameItem = new ItemBuilder(Material.BED)
                .setDisplayName(CC.RED + ChatColor.BOLD.toString() + "Leave Arena")
                .create();
        this.stopSpectateItem = new ItemBuilder(Material.BED)
                .setDisplayName(CC.RED + ChatColor.BOLD.toString() + "Stop Spectating")
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

        this.hotbarCommandMap = new HashMap<>();

        this.loadLobbyHotbar(false);
    }

    public void loadLobbyHotbar(boolean reload) {
        this.lobbyHotbar = new ItemStack[36];
        this.hotbarCommandMap.clear();

        final FileConfiguration config = CorePlugin.getInstance().getConfigHandler().getConfig().getConfig();

        for (String key : config.getConfigurationSection("hotbar").getKeys(false)) {
            try {
                final ConfigurationSection section = config.getConfigurationSection("hotbar." + key);
                final ItemBuilder builder = new ItemBuilder(Material.valueOf(section.getString("material")));

                final int slot = section.getInt("slot") - 1;
                final List<HotbarItemCommand> commands = new ArrayList<>();

                if (section.get("display-name") != null) {
                    builder.setDisplayName(section.getString("display-name"));
                }
                if (section.get("data") != null) {
                    builder.setDurability(section.getInt("data"));
                }
                if (section.get("lore") != null) {
                    builder.addLore(section.getStringList("lore"));
                }
                if (section.get("commands") != null && section.getBoolean("commands.enabled")) {
                    section.getStringList("commands.commands").forEach(s -> {
                        commands.add(new HotbarItemCommand(s.startsWith("[CONSOLE] "), s.replace("[CONSOLE] ", "").replace("[PLAYER] ", "")));
                    });
                }

                if (!commands.isEmpty()) {
                    this.hotbarCommandMap.put(slot, commands);
                }
                this.lobbyHotbar[slot] = builder.create();

                System.out.println("[MLGRush] Loaded hotbar item with '" + key + "' key, " + commands.size() + " commands.");
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("[MLGRush] Failed to load hotbar item with '" + key + "' key.");
            }
        }

        if (reload) {
            final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (arenaHandler.isInArena(player) || arenaHandler.isSpectating(player)) continue;

                this.setupLobbyHotbar(player);
            }
        }
    }

    public void setupLobbyHotbar(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(this.lobbyHotbar);

        player.updateInventory();
    }

    public void setupArenaWaitingHotbar(Player player) {
        player.getInventory().clear();
        player.getInventory().setItem(8, this.leaveGameItem);

        player.updateInventory();
    }

    public void setupArenaInGameHotbar(Player player) {
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());

        try {
            gamePlayer.getLayout().applyInventory(player);
        } catch (Exception ignored) {
            player.getInventory().clear();

            for (int i = 0; i <= 8; i++) {
                final ItemStack itemStack = CorePlugin.getInstance().getHotbarHandler().getDefaultInventory().clone()[i];

                if (itemStack == null) {
                    player.getInventory().setItem(i, this.placeholder);
                } else {
                    player.getInventory().setItem(i, CorePlugin.getInstance().getHotbarHandler().getDefaultInventory().clone()[i]);
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

    @Getter
    @RequiredArgsConstructor
    public static class HotbarItemCommand {

        private final boolean consoleCommand;
        private final String commandLine;

        public void execute(Player player) {
            if (this.consoleCommand) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
            } else {
                player.performCommand(commandLine);
            }
        }
    }
}
