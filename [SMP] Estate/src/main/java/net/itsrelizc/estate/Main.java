package net.itsrelizc.estate;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.estate.chestshop.ChestShop;
import net.itsrelizc.estate.commands.CommandEstateMap;
import net.itsrelizc.estate.commands.EstateListener;
import net.itsrelizc.estate.marker.BorderMarker;
import net.itsrelizc.events.EventRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new CommandEstateMap());
		CommandRegistery.register(new BorderMarker.CommandBorderMarker());
		
		EventRegistery.register(new EstateListener());
		EventRegistery.register(new BorderMarker());
		
		new ChestShop().enable();
		
	}

}
