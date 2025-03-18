package net.itsrelizc.smp.insurance;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.smp.insurance.listeners.ListenerCraftItem;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new InsuranceCommand());
		
		EventRegistery.register(new ListenerCraftItem());
		
	}
	
	@Override
	public void onDisable() {
		
	}

}
