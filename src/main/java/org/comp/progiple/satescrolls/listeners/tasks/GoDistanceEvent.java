package org.comp.progiple.satescrolls.listeners.tasks;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

public class GoDistanceEvent implements Listener {
    @EventHandler
    public void inject(PlayerMoveEvent e) {
        if (e.hasChangedPosition()) {
            Player player = e.getPlayer();
            if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) return;

            ItemStack item = player.getInventory().getItemInOffHand();
            if (item.getType() == Material.AIR) return;

            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("sateScrollTypeByte") || ScrollManager.getType(item) != 1) return;

            if (ScrollManager.getTaskType(item) == TaskType.GO_DISTANCE) {
                String type = ScrollManager.getAdditive(item, TaskType.GO_DISTANCE).toUpperCase();
                if ((type.contains("RUN") && player.isSprinting() && !player.isSwimming() && !player.isGliding()) ||
                        (type.contains("FOOT") && !player.isSprinting() && !player.isSwimming() && !player.isGliding() && !player.isSneaking()) ||
                        (type.contains("SWIM") && player.isSwimming()) ||
                        (type.contains("ELYTRA") && player.isGliding()) ||
                        (type.contains("SITTING") && player.isSneaking())) {
                    int nowCount = ScrollManager.getNowCount(item);
                    if (nowCount <= 1) ScrollManager.complete(player, item);
                    else ScrollManager.removeCount(item, nowCount);
                }
            }
        }
    }
}
