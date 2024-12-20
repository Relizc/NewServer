package net.itsrelizc.menus.templates;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.itsrelizc.menus.ClassicMenu;

public interface TemplateBase {
	
	public boolean onClick(InventoryClickEvent event);
	
	public boolean onClose(InventoryCloseEvent event);

	public void loadTemplate(ClassicMenu classicMenu);
}
