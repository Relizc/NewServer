package net.itsrelizc.smp.modsmp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KunKeepInventory implements Listener {
	
	@EventHandler
	public void die(PlayerDeathEvent event) {
		if (event.getEntity().getUniqueId().toString().equalsIgnoreCase("18552047-829c-49bc-a3b3-e730c2b6885e")) { // kunbro
			event.setKeepInventory(true);
		}
	}

}
