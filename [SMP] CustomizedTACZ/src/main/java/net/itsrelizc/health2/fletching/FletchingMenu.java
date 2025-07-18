package net.itsrelizc.health2.fletching;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.LocaleSession;

public class FletchingMenu extends MenuTemplate2 {
	
	public static class FletchingOpener implements Listener {
		@EventHandler
	    public void onFletchingTableClick(PlayerInteractEvent event) {
	        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
	            event.getClickedBlock() != null &&
	            event.getClickedBlock().getType() == Material.FLETCHING_TABLE) {

	            
	            event.setCancelled(true); // Optional: prevent default behavior
	            
	            Menu2 menu = new Menu2(event.getPlayer(), 6, new FletchingMenu(Locale.a(event.getPlayer(), "menu.fletching_table.title") + " → " + Locale.a(event.getPlayer(), "menu.fletching_table.make_arrow.title")));
	            menu.open();
	        }
	    }
	}
	
	LocaleSession session;

	public FletchingMenu(String title) {
		super(title);
		session = new LocaleSession(getPlayer());
		
		// TODO Auto-generated constructor stub
	}

}
