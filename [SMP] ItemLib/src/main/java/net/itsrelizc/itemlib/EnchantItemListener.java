package net.itsrelizc.itemlib;

import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class EnchantItemListener implements Listener {
	
	@EventHandler
	public void onEnchantItem(EnchantItemEvent event) {
	    Player player = event.getEnchanter();
	    ItemStack item = event.getItem();
	    Map<Enchantment, Integer> enchants = event.getEnchantsToAdd();
	    // You can read or modify the enchantments here
	}
	
	
	@EventHandler
	public void onPrepareAnvil(PrepareAnvilEvent event) {
	    ItemStack result = event.getResult();
	    if (result != null && result.getEnchantments().size() > 0) {
	        
	    	if (!(event.getView().getPlayer() instanceof Player)) return;
	    	Player player = (Player) event.getView().getPlayer();
	    	
	    	RelizcItemStack it = ItemUtils.castOrCreateItem(player, event.getResult());
	    	event.setResult(it.getBukkitItem());
	    	
	    }
	}

	@EventHandler
	public void onAnvilClick(InventoryClickEvent event) {
	    if (event.getInventory() instanceof AnvilInventory &&
	        event.getSlotType() == InventoryType.SlotType.RESULT &&
	        event.getCurrentItem() != null &&
	        event.getCurrentItem().getEnchantments().size() > 0) {
	    	
	    	
	    	if (!(event.getWhoClicked() instanceof Player)) return;
	    	
	    	Player player = (Player) event.getWhoClicked();
	    	
	    	RelizcItemStack it = ItemUtils.castOrCreateItem(player, event.getCurrentItem());
	    	event.setCurrentItem(it.getBukkitItem());
	    	
	    	//ItemUtils.renderNames(player, event.getCurrentItem(), player);
	    	
	    }
	}


}
