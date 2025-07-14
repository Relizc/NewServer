package net.itsrelizc.bundler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.commands.CSetTabListName;
import net.itsrelizc.commands.CommandHelp;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.commands.CommandTestSign;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.nbt.CommandGetNBTOnHand;
import net.itsrelizc.npc.CommandPath;
import net.itsrelizc.players.AFKDetector;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.locales.CommandReloadLocale;
import net.itsrelizc.players.locales.LangSelector;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.itsrelizc.tablist.TabListUtils;

public class Main extends JavaPlugin {
	
	public static String type = "1.20.1 模组生存";
	
	@Override
	public void onLoad() {
		JSON.initializeDatabasePath();
		EventRegistery.main = this;
	}
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new RelizcCommand("mycrushisneitherofthegirlsyouknow", "you suck", StringUtils.fromArgs("testcommandpleaseignore", "test")));
		
		TabListUtils.enable(this);
		
		Grouping.initlizeRankGroups(this);
		
		AFKDetector afk = new AFKDetector();
		EventRegistery.register(afk);
		afk.onEnable();
		
		Locale.load_all();
		
		CommandRegistery.register(new LangSelector());
		CommandRegistery.register(new CSetTabListName());
		CommandRegistery.register(new CommandTestSign());
		CommandRegistery.register(new CommandReloadLocale());
		CommandRegistery.register(new CommandHelp());
		//CommandRegistery.register(new CommandCheckEvents());
		CommandRegistery.register(new CommandGetNBTOnHand());
		CommandRegistery.register(new CommandVersion());
		CommandRegistery.register(new CommandPath());
		
		

		
//		try {
//	        SimpleCommandMap commandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
//
//	        // Remove the default version command
//	        Command old = commandMap.getCommand("bukkit:version");
//	        if (old != null) {
//	            old.unregister(commandMap);
//	        }
//
//
//	        commandMap.register("version", new CommandVersion());
//
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
		
		//EventRegistery.register(new ModerationListener());
		//EventRegistery.register(new Menu2Failsafe());
		
	}
	
	@Override
	public void onDisable() {
		for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
			team.unregister();
		}
	}

	public static String getVersion() {
		return "8.1";
	}

}
