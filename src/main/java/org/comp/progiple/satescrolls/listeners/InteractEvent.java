package org.comp.progiple.satescrolls.listeners;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadableNBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.RarityConfig;
import org.comp.progiple.satescrolls.configs.ScrollConfig;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.completed.Menu;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;
import org.novasparkle.lunaspring.Menus.MenuManager;

import java.util.Map;
import java.util.Random;

public class InteractEvent implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return;

            ReadableNBT readableNBT = NBT.readNbt(item);
            if (!readableNBT.hasTag("sateScrollTypeByte")) return;

            switch (ScrollManager.getType(item)) {
                case 0 -> {
                    ScrollConfig targetConfig = null;
                    String id = "";

                    double totalChance = ScrollConfig.getScrollCfgMap().values().stream().mapToDouble(cfg -> cfg.getRarity().getChance()).sum();
                    double randomChance = totalChance * new Random().nextDouble();
                    for (Map.Entry<String, ScrollConfig> entry : ScrollConfig.getScrollCfgMap().entrySet()) {
                        ScrollConfig config = entry.getValue();

                        Rarity rarity = config.getRarity();
                        if (targetConfig == null) targetConfig = config;

                        randomChance -= rarity.getChance();
                        if (randomChance <= 0) {
                            targetConfig = config;
                            id = entry.getKey();
                            break;
                        }
                    }

                    if (targetConfig == null) return;
                    Scroll scroll = new Scroll(id, targetConfig.getItemSection(), targetConfig.getScrollSection());
                    scroll.give(player);
                }
                case 1 -> {
                    player.sendMessage(Config.getMessageMap().get("left")
                            .replace("$left", String.valueOf(ScrollManager.getNowCount(item)))
                            .replace("$max", String.valueOf(ScrollManager.getMaxCount(item))));
                    return;
                }
                case 2 -> {
                    RarityConfig rarityConfig = RarityConfig.getRarityCfgMap().get(ScrollManager.getRarity(item));
                    if (rarityConfig == null) return;

                    MenuManager.openInventory(player, new Menu(player, rarityConfig.getMenuSection()));
                }
            }
            item.setAmount(item.getAmount() - 1);
        }
    }
}
