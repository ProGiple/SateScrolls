package org.comp.progiple.satescrolls.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;

public class BlockPlaceHandler implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();

        if (item.getType() == block.getType()) {
            if (item.getType() == Material.AIR) return;

            if (!NBTManager.hasTag(item, "sateScrollTypeByte")) return;
            e.setCancelled(true);
        }
    }
}
