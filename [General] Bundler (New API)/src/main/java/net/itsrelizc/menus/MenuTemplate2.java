package net.itsrelizc.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.api.RelizcInteractiveMenuTemplate;
import net.itsrelizc.players.locales.Locale;

public class MenuTemplate2 implements RelizcInteractiveMenuTemplate {
	
	public static ItemStack BLACK_GLASS() {
		return ItemGenerator.generate(Material.BLACK_STAINED_GLASS_PANE, 1, " ");
	}
	
	public static ItemStack DIM_GLASS() {
		return ItemGenerator.generate(Material.GRAY_STAINED_GLASS_PANE, 1, " ");
	}
	
	public static ItemStack CLOSE_BARRIER(Player player) {
		return ItemGenerator.generate(Material.BARRIER, 1, Locale.get(player, "item.close_barrier.name"));
	} 
	
	private String title;
	protected Menu2 menu;
	
	public MenuTemplate2(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
		
	}
	
	public Player getPlayer() {
		return this.menu.getPlayer();
	}
	
	public void fillAllWith(ItemStack item) {
		for (int i = 0; i < this.menu.getSize(); i ++) {
			this.menu.setItem(i, item);
		}
	}
	
	public void leaveMiddleWith(ItemStack item) {
		for (int i = 10; i < this.menu.getRows() * 9 - 10; i ++) {
			if ((i - 8) % 9 == 0 || (i - 8) % 9 == 1) continue;
			this.menu.setItem(i, item);
		}
	}
	
	public void leaveMiddleWithAir() {
		for (int i = 10; i < this.menu.getRows() * 9 - 10; i ++) {
			if ((i - 8) % 9 == 0 || (i - 8) % 9 == 1) continue;
			this.menu.clearItem(i);
		}
	}
	
	/* This preset creates a menu with the following:
	 * 
	 * Black glass border
	 * Light gray class interior
	 * Close button barrier
	 * 
	 * */
	public void defaultPreset() {
		this.fillAllWith(BLACK_GLASS());
		this.leaveMiddleWith(DIM_GLASS());
		this.putCloseButton();
	}
	
	/* This preset creates a menu with the following:
	 * 
	 * Black glass border
	 * Air interior (empty slots interior)
	 * Close button barrier
	 * 
	 * */
	public void stashPreset() {
		this.fillAllWith(BLACK_GLASS());
		this.leaveMiddleWithAir();
		this.putCloseButton();
	}
	
	public void putCloseButton() {
		this.menu.setItem(this.menu.getRows() * 9 - 5, CLOSE_BARRIER(this.menu.getPlayer()));
	}
	
	public void setItem(int slot, ItemStack item) {
		this.menu.setItem(slot, item);
	}
	
	void apply_wrapper(Menu2 menu2) {
		this.menu = menu2;
		this.apply();
	}

	public void apply() {
		
		
		
	}
	
	public void onClick(InventoryClickEvent event) {
		
	}

	public void onClose(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
		
	}

}
