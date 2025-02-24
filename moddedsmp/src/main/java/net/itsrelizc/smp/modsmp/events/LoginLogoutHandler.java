package net.itsrelizc.smp.modsmp.events;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.ChatUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class LoginLogoutHandler implements Listener {
		
	@EventHandler
	public void playerjoin(PlayerJoinEvent event) {
		
		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "smp.login.welcome"));
		
		TextComponent x = new TextComponent(Locale.get(event.getPlayer(), "smp.login.serverstatus"));
		TextComponent y = new TextComponent(Locale.get(event.getPlayer(), "smp.login.updates"));
		ChatUtils.attachCommand(y, "updates", null);
		x.addExtra(y);
		event.getPlayer().spigot().sendMessage(x);
		
		TextComponent fairplay = new TextComponent(Locale.get(event.getPlayer(), "smp.login.fairplaylink"));
		ChatUtils.attachOpenURL(fairplay, "https://relizc.github.io/relizcnetwork/fair-play-policy.html");
		
		
		TextComponent pre = new TextComponent(Locale.get(event.getPlayer(), "smp.login.agreeto"));
		
		pre.addExtra(fairplay);
		
		event.getPlayer().spigot().sendMessage(pre);
		
		event.getPlayer().sendMessage("\n\n\n");
		event.getPlayer().sendMessage("§c§m--------------------------------");
		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "smp.login.anticheatannounce"));
		event.getPlayer().sendMessage("§c§m--------------------------------");
		
		
		DiamondPurse.loadPurse(event.getPlayer());
		
		
	}
	

}
