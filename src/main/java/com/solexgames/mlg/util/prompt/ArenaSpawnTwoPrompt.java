package com.solexgames.mlg.util.prompt;

import com.solexgames.mlg.model.Arena;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.cuboid.Cuboid;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@RequiredArgsConstructor
public class ArenaSpawnTwoPrompt extends StringPrompt {

    private final Player player;
    private final String name;
    private final int size;

    private final Location location;
    private final Location locationTwo;

    private final Location spawnOne;

    @Override
    public String getPromptText(ConversationContext context) {
        return Color.SECONDARY + "Please type " + Color.PRIMARY + "'here'" + Color.SECONDARY + " when you are at the arena " + ChatColor.RED + "Red" + Color.SECONDARY + " location! " + ChatColor.GRAY + "(" + Color.PRIMARY + "Type 'cancel' to exit at any time!" + ChatColor.GRAY + ")";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("here")) {
            final Arena arena = new Arena(UUID.randomUUID(), this.name);

            arena.setTeamSize(this.size);
            arena.setMaxPlayers(this.size + this.size);
            arena.setCuboid(new Cuboid(this.location, this.locationTwo));
            arena.setConfigPath(this.name.replace(" ", "-").toLowerCase());

            arena.setSpawnOne(this.spawnOne);
            arena.setSpawnTwo(this.player.getLocation());

            arena.saveArenaData();

            context.getForWhom().sendRawMessage(Color.PRIMARY + "Congrats!" + Color.SECONDARY + " You've just created a new arena!");

            return END_OF_CONVERSATION;
        } else {
            context.getForWhom().sendRawMessage(Color.SECONDARY + "I couldn't understand what you said.");

            return this;
        }
    }
}
