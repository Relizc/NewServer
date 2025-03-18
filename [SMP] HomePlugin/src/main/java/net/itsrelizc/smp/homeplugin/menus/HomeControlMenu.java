package net.itsrelizc.smp.homeplugin.menus;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.MenuTemplate;
import net.itsrelizc.string.StringUtils;

public class HomeControlMenu extends MenuTemplate {
	
	@Override
	public boolean onClick(InventoryClickEvent event) {
		
		
		StringUtils.broadcastSystemMessage("testchanclick", event.toString());
		
		return true;
		
		
		
	}

	@Override
	public boolean onClose(InventoryCloseEvent event) {
		
		StringUtils.broadcastSystemMessage("testchanclose", event.toString());
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void loadTemplate(ClassicMenu classicMenu) {
		
		StringUtils.broadcastSystemMessage("testchanload", "ontemplateload");
		// TODO Auto-generated method stub
		
	}
	
	

}
