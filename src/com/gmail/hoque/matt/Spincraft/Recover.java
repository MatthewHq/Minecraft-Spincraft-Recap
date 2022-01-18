/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.hoque.matt.Spincraft;

import java.io.Serializable;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Owner
 */
public class Recover implements Serializable {

    public int amount;
    public String type;
    public String name;
    public List<String> lore;
    public String enchant = null;
    public int enchantLevel;
    public String storedEnchant = null;
    public int storedEnchantLevel = 0;
    public short data;

    public Recover(ItemStack recover) {
        amount = recover.getAmount();
        type = recover.getType().toString();
        name = recover.getItemMeta().getDisplayName();
        data=recover.getData().getData();
        if (recover.getItemMeta().getEnchants().keySet().toArray().length > 0) {
            Enchantment temp = (Enchantment) recover.getItemMeta().getEnchants().keySet().toArray()[0];
            enchant = temp.getName();
            enchantLevel = recover.getItemMeta().getEnchantLevel(temp);
        }

        if (recover.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta enchMeta = (EnchantmentStorageMeta) recover.getItemMeta();
            if (enchMeta.hasStoredEnchants()) {
                Enchantment tempChant = (Enchantment) enchMeta.getStoredEnchants().keySet().toArray()[0];
                storedEnchant = tempChant.getName();
                storedEnchantLevel = enchMeta.getStoredEnchantLevel(tempChant);
            }
        }

        lore = recover.getItemMeta().getLore();
    }

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.getMaterial(type), amount,data);

        if (storedEnchant == null) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            if (enchant != null) {
                meta.addEnchant(Enchantment.getByName(enchant), enchantLevel, true);
            }
            item.setItemMeta(meta);
        } else {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.addStoredEnchant(Enchantment.getByName(storedEnchant), storedEnchantLevel, true);
            item.setItemMeta(meta);
        }

        return item;
    }
}
