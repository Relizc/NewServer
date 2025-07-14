package net.itsrelizc.secretchat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Rank;

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
	public void customTeleport(CustomPlayerTeleportEvent event) {
		Player player = event.getPlayer();
		
		Entity prim = player.getPassenger();
		if (prim != null) player.removePassenger(prim);
		
		
		player.teleport(event.getTo());
		if (prim != null) player.setPassenger(prim);
		else {
			spawnArmorStand(player);
		}
	}
	
	public void spawnArmorStand(Player player) {
		ArmorStand a = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		a.setMarker(true);
		a.setInvulnerable(true);
		a.setInvisible(true);
		
		
		tracker.put(player, a);
		messages.put(player, 0);
		
		player.setPassenger(a);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		spawnArmorStand(event.getPlayer());
		
	}
	
	@EventHandler
	public void quit(PlayerQuitEvent event) {
		tracker.get(event.getPlayer()).remove();
		tracker.remove(event.getPlayer());
		messages.remove(event.getPlayer());
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void asyncChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true); // Cancel default broadcast

        Player sender = event.getPlayer();
        Profile prof = Profile.findByOwner(sender);
        String message = event.getMessage();

        for (Player receiver : Bukkit.getOnlinePlayers()) {
            if (receiver.equals(sender)) {
            	
            	
            	
                receiver.sendMessage(Rank.findByPermission(prof.permission).displayName + " " + event.getPlayer().getDisplayName() + "§7: §r" + event.getMessage());
                
                continue;
            }

            double distance = sender.getLocation().distance(receiver.getLocation());

            if (distance > 32) continue; // Too far, ignore

            String finalMessage = message;

            if (distance > 16) {
                double percent = (distance - 16) / 16.0; // 0.0 to 1.0
                finalMessage = obfuscateMessage(message, percent);
            }

            receiver.sendMessage(Rank.findByPermission(prof.permission).displayName + "§7: §r" + finalMessage);
        }
	}
	
	private String obfuscateMessage(String message, double percentObfuscate) {
        StringBuilder result = new StringBuilder();
        for (char c : message.toCharArray()) {
//            if (Character.isWhitespace(c)) {
//                result.append(c);
//                continue;
//            }

            if (Math.random() < percentObfuscate) {
                result.append(ChatColor.MAGIC).append("?").append(ChatColor.RESET);
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
	
	@EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player && event.getDismounted() instanceof Player) {
            Player player = (Player) event.getEntity();
            
            
            Player dismounted = (Player) event.getDismounted();
            
            dismounted.setPassenger(tracker.get(dismounted));
        }
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
				
//				//("Tracking task " + tasktracker.get(a) + " " + a.getTicksLived());
				
				if (displayedmsgamount.get(a) != messageamount.get(a)) {
					displayedmsgamount.put(a, messageamount.get(a));
//					//("additional message added");
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
