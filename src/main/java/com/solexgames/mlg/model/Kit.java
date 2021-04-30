package com.solexgames.mlg.model;

import com.google.gson.annotations.SerializedName;
import com.solexgames.mlg.CorePlugin;
import com.solexgames.mlg.util.InventoryUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
public class Kit {

    @SerializedName("_id")
    private final UUID uuid;

    private String name;
    private ItemStack[] itemStacks;
    private Material icon;

    private String permission;

    /**
     * Creates a new instance of {@link Kit}
     * <p>
     *
     * @param uuid Kit specified UUID
     * @param name Kit specified Name
     */
    public Kit(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        CorePlugin.getInstance().getKitHandler().getAllKits().add(this);
    }

    public void saveKitData() {
        final ConfigurationSection configurationSection = CorePlugin.getInstance().getConfig().getConfigurationSection("kits");

        try {
            configurationSection.set(this.name + ".uuid", this.uuid.toString());
            configurationSection.set(this.name + ".inventory", InventoryUtil.itemStackArrayToBase64(this.itemStacks));
        } catch (Exception exception) {
            CorePlugin.getInstance().getLogger().severe("[Kit] Couldn't save the kit " + this.name + ": " + exception.getMessage());
        }

        CorePlugin.getInstance().saveConfig();
    }
}
