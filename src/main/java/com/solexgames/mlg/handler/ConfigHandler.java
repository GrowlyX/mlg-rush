package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Color;
import com.solexgames.mlg.util.Config;
import com.solexgames.mlg.util.Locale;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * @author puugz
 * @since 28/05/2021 22:21
 * @revised GrowlyX
 */
@Getter
public class ConfigHandler {

	private final CorePlugin plugin = CorePlugin.getInstance();

	private final Map<String, Map.Entry<String, List<String>>> scoreboardMap = new HashMap<>();

	private final Config config;
	private final Config scoreboardConfig;
	private final Config arenasConfig;
	private final Config npcsConfig;
	private final Config langConfig;

	public ConfigHandler() {
		this.config = new Config("config", this.plugin);
		this.scoreboardConfig = new Config("scoreboard", this.plugin);
		this.arenasConfig = new Config("arenas", this.plugin);
		this.npcsConfig = new Config("npcs", this.plugin);
		this.langConfig = new Config("lang", this.plugin);

		this.loadScoreboards();
	}

	public void loadScoreboards() {
		for (String key : this.scoreboardConfig.getConfig().getKeys(false)) {
			final ConfigurationSection section = this.scoreboardConfig.getConfig().getConfigurationSection(key);

			if (section != null && section.get("lines") != null) {
				this.scoreboardMap.put(key, new AbstractMap.SimpleEntry<>(Color.translate(section.getString("title")), Color.translate(section.getStringList("lines"))));
			}
		}
	}

	public void reload() {
		this.config.reload();
		this.scoreboardConfig.reload();
		this.langConfig.reload();

		this.loadScoreboards();
	}
}
