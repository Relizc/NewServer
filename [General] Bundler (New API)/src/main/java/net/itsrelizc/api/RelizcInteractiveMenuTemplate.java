package net.itsrelizc.api;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface RelizcInteractiveMenuTemplate {
	
	public void apply();
	
	public void onClick(InventoryClickEvent event);

	public void onClose(InventoryCloseEvent event);
	
	public void fillAllWith(ItemStack item);
	
	public void leaveMiddleWith(ItemStack item);
	
	public void leaveMiddleWithAir();
	
	/* This preset creates a menu with the following:
	 * 
	 * Black glass border
	 * Light gray class interior
	 * Close button barrier
	 * 
	 * */
	public void defaultPreset();
	
	/* This preset creates a menu with the following:
	 * 
	 * Black glass border
	 * Air interior (empty slots interior)
	 * Close button barrier
	 * 
	 * */
	public void stashPreset();
	
	public void putCloseButton();
	
	public void setItem(int slot, ItemStack item);

}
