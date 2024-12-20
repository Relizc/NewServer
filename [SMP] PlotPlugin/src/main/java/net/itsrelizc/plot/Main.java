package net.itsrelizc.plot;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.plot.events.ChunkPopulate;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		EventRegistery.register(new ChunkPopulate());
	}
	
}
