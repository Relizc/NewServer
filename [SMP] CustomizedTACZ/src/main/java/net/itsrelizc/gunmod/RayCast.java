package net.itsrelizc.gunmod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

@Deprecated
public class RayCast {
	
	public static boolean rayIntersectsAABB(Vector rayOrigin, Vector rayDirection, Vector boxMin, Vector boxMax) {
        // Calculate tMin and tMax for each axis
        double tMinX = (boxMin.getX() - rayOrigin.getX()) / rayDirection.getX();
        double tMaxX = (boxMax.getX() - rayOrigin.getX()) / rayDirection.getX();

        double tMinY = (boxMin.getY() - rayOrigin.getY()) / rayDirection.getY();
        double tMaxY = (boxMax.getY() - rayOrigin.getY()) / rayDirection.getY();

        double tMinZ = (boxMin.getZ() - rayOrigin.getZ()) / rayDirection.getZ();
        double tMaxZ = (boxMax.getZ() - rayOrigin.getZ()) / rayDirection.getZ();

        // Ensure tMin < tMax for each axis
        if (tMinX > tMaxX) {
            double temp = tMinX;
            tMinX = tMaxX;
            tMaxX = temp;
        }
        if (tMinY > tMaxY) {
            double temp = tMinY;
            tMinY = tMaxY;
            tMaxY = temp;
        }
        if (tMinZ > tMaxZ) {
            double temp = tMinZ;
            tMinZ = tMaxZ;
            tMaxZ = temp;
        }

        // Find the intersection ranges
        double tEnter = Math.max(Math.max(tMinX, tMinY), tMinZ);
        double tExit = Math.min(Math.min(tMaxX, tMaxY), tMaxZ);

        // The ray intersects if tEnter <= tExit and tExit >= 0
        return tEnter <= tExit && tExit >= 0;
    }
	
	public static boolean rayIntersectsAABBWithGravity(Vector rayOrigin, Vector rayDirection, Vector boxMin, Vector boxMax, double bulletSpeed) {
//	    // Calculate distance from ray origin to box center
//	    Vector boxCenter = boxMin.clone().midpoint(boxMax);
//	    double distance = rayOrigin.distance(boxCenter);
//
//	    // Calculate vertical shift due to gravity
//	    double time = distance / bulletSpeed; // Time of flight
//	    double yShift = 0.5 * Hit.g * time * time;
//	    
////	    Bukkit.broadcastMessage("g: " + Hit.g + " bs: " + bulletSpeed);
////	    Bukkit.broadcastMessage("distance of projectile " + distance);
////	    Bukkit.broadcastMessage("time of projectile " + time);
////	    Bukkit.broadcastMessage("yShift of projectile " + yShift);
//
//	    // Adjust the AABB to simulate gravity
//	    boxMin.add(new Vector(0, yShift, 0));
//	    boxMax.add(new Vector(0, yShift, 0));
//
//	    // Perform the standard ray-AABB intersection check
//	    boolean hit = rayIntersectsAABB(rayOrigin, rayDirection, boxMin, boxMax);
//	    
//	    
//	    
//	    return hit;
		return false;
	}
//	
//	public static String rayIntersectsPlayerWithBodyParts(Vector rayOrigin, Vector rayDirection, Player player, double bulletSpeed) {
//	    BoundingBox hitbox = player.getBoundingBox();
//	    Vector boxMin = hitbox.getMin();
//	    Vector boxMax = hitbox.getMax();
//
//	    // Gravity adjustment
//	    double distance = rayOrigin.distance(boxMin);
//	    
//	    Bukkit.broadcastMessage("Distance: " + distance);
//	    
//	    double time = distance / bulletSpeed;
//	    double gravity = Hit.g; // Minecraft's gravity in blocks/second^2
//	    double yShift = 0.5 * gravity * time * time;
//
//	    // Apply gravity shift
//	    boxMin.add(new Vector(0, yShift, 0));
//	    boxMax.add(new Vector(0, yShift, 0));
//
//	    // Perform ray-AABB intersection
//	    boolean hit = rayIntersectsAABB(rayOrigin, rayDirection, boxMin, boxMax);
//	    if (!hit || distance < 0) return "No Hit";
//
//        double hitY = rayOrigin.getY() + rayDirection.getY() * distance;
//        double relativeY = hitY - boxMin.getY();
//
//        // Body part detection
//        if (relativeY < 0.6) {
//            return "Feet - Distance: " + relativeY;
//        } else if (relativeY < 1.8) {
//            return "Chest - Distance: " + relativeY;
//        } else {
//            return "Head - Distance: " + relativeY;
//        }
//
//	}

	
	public void checkBulletHit(Player shooter, Location bulletStart, Vector bulletDirection) {
        // Normalize the direction vector
        bulletDirection.normalize();

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(shooter)) {
                continue; // Skip the shooter
            }

            // Get the player's hitbox (AABB)
            Location playerLocation = target.getLocation();
            Vector boxMin = playerLocation.toVector().add(new Vector(-0.3, 0, -0.3)); // Adjust for width
            Vector boxMax = playerLocation.toVector().add(new Vector(0.3, 1.8, 0.3)); // Adjust for height

            // Check for intersection
            if (RayCast.rayIntersectsAABB(bulletStart.toVector(), bulletDirection, boxMin, boxMax)) {
                shooter.sendMessage("You hit " + target.getName() + "!");
                target.sendMessage("You were hit by " + shooter.getName() + "!");
                break; // Stop checking after a hit
            }
        }
    }
	
	

}
