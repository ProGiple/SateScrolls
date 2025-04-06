package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.novasparkle.lunaspring.API.Configuration.IConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScrollConfig {
    @Getter private final static Map<String, ScrollConfig> scrollCfgMap = new HashMap<>();

    private final IConfig config;
    @Getter private final String id;
    public ScrollConfig(File file) {
        this.config = new IConfig(file);
        this.id = file.getName().replace(".yml", "");
        scrollCfgMap.put(this.id, this);
    }

    public Rarity getRarity() {
        return Rarity.getRarityMap().get(Objects.requireNonNull(this.config.getString("scroll.rarity")).toUpperCase());
    }

    public ConfigurationSection getItemSection() {
        return this.config.getSection("item");
    }

    public ConfigurationSection getScrollSection() {
        return this.config.getSection("scroll");
    }
}
