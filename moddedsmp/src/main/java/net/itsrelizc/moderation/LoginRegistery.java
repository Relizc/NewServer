package net.itsrelizc.moderation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.json.simple.JSONObject;

public class LoginRegistery implements Listener {
	
	private static String template = "§c由于你违反了§a§n公平游戏条款§r§c，你已被服务器§l封禁\n\n";
	
	@EventHandler
	public void login(PlayerLoginEvent event) {
		
		JSONObject loader = DataManager.loadPureJsonFromDb("banlist.json");
		
		Object data = loader.get(event.getPlayer().getUniqueId().toString());
		
		if (data == null) {
			
		} else {
			JSONObject content = (JSONObject) data;
			
			String id = (String) content.get("id");
			
			String reason = (String) content.get("reason");
			
			Long finish = (Long) content.get("expires");
		}
		
	}

}
