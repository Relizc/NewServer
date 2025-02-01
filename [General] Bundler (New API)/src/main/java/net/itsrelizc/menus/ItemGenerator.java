package net.itsrelizc.menus;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGenerator {
	
	public static ItemStack generate(ItemStack item) {
		return item;
	}
	
	public static ItemStack generate(Material material, int amount) {
		return new ItemStack(material, amount);
	}
	
	public static ItemStack generate(Material material, int amount, String name) {
		ItemStack t = generate(material, amount);
		ItemMeta m = t.getItemMeta();
		
		m.setDisplayName(name);
		
		t.setItemMeta(m);
		return t;
	}
	
	public static ItemStack generate(Material material, int amount, String name, String... lore) {
		ItemStack t = generate(material, amount);
		ItemMeta m = t.getItemMeta();
		
		m.setDisplayName(name);
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < lore.length; i ++) {
			l.add((String) lore[i]);
		}
		m.setLore(l);
		
		t.setItemMeta(m);
		return t;
	}
	
	public static ItemStack generateLore(Material material, int amount, String... lore) {
		ItemStack t = generate(material, amount);
		ItemMeta m = t.getItemMeta();

		List<String> l = new ArrayList<String>();
		for (int i = 0; i < lore.length; i ++) {
			l.add((String) lore[i]);
		}
		m.setLore(l);
		
		t.setItemMeta(m);
		return t;
	}
	
	public static ItemStack generate(ItemStack formed, int amount, String name, String... lore) {
		ItemStack t = formed;
		ItemMeta m = t.getItemMeta();
		
		m.setDisplayName(name);
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < lore.length; i ++) {
			l.add((String) lore[i]);
		}
		m.setLore(l);
		
		t.setItemMeta(m);
		return t;
	}
	
	
	
}
