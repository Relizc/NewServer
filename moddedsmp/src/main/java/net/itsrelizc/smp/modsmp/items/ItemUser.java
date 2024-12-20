package net.itsrelizc.smp.modsmp.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.itsrelizc.menus.ClassicMenu;
import net.itsrelizc.menus.templates.ItemDuperUseMenu;
import net.itsrelizc.players.locales.Locale;

public class ItemUser implements Listener {
	
	@EventHandler
	public void useEvent(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		 
	    if (p.getItemInHand().getType() == Material.SCULK_SHRIEKER){

	        if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§e物品复制器")) {
	        	
	        	event.setCancelled(false);
//	        	p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
//	        	
//	        	ClassicMenu menu = new ClassicMenu(p, 5, "贾斯丁物品复制器™", new ItemDuperUseMenu());
//	        	menu.show();
	        	
	        	p.sendMessage(Locale.get(p, "item.duper.deny"));
	        	p.sendMessage(Locale.get(p, "item.duper.comfort"));
	        	
	        	
	        	
	        }
	    }

	}
	

}
