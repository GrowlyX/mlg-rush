package com.solexgames.mlg.util.prompt;

import com.solexgames.mlg.util.Locale;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ArenaBuildableOnePrompt extends StringPrompt {

    private final Player player;
    private final String name;
    private final int size;

    private final Location location;
    private final Location locationTwo;

    @Override
    public String getPromptText(ConversationContext context) {
        return Locale.PROMPT_BUILDABLE_ONE.format();
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (input.equalsIgnoreCase("cancel")) {
            context.getForWhom().sendRawMessage(Locale.PROMPT_CANCELLED.format());
            return END_OF_CONVERSATION;
        } else if (input.equalsIgnoreCase("here")) {
            return new ArenaBuildableTwoPrompt(this.player, this.name, this.size, this.location, this.locationTwo, this.player.getLocation());
        } else {
            context.getForWhom().sendRawMessage(Locale.PROMPT_ERROR.format());

            return this;
        }
    }
}
