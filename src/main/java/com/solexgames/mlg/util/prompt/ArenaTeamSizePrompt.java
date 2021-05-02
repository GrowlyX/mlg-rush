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
public class ArenaTeamSizePrompt extends StringPrompt {

    private final Player player;
    private final String name;

    @Override
    public String getPromptText(ConversationContext context) {
        return Color.SECONDARY + "What do you want the arena team size to be? " + ChatColor.GRAY + "(" + Color.PRIMARY + "Type 'cancel' to exit at any time!" + ChatColor.GRAY + ")";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        try {
            final int size = Integer.parseInt(input);

            return new ArenaLocationOnePrompt(this.player, this.name, size);
        } catch (Exception ignored) {
            context.getForWhom().sendRawMessage(ChatColor.RED + "Sorry, that's not a valid integer! Try again!");

            return this;
        }
    }
}
