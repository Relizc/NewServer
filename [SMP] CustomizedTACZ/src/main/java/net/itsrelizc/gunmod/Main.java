package net.itsrelizc.gunmod;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.blood.SwapHands;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		EventRegistery.register(new Hit());
		EventRegistery.register(new SwapHands());
		
		CommandRegistery.register(new GDebugCommand());
	}

}
