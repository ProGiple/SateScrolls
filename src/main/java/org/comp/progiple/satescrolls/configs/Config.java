package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.Utils;
import org.novasparkle.lunaspring.Configuration.Configuration;
import org.novasparkle.lunaspring.Configuration.IConfig;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Config {
    @Getter private static final Map<String, String> messageMap = new HashMap<>();

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
}
