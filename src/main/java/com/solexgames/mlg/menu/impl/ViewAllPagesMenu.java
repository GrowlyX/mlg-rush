package com.solexgames.mlg.menu.impl;

import com.solexgames.mlg.menu.button.Button;
import com.solexgames.mlg.menu.AbstractMenu;
import com.solexgames.mlg.menu.button.impl.SelectPageButton;
import com.solexgames.mlg.menu.paginated.AbstractPaginatedMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ViewAllPagesMenu extends AbstractMenu {

    public final AbstractPaginatedMenu menu;

    @Override
    public String getTitle(Player player) {
        return "Open a page...";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final HashMap<Integer, Button> buttons = new HashMap<>();

        int index = 0;
        for (int i = 1; i <= menu.getPages(player); i++) {
            buttons.put(index++, new SelectPageButton(i, this.menu, this.menu.getPage() == i));
        }

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
