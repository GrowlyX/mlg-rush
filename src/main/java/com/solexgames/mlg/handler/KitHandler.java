package com.solexgames.mlg.handler;

import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.model.Kit;
import com.solexgames.mlg.util.InventoryUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author GrowlyX
 * @since 3/23/2021
 */

@Getter
public class KitHandler {

    private final List<Kit> allKits = new ArrayList<>();

    public KitHandler() {
        this.loadKits();
    }

    private void loadKits() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("kits");

        configurationSection.getKeys(true).forEach(s -> {
            final Kit kit = new Kit(UUID.fromString(configurationSection.getString(s + ".uuid")), configurationSection.getString(s));
            final ItemStack[] itemStacks = InventoryUtil.itemStackArrayFromBase64(configurationSection.getString(s + ".inventory"));

            if (itemStacks != null) {
                kit.setItemStacks(itemStacks);
            } else {
                kit.setItemStacks(new ItemStack[]{});
            }
        });
    }

    /**
     * Filters through all available kits and finds a kit with the same name as {@param name}
     *
     * @param name Name parameter
     * @return A kit with the name {@param name}
     */
    public Kit getPathFromKitName(String name) {
        return this.allKits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters through all available kits and finds a kit with the same name as {@param name}
     *
     * @param name Name parameter
     * @return A kit with the name {@param name}
     */
    public Kit getByName(String name) {
        return this.allKits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters through all available kits and finds a kit with the same {@link UUID} as {@param uuid}
     *
     * @param uuid UUID parameter
     * @return A kit with the uuid {@param uuid}
     */
    public Kit getByUuid(UUID uuid) {
        return this.allKits.stream()
                .filter(kit -> kit.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }
}
