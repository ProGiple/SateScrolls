package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.ScrollType;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Objects;
import java.util.Optional;

public class BreakItemEvent implements Listener {
    @EventHandler
    public void inject(PlayerItemBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || NBT.get(item, nbt -> (byte) nbt.getByte("sateScrollTypeByte")) != 1) return;

        Optional<IScroll> optional = SateScrolls.getIScrollSet().stream().filter(iScroll -> iScroll.getItem().equals(item)).findFirst();
        Scroll scroll = (Scroll) optional.orElseGet(() -> new Scroll(item));

        if (scroll.getType() == ScrollType.BREAK_ITEM) {
            Material material = e.getBrokenItem().getType();
            if (Objects.equals(Material.getMaterial(scroll.getAdditive()), material)) {
                if (scroll.getNowCount() == 1) scroll.complete(player);
                else scroll.removeCount();
            }
        }
    }
}
