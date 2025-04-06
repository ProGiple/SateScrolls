package org.comp.progiple.satescrolls.listeners.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;

@AllArgsConstructor @Getter
public abstract class TaskListener implements Listener {
    private final TaskType taskType;

    public void progress(Player player, Request request, int count) {
        ItemStack item = player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR || !NBTManager.hasTag(item, "sateScrollTypeByte")
            || ScrollManager.getType(item) != 1) return;

        if (ScrollManager.getTaskType(item) == this.taskType) {
            if (request.work(item)) {
                int nowCount = ScrollManager.getNowCount(item);
                if (nowCount <= count) {
                    ScrollManager.complete(player, item);
                    player.closeInventory();
                }
                else ScrollManager.removeCount(item, nowCount, (byte) count);
            }
        }
    }

    @FunctionalInterface
    public interface Request {
        boolean work(ItemStack scroll);
    }
}
