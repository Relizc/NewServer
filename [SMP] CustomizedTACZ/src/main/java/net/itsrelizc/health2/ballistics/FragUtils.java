package net.itsrelizc.health2.ballistics;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.itsrelizc.gunmod.deathutils.DeathUtils;

public class FragUtils {
	
	public static void spawnStandardFragmentation(Location center, int rays, double velocity) {

		List<LivingEntity> players = getPlayersInRadius(center, Bullet.maxDistance);
		
		for (int i = 0; i < rays; i++) {
			Vector random = Vectors.getRandomUnitVector().multiply(velocity);
	        Bullet bullet = new Bullet(center, random, velocity, players);
	        bullet.execute();
	    }
		
	}
	
	public static void spawnSingleFragment(Location center, Vector direction, double velocity) {
		
		List<LivingEntity> players = getPlayersInRadius(center, Bullet.maxDistance);

        Bullet bullet = new Bullet(center, direction, velocity, players);
        bullet.execute();

		
	}
	
	public static void spawnFragmentsRandomly(Location center, double velocity, int count) {
	    Random random = new Random();

	    for (int i = 0; i < count; i++) {
	        Vector dir = getRandomUnitVector(random);
	        spawnSingleFragment(center, dir, velocity);
	    }
	}
	
	private static Vector getRandomUnitVector(Random random) {
	    double theta = 2 * Math.PI * random.nextDouble();  // azimuthal angle
	    double phi = Math.acos(2 * random.nextDouble() - 1);  // polar angle

	    double x = Math.sin(phi) * Math.cos(theta);
	    double y = Math.sin(phi) * Math.sin(theta);
	    double z = Math.cos(phi);

	    return new Vector(x, y, z);
	}
	
	private static List<LivingEntity> getPlayersInRadius(Location center, double radius) {

	    return center.getWorld().getNearbyEntities(center, radius, radius, radius).stream()
	    	    .filter(e -> e instanceof LivingEntity)
	    	    .map(e -> (LivingEntity) e)
	    	    .toList(); // Java 16+ (or use collect(Collectors.toList()) for older versions)
	}

}
