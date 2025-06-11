package net.itsrelizc.smp.modsmp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.PlayerAFKEvent;
import net.itsrelizc.scoreboards.RelizcScoreboard;

public class SMPScoreboardListener implements Listener {
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerJoinEvent event) {


		SMPScoreboard board = new SMPScoreboard(event.getPlayer());


		
	}
	
	static Set<Player> afkers = new HashSet<Player>();
	
	@EventHandler
	public void s(PlayerAFKEvent event) {
		SMPScoreboard b = SMPScoreboard.boards.get(event.getPlayer());
		if (event.isAFK()) {
			afkers.add(event.getPlayer());
		} else {
			afkers.remove(event.getPlayer());
		}
		b.updateStatus();
	}
	
	public static String getAFKStatus(Player player) {
		if (afkers.contains(player)) {
			return "§7𝗓ᶻ ";
		} else {
			return "";
		}
	}

}
