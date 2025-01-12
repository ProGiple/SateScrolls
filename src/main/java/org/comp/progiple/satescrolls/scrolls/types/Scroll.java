package org.comp.progiple.satescrolls.scrolls.types;

import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.Utils;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.*;

@Getter
public class Scroll implements IScroll {
    private final String id;
    private final ItemStack item;
    private final List<String> defaultLore;

    private final Rarity rarity;
    private final TaskType type;
    private final int count;
    private final String additive;

    @Setter private int nowCount;

    @SuppressWarnings("deprecation")
    public Scroll(String id, ConfigurationSection itemSection, ConfigurationSection scrollSection) {
        this.id = id;
        this.rarity = Rarity.getRarityMap().get(scrollSection.getString("rarity"));
        this.type = TaskType.valueOf(Objects.requireNonNull(scrollSection.getString("type")).toUpperCase());

        String[] splitedCount = Objects.requireNonNull(scrollSection.getString("count")).split("-");
        this.count = splitedCount.length <= 1 ? Integer.parseInt(splitedCount[0]) : new Random().nextInt(
                Integer.parseInt(splitedCount[1]) - Integer.parseInt(splitedCount[0])) + Integer.parseInt(splitedCount[0]);

        this.nowCount = this.count;
        switch (this.type) {
            default -> this.additive = null;
            case KILL_MOB -> this.additive = scrollSection.getString("mob");
            case GO_DISTANCE -> this.additive = scrollSection.getString("distance_type");
            case BREAK_ITEM, CRAFT, SMITH_ITEM, FURNACE_BURN_ITEM -> this.additive = scrollSection.getString("item_material");
        }

        String stringMaterial = itemSection.getString("material");
        Material material = Material.STONE;
        if (stringMaterial != null) material = Material.getMaterial(stringMaterial);

        boolean isGlowed = itemSection.getBoolean("glowing");
        String name = Utils.color(itemSection.getString("name"));
        this.defaultLore = itemSection.getStringList("lore");

        assert material != null;
        this.item = new ItemStack(material);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES);
        this.item.setItemMeta(meta);

        if (isGlowed) {
            this.item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        }
        NBT.modify(this.item, nbt -> {
            nbt.setByte("sateScrollTypeByte", (byte) 1);
            nbt.setString("scroll-id", this.id);
            nbt.setInteger("scroll-nowCount", this.nowCount);
            nbt.setInteger("scroll-count", this.count);
            nbt.setString("stackable", Config.getBool("config.mainScrollsCanStack") ? null : UUID.randomUUID().toString());
        });

        ScrollManager.updateLore(this.item, this.count, this.nowCount);
        SateScrolls.getIScrollSet().add(this);
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
