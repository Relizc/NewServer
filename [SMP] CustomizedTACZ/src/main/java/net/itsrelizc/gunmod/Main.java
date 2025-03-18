package net.itsrelizc.gunmod;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.blood.Container;
import net.itsrelizc.gunmod.blood.SwapHands;
import net.itsrelizc.gunmod.commands.CommandGenerateBullet;
import net.itsrelizc.gunmod.craft.CartridgeAssemblerListener;
import net.itsrelizc.gunmod.craft.WeaponWorkstationBan;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		EventRegistery.register(new Hit());
		EventRegistery.register(new SwapHands());
		EventRegistery.register(new CartridgeAssemblerListener());
		EventRegistery.register(new WeaponWorkstationBan());
		
		Container.init();
		
		CommandRegistery.register(new GDebugCommand());
		CommandRegistery.register(new CommandGenerateBullet());
		
		
	}

}
