package com.solexgames.mlg.listener;

import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

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
            if (itemStack.getType().equals(Material.BED_BLOCK)) {
                event.setCancelled(true);
                return;
            }

            switch (itemStack.getType()) {
                case EMERALD:
                    player.sendMessage(ChatColor.RED + "Profiles are coming soon!");
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

            if ((int) player.getLocation().getY() <= arena.getCuboid().getYMin()) {
                final PlayerDeathEvent playerDeathEvent = new PlayerDeathEvent(player, new ArrayList<>(), 0, "");
                Bukkit.getPluginManager().callEvent(playerDeathEvent);
                return;
            }

            if (!arena.getCuboid().isInWithMarge(player.getLocation(), 0.2)) {
                player.teleport(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);
            final GamePlayer gamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(player.getName());

            if (gamePlayer != null) {
                final ArenaPlayer arenaPlayer = arena.getByPlayer(player);

                gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
                arenaPlayer.setDeaths(arenaPlayer.getDeaths() + 1);

                player.spigot().respawn();
                player.teleport((arenaPlayer.getArenaTeam() == ArenaTeam.BLUE ? arena.getSpawnOne() : arena.getSpawnTwo()));
                player.getInventory().setArmorContents((arenaPlayer.getArenaTeam() == ArenaTeam.BLUE ? Arena.BLUE_ITEM_STACK_ARRAY : Arena.RED_ITEM_STACK_ARRAY));

                CorePlugin.getInstance().getHotbarHandler().setupArenaInGameHotbar(player);
            }

            final Player damagingPlayer = event.getEntity().getKiller();

            if (damagingPlayer != null) {
                final GamePlayer damagingGamePlayer = CorePlugin.getInstance().getPlayerHandler().getByName(damagingPlayer.getName());

                if (damagingGamePlayer != null) {
                    final ArenaPlayer damagingArenaPlayer = arena.getByPlayer(damagingPlayer);

                    damagingGamePlayer.setKills(damagingGamePlayer.getKills() + 1);
                    damagingArenaPlayer.setKills(damagingArenaPlayer.getDeaths() + 1);
                }

                arena.broadcastMessage(ChatColor.RED + player.getName() + Color.SECONDARY + " was killed by " + ChatColor.GREEN + damagingPlayer.getName() + Color.SECONDARY + "!");
            }
        }

        event.setKeepInventory(true);
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (!this.isInArena(player)) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        } else {
            final Arena arena = this.getArena(player);

            if (!arena.getCuboid().isIn(player)) {
                player.sendMessage(ChatColor.RED + "You cannot place blocks past the arena boundaries!");

                event.setCancelled(true);
            } else if (arena.getState().equals(ArenaState.IN_GAME)) {
                if (arena.isCloseToSpawn(event.getBlock().getLocation(), arena.getByPlayer(player).getArenaTeam())) {
                    event.setCancelled(true);
                    return;
                }

                if (arena.isCloseToSpawn(event.getBlock().getLocation(), arena.getOpposingTeam(arena.getByPlayer(player)))) {
                    event.setCancelled(true);
                    return;
                }

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

            if (event.getBlock().getType().equals(Material.BED_BLOCK) && arena.isTeamsBed(event.getBlock().getLocation(), arena.getOpposingTeam(arena.getByPlayer(player))) && arena.getState().equals(ArenaState.IN_GAME)) {
                arena.incrementPointAndStartRound(player);
            }

            event.setCancelled(true);
        } else {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
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

        event.setQuitMessage(null);
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

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        final Entity entity = event.getDamager();

        if (entity instanceof Player) {
            final Player player = (Player) event.getDamager();

            if (this.isInArena(player)) {
                final Arena arena = this.getArena(player);

                if (arena.getState().equals(ArenaState.AVAILABLE)) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }
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
