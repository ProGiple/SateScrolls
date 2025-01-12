package org.comp.progiple.satescrolls.configs;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.comp.progiple.satescrolls.scrolls.Rarity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RarityConfig {
    @Getter
    private final static Map<Rarity, RarityConfig> rarityCfgMap = new HashMap<>();

    private final Rarity rarity;
    private final File file;
    private FileConfiguration cfg;
    public RarityConfig(File file) {
        this.file = file;
        this.reload();

        String id = file.getName().replace(".yml", "").toUpperCase();
        this.rarity = new Rarity(id, this.getString("name"), this.getDouble("chance"));
        rarityCfgMap.put(this.rarity, this);
    }

    public void reload() {
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
    }

    public String getString(String path) {
        return this.cfg.getString(path);
    }

    public double getDouble(String path) {
        return this.cfg.getDouble(path);
    }

    public ConfigurationSection getMenuSection() {
        return this.cfg.getConfigurationSection("menu");
    }

    @SneakyThrows
    public void setItem(String id, ItemStack itemStack) {
        this.cfg.set(String.format("menu.items.loot.%s", id), itemStack);
        this.cfg.save(this.file);
    }
}
