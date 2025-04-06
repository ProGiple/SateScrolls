package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.Rarity;
import org.novasparkle.lunaspring.API.Configuration.Configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RarityConfig {
    @Getter
    private final static Map<Rarity, RarityConfig> rarityCfgMap = new HashMap<>();

    @Getter private final Rarity rarity;
    @Getter private final String id;
    private final Configuration config;
    public RarityConfig(File file) {
        this.config = new Configuration(file);

        this.id = file.getName().replace(".yml", "").toUpperCase();
        this.rarity = new Rarity(this.id, this.getString("name"), this.getDouble("chance"));
        rarityCfgMap.put(this.rarity, this);
    }

    public String getString(String path) {
        return this.config.getString(path);
    }

    public double getDouble(String path) {
        return this.config.self().getDouble(path);
    }

    public ConfigurationSection getMenuSection() {
        return this.config.getSection("menu");
    }

    @SneakyThrows
    public void setItem(String id, ItemStack itemStack) {
        this.config.set(String.format("menu.items.loot.%s", id), itemStack);
        this.config.save();
    }
}
