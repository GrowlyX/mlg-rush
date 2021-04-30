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

@RequiredArgsConstructor
public class ArenaLocationTwoPrompt extends StringPrompt {

    private final Player player;
    private final String name;
    private final int size;

    private final Location location;

    @Override
    public String getPromptText(ConversationContext context) {
        return Color.SECONDARY + "Please type " + Color.PRIMARY + "'here'" + Color.SECONDARY + " when you are at the arena maximum location! " + ChatColor.GRAY + "(" + Color.PRIMARY + "Type 'cancel' to exit at any time!" + ChatColor.GRAY + ")";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("here")) {
            context.getForWhom().sendRawMessage(Color.SECONDARY + "You've created a new arena! " + Color.PRIMARY + "Thanks for using ArenaBot!");

            final Arena arena = new Arena(UUID.randomUUID(), this.name);

            arena.setTeamSize(this.size);
            arena.setMaxPlayers(this.size + this.size);
            arena.setCuboid(new Cuboid(this.location, player.getLocation()));
            arena.setConfigPath(this.name.replace(" ", "-").toLowerCase());

            arena.saveArenaData();

            return END_OF_CONVERSATION;
        } else {
            context.getForWhom().sendRawMessage(Color.SECONDARY + "I couldn't understand what you said.");

            return this;
        }
    }
}
