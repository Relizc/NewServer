package net.itsrelizc.diamonds;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;

public class DiamondPurse {
	
	private static HashMap<Player, Long> balance = new HashMap<Player, Long>();
	
	public static void loadPurse(Player player) {
		JSONObject content = JSON.loadDataFromDataBase("diamond_purse.json");
		
		if (content.get(player.getUniqueId().toString()) == null) {
			content.put(player.getUniqueId().toString(), 0L);
			JSON.saveDataFromDataBase("diamond_purse.json", content);
		}
		
		long value = (long) content.get(player.getUniqueId().toString());
		
		balance.put(player, value);
		
	}
	
	public static float getPurse(Player player) {
		return (float) (balance.getOrDefault(player, -1L) / 100000.0);
	}

}
