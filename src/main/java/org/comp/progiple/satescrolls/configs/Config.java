package org.comp.progiple.satescrolls.configs;

import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.satescrolls.SateScrolls;
import org.novasparkle.lunaspring.API.Configuration.IConfig;
import org.novasparkle.lunaspring.API.Events.CooldownPrevent;

@UtilityClass
public class Config {
    private final IConfig cfg;
    static {
        cfg = new IConfig(SateScrolls.getPlugin());
    }

    public void reload() {
        cfg.reload(SateScrolls.getPlugin());
    }

    public ConfigurationSection getSection(String path) {
        return cfg.getSection(path);
    }

    public boolean getBool(String path) {
        return cfg.getBoolean(path);
    }

    public String getString(String path) {
        return cfg.getString(path);
    }

    private final CooldownPrevent<CommandSender> cd = new CooldownPrevent<>(50);
    public void sendMessage(CommandSender sender, String id, String... rpl) {
        if (!cd.isCancelled(null, sender)) cfg.sendMessage(sender, id, rpl);
    }
}
