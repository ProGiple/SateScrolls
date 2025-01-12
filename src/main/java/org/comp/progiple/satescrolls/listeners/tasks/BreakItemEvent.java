package org.comp.progiple.satescrolls.listeners.tasks;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class BreakItemEvent implements Listener {
    @EventHandler
    public void inject(PlayerItemBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;

        if (ScrollManager.getTaskType(item) == TaskType.BREAK_ITEM) {
            Material material = e.getBrokenItem().getType();
            if (Objects.equals(Material.getMaterial(ScrollManager.getAdditive(item, TaskType.BREAK_ITEM)), material)) {
                int nowCount = ScrollManager.getNowCount(item);
                if (nowCount <= 1) ScrollManager.complete(player, item);
                else ScrollManager.removeCount(item, nowCount);
            }
        }
    }
}
