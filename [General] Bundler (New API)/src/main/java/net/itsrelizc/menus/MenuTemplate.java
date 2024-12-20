package net.itsrelizc.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.itsrelizc.menus.templates.TemplateBase;

public class MenuTemplate implements TemplateBase {

	@Override
	public boolean onClick(InventoryClickEvent event) {
		return true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onClose(InventoryCloseEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void loadTemplate(ClassicMenu classicMenu) {
		// TODO Auto-generated method stub
		
	}

}
