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
import net.itsrelizc.quests.QuestListener;
import net.itsrelizc.quests.QuestNewArrival;
import net.itsrelizc.quests.QuestUtils;
import net.itsrelizc.quests.levelling.LevelListeners;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new DiamondCommand());
		CommandRegistery.register(new GiveDiamondPotion());
		CommandRegistery.register(new CommandSpit());
		CommandRegistery.register(new CommandPay());
		
		
		//Contract.loadContractSigned();
		
		//CorporateBusiness.startClearCache();
		
		EventRegistery.register(new DiamondJar());
		EventRegistery.register(new DiamondEatListener());
		EventRegistery.register(new QuestListener());
		EventRegistery.register(new LevelListeners());
		
		QuestUtils.registerQuest(QuestNewArrival.INSTANCE);
		//EventRegistery.register(new ContractListener());
		
		
	}
	
	@Override
	public void onDisable() {
		DiamondPurse.savePurse();
		
		//Contract.saveContractSigned();
	}

}
