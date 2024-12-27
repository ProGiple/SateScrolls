package org.comp.progiple.satescrolls.scrolls;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface IScroll {
    ItemStack getItem();
    void onClick(PlayerInteractEvent e);
    void give(Player player);
}
