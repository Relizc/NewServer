package net.itsrelizc.diamonds;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;

public class DiamondPurse {
	
	private static HashMap<Player, Long> balance = new HashMap<Player, Long>();
	private static HashMap<Player, Long> bloodsugar = new HashMap<Player, Long>();
	
	public static void loadPurse(Player player) {
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
	
	public static float getPurse(Player player) {
		return (float) (balance.getOrDefault(player, -1L) / 1000.0);
	}

	public static double getBloodSugar(Player p) {
		return (double) (bloodsugar.getOrDefault(p, -1L) / 10.0);
	}

	public static void addPurse(Player player, int d) {
		balance.put(player, balance.get(player) + d);
		
	}

	public static void addSugar(Player player, long sugar) {
		bloodsugar.put(player, bloodsugar.get(player) + sugar);
		
	}

}
