package com.solexgames.mlg.adapter;

import com.google.common.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.solexgames.mlg.CorePlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class PotionEffectTypeAdapter extends TypeAdapter<PotionEffect> {

    private static final Type seriType = new TypeToken<Map<String, Object>>() {
    }.getType();

    private static final String TYPE = "effect";
    private static final String DURATION = "duration";
    private static final String AMPLIFIER = "amplifier";
    private static final String AMBIENT = "ambient";

    @Override
    public void write(JsonWriter jsonWriter, PotionEffect potionEffect) throws IOException {
        if (potionEffect == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(getRaw(potionEffect));
    }

    @Override
    public PotionEffect read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return fromRaw(jsonReader.nextString());
    }

    private String getRaw(PotionEffect potion) {
        Map<String, Object> serial = potion.serialize();

        return CorePlugin.GSON.toJson(serial);
    }

    private PotionEffect fromRaw(String raw) {
        Map<String, Object> keys = CorePlugin.GSON.fromJson(raw, seriType);
        return new PotionEffect(PotionEffectType.getById(((Double) keys.get(TYPE)).intValue()), ((Double) keys.get(DURATION)).intValue(), ((Double) keys.get(AMPLIFIER)).intValue(), (Boolean) keys.get(AMBIENT));
    }
}
