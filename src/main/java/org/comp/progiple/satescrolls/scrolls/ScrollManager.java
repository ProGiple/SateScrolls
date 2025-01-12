package org.comp.progiple.satescrolls.scrolls;

import de.tr7zw.nbtapi.NBT;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.comp.progiple.satescrolls.Utils;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.ScrollConfig;
import org.comp.progiple.satescrolls.scrolls.types.CompletedScroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class ScrollManager {
    public String getId(ItemStack item) {
        return NBT.get(item, nbt -> (String) nbt.getString("scroll-id"));
    }

    public ScrollConfig getScrollCfg(String id) {
        return ScrollConfig.getScrollCfgMap().get(id);
    }

    public Rarity getRarity(ItemStack scroll) {
        Rarity rarity = null;
        switch (ScrollManager.getType(scroll)) {
            case 1 -> rarity = ScrollManager.getScrollCfg(ScrollManager.getId(scroll)).getRarity();
            case 2 -> rarity = Rarity.getRarityMap().get(NBT.get(scroll, nbt -> (String) nbt.getString("rarity")));
        }
        return rarity;
    }

    public void complete(Player player, ItemStack scroll) {
        Rarity rarity = ScrollManager.getRarity(scroll);
        scroll.setAmount(scroll.getAmount() - 1);
        CompletedScroll completedScroll = new CompletedScroll(Config.getSection("completedScroll"), rarity);
        completedScroll.give(player);
    }

    public int getMaxCount(ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> (Integer) nbt.getInteger("scroll-count"));
    }

    public int getNowCount(ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> (Integer) nbt.getInteger("scroll-nowCount"));
    }

    public byte getType(ItemStack itemStack) {
        return NBT.get(itemStack, nbt -> (Byte) nbt.getByte("sateScrollTypeByte"));
    }

    @SuppressWarnings("deprecation")
    public void updateLore(ItemStack scroll, int count, int nowCount) {
        List<String> lore = new ArrayList<>(ScrollManager.getScrollCfg(ScrollManager.getId(scroll))
                .getItemSection().getStringList("lore"));
        lore.replaceAll(line -> Utils.color(line
                .replace("$need_count", String.valueOf(count))
                .replace("$now_count", String.valueOf(nowCount))
                .replace("$rarity", ScrollManager.getRarity(scroll).getName())));

        ItemMeta meta = scroll.getItemMeta();
        meta.setLore(lore);
        scroll.setItemMeta(meta);
    }

    public TaskType getTaskType(ItemStack itemStack) {
        return TaskType.valueOf(Objects.requireNonNull(
                ScrollManager.getScrollCfg(ScrollManager.getId(itemStack)).getScrollSection().getString("type")).toUpperCase());
    }

    public String getAdditive(ItemStack itemStack, TaskType type) {
        ConfigurationSection section = ScrollManager.getScrollCfg(ScrollManager.getId(itemStack)).getScrollSection();
        switch (type) {
            case BREAK_ITEM, CRAFT, FURNACE_BURN_ITEM, SMITH_ITEM -> {
                return section.getString("item_material");
            }
            case GO_DISTANCE -> {
                return section.getString("distance_type");
            }
            case KILL_MOB -> {
                return section.getString("mob");
            }
        }
        return "none";
    }

    public void removeCount(ItemStack scroll, int nowCount) {
        ScrollManager.removeCount(scroll, nowCount, (byte) 1);
    }

    public void removeCount(ItemStack scroll, int nowCount, byte res) {
        ScrollManager.updateLore(scroll, ScrollManager.getMaxCount(scroll), nowCount - res);
        NBT.modify(scroll, nbt -> {
            nbt.setInteger("scroll-nowCount", nowCount - res);
        });
    }
}
