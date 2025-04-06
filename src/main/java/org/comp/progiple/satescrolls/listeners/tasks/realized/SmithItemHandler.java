package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class SmithItemHandler extends TaskListener {
    public SmithItemHandler() {
        super(TaskType.SMITH_ITEM);
    }

    @EventHandler
    public void smith(SmithItemEvent e) {
        ItemStack result = e.getInventory().getResult();
        if (result == null) return;

        Material material = result.getType();
        this.progress((Player) e.getWhoClicked(), scroll -> {
            Material taskMaterial = Material.getMaterial(ScrollManager.getAdditive(scroll, this.getTaskType()));
            return taskMaterial != null && Objects.equals(taskMaterial, material);
        }, result.getAmount());
    }
}
