package org.comp.progiple.satescrolls.scrolls.completed;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class Loot {
    private final List<ItemStack> items = new ArrayList<>();
    private final byte maxLootItems;
    public Loot(ConfigurationSection lootSection, byte maxLootItems) {
        this.maxLootItems = maxLootItems;

        for (String key : lootSection.getKeys(false)) {
            ItemStack itemStack = lootSection.getItemStack(key);
            if (itemStack == null && !lootSection.getKeys(false).isEmpty()) {
                Material material = Material.STONE;
                String stringMaterial = lootSection.getString(String.format("%s.material", key));
                if (stringMaterial != null && !stringMaterial.isEmpty()) material = Material.getMaterial(stringMaterial);
                assert material != null;

                byte amount = (byte) lootSection.getInt(String.format("%s.amount", key));
                if (amount == 0) amount = 1;

                itemStack = new ItemStack(material, amount);
            }

            if (itemStack != null) this.items.add(itemStack);
        }
    }

    public void insert(Inventory inventory) {
        Random random = new Random();
        Set<Byte> bytes = new HashSet<>();
        byte attempts = 0;

        for (byte i = 0; i < this.maxLootItems; i++) {
            if (attempts >= 15 || this.items.isEmpty()) break;

            byte slot = (byte) random.nextInt(inventory.getSize());
            ItemStack itemStack = inventory.getItem(slot);
            if (bytes.contains(slot) || (itemStack != null && itemStack.getType() == Material.AIR)) {
                attempts++;
                continue;
            }

            int index = random.nextInt(this.items.size());
            ItemStack item = this.items.get(index);
            this.items.remove(index);

            inventory.setItem(slot, item);
            bytes.add(slot);
            attempts = 0;
        }
    }
}
