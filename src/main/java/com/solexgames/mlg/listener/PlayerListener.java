package com.solexgames.mlg.listener;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.handler.ArenaHandler;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.player.ArenaPlayer;
import com.solexgames.mlg.player.GamePlayer;
import com.solexgames.mlg.state.impl.ArenaState;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.CoreConstants;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@SuppressWarnings("all")
public class PlayerListener implements Listener {

    private final CorePlugin plugin = CorePlugin.getInstance();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPreLoginLow(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            final GamePlayer gamePlayer = this.plugin.getPlayerHandler().setupPlayer(event.getUniqueId(), event.getName());

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

        if (event.getClickedBlock() != null && event.getClickedBlock().getType() != null && event.getClickedBlock().getType().equals(Material.BED_BLOCK) && event.getAction().name().contains("RIGHT") && !event.hasBlock()) {
            event.setCancelled(true);
            return;
        }

        if (event.getAction().name().contains("RIGHT") && itemStack != null) {
            switch (itemStack.getType()) {
                case EMERALD:
                    player.sendMessage(ChatColor.RED + "Profiles are coming soon!");
                    break;
                case COMPASS:
                    new SelectGameMenu().openMenu(player);
                    break;
                case BOOK:
                    new LoadoutEditorMenu().openMenu(player);
                    break;
                case ENDER_CHEST:
                    new MatchSpectateMenu().openMenu(player);
                    break;
                case BED:
                    if (this.isSpectating(player)) {
                        this.plugin.getArenaHandler().stopSpectating(player, this.getArena(player));
                        return;
                    } else if (this.isInArena(player)) {
                        this.plugin.getArenaHandler().leaveGame(player, this.getArena(player));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        final Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent event) {
        if (!this.isInArena((Player) event.getDestination().getViewers().get(0))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
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

            if (arena.getState().equals(ArenaState.IN_GAME)) {
                if ((int) player.getLocation().getY() <= arena.getCuboid().getYMin()) {
                    if (this.isSpectating(player)) {
                        player.teleport(arena.getSpawnOne());
                        return;
                    }

                    player.teleport(arena.getSpawnFromTeam(arena.getByPlayer(player).getArenaTeam()));
                    arena.broadcastMessage(ChatColor.RED + player.getName() + Color.SECONDARY + " fell into the void!");

                    this.plugin.getHotbarHandler().setupArenaInGameHotbar(player);
                }
            } else if (arena.getState().equals(ArenaState.AVAILABLE)) {
                if ((int) player.getLocation().getY() <= arena.getCuboid().getYMin()) {
                    if (this.isSpectating(player)) {
                        player.teleport(arena.getSpawnOne());
                        return;
                    }

                    player.teleport(arena.getSpawnFromTeam(arena.getByPlayer(player).getArenaTeam()));
                }
            } else {
                player.teleport(Bukkit.getWorld("mlg").getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        final EntityDamageEvent.DamageCause cause = event.getCause();

        switch (cause) {
            case DROWNING: case FALL: case FIRE: case VOID:
            case FIRE_TICK:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (this.isInArena(player)) {
            final Arena arena = this.getArena(player);

            if (this.isSpectating(player)) {
                player.spigot().respawn();
                player.teleport(arena.getSpawnOne());
                return;
            }

            final GamePlayer gamePlayer = this.plugin.getPlayerHandler().getByName(player.getName());

            if (gamePlayer != null) {
                final ArenaPlayer arenaPlayer = arena.getByPlayer(player);

                gamePlayer.setDeaths(gamePlayer.getDeaths() + 1);
                arenaPlayer.setDeaths(arenaPlayer.getDeaths() + 1);

                player.spigot().respawn();
                player.teleport((arenaPlayer.getArenaTeam() == ArenaTeam.BLUE ? arena.getSpawnOne() : arena.getSpawnTwo()));

                this.plugin.getHotbarHandler().setupArenaInGameHotbar(player);
            }

            final Player damagingPlayer = event.getEntity().getKiller();

            if (damagingPlayer != null) {
                final GamePlayer damagingGamePlayer = this.plugin.getPlayerHandler().getByName(damagingPlayer.getName());

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
        final Location blockLocation = event.getBlock().getLocation();

        if (!this.isInArena(player)) {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        } else {
            final Arena arena = this.getArena(player);

            if (this.isSpectating(player)) {
                event.setCancelled(true);
                return;
            }

            if (!arena.getCuboid().isIn(blockLocation) || !arena.getBuildableCuboid().isIn(blockLocation)) {
                player.sendMessage(ChatColor.RED + "You cannot place blocks here.");
                event.setCancelled(true);
                return;
            }

            if (arena.getState().equals(ArenaState.IN_GAME)) {
                if (Arena.SPAWN_PROTECTION) {
                    if (arena.isCloseToSpawn(blockLocation, arena.getByPlayer(player).getArenaTeam())) {
                        event.setCancelled(true);
                        return;
                    }

                    if (arena.isCloseToSpawn(blockLocation, arena.getOpposingTeam(arena.getByPlayer(player)))) {
                        event.setCancelled(true);
                        return;
                    }
                }

                if (blockLocation.getX() == arena.getSpawnOne().getX() && blockLocation.getZ() == arena.getSpawnOne().getZ()) {
                    event.setCancelled(true);
                    return;
                }

                if (blockLocation.getX() == arena.getSpawnTwo().getX() && blockLocation.getZ() == arena.getSpawnTwo().getZ()) {
                    event.setCancelled(true);
                    return;
                }

                arena.getBlockLocationList().add(blockLocation);

                event.setCancelled(false);
            } else if (arena.getState().equals(ArenaState.AVAILABLE)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
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

            if (this.isSpectating(player)) {
                event.setCancelled(true);
                return;
            }

            if ((event.getBlock().getType().equals(Material.BED_BLOCK) || event.getBlock().getType().equals(Material.BED)) && arena.isTeamsBed(event.getBlock().getLocation(), arena.getOpposingTeam(arena.getByPlayer(player))) && arena.getState().equals(ArenaState.IN_GAME)) {
                arena.incrementPointAndStartRound(player);
            }

            if (!arena.getBlockLocationList().contains(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        } else {
            if (!player.isOp()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDisconnect(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final GamePlayer gamePlayer = this.plugin.getPlayerHandler().getByUuid(player.getUniqueId());

        if (gamePlayer != null) {
            gamePlayer.savePlayerData(true);
        }

        final Arena arena = this.getArena(player);

        if (this.isSpectating(player)) {
            this.plugin.getArenaHandler().stopSpectating(player, arena);
        }

        if (arena != null) {
            if (arena.getState().equals(ArenaState.IN_GAME)) {
                arena.end(arena.getOpponentPlayer(player));
            } else {
                this.plugin.getArenaHandler().leaveGame(player, arena);
            }
        }

        event.setQuitMessage(null);
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onConnect(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Location spawn = Bukkit.getWorld("mlg").getSpawnLocation();

        if (spawn != null) {
            player.teleport(spawn);
        }

        this.plugin.getHotbarHandler().setupLobbyHotbar(player);

        event.setJoinMessage(null);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
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

            if (event.getEntity() instanceof Player) {
                event.setDamage(0.0D);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();

        if (entity instanceof Player) {
            final Player player = (Player) event.getEntity();

            event.setDamage(0.0D);
        }
    }

    private boolean isInArena(Player player) {
        final ArenaHandler arenaHandler = this.plugin.getArenaHandler();

        return arenaHandler.isInArena(player) || arenaHandler.isSpectating(player);
    }

    private boolean isSpectating(Player player) {
        final ArenaHandler arenaHandler = this.plugin.getArenaHandler();

        return arenaHandler.isSpectating(player);
    }

    private Arena getArena(Player player) {
        final ArenaHandler arenaHandler = this.plugin.getArenaHandler();

        if (arenaHandler.getByPlayer(player) != null) {
            return arenaHandler.getByPlayer(player);
        } else if (arenaHandler.getSpectating(player) != null) {
            return arenaHandler.getSpectating(player);
        } else {
            return null;
        }
    }
}
