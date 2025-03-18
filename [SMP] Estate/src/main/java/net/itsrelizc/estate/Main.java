package net.itsrelizc.estate;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.estate.commands.CommandEstateMap;
import net.itsrelizc.estate.commands.EstateListener;
import net.itsrelizc.events.EventRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new CommandEstateMap());
		
		EventRegistery.register(new EstateListener());
		
	}

}
