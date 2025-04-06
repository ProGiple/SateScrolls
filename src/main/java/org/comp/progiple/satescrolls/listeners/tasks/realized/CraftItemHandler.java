package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class CraftItemHandler extends TaskListener {
    public CraftItemHandler() {
        super(TaskType.CRAFT);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        ItemStack result = e.getInventory().getResult();
        if (result == null) return;

        Player player = (Player) e.getWhoClicked();
        ClickType clickType = e.getClick();

        Material material = result.getType();
        this.progress(player, scroll -> {
            if (clickType.isShiftClick()) {
                e.setCancelled(true);
                Config.sendMessage(player, "craftingWithShift");
                return false;
            }

            Material taskMaterial = Material.getMaterial(ScrollManager.getAdditive(scroll, TaskType.CRAFT));
            return taskMaterial != null && Objects.equals(taskMaterial, material);
        }, result.getAmount());
    }
}
