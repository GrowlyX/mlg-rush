package com.solexgames.mlg.model;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.NPCAction;
import lombok.Data;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;

import java.util.List;

/**
 * @author GrowlyX
 * @since 5/29/2021
 */

@Data
public class NPCModel {

    private final String id;
    private final String name;
    private final List<String> lines;

    private Location location;
    private Skin skin;
    private NPC npc;
    private NPCAction action;

    public NPC build() {
        final NPC npc = CorePlugin.getInstance().getNpcHandler().getLibrary().createNPC(this.lines);

        npc.setSkin(this.skin);
        npc.setLocation(this.location);

        this.npc = npc;
        return this.npc;
    }
}
