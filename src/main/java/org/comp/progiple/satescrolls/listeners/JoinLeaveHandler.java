package org.comp.progiple.satescrolls.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.comp.progiple.satescrolls.Runnable;
import org.comp.progiple.satescrolls.SateScrolls;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinLeaveHandler implements Listener {
    private final Map<UUID, Integer> tasks = new HashMap<>();

    @EventHandler public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Runnable runnable = new Runnable(player);

        int task = runnable.runTaskTimer(SateScrolls.getPlugin(), 1200L, 1200L).getTaskId();
        this.tasks.put(player.getUniqueId(), task);
    }

    @EventHandler public void onLeave(PlayerQuitEvent e) {
        Bukkit.getScheduler().cancelTask(this.tasks.get(e.getPlayer().getUniqueId()));
    }
}
