package net.itsrelizc.bundler;

import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.locales.LangSelector;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.ChatUtils;
import net.itsrelizc.tablist.TabListUtils;

public class Main extends JavaPlugin {
	
	public static String type = "1.20.1模组生存";
	
	@Override
	public void onEnable() {
		CommandRegistery.register(new RelizcCommand("mycrushisneitherofthegirlsyouknow", "you suck", ChatUtils.fromArgs("testcommandpleaseignore", "test")));
		
		TabListUtils.enable(this);
		
		Grouping.initlizeRankGroups(this);
		
		Locale.load_all();
		
		CommandRegistery.register(new LangSelector());
		
		EventRegistery.main = this;
	}

}
