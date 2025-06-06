package org.comp.progiple.satescrolls;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.RarityConfig;
import org.comp.progiple.satescrolls.configs.ScrollConfig;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;

import java.io.File;
import java.util.Objects;

@UtilityClass
public class Utils {
    public void loadAllConfigs() {
        loadRaritiesConfigs();
        loadScrollConfigs();
        Config.reload();
    }

    public void loadScrollConfigs() {
        File scrollDir = new File(SateScrolls.getPlugin().getDataFolder(), "scrolls");
        if (scrollDir.exists() && scrollDir.isDirectory()) {
            ScrollConfig.getScrollCfgMap().clear();
            for (File file : Objects.requireNonNull(scrollDir.listFiles())) {
                if (!file.exists() || file.isDirectory()) continue;
                new ScrollConfig(file);
            }
        }
    }

    public void loadRaritiesConfigs() {
        File rarityDir = new File(SateScrolls.getPlugin().getDataFolder(), "rarities");
        if (rarityDir.exists() && rarityDir.isDirectory()) {
            RarityConfig.getRarityCfgMap().clear();
            for (File file : Objects.requireNonNull(rarityDir.listFiles())) {
                if (!file.exists() || file.isDirectory()) continue;
                new RarityConfig(file);
            }
        }
    }
}
