package net.itsrelizc.scoreboards;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.itsrelizc.events.TaskDelay;

public class GameInfoBoardHandler implements Listener {
	
	private Map<Player, RelizcScoreboard> boards = new HashMap<Player, RelizcScoreboard>();
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		TaskDelay.delayTask(new Runnable() {

			@Override
			public void run() {
				RelizcScoreboard board = new RelizcScoreboard(event.getPlayer());
			}
			
		}, 20L);
		Player player = event.getPlayer();
	}
	
	@EventHandler
	public void join(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		boards.remove(player);
	}
	
	

}
