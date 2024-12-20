package net.itsrelizc.smp.homeplugin;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.smp.homeplugin.commands.HomeCommand;

public class Main extends JavaPlugin {
	
	public static JavaPlugin main;
	
	@Override
	public void onEnable() {
		main = this;
		
		CommandRegistery.register(new HomeCommand());
	}
	
	

}
