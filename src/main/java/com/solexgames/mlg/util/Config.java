package com.solexgames.mlg.util;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("all")
public class Config {

    private final File configFile;
    protected boolean wasCreated;
    private FileConfiguration config;

    public Config(String name, JavaPlugin plugin) {
        this.configFile = new File(plugin.getDataFolder(), name + ".yml");

        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            try {
                plugin.saveResource(this.configFile.getName(), false);
            } catch (IllegalArgumentException ex) {
                try {
                    this.configFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.wasCreated = true;
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save() {
        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getDouble(final String path) {
        if (this.config.contains(path)) {
            return this.config.getDouble(path);
        }
        return 0.0;
    }

    public float getFloat(final String path) {
        if (this.config.contains(path)) {
            return this.config.get(path) instanceof Float ? (Float) this.config.get(path) : 0.0f;
        }
        return 0.0f;
    }

    public int getInt(final String path) {
        if (this.config.contains(path)) {
            return this.config.getInt(path);
        }
        return 0;
    }

    public boolean getBoolean(final String path) {
        return this.config.contains(path) && this.config.getBoolean(path);
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public String getString(final String path) {
        return this.config.getString(path);
    }

    public String getString(final String path, final String callback) {
        return getString(path, callback, false);
    }

    public String getString(final String path, final String callback, final boolean colorize) {
        if (!this.config.contains(path)) {
            return callback;
        }
        if (colorize) {
            return ChatColor.translateAlternateColorCodes('&', this.config.getString(path));
        }
        return this.config.getString(path);
    }

    public List<String> getReversedStringList(final String path) {
        final List<String> list = this.getStringList(path);
        if (list != null) {
            final int size = list.size();
            final List<String> toReturn = new ArrayList<String>();
            for (int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }
            return toReturn;
        }
        return null;
    }

    public List<String> getStringList(final String path) {
        if (this.config.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.config.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return null;
    }

    public List<String> getStringListOrDefault(final String path, final List<String> toReturn) {
        if (this.config.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.config.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return toReturn;
    }
}
