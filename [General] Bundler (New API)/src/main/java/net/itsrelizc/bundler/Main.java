package net.itsrelizc.bundler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.itsrelizc.commands.CSetTabListName;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.commands.CommandTestSign;
import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Grouping;
import net.itsrelizc.players.locales.LangSelector;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.moderation.ModerationListener;
import net.itsrelizc.string.StringUtils;
import net.itsrelizc.tablist.TabListUtils;

public class Main extends JavaPlugin {
	
	public static String type = "1.20.1 模组生存";
	
	@Override
	public void onLoad() {
		JSON.initializeDatabasePath();
	}
	
	@Override
	public void onEnable() {
		
		
		CommandRegistery.register(new RelizcCommand("mycrushisneitherofthegirlsyouknow", "you suck", StringUtils.fromArgs("testcommandpleaseignore", "test")));
		
		TabListUtils.enable(this);
		
		Grouping.initlizeRankGroups(this);
		
		Locale.load_all();
		
		CommandRegistery.register(new LangSelector());
		CommandRegistery.register(new CSetTabListName());
		CommandRegistery.register(new CommandTestSign());
		
		EventRegistery.main = this;
		
		EventRegistery.register(new ModerationListener());
		
	}

}
