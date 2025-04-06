package org.comp.progiple.satescrolls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.RarityConfig;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.comp.progiple.satescrolls.scrolls.types.InactiveScroll;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Command implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("satescrolls.admin")) {
            if (strings.length < 1) {
                Config.sendMessage(commandSender, "noArgs");
                return true;
            }

            switch (strings[0]) {
                default -> Config.sendMessage(commandSender, "unknownCommand");
                case "reload" -> {
                    if (strings.length < 2) {
                        Config.sendMessage(commandSender, "noArgs");
                        return true;
                    }

                    switch (strings[1]) {
                        default -> Config.sendMessage(commandSender, "noArgs");
                        case "all" -> {
                            Utils.loadAllConfigs();
                            Config.sendMessage(commandSender, "reloadAll");
                        }
                        case "scrolls" -> {
                            Utils.loadScrollConfigs();
                            Config.sendMessage(commandSender, "reloadScrolls");
                        }
                        case "rarities" -> {
                            Utils.loadRaritiesConfigs();
                            Config.sendMessage(commandSender, "reloadRarities");
                        }
                        case "config" -> {
                            Config.reload();
                            Config.sendMessage(commandSender, "reloadConfig");
                        }
                    }
                }
                case "give" -> {
                    if (strings.length < 3) {
                        Config.sendMessage(commandSender, "noArgs");
                        return true;
                    }

                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(strings[1]);
                    if (offlinePlayer == null || !offlinePlayer.isOnline()) {
                        Config.sendMessage(commandSender, "playerOffline");
                        return true;
                    }

                    Player player = (Player) offlinePlayer;
                    byte amount = Byte.parseByte(strings[2]);

                    for (byte i = 0; i < amount; i++) {
                        InactiveScroll inactiveScroll = new InactiveScroll(Config.getSection("inactiveScroll"));
                        inactiveScroll.give(player);
                    }
                }
                case "set" -> {
                    if (strings.length < 2) {
                        Config.sendMessage(commandSender, "noArgs");
                        return true;
                    }

                    Rarity rarity = Rarity.getRarityMap().get(strings[1]);
                    if (rarity == null) {
                        Config.sendMessage(commandSender, "notExists");
                        return true;
                    }

                    RarityConfig rarityConfig = RarityConfig.getRarityCfgMap().get(rarity);
                    if (rarityConfig == null) {
                        Config.sendMessage(commandSender, "notExists");
                        return true;
                    }

                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        ItemStack itemStack = player.getInventory().getItemInMainHand();
                        if (itemStack.getType() == Material.AIR) {
                            Config.sendMessage(commandSender, "itemIsAir");
                            return true;
                        }

                        rarityConfig.setItem(org.novasparkle.lunaspring.API.Util.utilities.Utils.getRKey((byte) 24), itemStack);
                        Config.sendMessage(commandSender, "setItem", itemStack.getType().name(), rarity.getName());
                    }
                }
            }
        }
        else Config.sendMessage(commandSender, "noPerm");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("reload", "give", "set");
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("reload")) {
            return List.of("all", "scrolls", "rarities", "config");
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("give")) {
            return Bukkit.getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .filter(n -> n.toUpperCase().startsWith(strings[1].toUpperCase()))
                    .collect(Collectors.toList());
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("set")) {
            return new ArrayList<>(Rarity.getRarityMap().keySet());
        }
        else if (strings.length == 3 && strings[0].equalsIgnoreCase("give")) {
            return List.of("1", "2", "16", "32", "64", "<amount>");
        }
        return List.of();
    }
}
