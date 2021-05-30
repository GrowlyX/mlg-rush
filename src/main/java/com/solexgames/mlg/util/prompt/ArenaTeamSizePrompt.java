package com.solexgames.mlg.util.prompt;

import com.solexgames.mlg.util.CC;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Locale;
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
        return Locale.PROMPT_TEAM_SIZE.format();
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        try {
            final int size = Integer.parseInt(input);

            return new ArenaLocationOnePrompt(this.player, this.name, size);
        } catch (Exception ignored) {
            context.getForWhom().sendRawMessage(Locale.PROMPT_INVALID_INT.format());

            return this;
        }
    }
}
