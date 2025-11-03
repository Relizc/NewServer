package net.itsrelizc.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.npc.NPC;

public class LookAtPlayerTask extends BukkitRunnable {
	
	private static class LookHelper {

	    /**
	     * Gets a target location a certain distance ahead based on yaw and pitch.
	     *
	     * @param origin The starting location (NPC head or spawn location)
	     * @param distance How far ahead to look (e.g., 5)
	     * @return The target location ahead
	     */
	    public static Location getLookTargetLocation(Location origin, double distance) {
	        Location loc = origin.clone();
	        Vector dir = getDirectionFromYawPitch(loc.getYaw(), loc.getPitch());
	        return loc.add(dir.multiply(distance));
	    }

	    /**
	     * Converts yaw and pitch into a normalized direction vector.
	     */
	    private static Vector getDirectionFromYawPitch(float yaw, float pitch) {
	        double pitchRad = Math.toRadians(pitch);
	        double yawRad = Math.toRadians(-yaw - 90); // align with Bukkit coordinate system

	        double x = Math.cos(pitchRad) * Math.cos(yawRad);
	        double y = -Math.sin(pitchRad);
	        double z = Math.cos(pitchRad) * Math.sin(yawRad);

	        return new Vector(x, y, z).normalize();
	    }
	}
	
	
	
	
    private final NPC npc;
    private final double range;
	private Location defaul;

    public LookAtPlayerTask(NPC npc, double range, Location defaul) {
        this.npc = npc;
        this.range = range;
        this.defaul = defaul;
    }

    @Override
    public void run() {
        // Ensure NPC is still spawned
        if (!npc.isSpawned()) {
        	this.cancel();
            return;
            
        }

        Location npcLoc = npc.getEntity().getLocation();
        Player nearest = null;
        double nearestDistSq = range * range;

        for (Player player : npcLoc.getWorld().getPlayers()) {
            // skip offline or same as NPC entity if relevant
            if (!player.isOnline()) {
                continue;
            }

            Location pLoc = player.getLocation();
            double distSq = pLoc.distanceSquared(npcLoc);
            if (distSq < nearestDistSq) {
                nearestDistSq = distSq;
                nearest = player;
            }
        }

        if (nearest != null) {
            // Make NPC look at the player
            // Option 1: Use faceLocation
            npc.faceLocation(nearest.getLocation());

            // Option 2: If you want to constrain yaw/pitch speed, you may
            // retrieve the bukkit entity and adjust manually using setYaw/setPitch etc.
        } else {
        	npc.faceLocation(LookHelper.getLookTargetLocation(defaul.clone().add(0, 1.6, 0), 5));
        }
    }

    public static void scheduleFor(Plugin plugin, NPC npc, double range, long intervalTicks, Location defaul) {
        LookAtPlayerTask task = new LookAtPlayerTask(npc, range, defaul);
        task.runTaskTimer(plugin, 0L, intervalTicks);
    }
}

