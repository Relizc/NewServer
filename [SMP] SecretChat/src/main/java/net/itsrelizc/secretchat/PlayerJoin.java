package net.itsrelizc.secretchat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.Grouping;

public class PlayerJoin implements Listener {
	
	private static Map<Player, ArmorStand> tracker = new HashMap<Player, ArmorStand>();
	
	private static Map<ArmorStand, Integer> tasktracker = new HashMap<ArmorStand, Integer>();
	
	private static Map<Player, Integer> messages = new HashMap<Player, Integer>();
	private static Map<ArmorStand, Integer> armordistance = new HashMap<ArmorStand, Integer>();
	
	private static Map<String, ArmorStand> messagecon = new HashMap<String, ArmorStand>();
	private static Map<ArmorStand, String> messagecon_2 = new HashMap<ArmorStand, String>();
	
	private static Map<ArmorStand, Integer> messageamount = new HashMap<ArmorStand, Integer>();
	private static Map<ArmorStand, Integer> displayedmsgamount = new HashMap<ArmorStand, Integer>();
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		ArmorStand a = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ARMOR_STAND);
		a.setMarker(true);
		a.setInvulnerable(true);
		a.setInvisible(true);
		
		
		tracker.put(event.getPlayer(), a);
		messages.put(event.getPlayer(), 0);
		
		event.getPlayer().setPassenger(a);
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		tracker.get(event.getPlayer()).remove();
		tracker.remove(event.getPlayer());
		messages.remove(event.getPlayer());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void chat(PlayerChatEvent event) {
		
		if (messagecon.containsKey(event.getMessage())) {
			messagecon.get(event.getMessage()).setTicksLived(1);
			messageamount.put(messagecon.get(event.getMessage()), messageamount.get(messagecon.get(event.getMessage())) + 1);
			return;
		}
		
		
		
		ArmorStand a = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation().add(0, 2, 0), EntityType.ARMOR_STAND);
		a.setInvulnerable(true);
		a.setGravity(false);
		a.setCustomName("§a" + event.getMessage());
		a.setCustomNameVisible(true);
		a.setInvisible(true);
		a.setMarker(true);
		
		event.getPlayer().hideEntity(EventRegistery.main, a);
		
		messagecon.put(event.getMessage(), a);
		messagecon_2.put(a, event.getMessage());
		displayedmsgamount.put(a, 1);
		messageamount.put(a, 1);
		
		messages.put(event.getPlayer(), messages.getOrDefault(event.getPlayer(), 0) + 1);
		
		armordistance.put(a, messages.get(event.getPlayer()));
		
		int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				
				if (messages.get(event.getPlayer()) == null) {
					a.remove();
					Bukkit.getScheduler().cancelTask(tasktracker.get(a));
					
					tasktracker.remove(a);
					messageamount.remove(a);
					displayedmsgamount.remove(a);
					
					messagecon.remove(messagecon_2.get(a));
					messagecon_2.remove(a);
					armordistance.remove(a);
				}
				
//				Bukkit.broadcastMessage("Tracking task " + tasktracker.get(a) + " " + a.getTicksLived());
				
				if (displayedmsgamount.get(a) != messageamount.get(a)) {
					displayedmsgamount.put(a, messageamount.get(a));
//					Bukkit.broadcastMessage("additional message added");
					a.setCustomName("§a" + event.getMessage() + " §7x" + messageamount.get(a));
				}
				
				int delta = messages.get(event.getPlayer()) - armordistance.get(a);
				a.teleport(event.getPlayer().getLocation().add(0, 2 + delta * 0.3, 0));
				
				if (a.getTicksLived() > 100) {
					
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (p != event.getPlayer()) {
							p.spawnParticle(Particle.CLOUD, a.getLocation().add(0, 0.5, 0), 5, 0, 0, 0, 0.03);
						}
					}

					a.remove();
					Bukkit.getScheduler().cancelTask(tasktracker.get(a));
					
					tasktracker.remove(a);
					messageamount.remove(a);
					displayedmsgamount.remove(a);
					
					messagecon.remove(messagecon_2.get(a));
					messagecon_2.remove(a);
					armordistance.remove(a);
					
					
					
				}
			}
			
		}, 0L, 1L);
		
		tasktracker.put(a, task);
	}
	
	@EventHandler
	public void death(PlayerRespawnEvent event) {
		TaskDelay.delayTask(new Runnable() {

			@Override
			public void run() {
				event.getPlayer().setPassenger(tracker.get(event.getPlayer()));
				ArmorStand a = (ArmorStand) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getLocation(), EntityType.ARMOR_STAND);
				a.setMarker(true);
				a.setInvulnerable(true);
				a.setInvisible(true);
				
				
				tracker.put(event.getPlayer(), a);
				messages.put(event.getPlayer(), 0);
				
				event.getPlayer().setPassenger(a);
				
			}
			
		}, 10L);
	}

}
