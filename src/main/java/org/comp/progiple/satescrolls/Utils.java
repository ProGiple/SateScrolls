package org.comp.progiple.satescrolls;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.configs.RarityConfig;
import org.comp.progiple.satescrolls.configs.ScrollConfig;

import java.io.File;
import java.util.Objects;

@UtilityClass
public class Utils {
    public String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void loadAllConfigs() {
        loadRaritiesConfigs();
        loadScrollConfigs();
        loadMessages();
    }

    public void loadScrollConfigs() {
        File scrollDir = new File("scrolls");
        System.out.println(scrollDir);
        if (scrollDir.exists()) {
            ScrollConfig.getScrollCfgMap().clear();
            for (File file : Objects.requireNonNull(scrollDir.listFiles())) {
                new ScrollConfig(file);
            }
        }
    }

    public void loadRaritiesConfigs() {
        File rarityDir = new File("rarities");
        if (rarityDir.exists()) {
            RarityConfig.getRarityCfgMap().clear();
            for (File file : Objects.requireNonNull(rarityDir.listFiles())) {
                new RarityConfig(file);
            }
        }
    }

    public void loadMessages() {
        for (String messageId : Config.getSection("messages").getKeys(false)) {
            Config.getMessageMap().put(messageId, Utils.color(Config.getString(String.format("messages.%s", messageId))));
        }
    }
}
