package net.itsrelizc.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventRegistery {
	
	public static Plugin main;
	
	public static void register(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, main);
	}

}
