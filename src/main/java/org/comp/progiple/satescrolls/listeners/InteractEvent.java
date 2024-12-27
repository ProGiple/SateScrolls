package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.types.CompletedScroll;
import org.comp.progiple.satescrolls.scrolls.types.InactiveScroll;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Optional;

public class InteractEvent implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK ||
            e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return;

            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("sateScrollTypeByte")) return;

            Optional<IScroll> optional = SateScrolls.getIScrollSet().stream().filter(iScroll -> iScroll.getItem().equals(item)).findFirst();
            IScroll iScroll = optional.orElseGet(() -> {
                switch (NBT.get(item, nbt -> (byte) nbt.getByte("sateScrollTypeByte"))) {
                    case 0 -> {
                        return new InactiveScroll(item);
                    }
                    case 1 -> {
                        return new Scroll(item);
                    }
                    case 2 -> {
                        return new CompletedScroll(item);
                    }
                }
                return null;
            });
            if (iScroll != null) iScroll.onClick(e);
        }
    }
}
