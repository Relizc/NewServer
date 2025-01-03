package net.itsrelizc.smp.insurance;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new InsuranceCommand());
		
	}
	
	@Override
	public void onDisable() {
		
	}

}
