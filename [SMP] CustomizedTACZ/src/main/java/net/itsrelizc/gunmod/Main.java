package net.itsrelizc.gunmod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.itsrelizc.health2.Body;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		
		Bukkit.getPluginManager().registerEvents(this, this);
		
	}
	
	@EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Arrow )) return;
        
        Player player = (Player) event.getEntity();
        Arrow arrow = (Arrow) event.getDamager();

        Location playerLoc = player.getLocation();
        Location arrowLoc = arrow.getLocation();

        // Player body yaw (0 is south, increases clockwise)
        float bodyYaw = playerLoc.getYaw();

        // Calculate vector from player feet location to arrow hit location
        Vector relativeHitVec = arrowLoc.toVector().subtract(playerLoc.toVector());

        // Rotate relativeHitVec by -bodyYaw degrees to get it in player's local coordinate system
        Vector localVec = rotateVectorAroundY(relativeHitVec, -bodyYaw);

        // Now localVec.x < 0 means left side, > 0 means right side
        // localVec.y = height difference (vertical)
        // localVec.z = forward/backward (we can ignore or use for depth)

        String hitPart = determineHitPart(localVec);

        player.sendMessage("Hit on your " + hitPart);
    }

    // Helper: rotate vector around Y axis by degrees
    private Vector rotateVectorAroundY(Vector vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.getX() * cos - vec.getZ() * sin;
        double z = vec.getX() * sin + vec.getZ() * cos;

        return new Vector(x, vec.getY(), z);
    }

    // Determine hit part based on local vector
    private String determineHitPart(Vector vec) {
        double y = vec.getY();

        // Height zones (approximate for standing player)
        if (y > 1.6) {
            return "head";
        } else if (y > 1) {
            // Upper torso and arms zone
            if (vec.getX() > 0.3) {
                return "larm";
            } else if (vec.getX() < -0.3) {
                return "rarm";
            } else {
                if (y > 1.2) {
                	return "chest";
                } else {
                	return "abs";
                }
            }
        } else {
        	if (vec.getX() > 0) {
                return "lleg";
            } else if (vec.getX() < 0) {
                return "rleg";
            } else {
                return "miss";
            }
        }
    }
    
    @EventHandler
    public void join(PlayerJoinEvent event) {
    	
    	if (!Body.parts.containsKey(event.getPlayer().getUniqueId().toString())) {
    		Body.parts.put(event.getPlayer().getUniqueId().toString(), new Body(event.getPlayer()));
    	}
    	
    }
    


}
