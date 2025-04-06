package org.comp.progiple.satescrolls.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IScroll {
    ItemStack getItemStack();
    void give(Player player);
}
