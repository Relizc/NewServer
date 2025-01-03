package net.itsrelizc.secretchat;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.events.EventRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		EventRegistery.register(new PlayerJoin());
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
