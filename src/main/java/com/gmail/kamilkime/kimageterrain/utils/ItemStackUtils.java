package com.gmail.kamilkime.kimageterrain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtils {

	public static ItemStack create(Material m, int amount) {
		return new ItemStack(m, amount);
	}
	
	public static ItemStack create(Material m, int amount, short data) {
		return new ItemStack(m, amount, data);
	}
	
	public static ItemStack create(Material m, int amount, short data, String name) {
		ItemStack i = new ItemStack(m, amount, data);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}
	
	public static ItemStack create(Material m, int amount, short data, List<String> lore) {
		ItemStack i = new ItemStack(m, amount, data);
		ItemMeta im = i.getItemMeta();
		im.setLore(lore);
		i.setItemMeta(im);
		return i;
	}
	
	public static ItemStack create(Material m, int amount, short data, String name, List<String> lore) {
		ItemStack i = new ItemStack(m, amount, data);
		ItemMeta im = i.getItemMeta();
		im.setLore(lore);
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}
	
	public static void name(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
	}
	
	public static void lore(ItemStack is, List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
	}
	
	public static void enchant(ItemStack is, Enchantment e, int lvl) {
		is.addUnsafeEnchantment(e, lvl);
	}
	
	public static void enchant(ItemStack is, List<Enchantment> e, int lvl) {
		for(Enchantment en : e) is.addUnsafeEnchantment(en, lvl);
	}
	
	public static void enchant(ItemStack is, HashMap<Enchantment, Integer> enchs) {
		for(Enchantment e : enchs.keySet()) is.addUnsafeEnchantment(e, enchs.get(e));
	}
	
	public static void addLoreLine(ItemStack is, String line) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = (im.getLore() == null ? new ArrayList<String>() : im.getLore());
		lore.add(line);
		im.setLore(lore);
		is.setItemMeta(im);
	}
	
	public static void changeLoreLine(ItemStack is, int lineNum, String newLine) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = (im.getLore() == null ? new ArrayList<String>() : im.getLore());
		lore.set(lineNum, newLine);
		im.setLore(lore);
		is.setItemMeta(im);
	}
	
	public static void clearMeta(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.setLore(null);
		im.setDisplayName(null);
		is.setItemMeta(im);
	}
}