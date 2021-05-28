package com.solexgames.mlg.handler;

import com.solexgames.mlg.player.GamePlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@Getter
@NoArgsConstructor
public class PlayerHandler {

    private final Map<UUID, GamePlayer> playerList = new HashMap<>();

    public GamePlayer setupPlayer(UUID uuid, String name) {
        final GamePlayer gamePlayer = new GamePlayer(uuid, name);

        this.playerList.put(uuid, gamePlayer);

        return gamePlayer;
    }

    /**
     * Filters through all available profiles and finds a profile with the same name as {@param name}
     *
     * @param name Name parameter
     * @return A profile with the name {@param name}
     */
    public GamePlayer getByName(String name) {
        final Player player = Bukkit.getPlayer(name);

        if (player == null) {
            return null;
        }

        return this.playerList.getOrDefault(player.getUniqueId(), null);
    }

    /**
     * Filters through all available profiles and finds a profile with the same {@link UUID} as {@param uuid}
     *
     * @param uuid UUID parameter
     * @return A profile with the name {@param uuid}
     */
    public GamePlayer getByUuid(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);

        if (player == null) {
            return null;
        }

        return this.playerList.getOrDefault(player.getUniqueId(), null);
    }
}
