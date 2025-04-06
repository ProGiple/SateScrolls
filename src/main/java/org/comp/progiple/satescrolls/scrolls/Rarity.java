package org.comp.progiple.satescrolls.scrolls;

import lombok.Getter;
import org.novasparkle.lunaspring.API.Util.Service.managers.ColorManager;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Rarity {
    @Getter private static final Map<String, Rarity> rarityMap = new HashMap<>();

    private final String id;
    private final String name;
    private final double chance;
    public Rarity(String id, String name, double chance) {
        this.id = id;
        this.name = ColorManager.color(name);
        this.chance = chance;
        rarityMap.put(this.id, this);
    }
}
