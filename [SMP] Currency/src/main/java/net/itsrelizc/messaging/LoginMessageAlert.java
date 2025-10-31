package net.itsrelizc.messaging;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.messaging.Messaging.Message;
import net.itsrelizc.players.locales.Locale;

public class LoginMessageAlert implements Listener {
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		int unread = 0;
		int unclaimed = 0;
		
		List<Message> messages = Messaging.getMessages(event.getPlayer(), Messaging.GENERAL);
		for (Message msg : messages) {
			if (msg.hasItems()) {
				unclaimed ++;
			}
			else if (!msg.read) {
				unread ++;
			}
		}
		
		messages = Messaging.getMessages(event.getPlayer(), Messaging.TRADES);
		for (Message msg : messages) {
			if (msg.hasItems()) {
				unclaimed ++;
			}
			else if (!msg.read) {
				unread ++;
			}
		}
		
		messages = Messaging.getMessages(event.getPlayer(), Messaging.SOCIAL);
		for (Message msg : messages) {
			if (msg.hasItems()) {
				unclaimed ++;
			}
			else if (!msg.read) {
				unread ++;
			}
		}
		
		 messages = Messaging.getMessages(event.getPlayer(), Messaging.SECURITY);
		for (Message msg : messages) {
			if (msg.hasItems()) {
				unclaimed ++;
			}
			else if (!msg.read) {
				unread ++;
			}
		}
		
		String a = unread > 0 ? " " + Locale.a(event.getPlayer(), "messages.unread.msg").formatted(unread) + " " : "";
		String b = unclaimed > 0 ? " " + Locale.a(event.getPlayer(), "messages.unread.package").formatted(unclaimed) + " " : "";
		
		if (unread == 0 && unclaimed == 0) {
			
		} else {
			new BukkitRunnable() {

				@Override
				public void run() {
					event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_YES, 1f, 1f);
					event.getPlayer().sendMessage(Locale.a(event.getPlayer(), "messages.unread").formatted(a, b));
				}
				
			}.runTaskLater(EventRegistery.main, 20l*3);
			
		}
	}

}
