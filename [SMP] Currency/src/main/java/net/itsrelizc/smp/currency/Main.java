package net.itsrelizc.smp.currency;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.diamonds.DiamondCommand;
import net.itsrelizc.diamonds.DiamondEatListener;
import net.itsrelizc.diamonds.DiamondJar;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.diamonds.commands.CommandPay;
import net.itsrelizc.diamonds.commands.CommandSpit;
import net.itsrelizc.diamonds.commands.GiveDiamondPotion;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.smp.corps.CorporateBusiness;
import net.itsrelizc.smp.corps.Contract;
import net.itsrelizc.smp.corps.ContractListener;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new DiamondCommand());
		CommandRegistery.register(new GiveDiamondPotion());
		CommandRegistery.register(new CommandSpit());
		CommandRegistery.register(new CommandPay());
		
		EventRegistery.register(new DiamondJar());
		EventRegistery.register(new DiamondEatListener());
		EventRegistery.register(new ContractListener());
		
		CorporateBusiness.startClearCache();
		
		Contract.loadContractSigned();
	}
	
	@Override
	public void onDisable() {
		DiamondPurse.savePurse();
		
		Contract.saveContractSigned();
	}

}
