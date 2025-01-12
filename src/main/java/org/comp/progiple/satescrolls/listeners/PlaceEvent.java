package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceEvent implements Listener {
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        ItemStack item = e.getItemInHand();
        Block block = e.getBlockPlaced();

        if (item.getType() == block.getType()) {
            if (item.getType() == Material.AIR) return;

            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("sateScrollTypeByte")) return;
            e.setCancelled(true);
        }
    }
}
