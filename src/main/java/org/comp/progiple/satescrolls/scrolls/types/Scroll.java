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
import org.novasparkle.lunaspring.API.Menus.Items.NonMenuItem;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;

import java.util.*;

@Getter
public class Scroll extends NonMenuItem implements IScroll {
    private final String id;

    private final Rarity rarity;
    private final TaskType type;
    private final int count;
    private final String additive;

    @Setter private int nowCount;

    public Scroll(String id, ConfigurationSection itemSection, ConfigurationSection scrollSection) {
        super(itemSection);
        this.id = id;
        this.rarity = Rarity.getRarityMap().get(scrollSection.getString("rarity"));
        this.type = TaskType.valueOf(Objects.requireNonNull(scrollSection.getString("type")).toUpperCase());

        String strCount = scrollSection.getString("count");
        this.count = strCount == null ? 10 : (strCount.contains("-") ?
                        LunaMath.getRandomInt(Objects.requireNonNull(strCount))
                        : LunaMath.toInt(strCount));
        this.nowCount = this.count;
        switch (this.type) {
            default -> this.additive = null;
            case KILL_MOB -> this.additive = scrollSection.getString("mob");
            case GO_DISTANCE -> this.additive = scrollSection.getString("distance_type");
            case BREAK_ITEM, CRAFT, SMITH_ITEM, FURNACE_BURN_ITEM -> this.additive = scrollSection.getString("item_material");
        }

        this.setGlowing(itemSection.getBoolean("glowing"));
        this.getItemStack().addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ATTRIBUTES);

        NBTManager.setByte(this.getItemStack(), "sateScrollTypeByte", (byte) 1);
        NBTManager.setString(this.getItemStack(), "scroll-id", this.id);
        NBTManager.setInt(this.getItemStack(), "scroll-nowCount", this.nowCount);
        NBTManager.setInt(this.getItemStack(), "scroll-count", this.count);
        NBTManager.setString(this.getItemStack(), "stackable",
                Config.getBool("config.mainScrollsCanStack") ? null : UUID.randomUUID().toString());

        ScrollManager.updateLore(this.getItemStack(), this.count, this.nowCount);
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.getItemStack());
    }
}
