package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.ScrollType;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Objects;
import java.util.Optional;

public class KillEntityEvent implements Listener {
    @EventHandler
    public void inject(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        EntityType entityType = e.getEntity().getType();
        ItemStack item = killer.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || NBT.get(item, nbt -> (byte) nbt.getByte("sateScrollTypeByte")) != 1) return;

        Optional<IScroll> optional = SateScrolls.getIScrollSet().stream().filter(iScroll -> iScroll.getItem().equals(item)).findFirst();
        Scroll scroll = (Scroll) optional.orElseGet(() -> new Scroll(item));

        if (scroll.getType() == ScrollType.KILL_MOB) {
            if (Objects.equals(EntityType.valueOf(scroll.getAdditive()), entityType)) {
                if (scroll.getNowCount() == 1) scroll.complete(killer);
                else scroll.removeCount();
            }
        }
    }
}
