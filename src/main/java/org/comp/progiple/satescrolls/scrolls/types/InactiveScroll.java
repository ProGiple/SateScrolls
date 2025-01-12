package org.comp.progiple.satescrolls.scrolls.types;

import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.Utils;
import org.comp.progiple.satescrolls.scrolls.IScroll;

import java.util.List;
import java.util.UUID;

@Getter
public class InactiveScroll implements IScroll {
    private final ItemStack item;

    @SuppressWarnings("deprecation")
    public InactiveScroll(ConfigurationSection section) {
        Material material = Material.STONE;
        String stringMaterial = section.getString("material");
        if (stringMaterial != null) material = Material.getMaterial(stringMaterial);

        assert material != null;
        this.item = new ItemStack(material);

        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Utils.color(section.getString("name")));

        List<String> lore = section.getStringList("lore");
        lore.replaceAll(Utils::color);
        meta.setLore(lore);
        this.item.setItemMeta(meta);

        if (section.getBoolean("glowing")) {
            this.item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        }

        NBT.modify(this.item, nbt -> {
            nbt.setByte("sateScrollTypeByte", (byte) 0);
            nbt.setString("stackable", section.getBoolean("stackable") ? null : UUID.randomUUID().toString());
        });
        SateScrolls.getIScrollSet().add(this);
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
