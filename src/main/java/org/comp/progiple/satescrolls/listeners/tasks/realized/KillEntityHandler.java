package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

public class KillEntityHandler extends TaskListener {
    public KillEntityHandler() {
        super(TaskType.KILL_MOB);
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        EntityType entityType = e.getEntity().getType();
        this.progress(killer, scroll -> {
            EntityType taskType = EntityType.valueOf(ScrollManager.getAdditive(scroll, TaskType.KILL_MOB));
            return entityType == taskType;
        }, 1);
    }
}
