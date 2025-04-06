package org.comp.progiple.satescrolls.listeners;

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
import org.comp.progiple.satescrolls.scrolls.ScrollManager;
import org.comp.progiple.satescrolls.scrolls.completed.Menu;
import org.comp.progiple.satescrolls.scrolls.types.Scroll;
import org.novasparkle.lunaspring.API.Menus.MenuManager;
import org.novasparkle.lunaspring.API.Util.Service.managers.NBTManager;
import org.novasparkle.lunaspring.API.Util.utilities.LunaMath;

public class InteractHandler implements Listener {
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) return;

            if (!NBTManager.hasTag(item, "sateScrollTypeByte")) return;
            switch (ScrollManager.getType(item)) {
                case 0 -> {
                    ScrollConfig targetConfig = ScrollConfig.getScrollCfgMap().values()
                            .stream()
                            .reduce((current, next) -> {
                                double randomChance = ScrollConfig.getScrollCfgMap().values().stream()
                                        .mapToDouble(cfg -> cfg.getRarity().getChance())
                                        .sum() * LunaMath.getRandom().nextDouble();

                                randomChance -= next.getRarity().getChance();
                                return (randomChance <= 0) ? next : current;
                            }).orElse(null);

                    if (targetConfig == null) return;
                    Scroll scroll = new Scroll(targetConfig.getId(), targetConfig.getItemSection(), targetConfig.getScrollSection());

                    item.setAmount(item.getAmount() - 1);
                    scroll.give(player);
                }
                case 1 -> {
                    Config.sendMessage(player, "left", String.valueOf(ScrollManager.getNowCount(item)),
                            String.valueOf(ScrollManager.getMaxCount(item)));
                }
                case 2 -> {
                    RarityConfig rarityConfig = RarityConfig.getRarityCfgMap().get(ScrollManager.getRarity(item));
                    if (rarityConfig == null) return;

                    item.setAmount(item.getAmount() - 1);
                    MenuManager.openInventory(player, new Menu(player, rarityConfig.getMenuSection()));
                }
            }
        }
    }
}
