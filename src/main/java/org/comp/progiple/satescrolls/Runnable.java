package org.comp.progiple.satescrolls;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class Runnable extends BukkitRunnable {
    private final Player player;

    @Override
    public void run() {
        ItemStack item = this.player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;

        int nowCount = ScrollManager.getNowCount(item);
        if (ScrollManager.getTaskType(item) == TaskType.PLAY_TIME) {
            if (nowCount <= 1) ScrollManager.complete(player, item);
            else ScrollManager.removeCount(item, nowCount);
        }
    }
}
