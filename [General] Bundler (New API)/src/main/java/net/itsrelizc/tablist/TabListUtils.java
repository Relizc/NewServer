package net.itsrelizc.tablist;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.itsrelizc.bundler.Main;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.locales.Locale;

public class TabListUtils implements Listener {
	
	public static class TablistPair {
		private String h;
		private String f;
		public String getHeader() {
			return h;
		}
		public String getFooter() {
			return f;
		}
		public TablistPair(String a, String b) {
			h = a;
			f = b;
		}
	}
	
	private static HashMap<Player, TablistPair> content = new HashMap<Player, TablistPair>();
	private static String default_h;
	private static String default_f;
	
//	private static String _d = "\n§e§lRelizc Network§r §81.20.1\n§e您正在游玩§d" + Main.type + "\n\n§7玩家: %d/%d (%d)";
//	private static String _k = "\n§7今日新闻: §a同性恋贾斯丁疑似垄断钻石行业！§r\n";	
	
	public static void enable(Plugin plugin) {
		
		
		
		default_h = "";
		default_f = "";
		Bukkit.getPluginManager().registerEvents(new TabListUtils(), plugin);
		
	}
	
	private static TablistPair a(String b, String c) {
		TablistPair k = new TablistPair(b, c);
		return k;
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerLoginEvent event) {
		
		content.put(event.getPlayer(), a(default_h, default_f));
		update(event.getPlayer());
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		content.remove(event.getPlayer());
	}
	
	
	
	public static void update(Player player) {
		
//		System.out.println("Attempted update tablist");
		
		TablistPair k = content.get(player);
		
//		System.out.println("Attempted update tablist error1");
//		PacketContainer p = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
//		System.out.println("Attempted update tablist error2");
		String s = String.format(Locale.get(player, "smp.tablist.title"), Main.type, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers(), 0);
//		System.out.println("Attempted update tablist error3");
//		p.getChatComponents().write(0, WrappedChatComponent.fromText(k.h + s)).write(1, WrappedChatComponent.fromText(Locale.get(player, "smp.tablist.footer") + k.f));
//		p.getChatComponents().write(0, WrappedChatComponent.fromText("Hello")).write(1, WrappedChatComponent.fromText("world"));
//		System.out.println("Attempted update tablist error4");
		
//		TaskDelay.delayTask(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					ProtocolLibrary.getProtocolManager().sendServerPacket(player, p);
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					System.out.println("Attempted update tablist error");
//					e.printStackTrace();
//				}
//			}
//			
//		}, 10L);
		
		player.setPlayerListHeaderFooter(k.h + s, Locale.get(player, "smp.tablist.footer") + k.f);
		
//		System.out.println("Attempted update tablist complete");

	}
	
	public static void updateHeader(Player player, String header) {
		if (!content.containsKey(player)) content.put(player, a(default_h, default_f));
		content.get(player).h = header;
		update(player);

	}
	
	public static void updateFooter(Player player, String footer) {
		if (!content.containsKey(player)) content.put(player, a(default_h, default_f));
		content.get(player).f = footer;
		update(player);

	}

}
