package com.solexgames.mlg.util.prompt;

import com.solexgames.mlg.util.Color;
import lombok.AllArgsConstructor;
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
public class ArenaNamePrompt extends StringPrompt {

    private final Player player;

    @Override
    public String getPromptText(ConversationContext context) {
        return Color.SECONDARY + "Hello! My name is ArenaBot! What do you want the arena name to be? " + ChatColor.GRAY + "(" + Color.PRIMARY + "Type 'cancel' to exit at any time!" + ChatColor.GRAY + ")";
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        return new ArenaTeamSizePrompt(this.player, input);
    }
}
