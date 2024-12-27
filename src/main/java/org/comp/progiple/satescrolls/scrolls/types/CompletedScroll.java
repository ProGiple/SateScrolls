package org.comp.progiple.satescrolls.scrolls.types;

import de.tr7zw.nbtapi.NBT;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.comp.progiple.satescrolls.Utils;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.Rarity;

import java.util.List;
import java.util.UUID;

@Getter
public class CompletedScroll implements IScroll {
    private final ItemStack item;
    private final Rarity rarity;

    @SuppressWarnings("deprecation")
    public CompletedScroll(ConfigurationSection section, Rarity rarity) {
        Material material = Material.STONE;
        String stringMaterial = section.getString("material");
        if (stringMaterial != null) material = Material.getMaterial(stringMaterial);

        assert material != null;
        this.item = new ItemStack(material);
        this.rarity = rarity;

        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Utils.color(section.getString("name")));

        List<String> lore = section.getStringList("lore");
        lore.forEach(line -> Utils.color(line.replace("$rarity", this.rarity.getName())));
        meta.setLore(lore);
        this.item.setItemMeta(meta);

        if (section.getBoolean("glowing")) {
            this.item.addEnchantment(Enchantment.ARROW_FIRE, 1);
        }

        NBT.modify(this.item, nbt -> {
            nbt.setByte("sateScrollTypeByte", (byte) 2);
            nbt.setString("stackable", section.getBoolean("stackable") ? null : UUID.randomUUID().toString());
            nbt.setString("rarity", this.rarity.getId());
        });
    }

    public CompletedScroll(ItemStack item) {
        this.item = item;
        this.rarity = Rarity.getRarityMap().get(NBT.get(this.item, nbt -> (String) nbt.getString("rarity")));
        NBT.modify(this.item, nbt -> {
            nbt.setString("stackable", Config.getBool("completedScroll.stackable") ? null : UUID.randomUUID().toString());
        });
    }

    @Override
    public void onClick(PlayerInteractEvent e) {

    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
