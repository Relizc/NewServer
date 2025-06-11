package net.itsrelizc.bundler;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.itsrelizc.commands.CSetTabListName;
import net.itsrelizc.commands.CommandHelp;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.commands.CommandTestSign;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.CommandCheckEvents;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.menus.Menu2Failsafe;
import net.itsrelizc.nbt.CommandGetNBTOnHand;
import net.itsrelizc.players.AFKDetector;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.locales.CommandReloadLocale;
import net.itsrelizc.players.locales.LangSelector;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.moderation.ModerationListener;
import net.itsrelizc.scoreboards.GameInfo;
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
		CommandRegistery.register(new CommandCheckEvents());
		CommandRegistery.register(new CommandGetNBTOnHand());
		
		
		
		//EventRegistery.register(new ModerationListener());
		//EventRegistery.register(new Menu2Failsafe());
		
	}

}
