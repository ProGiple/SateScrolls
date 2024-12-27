package org.comp.progiple.satescrolls.scrolls.types;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.Utils;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.ScrollConfig;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.comp.progiple.satescrolls.scrolls.ScrollType;

import java.util.*;

@Getter
public class Scroll implements IScroll {
    private final String id;
    private final ItemStack item;
    private final List<String> defaultLore;

    private final Rarity rarity;
    private final ScrollType type;
    private final int count;
    private final String additive;

    @Setter private int nowCount;

    @SuppressWarnings("deprecation")
    public Scroll(String id, ConfigurationSection itemSection, ConfigurationSection scrollSection) {
        this.id = id;
        this.rarity = Rarity.getRarityMap().get(scrollSection.getString("rarity"));
        this.type = ScrollType.valueOf(Objects.requireNonNull(scrollSection.getString("type")).toUpperCase());
        this.count = scrollSection.getInt("count");
        this.nowCount = this.count;
        switch (this.type) {
            default -> this.additive = null;
            case KILL_MOB -> this.additive = scrollSection.getString("mob");
            case GO_DISTANCE -> this.additive = scrollSection.getString("distance_type");
            case BREAK_ITEM -> this.additive = scrollSection.getString("item_material");
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
        this.updateLore();

        if (isGlowed) {
            this.item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
        }
        NBT.modify(this.item, nbt -> {
            nbt.setByte("sateScrollTypeByte", (byte) 1);
            nbt.setString("scroll-id", this.id);
            nbt.setInteger("scroll-nowCount", this.nowCount);
            nbt.setString("stackable", Config.getBool("config.mainScrollsCanStack") ? null : UUID.randomUUID().toString());
            nbt.setLong("time", this.type.equals(ScrollType.WAIT_TIME) ? System.currentTimeMillis() : 0);
        });
        SateScrolls.getIScrollSet().add(this);
    }

    public Scroll(ItemStack item) {
        this.item = item;
        this.id = NBT.get(this.item, nbt -> (String) nbt.getString("scroll-id"));
        this.nowCount = NBT.get(this.item, nbt -> (int) nbt.getInteger("scroll-nowCount"));

        ScrollConfig scrollConfig = ScrollConfig.getScrollCfgMap().get(this.id);
        ConfigurationSection scrollSection = scrollConfig.getScrollSection();
        ConfigurationSection itemSection = scrollConfig.getItemSection();
        this.defaultLore = itemSection.getStringList("lore");

        this.rarity = Rarity.getRarityMap().get(scrollSection.getString("rarity"));
        this.type = ScrollType.valueOf(Objects.requireNonNull(scrollSection.getString("type")).toUpperCase());
        this.count = scrollSection.getInt("count");
        switch (this.type) {
            default -> this.additive = null;
            case KILL_MOB -> this.additive = scrollSection.getString("mob");
            case GO_DISTANCE -> this.additive = scrollSection.getString("distance_type");
            case BREAK_ITEM -> this.additive = scrollSection.getString("item_material");
        }
        this.updateLore();
        NBT.modify(this.item, nbt -> {
            nbt.setString("stackable", Config.getBool("config.mainScrollsCanStack") ? null : UUID.randomUUID().toString());
        });
        SateScrolls.getIScrollSet().add(this);
    }
    
    @SuppressWarnings("deprecation")
    public void updateLore() {
        List<String> lore = new ArrayList<>(this.defaultLore);
        lore.forEach(line -> Utils.color(line
                .replace("$need_count", String.valueOf(this.count))
                .replace("$now_count", String.valueOf(this.nowCount))
                .replace("$rarity", this.rarity.getName())));

        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        this.item.setItemMeta(meta);
    }

    public void removeCount(int amo) {
        this.nowCount -= amo;
        this.updateLore();
        NBT.modify(this.item, nbt -> {
            nbt.setInteger("scroll-nowCount", this.nowCount);
        });
        SateScrolls.getIScrollSet().remove(this);
    }

    public void removeCount() {
        this.removeCount(1);
    }

    public void complete(Player player) {
        this.item.setAmount(this.item.getAmount() - 1);
        CompletedScroll completedScroll = new CompletedScroll(Config.getSection("completedScroll"), this.rarity);
        completedScroll.give(player);
    }

    @Override
    public void onClick(PlayerInteractEvent e) {
        e.getPlayer().sendMessage(Config.getMessageMap().get("left")
                .replace("$left", String.valueOf(this.nowCount))
                .replace("$max", String.valueOf(this.count)));
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
