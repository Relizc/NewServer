package net.itsrelizc.smp.modsmp.events;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class EnchantmentBan implements Listener {
	
	@EventHandler
	public void craft(PrepareAnvilEvent event) {
		
		ItemStack it = event.getResult();
		
		ItemMeta im = it.getItemMeta();
		
		if (im == null) return;
		
		if (im.getEnchants().containsKey(Enchantment.MENDING)) {
			im.removeEnchant(Enchantment.MENDING);
			im.addEnchant(Enchantment.DURABILITY, 1, false);
		}
		
		if (im.getEnchants().containsKey(Enchantment.DURABILITY)) {
			im.removeEnchant(Enchantment.DURABILITY);
			im.addEnchant(Enchantment.DURABILITY, 1, false);
		}
		
		it.setItemMeta(im);
		event.setResult(it);
		
	}
	
	@EventHandler
	public void enchant(PrepareItemEnchantEvent event) {
		
		for (@NotNull EnchantmentOffer offer : event.getOffers()) {
			
			if (offer.getEnchantment() == Enchantment.DURABILITY) {
				offer.setEnchantmentLevel(1);
			}
			
		}
		
		
		
	}

}
