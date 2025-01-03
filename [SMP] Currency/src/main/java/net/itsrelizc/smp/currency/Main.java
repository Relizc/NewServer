package net.itsrelizc.smp.currency;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.diamonds.DiamondCommand;
import net.itsrelizc.diamonds.DiamondEatListener;
import net.itsrelizc.diamonds.DiamondJar;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.diamonds.commands.GiveDiamondPotion;
import net.itsrelizc.events.EventRegistery;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new DiamondCommand());
		CommandRegistery.register(new GiveDiamondPotion());
		
		EventRegistery.register(new DiamondJar());
		EventRegistery.register(new DiamondEatListener());
	}
	
	@Override
	public void onDisable() {
		DiamondPurse.savePurse();
	}

}
