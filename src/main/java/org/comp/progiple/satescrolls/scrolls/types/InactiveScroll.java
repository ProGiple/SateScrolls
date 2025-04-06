package org.comp.progiple.satescrolls.scrolls.types;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.novasparkle.lunaspring.API.Menus.Items.NonMenuItem;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;

import java.util.UUID;

@Getter
public class InactiveScroll extends NonMenuItem implements IScroll {
    public InactiveScroll(ConfigurationSection section) {
        super(section);
        this.getItemStack().addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS);
        this.setGlowing(section.getBoolean("glowing"));

        NBTManager.setByte(this.getItemStack(), "sateScrollTypeByte", (byte) 0);
        NBTManager.setString(this.getItemStack(), "stackable",
                section.getBoolean("stackable") ? null : UUID.randomUUID().toString());
    }

    @Override
    public void give(Player player) {
        player.getInventory().addItem(this.getItemStack());
    }
}
