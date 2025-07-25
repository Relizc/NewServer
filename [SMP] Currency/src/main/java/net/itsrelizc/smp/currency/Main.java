package net.itsrelizc.smp.currency;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.diamonds.DiamondCommand;
import net.itsrelizc.diamonds.DiamondEatListener;
import net.itsrelizc.diamonds.DiamondJar;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.diamonds.RelizcItemDiamondJar;
import net.itsrelizc.diamonds.commands.CommandPay;
import net.itsrelizc.diamonds.commands.CommandSpit;
import net.itsrelizc.diamonds.commands.GiveDiamondPotion;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
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
		
		EventRegistery.register(new QuestNewArrival.Listeners());
		
		QuestUtils.registerQuest(QuestNewArrival.INSTANCE);
		//EventRegistery.register(new ContractListener());
		ItemUtils.register(RelizcItemDiamondJar.class);
		
		
	}
	
	@Override
	public void onDisable() {
		DiamondPurse.savePurse();
		
		//Contract.saveContractSigned();
	}

}
