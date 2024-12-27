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
import org.comp.progiple.satescrolls.configs.ScrollConfig;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.Rarity;

import java.util.List;
import java.util.Map;
import java.util.Random;
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
            this.item.addEnchantment(Enchantment.ARROW_FIRE, 1);
        }

        NBT.modify(this.item, nbt -> {
            nbt.setByte("sateScrollTypeByte", (byte) 0);
            nbt.setString("stackable", section.getBoolean("stackable") ? null : UUID.randomUUID().toString());
        });
    }

    public InactiveScroll(ItemStack item) {
        this.item = item;
        NBT.modify(this.item, nbt -> {
            nbt.setString("stackable", Config.getBool("inactiveScroll.stackable") ? null : UUID.randomUUID().toString());
        });
    }

    @Override
    public void onClick(PlayerInteractEvent e) {
        ScrollConfig targetConfig = null;
        String id = "";

        double totalChance = ScrollConfig.getScrollCfgMap().values().stream().mapToDouble(cfg -> cfg.getRarity().getChance()).sum();
        double randomChance = totalChance * new Random().nextDouble();
        for (Map.Entry<String, ScrollConfig> entry : ScrollConfig.getScrollCfgMap().entrySet()) {
            ScrollConfig config = entry.getValue();

            Rarity rarity = config.getRarity();
            if (targetConfig == null) targetConfig = config;

            randomChance -= rarity.getChance();
            if (randomChance <= 0) {
                targetConfig = config;
                id = entry.getKey();
                break;
            }
        }

        if (targetConfig == null) return;
        Scroll scroll = new Scroll(id, targetConfig.getItemSection(), targetConfig.getScrollSection());
        scroll.give(e.getPlayer());
        this.item.setAmount(this.item.getAmount() - 1);
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.item);
    }
}
