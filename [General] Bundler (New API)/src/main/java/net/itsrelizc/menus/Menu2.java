package net.itsrelizc.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.events.EventRegistery;

public class Menu2 implements Listener {
	
	private Inventory inventory;
	private MenuTemplate2 currentTemplate;
	private Player player;
	
	public Menu2(Player player, int rows, MenuTemplate2 template) {
		
		this.player = player;
		this.currentTemplate = template;
		this.inventory = Bukkit.createInventory(player, rows * 9, template.getTitle());
		this.currentTemplate.apply_wrapper(this);
		
		EventRegistery.register(this);
		
	}
	
	public void open() {
		this.currentTemplate.apply_wrapper(this);
		this.player.openInventory(inventory);
	}
	
	public int getSize() {
		return this.inventory.getSize();
	}
	
	public int getRows() {
		return this.getSize() / 9;
	}
	
	public void setItem(int slot, ItemStack item) {
		this.inventory.setItem(slot, item);
	}
	
	public void clearItem(int slot) {
		this.inventory.clear(slot);
	}

	public Player getPlayer() {
		// TODO Auto-generated method stub
		return this.player;
	}
	
	@EventHandler
	public void event_onClick(InventoryClickEvent event) {
		if (((Player) event.getWhoClicked()) != player) {
			return;
		}
		event.setCancelled(true);
		this.currentTemplate.onClick(event);
		
//		Bukkit.broadcastMessage("invclick by " + event.getWhoClicked().getName());
	}
	
	@EventHandler
	public void event_onClose(InventoryCloseEvent event) {
		if (((Player) event.getPlayer()) != player) {
			return;
		}
		this.currentTemplate.onClose(event);
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);
		
//		Bukkit.broadcastMessage("invclose by " + event.getPlayer().getName());
	}

}
