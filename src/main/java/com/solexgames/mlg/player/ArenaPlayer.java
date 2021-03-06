package com.solexgames.mlg.player;

import com.solexgames.mlg.enums.ArenaTeam;
import com.solexgames.mlg.state.impl.Arena;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * Temporary memory stored player profile object with game details
 */

@Getter
@Setter
@RequiredArgsConstructor
public class ArenaPlayer {

    private final Arena arena;
    private final ArenaTeam arenaTeam;

    private final Player player;

    private int points;
    private int kills;
    private int deaths;

}
