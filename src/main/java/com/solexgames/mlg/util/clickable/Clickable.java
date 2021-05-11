package com.solexgames.mlg.util.clickable;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Clickable {

    private final List<TextComponent> components = new ArrayList<>();

    public Clickable(String msg) {
        final TextComponent message = new TextComponent(msg);

        this.components.add(message);
    }

    public Clickable(String msg, String hoverMsg, String clickString) {
        this.add(msg, hoverMsg, clickString, ClickEvent.Action.RUN_COMMAND);
    }

    public TextComponent add(String msg, String hoverMsg, String clickString, ClickEvent.Action action) {
        final TextComponent message = new TextComponent(msg);

        if (hoverMsg != null) {
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder(hoverMsg)).create()));
        }
        if (clickString != null) {
            message.setClickEvent(new ClickEvent(action, clickString));
        }

        this.components.add(message);
        return message;
    }

    public void add(String message) {
        this.components.add(new TextComponent(message));
    }

    public TextComponent[] asComponents() {
        return this.components.toArray(new TextComponent[0]);
    }
}
