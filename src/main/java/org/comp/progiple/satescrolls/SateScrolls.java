package org.comp.progiple.satescrolls;

import lombok.Getter;
import org.comp.progiple.satescrolls.configs.Config;
import org.comp.progiple.satescrolls.listeners.*;
import org.comp.progiple.satescrolls.listeners.tasks.realized.*;
import org.novasparkle.lunaspring.LunaPlugin;

public final class SateScrolls extends LunaPlugin {
    @Getter private static SateScrolls plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();

        this.initialize();
        this.loadFiles(Config.getBool("config.loadExamples"),
                "rarities/common.yml", "scrolls/scroll1.yml", "scrolls/scroll2.yml",
                "scrolls/scroll3.yml", "scrolls/scroll4.yml", "scrolls/scroll5.yml",
                "scrolls/scroll6.yml", "scrolls/scroll7.yml");
        Utils.loadAllConfigs();

        this.registerListeners(new BlockPlaceHandler(), new CraftItemHandler(), new FurnaceHandler(),
                new SmithItemHandler(), new InteractHandler(), new KillEntityHandler(), new JoinLeaveHandler(),
                new BreakItemHandler(), new GoDistanceHandler());
        this.registerTabExecutor(new Command(), "satescrolls");
    }
}
