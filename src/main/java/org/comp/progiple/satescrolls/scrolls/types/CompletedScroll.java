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
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.novasparkle.lunaspring.API.Menus.Items.NonMenuItem;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CompletedScroll extends NonMenuItem implements IScroll {
    private final Rarity rarity;

    public CompletedScroll(ConfigurationSection section, Rarity rarity) {
        super(section);
        this.rarity = rarity;

        this.getItemStack().addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>(this.getLore());
        lore.replaceAll(line -> ColorManager.color(line.replace("$rarity", this.rarity.getName())));
        this.setLore(lore);

        this.setGlowing(section.getBoolean("glowing"));

        NBTManager.setByte(this.getItemStack(), "sateScrollTypeByte", (byte) 2);
        NBTManager.setString(this.getItemStack(), "stackable",
                section.getBoolean("stackable") ? null : UUID.randomUUID().toString());
        NBTManager.setString(this.getItemStack(), "rarity", this.rarity.getId());
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.getItemStack());
    }
}
