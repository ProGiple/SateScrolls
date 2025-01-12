package org.comp.progiple.satescrolls.listeners.tasks;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class SmithItemEvent implements Listener {
    @EventHandler
    public void inject(org.bukkit.event.inventory.SmithItemEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;

        if (ScrollManager.getTaskType(item) == TaskType.SMITH_ITEM) {
            ItemStack result = e.getInventory().getResult();
            if (result == null) return;

            Material material = result.getType();
            byte count = (byte) result.getAmount();
            if (Objects.equals(Material.getMaterial(ScrollManager.getAdditive(item, TaskType.SMITH_ITEM)), material)) {
                int nowCount = ScrollManager.getNowCount(item);
                if (nowCount <= count) {
                    ScrollManager.complete(player, item);
                    player.closeInventory();
                }
                else ScrollManager.removeCount(item, nowCount, count);
            }
        }
    }
}
