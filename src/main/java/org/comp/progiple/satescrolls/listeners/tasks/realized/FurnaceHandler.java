package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class FurnaceHandler extends TaskListener {
    public FurnaceHandler() {
        super(TaskType.FURNACE_BURN_ITEM);
    }

    @EventHandler
    public void extractItem(FurnaceExtractEvent e) {
        Material material = e.getItemType();
        this.progress(e.getPlayer(), (scroll) -> {
            Material taskMaterial = Material.getMaterial(ScrollManager.getAdditive(scroll, this.getTaskType()));
            return taskMaterial != null && Objects.equals(taskMaterial, material);
        }, e.getItemAmount());
    }
}