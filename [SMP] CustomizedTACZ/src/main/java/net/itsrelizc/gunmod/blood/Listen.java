package net.itsrelizc.gunmod.blood;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listen implements Listener {
	
	private static Map<Player, Container> content = new HashMap<Player, Container>();
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		content.put(event.getPlayer(), new Container(event.getPlayer()));
		
	}
	
	 @EventHandler
	 public void leave(PlayerQuitEvent event) {
		 
		 
		 content.remove(event.getPlayer());
		 
	 }

}
