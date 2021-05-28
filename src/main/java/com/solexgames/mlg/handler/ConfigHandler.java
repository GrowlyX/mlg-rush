package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Config;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author puugz
 * @since 28/05/2021 22:21
 */
@Getter
public class ConfigHandler {

	private final Map<String, List<String>> scoreboardList = new HashMap<>();

	private final Config scoreboardConfig;

	public ConfigHandler() {
		this.scoreboardConfig = new Config("scoreboard", CorePlugin.getInstance());

		// Put all the scoreboards from the config here so it's cached
		// scoreboardList.put("lobby", this.scoreboardConfig.getStringList("..."));
	}
}
