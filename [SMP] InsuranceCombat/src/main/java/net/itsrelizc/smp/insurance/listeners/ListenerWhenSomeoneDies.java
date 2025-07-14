package net.itsrelizc.smp.insurance.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.itsrelizc.gunmod.deathutils.DeathUtils.PlayerGhostEvent;
import net.itsrelizc.players.locales.Locale;

public class ListenerWhenSomeoneDies implements Listener {
	
	@EventHandler
	public void dead(PlayerGhostEvent event) {
		
		if (!event.isGhost()) return;
		
		Player player = event.getPlayer();
		
		event.getPlayer().sendTitle("§c§l" + Locale.a(player, "general.yousuck"), "§e" + Locale.a(player, convert(event.getCause())), 0, 20 * 3, 10);
		
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
	
	public static Location findRandomNearbySpawnableLocationBogo(Location origin, int radius, int maxAttempts) {
	    World world = origin.getWorld();
	    if (world == null) return null;

	    Random random = new Random();
	    int ox = origin.getBlockX();
	    int oy = origin.getBlockY();
	    int oz = origin.getBlockZ();

	    int minY = Math.max(oy - radius, world.getMinHeight() + 1);
	    int maxY = Math.min(oy + radius, world.getMaxHeight() - 2);

	    for (int attempts = 0; attempts < maxAttempts; attempts++) {
	        int x = ox + random.nextInt(radius * 2 + 1) - radius;
	        int y = random.nextInt(maxY - minY + 1) + minY;
	        int z = oz + random.nextInt(radius * 2 + 1) - radius;

	        Block ground = world.getBlockAt(x, y - 1, z);
	        Block space1 = world.getBlockAt(x, y, z);
	        Block space2 = world.getBlockAt(x, y + 1, z);

	        if (ground.getType().isSolid() &&
	            space1.getType() == Material.AIR &&
	            space2.getType() == Material.AIR) {

	            return new Location(world, x + 0.5, y, z + 0.5);
	        }
	    }

	    return null; // Couldn't find a valid spot in given attempts
	}

}
