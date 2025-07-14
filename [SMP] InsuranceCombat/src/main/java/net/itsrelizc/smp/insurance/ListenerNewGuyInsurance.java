package net.itsrelizc.smp.insurance;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;

public class ListenerNewGuyInsurance implements Listener {
	
	@EventHandler
	public void h(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("insurance", null);
	}

}
