package net.itsrelizc.smp.modsmp;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;
import net.itsrelizc.health2.Body.PlayerBodyHealthStatusChangedEvent;
import net.itsrelizc.players.PlayerAFKEvent;
import net.itsrelizc.quests.QuestUtils.PlayerQuestStatusChangedEvent;
import net.itsrelizc.smp.modsmp.SMPScoreboard.Pages;

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
	
	@EventHandler
	public void change(PlayerQuestStatusChangedEvent event) {
		
		SMPScoreboard board = SMPScoreboard.boards.get(event.getPlayer());
		board.refreshMainPage();
		
	}
	
	@EventHandler
	public void ghost(PlayerGhostEvent event) {
		if (event.isGhost()) {
			
			SMPScoreboard b = SMPScoreboard.boards.get(event.getPlayer());
			b.setPage(Pages.DEATH);
			
		}
	}
	
	@EventHandler
	public void change(PlayerBodyHealthStatusChangedEvent event) {

		
		SMPScoreboard b = SMPScoreboard.boards.get(event.getPlayer());
		if (b == null) return;
		
		if (event.getBody().getHealth() != event.getBody().getMaxHealth()) {
			b.setPage(Pages.HEALTH);
		} else {
			
			new BukkitRunnable() {

				@Override
				public void run() {
					
					if (event.getBody().getHealth() != event.getBody().getMaxHealth()) return;
					
					b.setPage(Pages.MAIN);
					
				}
				
			}.runTaskLater(EventRegistery.main, 100L);
			
		}
		
		//Bukkit.broadcastMessage(event.getBody().getHealth() + " ");
		b.changed(event.getLimbId());
		
	}

}
