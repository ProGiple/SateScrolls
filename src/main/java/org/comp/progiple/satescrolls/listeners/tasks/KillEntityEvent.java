package org.comp.progiple.satescrolls.listeners.tasks;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

public class KillEntityEvent implements Listener {
    @EventHandler
    public void inject(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        EntityType entityType = e.getEntity().getType();
        ItemStack item = killer.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;

        if (ScrollManager.getTaskType(item) == TaskType.KILL_MOB) {
            if (entityType == EntityType.valueOf(ScrollManager.getAdditive(item, TaskType.KILL_MOB))) {
                int nowCount = ScrollManager.getNowCount(item);
                if (nowCount <= 1) ScrollManager.complete(killer, item);
                else ScrollManager.removeCount(item, nowCount);
            }
        }
    }
}
