package org.comp.progiple.satescrolls;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;

@AllArgsConstructor
public class Runnable extends BukkitRunnable {
    private final Player player;

    @Override
    public void run() {
        ItemStack item = this.player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        if (!NBTManager.hasTag(item, "sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;
        int nowCount = ScrollManager.getNowCount(item);

        if (ScrollManager.getTaskType(item) == TaskType.PLAY_TIME) {
            if (nowCount <= 1) ScrollManager.complete(player, item);
            else ScrollManager.removeCount(item, nowCount);
        }
    }
}
