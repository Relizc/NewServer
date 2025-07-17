package net.itsrelizc.smp.modsmp.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.bundler.Main;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.quests.QuestNewArrival;
import net.itsrelizc.quests.QuestUtils;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class LoginLogoutHandler implements Listener {
	
	public static class T4j extends MenuTemplate2 {

		public T4j(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void apply() {
			fillAllWith(ItemGenerator.generate(Material.GRAY_STAINED_GLASS_PANE, 1));
			
			int constant = 128;
			if (Locale.getLanguage(getPlayer()) == Language.ZH_CN) {
				constant = 32;
			}
			
			List<String> list = StringUtils.fromNewList();
			
			for (int i = 0; i < 10; i ++) {
				list.add(" ");
				
				List<String> wrapped = null;
				if (Locale.getLanguage(getPlayer()) == Language.ZH_CN) {
					wrapped = StringUtils.wrap(Locale.get(getPlayer(), "t4j." + i), constant);
				}
				
				for (String s : wrapped) {
					list.add("§r§7" + s);
				}
				
			}
			org.bukkit.inventory.ItemStack letter = ItemGenerator.generateByList(Material.ENCHANTED_BOOK, 1, Locale.get(getPlayer(), "t4j.hello"), list);
			this.menu.setItem(22, letter);
			this.menu.setItem(40, ItemGenerator.generate(Material.BARRIER, 1, Locale.get(getPlayer(), "t4j.quit")));
		
		}
		
		@Override
		public void onClick(InventoryClickEvent event) {
			
		}
		
		@Override
		public void onClose(InventoryCloseEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void newguy(NewPlayerJoinedEvent event) {
		Locale.sendLanguageInfo(event.getPlayer());
		
		
	}
	
	@EventHandler
	public void join(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("activeQuest", null);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				QuestUtils.startQuest(event.getPlayer(), QuestNewArrival.INSTANCE);
				QuestUtils.setActiveQuest(event.getPlayer(), QuestNewArrival.INSTANCE);
			}
			
		}.runTaskLater(EventRegistery.main, 40L);
	}
		
	@EventHandler
	public void playerjoin(PlayerJoinEvent event) {
		
		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "smp.login.welcome"));
		
//		TextComponent x = new TextComponent(Locale.get(event.getPlayer(), "smp.login.serverstatus"));
//		TextComponent y = new TextComponent(Locale.get(event.getPlayer(), "smp.login.updates"));
//		StringUtils.attachCommand(y, "updates", null);
//		x.addExtra(y);
//		event.getPlayer().spigot().sendMessage(x);
		
		TextComponent fairplay = new TextComponent(Locale.get(event.getPlayer(), "smp.login.fairplaylink"));
		StringUtils.attachOpenURL(fairplay, "https://relizc.github.io/relizcnetwork/fair-play-policy.html");
		
		
		TextComponent pre = new TextComponent(Locale.get(event.getPlayer(), "smp.login.agreeto"));
		
		pre.addExtra(fairplay);
		
		event.getPlayer().spigot().sendMessage(pre);
		
		event.getPlayer().sendMessage("\n\n\n");
		event.getPlayer().sendMessage("§c§m--------------------------------");
		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "smp.login.anticheatannounce"));
		event.getPlayer().sendMessage("§c§m--------------------------------");
		
		
		DiamondPurse.loadPurse(event.getPlayer());
		
//		Menu2 menu = new Menu2(event.getPlayer(), 5, new T4j(Locale.get(event.getPlayer(), "t4j.toyou")));
//		menu.open();
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);
				
				String content = " §7" + Main.getVersion() + " §a更新日志 §8(2025/7/17)";
				content += "\n §8- §r修复了部分箭矢显示失常的情况";
				content += "\n §8- §r修复了服务器间歇性卡顿的情况";
				content += "\n §8- §r修复了部分箭矢造成神秘伤害的情况";
				content += "\n §8- §r修复了经验系统失常的情况";
				content += "\n §8- §r修复了通过滥用死亡视角探索地图的问题";
				content += "\n §8- §r添加了踢出闲置玩家的功能";
				content += "\n §8- §r添加了记录游戏时长功能";
				content += "\n §8- §r添加了§c反作弊§r §7§o(例如反矿物透视)";
				content += "\n §8- §r重新添加了§b付款 §7(/pay) §r与§b吐出钻石 §7(/spit) §r的功能";
				content += "\n §7 ? 若想获得物理钻石, 可以尝试§c摧毁§7吐出来的§b钻石瓶§7!";
				
				event.getPlayer().sendMessage("\n\n§e§m--------------------------------§r\n" + content + "\n§e§m--------------------------------");
			}
			
		}.runTaskLater(EventRegistery.main, 80l);
		
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	

}
