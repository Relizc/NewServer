package net.itsrelizc.gunmod.craft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class WeaponWorkstationBan implements Listener {
	
	private static final Material WORKSTATION = Material.getMaterial("");

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		if (event.getClickedBlock() != null) {
			
			Bukkit.broadcastMessage(event.getClickedBlock().getType().toString());
			
		}
	}
	
}
