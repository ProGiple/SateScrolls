package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

import java.util.Objects;

public class BreakItemHandler extends TaskListener {
    public BreakItemHandler() {
        super(TaskType.BREAK_ITEM);
    }

    @EventHandler
    public void onBreak(PlayerItemBreakEvent e) {
        Material material = e.getBrokenItem().getType();
        this.progress(e.getPlayer(), scroll -> {
            Material taskMaterial = Material.getMaterial(ScrollManager.getAdditive(scroll, TaskType.BREAK_ITEM));
            return Objects.equals(taskMaterial, material);
        }, 1);
    }
}
