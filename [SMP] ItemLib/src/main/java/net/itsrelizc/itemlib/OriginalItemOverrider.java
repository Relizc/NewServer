package net.itsrelizc.itemlib;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class OriginalItemOverrider implements Listener {
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent event) {
		Player p = event.getPlayer();
		
		RelizcItemStack result = ItemUtils.castOrCreateItem(p, event.getItem().getItemStack());
		event.getItem().setItemStack(result.getBukkitItem());
	}

}
