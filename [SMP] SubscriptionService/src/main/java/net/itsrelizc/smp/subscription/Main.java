package net.itsrelizc.smp.subscription;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Grouping.PostPlayerProfileGenerateEvent;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.smp.subscription.commands.CommandAddSubscription;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		
		EventRegistery.register(this);
		
		CommandRegistery.register(new CommandAddSubscription());
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void login(PostPlayerProfileGenerateEvent e) {
		
		PlayerLoginEvent event = e.event;
		
		Profile p = Profile.findByOwner(event.getPlayer());
		p.reloadProfile();
		
		ZonedDateTime dateTime = Instant.ofEpochMilli(p.subscriptionEnd)
                .atZone(ZoneId.systemDefault()); // Use system default time zone	

		// Define the desired format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		
		// Format the ZonedDateTime to the required format
		String formattedDateTime = dateTime.format(formatter);
		
		if (p.subscriptionEnd == -1) {
			event.setResult(Result.KICK_WHITELIST);
			event.setKickMessage(Locale.get(event.getPlayer(), "subscription.none"));
		} else if (System.currentTimeMillis() > p.subscriptionEnd) {
			event.setResult(Result.KICK_WHITELIST);
			event.setKickMessage(Locale.get(event.getPlayer(), "subscription.expire").formatted(formattedDateTime));
		} else {
			TextComponent a = new TextComponent(Locale.get(event.getPlayer(), "subscription.info0"));
			TextComponent b = new TextComponent(Locale.get(event.getPlayer(), "subscription.info1"));
			TextComponent c = new TextComponent("§a§n"+formattedDateTime);
			
			long[] data = StringUtils.convertMillisToTime(p.subscriptionEnd - System.currentTimeMillis());
			
			StringUtils.attachHover(c, Locale.get(event.getPlayer(), "subscription.remaining").formatted(data[0], data[1], data[2], data[3]));
			
			a.addExtra(c);
			a.addExtra(b);
			
			
			TaskDelay.delayTask(new Runnable() {

				@Override
				public void run() {
					StringUtils.systemMessage(event.getPlayer(), Locale.get(event.getPlayer(), "subscription.name"), a);
				}
				
			}, 20l);
		}
		
	}

}
