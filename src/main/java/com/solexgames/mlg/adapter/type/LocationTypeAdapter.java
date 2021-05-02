package com.solexgames.mlg.adapter.type;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.solexgames.mlg.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * https://bukkit.org/threads/gsonfactory-gson-that-works-on-itemstack-potioneffect-location-objects.331161/
 */

public class LocationTypeAdapter extends TypeAdapter<Location> {

    private final Type typeToken = new TypeToken<Map<String, Object>>() {}.getType();

    private final String uuidPath = "uuid";
    private final String xPath = "x";
    private final String yPath = "y";
    private final String zPath = "z";
    private final String yawPath = "yaw";
    private final String pitchPath = "pitch";

    @Override
    public void write(JsonWriter jsonWriter, Location location) throws IOException {
        if (location == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(this.getRaw(location));
    }

    @Override
    public Location read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        return this.fromRaw(jsonReader.nextString());
    }

    private String getRaw(Location location) {
        final Map<String, Object> serial = new HashMap<>();

        serial.put(this.uuidPath, location.getWorld().getUID().toString());
        serial.put(this.xPath, Double.toString(location.getX()));
        serial.put(this.yPath, Double.toString(location.getY()));
        serial.put(this.zPath, Double.toString(location.getZ()));
        serial.put(this.yawPath, Float.toString(location.getYaw()));
        serial.put(this.pitchPath, Float.toString(location.getPitch()));

        return CorePlugin.GSON.toJson(serial);
    }

    private Location fromRaw(String raw) {
        final Map<String, Object> keys = CorePlugin.GSON.fromJson(raw, this.typeToken);
        final World world = Bukkit.getWorld(java.util.UUID.fromString((String) keys.get(this.uuidPath)));

        return new Location(world, Double.parseDouble((String) keys.get(this.xPath)), Double.parseDouble((String) keys.get(this.yPath)), Double.parseDouble((String) keys.get(this.zPath)), Float.parseFloat((String) keys.get(this.yawPath)), Float.parseFloat((String) keys.get(this.pitchPath)));
    }
}
