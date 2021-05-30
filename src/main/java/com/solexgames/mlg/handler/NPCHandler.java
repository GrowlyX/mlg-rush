package com.solexgames.mlg.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.enums.NPCAction;
import com.solexgames.mlg.model.NPCModel;
import com.solexgames.mlg.util.Config;
import com.solexgames.mlg.util.LocationUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 5/29/2021
 */

@Getter
@NoArgsConstructor
public class NPCHandler {

    private final Map<String, NPCModel> npcModelMap = new HashMap<>();

    private NPCLib library;

    public void setupLibrary(JavaPlugin plugin) {
        this.library = new NPCLib(plugin);
    }

    /**
     * Fetch a player's skin then apply it to a target {@link GameProfile}
     * <p>
     *
     * @param name Name of the disguise profile.
     * @param uuid UUID of the target player's skin.
     */
    public Skin getSkinModel(String name, UUID uuid) {
        try {
            final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString().replace("-", "") + "?unsigned=false");
            final JsonObject json = new JsonParser().parse(new InputStreamReader(url.openStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();

            final String skin = json.get("value").getAsString();
            final String signature = json.get("signature").getAsString();

            return new Skin(skin, signature);
        } catch (Exception exception) {
            return null;
        }
    }

    public NPCModel getModelByName(String name) {
        return this.npcModelMap.getOrDefault(name, null);
    }

    public NPCModel getModelByNPC(NPC npc) {
        return this.npcModelMap.values().stream()
                .filter(npcModel -> npcModel.getNpc().equals(npc))
                .findFirst().orElse(null);
    }

    public void loadNpcModels() {
        final Config npcConfig = CorePlugin.getInstance().getConfigHandler().getNpcsConfig();
        final ConfigurationSection section = npcConfig.getConfig().getConfigurationSection("npcs");

        for (String key : section.getKeys(false)) {
            final NPCModel model = new NPCModel(key, section.getString(key + ".name"), section.getStringList(key + ".lines"));

            model.setSkin(CorePlugin.GSON.fromJson(section.getString(key + ".skin"), Skin.class));
            model.setAction(NPCAction.valueOf(section.getString(key + ".action")));
            model.setLocation(LocationUtil.getLocationFromString(section.getString(key + ".location")).orElse(null));
            model.setUpdater(section.getBoolean(key + ".updater"));

            this.npcModelMap.put(key, model);
        }
    }

    public void saveNpcModels() {
        final Config npcConfig = CorePlugin.getInstance().getConfigHandler().getNpcsConfig();

        for (NPCModel model : this.npcModelMap.values()) {
            final String endpoint = "npcs." + model.getId();

            npcConfig.getConfig().set(endpoint, null);

            npcConfig.getConfig().set(endpoint + ".skin", CorePlugin.GSON.toJson(model.getSkin()));
            npcConfig.getConfig().set(endpoint + ".name", model.getName());
            npcConfig.getConfig().set(endpoint + ".action", model.getAction().toString());
            npcConfig.getConfig().set(endpoint + ".location", LocationUtil.getStringFromLocation(model.getLocation()).orElse(null));
            npcConfig.getConfig().set(endpoint + ".lines", model.getLines());
            npcConfig.getConfig().set(endpoint + ".updater", model.isUpdater());
        }

        npcConfig.save();
    }
}
