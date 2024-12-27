package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.comp.progiple.satescrolls.SateScrolls;
import org.comp.progiple.satescrolls.Utils;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Config {
    @Getter private static final Map<String, String> messageMap = new HashMap<>();

    private FileConfiguration cfg;
    static {
        reload();
    }

    public void reload() {
        cfg = SateScrolls.getPlugin().getConfig();
    }

    public ConfigurationSection getSection(String path) {
        return cfg.getConfigurationSection(path);
    }

    public boolean getBool(String path) {
        return cfg.getBoolean(path);
    }

    public String getString(String path) {
        return cfg.getString(path);
    }
}
