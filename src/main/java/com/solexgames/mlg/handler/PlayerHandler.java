package com.solexgames.mlg.handler;

import com.solexgames.mlg.player.GamePlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 3/23/2021
 */

@Getter
@NoArgsConstructor
public class PlayerHandler {

    private final List<GamePlayer> playerList = new ArrayList<>();

    public void setupPlayer(UUID uuid, String name) {
        final GamePlayer gamePlayer = new GamePlayer(uuid, name);

        this.playerList.add(gamePlayer);
    }

    /**
     * Filters through all available profiles and finds a profile with the same name as {@param name}
     *
     * @param name Name parameter
     * @return A profile with the name {@param name}
     */
    public GamePlayer getByName(String name) {
        return this.playerList.stream()
                .filter(gamePlayer -> gamePlayer.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters through all available profiles and finds a profile with the same {@link UUID} as {@param uuid}
     *
     * @param uuid UUID parameter
     * @return A profile with the name {@param uuid}
     */
    public GamePlayer getByUuid(UUID uuid) {
        return this.playerList.stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}
