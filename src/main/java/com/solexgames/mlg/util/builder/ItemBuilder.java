package com.solexgames.mlg.util.builder;

import com.cryptomorin.xseries.XMaterial;
import com.solexgames.mlg.util.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();

    private final ItemStack itemStack;
    private ItemMeta itemMeta;

    /**
     * Constructor to make a new item builder object
     *
     * @param material the type of the item
     */
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Constructor to make a new item builder object
     *
     * @param material the type of the item
     * @param data the data/durability of the material
     */
    public ItemBuilder(Material material, int data) {
        this.itemStack = new ItemStack(material, 1, (short) data);
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setOwner(String name) {
        final SkullMeta skullMeta = (SkullMeta) this.itemMeta;

        if (this.itemStack.getType().equals(XMaterial.SKELETON_SKULL.parseMaterial())) {
            skullMeta.setOwner(name);
        } else {
            return this;
        }

        this.itemMeta = skullMeta;
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        this.itemMeta.setDisplayName(Color.translate(name));
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        this.itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        final ArrayList<String> stringArrayList = new ArrayList<>();

        stringArrayList.add(Color.translate(lore));

        this.itemMeta.setLore(stringArrayList);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        this.itemMeta.setLore(Color.translate(lore));
        return this;
    }

    public ItemBuilder addLore(String... lore) {
        final List<String> strings = new ArrayList<>();

        for (String string : lore) {
            strings.add(Color.translate(string));
        }

        this.itemMeta.setLore(strings);

        return this;
    }

    public ItemBuilder setEnchant(Enchantment enchantment, int level) {
        this.enchantments.put(enchantment, level);

        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setColor(org.bukkit.Color color) {
        if (this.itemStack.getType() != null && this.itemStack.getType().name().contains("LEATHER")) {
            final LeatherArmorMeta armorMeta = (LeatherArmorMeta) this.itemMeta;

            armorMeta.setColor(color);
        }

        return this;
    }

    public ItemStack create() {
        if (this.itemMeta != null) {
            this.itemStack.setItemMeta(this.itemMeta);
        }

        if (!this.enchantments.isEmpty()) {
            this.itemStack.addEnchantments(this.enchantments);
        }

        return this.itemStack;
    }
}
