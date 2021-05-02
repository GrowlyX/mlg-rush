package com.solexgames.mlg.util.prompt;

import com.solexgames.mlg.util.Color;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

/**
 * @author GrowlyX
 * @since 4/30/2021
 */

@RequiredArgsConstructor
public class ArenaLocationOnePrompt extends StringPrompt {

    private final Player player;
    private final String name;
    private final int size;

    @Override
    public String getPromptText(ConversationContext context) {
        return Color.SECONDARY + "Please type " + Color.PRIMARY + "'here'" + Color.SECONDARY + " when you are at the arena minimum location! " + ChatColor.GRAY + "(" + Color.PRIMARY + "Type 'cancel' to exit at any time!" + ChatColor.GRAY + ")";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("here")) {
            return new ArenaLocationTwoPrompt(this.player, this.name, this.size, this.player.getLocation());
        } else {
            context.getForWhom().sendRawMessage(Color.SECONDARY + "I couldn't understand what you said.");

            return this;
        }
    }
}
