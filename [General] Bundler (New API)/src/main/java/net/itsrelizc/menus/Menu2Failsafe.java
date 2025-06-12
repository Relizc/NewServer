package net.itsrelizc.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Menu2Failsafe implements Listener {
	
	@EventHandler
	public void openEvent(InventoryClickEvent event) {
		if (Menu2.isDuped((Player) event.getWhoClicked(), event.getCurrentItem())) {
			event.getCurrentItem().setAmount(0);
		}
	}

}
