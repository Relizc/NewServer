package net.itsrelizc.health2.ballistics;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.itsrelizc.gunmod.deathutils.DeathUtils;

public class FragUtils {
	
	public static void spawnStandardFragmentation(Location center, int rays, double velocity) {
		
		List<Player> players = getPlayersInRadius(center, Bullet.maxDistance);
		
		for (int i = 0; i < rays; i++) {
			Vector random = Vectors.getRandomUnitVector().multiply(velocity);
	        Bullet bullet = new Bullet(center, random, velocity, players);
	        bullet.execute();
	    }
		
	}
	
	public static void spawnSingleFragment(Location center, Vector direction, double velocity) {
		
		List<Player> players = getPlayersInRadius(center, Bullet.maxDistance);

        Bullet bullet = new Bullet(center, direction, velocity, players);
        bullet.execute();

		
	}
	
	private static List<Player> getPlayersInRadius(Location center, double radius) {
	    return Bukkit.getOnlinePlayers().stream()
	        .filter(player -> player.getWorld().equals(center.getWorld()))
	        .filter(player -> player.getLocation().distance(center) <= radius)
	        .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
	        .filter(player -> !DeathUtils.isDead(player))
	        .collect(Collectors.toList());
	}

}
