package net.itsrelizc.bundler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.commands.CSetTabListName;
import net.itsrelizc.commands.CommandDebugStats;
import net.itsrelizc.commands.CommandHelp;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.commands.CommandTestSign;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.nbt.CommandGetNBTOnHand;
import net.itsrelizc.npc.CommandPath;
import net.itsrelizc.npc.NPCSessionCommand;
import net.itsrelizc.players.AFKDetector;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.locales.CommandReloadLocale;
import net.itsrelizc.players.locales.LangSelector;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.web.Authenticator;
import net.itsrelizc.string.StringUtils;
import net.itsrelizc.tablist.TabListUtils;

public class Main extends JavaPlugin {
	
	public static String type = "1.20.1 模组生存";
	
	@Override
	public void onLoad() {
		JSON.initializeDatabasePath();
		EventRegistery.main = this;
		
		Authenticator.init();
	}
	
	private final String url = "http://localhost:5000/keepalive";

	@Override
	public void onEnable() {
		
		getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(2000);
                conn.getResponseCode(); // Fire request
                conn.disconnect();
            } catch (IOException e) {
                getLogger().warning("Failed to send keepalive: " + e.getMessage());
            }
        }, 0L, 20L * 10); // every 30 seconds
		
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
		CommandRegistery.register(new CommandDebugStats());
		CommandRegistery.register(new NPCSessionCommand());
		
		Menu2.startTimedDupeChecking();
		
		

		
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
		return "8.15.3";
	}

}
