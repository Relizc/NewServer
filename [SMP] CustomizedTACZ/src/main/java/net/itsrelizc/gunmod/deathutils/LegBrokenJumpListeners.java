package net.itsrelizc.gunmod.deathutils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.itsrelizc.health2.Body;

public class LegBrokenJumpListeners implements Listener {
	
	Map<UUID, Boolean> wasInAir = new HashMap<>();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    UUID uuid = player.getUniqueId();

	    boolean onGround = player.isOnGround();

	    boolean wasAir = wasInAir.getOrDefault(uuid, false);

	    if (wasAir && onGround) {
	        
	    	Body body = Body.parts.get(uuid.toString());
	    	
	    	Random rand = new Random();
	    	
	    	if (body.convert(5).getHealth() <= 0) {
	    		body.damage(rand.nextInt(7), 1, "damage.fall", body.convert(5));
	    	}
	    	if (body.convert(6).getHealth() <= 0) {
	    		body.damage(rand.nextInt(7), 1, "damage.fall", body.convert(6));
	    	}
	    	
	    	
	    	
	    }

	    wasInAir.put(uuid, !onGround);
	}

}
