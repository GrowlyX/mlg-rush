package com.solexgames.mlg.listener;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.model.NPCModel;
import net.jitse.npclib.api.events.NPCInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author GrowlyX
 * @since 5/29/2021
 */

public class NPCListener implements Listener {

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        final NPCModel model = CorePlugin.getInstance().getNpcHandler().getModelByNPC(event.getNPC());

        if (model != null) {
            // TODO: 5/29/2021 add logic here
        }
    }
}
