package com.solexgames.mlg.listener;

import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.CoreConstants;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.player.GamePlayer;
import io.github.nosequel.scoreboard.ScoreboardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPreLoginHigh(AsyncPlayerPreLoginEvent event) {
        CorePlugin.getInstance().getPlayerHandler().setupPlayer(event.getUniqueId(), event.getName());

        event.allow();
    }

    @EventHandler(
            priority = EventPriority.LOWEST,
            ignoreCancelled = true
    )
    public void onAsyncPreLoginLow(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(event.getUniqueId());

            if (gamePlayer != null) {
                event.allow();
            } else {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, CoreConstants.PLAYER_DATA_LOAD);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItem();
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

        if (event.getAction().name().contains("RIGHT") && itemStack != null) {
            switch (itemStack.getType()) {
                case EMERALD:
                    player.sendMessage(ChatColor.RED + "not implemented but item working");
                    break;
                case COMPASS:
                    new SelectGameMenu().openMenu(player);
                    break;
                case BED:
                    if (arenaHandler.isInArena(player)) {
                        arenaHandler.leaveGame(player, arenaHandler.getByPlayer(player));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (player.hasMetadata("frozen")) {
            player.teleport(event.getFrom());
            return;
        }

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);

            if (!arena.getCuboid().isInWithMarge(player.getLocation(), 0.2)) {
                player.teleport(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (!this.isInArena(player)) {
            event.setCancelled(true);
        } else {
            final Arena arena = this.getArena(player);

            if (!arena.getCuboid().isIn(player)) {
                player.sendMessage(ChatColor.RED + "You cannot place blocks past the arena boundaries!");

                event.setCancelled(true);
            } else if (arena.getState().equals(ArenaState.IN_GAME)) {
                arena.getBlockLocationList().add(event.getBlock().getLocation());

                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryMoveItemEvent event) {
        final Player player = (Player) event.getInitiator().getViewers().get(0);

        if (!this.isInArena(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);

            if (event.getBlock().getType().equals(Material.BED_BLOCK) && arena.getState().equals(ArenaState.IN_GAME)) {
                arena.incrementPointAndStartRound(player);
            }
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDisconnect(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(player.getUniqueId());

        if (gamePlayer != null) {
            gamePlayer.savePlayerData();
        }

        final Arena arena = this.getArena(player);

        if (arena != null) {
            arena.end(arena.getByPlayer(player));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onConnect(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByUuid(event.getPlayer().getUniqueId());

        if (gamePlayer != null) {
            if (spawn != null) {
                player.teleport(spawn);
            }

            CorePlugin.getInstance().getHotbarHandler().setupLobbyHotbar(player);
        } else {
            event.getPlayer().kickPlayer(CoreConstants.PLAYER_DATA_LOAD);
        }
    }

    private boolean isInArena(Player player) {
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

        return arenaHandler.isInArena(player);
    }

    private Arena getArena(Player player) {
        final ArenaHandler arenaHandler = CorePlugin.getInstance().getArenaHandler();

        return arenaHandler.getByPlayer(player);
    }
}
