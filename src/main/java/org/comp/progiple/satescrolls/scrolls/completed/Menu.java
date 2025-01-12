package org.comp.progiple.satescrolls.scrolls.completed;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.novasparkle.lunaspring.Menus.AMenu;
import org.novasparkle.lunaspring.Menus.Items.Decoration;

import java.util.Objects;

public class Menu extends AMenu {
    private final Decoration decoration;
    private final Loot loot;
    public Menu(Player player, ConfigurationSection menuSection) {
        super(player, Objects.requireNonNull(menuSection.getString("title")), (byte) (menuSection.getInt("rows") * 9));

        this.decoration = new Decoration(Objects.requireNonNull(menuSection.getConfigurationSection("items.decorative")));
        this.decoration.insert(this);

        this.loot = new Loot(Objects.requireNonNull(
                menuSection.getConfigurationSection("items.loot")), (byte) menuSection.getInt("maxLootItems"));
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.loot.insert(this.getInventory());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        ItemStack itemStack = e.getCurrentItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        if (this.decoration.checkSlot((byte) e.getSlot()) && this.decoration.checkItemStack(itemStack)) e.setCancelled(true);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        for (byte i = 0; i < this.getInventory().getSize(); i++) {
            ItemStack itemStack = e.getInventory().getItem(i);
            if (this.decoration.checkSlot(i) && this.decoration.checkItemStack(itemStack)
                || itemStack == null || itemStack.getType() == Material.AIR) continue;
            this.getPlayer().getInventory().addItem(itemStack);
        }
    }
}
