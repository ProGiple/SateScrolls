package org.comp.progiple.satescrolls;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.listeners.*;
import org.comp.progiple.satescrolls.scrolls.IScroll;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SateScrolls extends JavaPlugin {
    @Getter private final static Set<IScroll> iScrollSet = new HashSet<>();
    @Getter private static SateScrolls plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Utils.loadAllConfigs();

        if (Config.getBool("config.loadExamples")) {
            plugin.saveResource("rarities/common.yml", false);
            plugin.saveResource("scrolls/scroll1.yml", false);
            plugin.saveResource("scrolls/scroll2.yml", false);
            plugin.saveResource("scrolls/scroll3.yml", false);
            plugin.saveResource("scrolls/scroll4.yml", false);
            plugin.saveResource("scrolls/scroll5.yml", false);
            System.out.println("!!! Выключите загрузку примеров в конфиге config.yml !!!");
        }

        this.reg(new GoDistanceEvent());
        this.reg(new BreakItemEvent());
        this.reg(new JoinLeaveEvents());
        this.reg(new KillEntityEvent());
        this.reg(new InteractEvent());

        Command command = new Command();
        Objects.requireNonNull(getCommand("satescrolls")).setExecutor(command);
        Objects.requireNonNull(getCommand("satescrolls")).setTabCompleter(command);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void reg(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
