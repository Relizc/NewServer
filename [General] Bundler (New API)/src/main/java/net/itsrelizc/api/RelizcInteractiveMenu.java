package net.itsrelizc.api;

import org.bukkit.inventory.ItemStack;

public interface RelizcInteractiveMenu {
	
	public void setItem(int slot, ItemStack item);
	
	public void clearItem(int slot);
	
	public void close();
	
	public RelizcInteractiveMenuTemplate getTemplate();
	
	

}
