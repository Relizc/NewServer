package net.itsrelizc.smp.insurance.listeners;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;
import net.itsrelizc.players.locales.Locale;

public class ListenerWhenSomeoneDies implements Listener {
	
	@EventHandler
	public void dead(PlayerGhostEvent event) {
		
		if (!event.isGhost()) return;
		
		Player player = event.getPlayer();
		
		event.getPlayer().sendTitle("§c§l" + Locale.a(player, "general.yousuck"), "§e" + Locale.a(player, convert(event.getCause())), 0, 20 * 10, 10);
		
	}
	
	private static String convert(String cause) {
		
		Random random = new Random();
		
		if (cause == null) {
			return "general.yousuck.generic" + random.nextInt(10);
		}
		
		if (random.nextInt(10) == 0) {
			
			
			
			if (cause.equals("damage.fragment")) {
				return "general.yousuck.block_explosion" + random.nextInt(3);
			} 
			
		}
		
		return "general.yousuck.generic" + random.nextInt(10);
		
	}

}
