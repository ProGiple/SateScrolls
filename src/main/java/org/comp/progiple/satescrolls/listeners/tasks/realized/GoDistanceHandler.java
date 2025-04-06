package org.comp.progiple.satescrolls.listeners.tasks.realized;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.comp.progiple.satescrolls.listeners.tasks.TaskListener;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.TaskType;

public class GoDistanceHandler extends TaskListener {
    public GoDistanceHandler() {
        super(TaskType.GO_DISTANCE);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.hasChangedPosition()) {
            if (!e.getFrom().getWorld().equals(e.getTo().getWorld())) return;

            Player player = e.getPlayer();
            this.progress(player, scroll -> {
                String type = ScrollManager.getAdditive(scroll, this.getTaskType()).toUpperCase();
                return (type.contains("RUN") && player.isSprinting()
                        && !player.isSwimming() && !player.isGliding()) ||
                        (type.contains("FOOT") && !player.isSprinting()
                                && !player.isSwimming()
                                && !player.isGliding()
                                && !player.isSneaking()) ||
                        (type.contains("SWIM") && player.isSwimming()) ||
                        (type.contains("ELYTRA") && player.isGliding()) ||
                        (type.contains("SITTING") && player.isSneaking());
            }, 1);
        }
    }
}
