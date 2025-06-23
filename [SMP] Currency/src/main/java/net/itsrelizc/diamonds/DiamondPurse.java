package net.itsrelizc.diamonds;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.quests.QuestNewArrival;

public class DiamondPurse {
	
	public static class PlayerPurseChangedEvent extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private Player player;


	    public PlayerPurseChangedEvent(Player player) {
	        this.player = player;
	    }

	    public Player getPlayer() {
	        return player;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return HANDLERS;
	    }

	    public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	}
	
	private static HashMap<Player, Long> balance = new HashMap<Player, Long>();
	private static HashMap<Player, Long> bloodsugar = new HashMap<Player, Long>();
	
	public static void loadPurse(Player player) {
		
		for (Player p : balance.keySet()) {
			if (p.getUniqueId().equals(player.getUniqueId())) {
				balance.put(player, balance.get(p));
				balance.remove(p);
				
				bloodsugar.put(player, bloodsugar.get(p));
				bloodsugar.remove(p);
				
				return;
			}
		}
		
		JSONObject content = JSON.loadDataFromDataBase("diamond_purse.json");
		
		if (content.get(player.getUniqueId().toString()) == null) {
			content.put(player.getUniqueId().toString(), 0L);
			JSON.saveDataFromDataBase("diamond_purse.json", content);
		}
		
		long value = (long) content.get(player.getUniqueId().toString());
		
		balance.put(player, balance.getOrDefault(player, value));
		
		
		
		
		
		content = JSON.loadDataFromDataBase("diamond_sugar.json");
		
		if (content.get(player.getUniqueId().toString()) == null) {
			content.put(player.getUniqueId().toString(), 0L);
			JSON.saveDataFromDataBase("diamond_sugar.json", content);
		}
		
		value = (long) content.get(player.getUniqueId().toString());
		
		bloodsugar.put(player, balance.getOrDefault(player, value));
		
	}
	
	public static void savePurse() {
		JSONObject content = JSON.loadDataFromDataBase("diamond_purse.json");
		
		for (Entry<Player, Long> p : balance.entrySet()) {
			
			content.put(p.getKey().getUniqueId().toString(), p.getValue());
			
		}
		
		JSON.saveDataFromDataBase("diamond_purse.json", content);
		
		
		
		
		
		content = JSON.loadDataFromDataBase("diamond_sugar.json");
		
		for (Entry<Player, Long> p : bloodsugar.entrySet()) {
			
			content.put(p.getKey().getUniqueId().toString(), p.getValue());
			
		}
		
		JSON.saveDataFromDataBase("diamond_sugar.json", content);
	}
	
	public static long getPurse(Player player) {
		return balance.getOrDefault(player, -1L);
	}

	public static double getBloodSugar(Player p) {
		return (double) (bloodsugar.getOrDefault(p, -1L) / 10.0);
	}

	public static void addPurse(Player player, long d) {
		balance.put(player, balance.get(player) + d);
		
		PlayerPurseChangedEvent event = new PlayerPurseChangedEvent(player);
		Bukkit.getPluginManager().callEvent(event);
	}

	public static void addSugar(Player player, long sugar) {
		bloodsugar.put(player, bloodsugar.get(player) + sugar);
		
	}


	public static void removePurse(Player player, long ct) {
		balance.put(player, balance.get(player) - ct);
	}

}
