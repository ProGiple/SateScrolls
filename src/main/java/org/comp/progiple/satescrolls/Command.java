package org.comp.progiple.satescrolls;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.scrolls.types.InactiveScroll;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("satescrolls.admin")) {
            if (strings.length >= 1) {
                switch (strings[0]) {
                    default -> commandSender.sendMessage(Config.getMessageMap().get("unknownCommand"));
                    case "reload" -> {
                        if (strings.length >= 2) {
                            switch (strings[1]) {
                                default -> commandSender.sendMessage(Config.getMessageMap().get("noArgs"));
                                case "all" -> {
                                    Utils.loadAllConfigs();
                                    commandSender.sendMessage(Config.getMessageMap().get("reloadAll"));
                                }
                                case "messages" -> {
                                    Utils.loadMessages();
                                    commandSender.sendMessage(Config.getMessageMap().get("reloadMessages"));
                                }
                                case "scrolls" -> {
                                    Utils.loadScrollConfigs();
                                    commandSender.sendMessage(Config.getMessageMap().get("reloadScrolls"));
                                }
                                case "rarities" -> {
                                    Utils.loadRaritiesConfigs();
                                    commandSender.sendMessage(Config.getMessageMap().get("reloadRarities"));
                                }
                            }
                        }
                        else commandSender.sendMessage(Config.getMessageMap().get("noArgs"));
                    }
                    case "give" -> {
                        if (strings.length >= 3) {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(strings[1]);
                            if (offlinePlayer != null && offlinePlayer.isOnline()) {
                                Player player = (Player) offlinePlayer;
                                byte amount = Byte.parseByte(strings[2]);

                                for (byte i = 0; i < amount; i++) {
                                    InactiveScroll inactiveScroll = new InactiveScroll(Config.getSection("inactiveScroll"));
                                    inactiveScroll.give(player);
                                }
                            }
                            else commandSender.sendMessage(Config.getMessageMap().get("playerOffline"));
                        }
                        else commandSender.sendMessage(Config.getMessageMap().get("noArgs"));
                    }
                }
            }
            else commandSender.sendMessage(Config.getMessageMap().get("noArgs"));
        }
        else commandSender.sendMessage(Config.getMessageMap().get("noPerm"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.addAll(List.of("reload", "give"));
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("reload")) {
            list.addAll(List.of("all", "messages", "scrolls", "rarities"));
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("give")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
        }
        else if (strings.length == 3 && strings[0].equalsIgnoreCase("give")) {
            list.addAll(List.of("1", "2", "16", "32", "64", "<amount>"));
        }
        return list;
    }
}
