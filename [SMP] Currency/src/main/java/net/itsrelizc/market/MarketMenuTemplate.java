package net.itsrelizc.market;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.itsrelizc.menus.MenuTemplate2;

public class MarketMenuTemplate extends MenuTemplate2 {

	public MarketMenuTemplate(String title) {
		super(title);
	}
	
	@Override
	public void apply() {
		this.leaveMiddleWithAir();
	}
	
	@Override
	public void onClick(InventoryClickEvent event) {
		
	}
	
	@Override
	public void onClose(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
	}

}
