package net.itsrelizc.smp.modsmp.events;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.world.item.ItemStack;

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
		
	@EventHandler
	public void playerjoin(PlayerJoinEvent event) {
		
		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "smp.login.welcome"));
		
		TextComponent x = new TextComponent(Locale.get(event.getPlayer(), "smp.login.serverstatus"));
		TextComponent y = new TextComponent(Locale.get(event.getPlayer(), "smp.login.updates"));
		StringUtils.attachCommand(y, "updates", null);
		x.addExtra(y);
		event.getPlayer().spigot().sendMessage(x);
		
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
		
		
	}
	

}
