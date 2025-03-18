package net.itsrelizc.diamonds;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import net.itsrelizc.string.StringUtils;

public class DiamondPickUpWatchdog implements Listener {
	
	@EventHandler
	public void a(PlayerPickupItemEvent event) {
		
//		ChatUtils.broadcastSystemMessage("diamondchecker", "picked up " + event.getItem().getType().toString());
		
		if (event.getItem().getItemStack().getType().toString().equalsIgnoreCase("DIAMOND")) {
//			ChatUtils.broadcastSystemMessage("diamondchecker", "picked up diamond");
			DiamondUtils.scanValueThenCreate(event.getItem().getItemStack());
		}
		
	}
	
}
