package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Config;
import javafx.util.Pair;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author puugz
 * @since 28/05/2021 22:21
 */
@Getter
public class ConfigHandler {

	private final Map<String, Pair<String, List<String>>> scoreboardList = new HashMap<>();

	private final Config scoreboardConfig;

	public ConfigHandler() {
		this.scoreboardConfig = new Config("scoreboard", CorePlugin.getInstance());

		for (String key : this.scoreboardConfig.getConfig().getKeys(false)) {
			final ConfigurationSection section = this.scoreboardConfig.getConfig().getConfigurationSection(key);

			if (section.get("lines") == null) {
				continue;
			}
			scoreboardList.put(key, new Pair<>(Color.translate(section.getString("title")), Color.translate(section.getStringList("lines"))));
		}

		// Put all the scoreboards from the config here so it's cached
		// scoreboardList.put("lobby", this.scoreboardConfig.getStringList("..."));
	}
}
