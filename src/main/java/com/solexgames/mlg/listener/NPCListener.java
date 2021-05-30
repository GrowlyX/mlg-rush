package com.solexgames.mlg.listener;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.menu.impl.LoadoutEditorMenu;
import com.solexgames.mlg.menu.impl.MatchSpectateMenu;
import com.solexgames.mlg.menu.impl.SelectGameMenu;
import com.solexgames.mlg.model.NPCModel;
import com.solexgames.mlg.state.impl.Arena;
import com.solexgames.mlg.util.Locale;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author GrowlyX
 * @author puugz
 * @since 5/29/2021
 */

public class NPCListener implements Listener {

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        final Player player = event.getWhoClicked();
        final NPCModel model = CorePlugin.getInstance().getNpcHandler().getModelByNPC(event.getNPC());

        if (model != null && model.getAction() != null) {
            switch (model.getAction()) {
                case JOIN_GAME:
                    new SelectGameMenu().openMenu(player);
                    break;
                case SPECTATE:
                    new MatchSpectateMenu().openMenu(player);
                    break;
                case EDIT_LAYOUT:
                    new LoadoutEditorMenu().openMenu(player);
                    break;
            }
        }
    }
}
