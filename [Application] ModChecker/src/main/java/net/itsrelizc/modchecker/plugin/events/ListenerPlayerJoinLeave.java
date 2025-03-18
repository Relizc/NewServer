package net.itsrelizc.modchecker.plugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.itsrelizc.modchecker.packets.ClientSpigotSubmitServerInsights;
import net.itsrelizc.modchecker.packets.ClientSpigotSubmitServerInsights.SpigotInsightType;
import net.itsrelizc.modchecker.plugin.MainPlugin;

public class ListenerPlayerJoinLeave implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerJoinEvent event) {
		
		ClientSpigotSubmitServerInsights packet = new ClientSpigotSubmitServerInsights(SpigotInsightType.JOIN, event.getPlayer().getName(), event.getPlayer().getUniqueId().toString());
		MainPlugin.client.sendPacket(packet);
		
	}

}
