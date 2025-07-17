package net.itsrelizc.gunmod.deathutils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;
import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.locales.Locale;

public class PlayerGhostOutsideBoundChecker implements Listener {
	
	public static class GhostManager {
	    public static class GhostData {
	        public final Location deathLocation;
	        public int violations = 0;

	        public GhostData(Location deathLocation) {
	            this.deathLocation = deathLocation;
	        }
	    }

	    private static final Map<UUID, GhostData> ghostPlayers = new HashMap<>();

	    public static void addGhost(Player player, Location deathLocation) {
	        ghostPlayers.put(player.getUniqueId(), new GhostData(deathLocation));
	    }

	    public static void removeGhost(Player player) {
	        ghostPlayers.remove(player.getUniqueId());
	    }

	    public static GhostData getGhostData(Player player) {
	        return ghostPlayers.get(player.getUniqueId());
	    }

	    public static boolean isGhost(Player player) {
	        return ghostPlayers.containsKey(player.getUniqueId());
	    }
	}

	
	@EventHandler
	public void onPlayerGhost(PlayerGhostEvent event) {
	    if (event.isGhost()) {
	        Player player = event.getPlayer();
	        Location deathLoc = player.getLocation();
	        GhostManager.addGhost(player, deathLoc);
	    } else {
	        GhostManager.removeGhost(event.getPlayer());
	    }
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
	    Player player = event.getPlayer();
	    if (!GhostManager.isGhost(player)) return;

	    GhostManager.GhostData data = GhostManager.getGhostData(player);
	    Location deathLoc = data.deathLocation;
	    Location current = player.getLocation();

	    double distance = current.distance(deathLoc);
	    if (distance > 64) {
	        data.violations++;

	        if (data.violations >= 5) {
	            //player.teleport(deathLoc);
	            CustomPlayerTeleportEvent.teleport(player, deathLoc);
	            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1f);
	            
	            
	            player.sendMessage(Locale.a(player, "death.move.toofar"));
	        } else {
	            // Bounce back with velocity
	            Vector bounce = deathLoc.toVector().subtract(current.toVector()).normalize().multiply(2);
	            bounce.setY(0.2); // Give some vertical lift
	            player.setVelocity(bounce);
	            player.sendMessage(Locale.a(player, "death.move.toofar"));
	            
	            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP, 1f, 1f);
	            
            	Vector bounceDir = bounce.clone().normalize();

				 // Choose a world-up vector (as long as it’s not parallel to bounceDir)
				 Vector up = new Vector(0, 1, 0);
				
				 // First perpendicular axis (horizontal)
				 Vector right = crossProduct(bounceDir, up).normalize();
				
				 // Second perpendicular axis (vertical-ish)
				 Vector upAdjusted = crossProduct(right, bounceDir).normalize();
				
				 // Build the plane
				 Location center = player.getLocation().subtract(bounceDir.clone().multiply(1));
				 int steps = 6;
				 double scale = 0.5;
				
				 for (int i = -steps; i <= steps; i++) {
				     for (int j = -steps; j <= steps; j++) {
				         Vector offset = right.clone().multiply(i * scale).add(upAdjusted.clone().multiply(j * scale));
				         Location spawnLoc = center.clone().add(offset);
				         player.spawnParticle(Particle.SPELL_WITCH, spawnLoc, 1, 0, 0, 0, 0);
				         player.spawnParticle(Particle.FIREWORKS_SPARK, spawnLoc, 1, 0, 0, 0, 0);
				     }
				 }


	        }
	    }
	}
	
	public static Vector crossProduct(Vector a, Vector b) {
	    double x = a.getY() * b.getZ() - a.getZ() * b.getY();
	    double y = a.getZ() * b.getX() - a.getX() * b.getZ();
	    double z = a.getX() * b.getY() - a.getY() * b.getX();
	    return new Vector(x, y, z);
	}

}
