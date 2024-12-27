package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.ScrollType;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Optional;

public class GoDistanceEvent implements Listener {
    @EventHandler
    public void inject(PlayerMoveEvent e) {
        if (e.hasChangedPosition()) {
            Player player = e.getPlayer();
            if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) return;
            int distance = (int) e.getFrom().distance(e.getTo());

            ItemStack item = player.getInventory().getItemInOffHand();
            if (item.getType() == Material.AIR) return;

            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("sateScrollTypeByte") || NBT.get(item, nbt -> (byte) nbt.getByte("sateScrollTypeByte")) != 1) return;

            Optional<IScroll> optional = SateScrolls.getIScrollSet().stream().filter(iScroll -> iScroll.getItem().equals(item)).findFirst();
            Scroll scroll = (Scroll) optional.orElseGet(() -> new Scroll(item));

            if (scroll.getType() == ScrollType.GO_DISTANCE) {
                String type = scroll.getAdditive().toUpperCase();
                if ((type.contains("RUN") && player.isSprinting()) ||
                        (type.contains("FOOT") && !player.isSprinting() && !player.isSwimming() && !player.isGliding() && !player.isSneaking()) ||
                        (type.contains("SWIM") && player.isSwimming()) ||
                        (type.contains("ELYTRA") && player.isGliding()) ||
                        (type.contains("SITTING") && player.isSneaking())) {
                    if (scroll.getNowCount() <= distance) scroll.complete(player);
                    else scroll.removeCount(distance);
                }
            }
        }
    }
}
