package net.itsrelizc.smp.modsmp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class DropItemClear {
	
	private static int timer = 15 * 60;
	
	public static void broadcast(int time) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			StringUtils.systemMessage(player, Locale.get(player, "smp.janitorjustin"), String.format(Locale.get(player, "smp.janitorjustin.inform"), time));
		}
	}
	
	public static void broadcast_v2(int time) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			StringUtils.systemMessage(player, Locale.get(player, "smp.janitorjustin"), String.format(Locale.get(player, "smp.janitorjustin.urgent"), time));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
		}
	}
	
	public static void broadcast_v3(int a) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			StringUtils.systemMessage(player, Locale.get(player, "smp.janitorjustin"), String.format(Locale.get(player, "smp.janitorjustin.complete"), a));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 0);
		}
	}
	
	public static void enable() {
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				
				timer -= 1;
				
				if (timer == 10 * 60) {
					
					broadcast(10);
					
				} else if (timer == 5 * 60) {
					
					broadcast(5);
					
				} else if (timer == 3 * 60) {
					
					broadcast(3);
					
				} else if (timer == 2 * 60) {
					
					broadcast(2);
					
				} else if (timer == 1 * 60) {
					
					broadcast(1);
					
				} else if (timer == 30) {
					
					broadcast_v2(30);
					
				} else if (timer <= 10 && timer > 0) {
					
					broadcast_v2(timer);
					
				} else if (timer == 0) {

					
					String itemlist = "§e已清除的物品：\n\n";
					Map<String, Integer> drops = new HashMap<String, Integer>();
					
					World world = Bukkit.getWorld("world");
				    List<Entity> entList = world.getEntities();
				    
				    int counter = 0;
				    
				    for (Iterator<Entity> localIterator = entList.iterator(); localIterator.hasNext(); ) {
				        Entity current = localIterator.next();
				        if (current instanceof org.bukkit.entity.Item) {
				        	current.remove();
				        	Item cur = (org.bukkit.entity.Item) current;
				        	
				        	counter ++;
				        	
				        }
				        	
				        
				     
				    } 
				    
				    broadcast_v3(counter);
				    
				    timer = 15 * 60;
				}
				
			}
			
		}, 0, 20L);
		
	}
	
}
