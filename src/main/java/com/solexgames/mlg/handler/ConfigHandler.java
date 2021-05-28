package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.Config;
import lombok.Getter;

/**
 * @author puugz
 * @since 28/05/2021 22:21
 */
@Getter
public class ConfigHandler {

	private final Config scoreboardConfig;

	public ConfigHandler() {
		this.scoreboardConfig = new Config("scoreboard", CorePlugin.getInstance());
	}
}
