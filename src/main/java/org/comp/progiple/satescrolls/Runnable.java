package org.comp.progiple.satescrolls;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.comp.progiple.satescrolls.scrolls.IScroll;
import org.comp.progiple.satescrolls.scrolls.ScrollType;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;

import java.util.Optional;

@AllArgsConstructor
public class Runnable extends BukkitRunnable {
    private final Player player;

    @Override
    public void run() {
        ItemStack item = this.player.getInventory().getItemInOffHand();
        if (item.getType() == Material.AIR) return;

        ReadableNBT readableNBT = NBT.readNbt(item);
        if (!readableNBT.hasTag("sateScrollTypeByte") || NBT.get(item, nbt -> (byte) nbt.getByte("sateScrollTypeByte")) != 1) return;

        Optional<IScroll> optional = SateScrolls.getIScrollSet().stream().filter(iScroll -> iScroll.getItem().equals(item)).findFirst();
        Scroll scroll = (Scroll) optional.orElseGet(() -> new Scroll(item));

        if (scroll.getType() == ScrollType.PLAY_TIME) {
            if (scroll.getNowCount() == 1) scroll.complete(this.player);
            else scroll.removeCount();
        }
        else if (scroll.getType() == ScrollType.WAIT_TIME) {
            long starterTime = NBT.get(item, nbt -> (long) nbt.getLong("time"));
            if (starterTime == 0) return;

            if (starterTime + (scroll.getCount() * 60 * 1000L) <= System.currentTimeMillis()) scroll.complete(this.player);
            else {
                long mixTime = System.currentTimeMillis() - starterTime;
                scroll.setNowCount((int) (mixTime / (60 * 1000L)) + 1);
                scroll.removeCount();
            }
        }
    }
}
