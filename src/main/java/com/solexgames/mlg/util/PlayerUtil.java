package com.solexgames.mlg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class PlayerUtil {

    /**
     * Resets player related functions
     * <p></p>
     *
     * @param player Player to reset
     */
    public static void resetPlayer(Player player) {
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);

        PlayerUtil.restorePlayer(player);
    }

    public static void restorePlayer(Player player) {
        player.getActivePotionEffects().clear();

        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0f);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);
        player.setSaturation(20);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        player.getInventory().setHeldItemSlot(0);

        player.updateInventory();
    }
}
