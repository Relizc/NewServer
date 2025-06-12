package net.itsrelizc.smp.modsmp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.diamonds.DiamondCommand;
import net.itsrelizc.diamonds.DiamondCounter;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.blood.Container;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.modsmp.commands.LOLCommand;
import net.itsrelizc.smp.modsmp.commands.PrestigeCommand;
import net.itsrelizc.smp.modsmp.commands.ShopCommand;
import net.itsrelizc.smp.modsmp.commands.SitCommand;
import net.itsrelizc.smp.modsmp.commands.TPACommand;
import net.itsrelizc.smp.modsmp.commands.TPAccept;
import net.itsrelizc.smp.modsmp.commands.TPDeny;
import net.itsrelizc.smp.modsmp.commands.TestItemGet;
import net.itsrelizc.smp.modsmp.commands.UpdateBook;
import net.itsrelizc.smp.modsmp.events.EnchantmentBan;
import net.itsrelizc.smp.modsmp.events.LoginLogoutHandler;
import net.itsrelizc.smp.modsmp.menus.Contract;
import net.itsrelizc.smp.modsmp.menus.Shop;
import net.itsrelizc.tablist.TabListUtils;
import net.itsrelizc.uptime.TPSService;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new LoginLogoutHandler(), this);
		
		Bukkit.getPluginManager().registerEvents(new Shop(), this);
		
		this.getCommand("tpa").setExecutor(new TPACommand());
		this.getCommand("tpy").setExecutor(new TPAccept());
		this.getCommand("tpn").setExecutor(new TPDeny());	
		
		this.getCommand("testbukkitpleaseignore").setExecutor(new TestItemGet());
		
		this.getCommand("shop").setExecutor(new ShopCommand());	
		
		this.getCommand("updates").setExecutor(new UpdateBook());	
		
		this.getCommand("lol").setExecutor(new LOLCommand());
		
		CommandRegistery.register(new SitCommand());
		CommandRegistery.register(new PrestigeCommand());
		
		SitCommand.enable(this);
		
		
		net.itsrelizc.uptime.TPSUtils.start(this);
		
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				//Bukkit.broadcastMessage(net.itsrelizc.uptime.TPSUtils.getDelayAsFormattedString());
				//Bukkit.broadcastMessage(net.itsrelizc.uptime.TPSUtils.getDelayYieldAsFormattedString());
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					try {
//						Container c = Container.get(p);
//						TabListUtils.updateFooter(p, String.format(Locale.get(p, "general.tablist.diamondsleft"), (float) DiamondCounter.remaining, DiamondPurse.getPurse(p), DiamondPurse.getBloodSugar(p), c.getHead(), c.getChest(), c.getLegs()) + TPSService.getTablistDisplayInfo(p));
					} catch (Exception e) {
						System.out.println("Footer Error " + e);
					}
					TabListUtils.updateFooter(p, String.format(Locale.get(p, "general.tablist.diamondsleft"), (float) DiamondCounter.remaining, DiamondPurse.getPurse(p), DiamondPurse.getBloodSugar(p)) + TPSService.getTablistDisplayInfo(p));

//					TabListUtils.updateFooter(p, String.format(Locale.get(p, "general.tablist.diamondsleft"), (float) DiamondCounter.remaining, DiamondPurse.getPurse(p)) + "[Server Insights unavaliable]");
				}
			}
			
		}, 0, 10L);
		
		//Contract.enable(this);
		
		DiamondCounter.enable(this);

		EventRegistery.register(new EnchantmentBan());
		EventRegistery.register(new NiceUtilities());
		EventRegistery.register(new SMPScoreboardListener());	
		NiceUtilities.startSendingTips();
//		EventRegistery.register(new We());
		
	}
	
	
	
	@Override
	public void onDisable() {
		DiamondCounter.save();
	}
	
	

}
