package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.comp.progiple.satescrolls.scrolls.Rarity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScrollConfig {
    @Getter private final static Map<String, ScrollConfig> scrollCfgMap = new HashMap<>();

    private final FileConfiguration cfg;
    public ScrollConfig(File file) {
        this.cfg = YamlConfiguration.loadConfiguration(file);
        String id = file.getName().replace(".yml", "");
        scrollCfgMap.put(id, this);
    }

    public Rarity getRarity() {
        return Rarity.getRarityMap().get(Objects.requireNonNull(this.cfg.getString("scroll.rarity")).toUpperCase());
    }

    public ConfigurationSection getItemSection() {
        return this.cfg.getConfigurationSection("item");
    }

    public ConfigurationSection getScrollSection() {
        return this.cfg.getConfigurationSection("scroll");
    }
}
