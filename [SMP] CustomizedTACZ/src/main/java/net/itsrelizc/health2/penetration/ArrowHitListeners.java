package net.itsrelizc.health2.penetration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.ballistics.Collisions;
import net.itsrelizc.health2.ballistics.Collisions.BodyPart;
import net.itsrelizc.health2.ballistics.Collisions.HitDirection;
import net.itsrelizc.health2.ballistics.Collisions.HitSide;
import net.itsrelizc.health2.fletching.ArrowUtils;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowFletching;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowPoint;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowShaft;
import net.itsrelizc.health2.fletching.RelizcNeoArrow;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.npc.AStarPathfinder;
import net.minecraft.nbt.CompoundTag;

public class ArrowHitListeners implements Listener{
	@EventHandler(ignoreCancelled=true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
		// DamageCause.PROJECTILE
		
		
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager().getType() == EntityType.ARROW || event.getDamager().getType() == EntityType.SPECTRAL_ARROW)) return;
        
        Player player = (Player) event.getEntity();
        Arrow damager = (Arrow) event.getDamager();
        
        if (DeathUtils.isDead(player)) {
        	event.setCancelled(true);
        	return;
        }
        
        damager.setKnockbackStrength(0);
        event.setDamage(0);
        
        
        
        return;
        
        
//        Arrow oldArrow = (Arrow) event.getDamager();
//        Location loc = oldArrow.getLocation();
//        Vector oldVelocity = oldArrow.getVelocity();
//        double speed = oldVelocity.length();
//        
//        
//        Vector newDirection = getRandomDirection(); // your method for random vector
//        Vector newVelocity = newDirection.multiply(speed);
//
//        // Remove old arrow
//        PersistentDataContainer oldPDC = oldArrow.getPersistentDataContainer();
//        oldArrow.remove();
//
//        // Spawn new arrow
//        Arrow newArrow = loc.getWorld().spawnArrow(loc, newVelocity, (float) speed, 0f);
//
//        // Copy PersistentDataContainer
//        copyPersistentData(oldArrow, newArrow);
//
//        // Copy other necessary basic properties
//        newArrow.setPickupStatus(oldArrow.getPickupStatus());
//        newArrow.setCritical(oldArrow.isCritical());
//        newArrow.setShooter(player);
//        newArrow.setDamage(oldArrow.getDamage());
//        
//        long penetration = oldArrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "penetration"), PersistentDataType.LONG);
//	      long damage = oldArrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "damage"), PersistentDataType.LONG);
//	      long point = oldArrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "point"), PersistentDataType.LONG);
//	    //arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "breakChance"), PersistentDataType.DOUBLE, shaft.flightBreak);
//
//        Location playerLoc = player.getLocation();
//        Location arrowLoc = oldArrow.getLocation();
//
//        // Player body yaw (0 is south, increases clockwise)
//        float bodyYaw = playerLoc.getYaw();
//
//        // Calculate vector from player feet location to arrow hit location
//        Vector relativeHitVec = arrowLoc.toVector().subtract(playerLoc.toVector());
//
//        // Rotate relativeHitVec by -bodyYaw degrees to get it in player's local coordinate system
//        Vector localVec = rotateVectorAroundY(relativeHitVec, -bodyYaw);
//
//        // Now localVec.x < 0 means left side, > 0 means right side
//        // localVec.y = height difference (vertical)
//        // localVec.z = forward/backward (we can ignore or use for depth)
//
//        int hitPart = Collisions.determineHitPart(localVec);
//        
//
//        if (hitPart != -1) {
//        	////(player.getUniqueId().toString());
//        	Body body = Body.parts.get(player.getUniqueId().toString());
//        	////(body.toString());
//        	
//        	
//        	LivingEntity source = null;
//        	if (oldArrow.getShooter() != null && oldArrow.getShooter() instanceof LivingEntity) {
//        		source = (LivingEntity) oldArrow.getShooter();
//        	}
//        	
//        	////(damage + " ");
//        	body.damage(hitPart, damage, "item.arrow_point.name." + point, null, source);
//        	//body.printStatus();
//        }
    }
	
	public static void copyPersistentData(Entity source, Entity target) {
        PersistentDataContainer sourceContainer = source.getPersistentDataContainer();
        PersistentDataContainer targetContainer = target.getPersistentDataContainer();

        for (NamespacedKey key : sourceContainer.getKeys()) {
            Object value = getValue(sourceContainer, key);
            if (value != null) {
                setValue(targetContainer, key, value);
            }
        }
    }

    // Helper to retrieve the value with the correct type
    private static Object getValue(PersistentDataContainer container, NamespacedKey key) {
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        } else if (container.has(key, PersistentDataType.LONG)) {
            return container.get(key, PersistentDataType.LONG);
        } else if (container.has(key, PersistentDataType.FLOAT)) {
            return container.get(key, PersistentDataType.FLOAT);
        } else if (container.has(key, PersistentDataType.DOUBLE)) {
            return container.get(key, PersistentDataType.DOUBLE);
        } else if (container.has(key, PersistentDataType.STRING)) {
            return container.get(key, PersistentDataType.STRING);
        } else if (container.has(key, PersistentDataType.BYTE)) {
            return container.get(key, PersistentDataType.BYTE);
        } else if (container.has(key, PersistentDataType.SHORT)) {
            return container.get(key, PersistentDataType.SHORT);
        } else if (container.has(key, PersistentDataType.BYTE_ARRAY)) {
            return container.get(key, PersistentDataType.BYTE_ARRAY);
        } else if (container.has(key, PersistentDataType.INTEGER_ARRAY)) {
            return container.get(key, PersistentDataType.INTEGER_ARRAY);
        } else if (container.has(key, PersistentDataType.LONG_ARRAY)) {
            return container.get(key, PersistentDataType.LONG_ARRAY);
        }
        return null; // Unknown or unsupported type
    }

    // Helper to set the value with the correct type
    private static void setValue(PersistentDataContainer container, NamespacedKey key, Object value) {
        if (value instanceof Integer) {
            container.set(key, PersistentDataType.INTEGER, (Integer) value);
        } else if (value instanceof Long) {
            container.set(key, PersistentDataType.LONG, (Long) value);
        } else if (value instanceof Float) {
            container.set(key, PersistentDataType.FLOAT, (Float) value);
        } else if (value instanceof Double) {
            container.set(key, PersistentDataType.DOUBLE, (Double) value);
        } else if (value instanceof String) {
            container.set(key, PersistentDataType.STRING, (String) value);
        } else if (value instanceof Byte) {
            container.set(key, PersistentDataType.BYTE, (Byte) value);
        } else if (value instanceof Short) {
            container.set(key, PersistentDataType.SHORT, (Short) value);
        } else if (value instanceof byte[]) {
            container.set(key, PersistentDataType.BYTE_ARRAY, (byte[]) value);
        } else if (value instanceof int[]) {
            container.set(key, PersistentDataType.INTEGER_ARRAY, (int[]) value);
        } else if (value instanceof long[]) {
            container.set(key, PersistentDataType.LONG_ARRAY, (long[]) value);
        }
    }
	
	public void redirectArrow(Projectile arrow, Vector newDirection) {
	    // Normalize the new direction
	    Vector directionNormalized = newDirection.clone().normalize();

	    // Get the current speed (magnitude of velocity)
	    double speed = arrow.getVelocity().length();

	    // Apply same speed in new direction
	    Vector newVelocity = directionNormalized.multiply(speed);
	    arrow.setVelocity(newVelocity);
	}
	
	public static class Vec2 {
        public final float pitchOffset;
        public final float yawOffset;

        public Vec2(float pitchOffset, float yawOffset) {
            this.pitchOffset = pitchOffset;
            this.yawOffset = yawOffset;
        }
    }

    public static Vec2 getRandomAimOffset(double bpsa) {
        // Convert BPSA to max angular deviation in degrees
        double maxRadians = bpsa / 100.0;  // approx radians
        double maxDegrees = Math.toDegrees(maxRadians);

        // Randomly offset within a circular cone
        double radius = ThreadLocalRandom.current().nextDouble() * maxDegrees;
        double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;

        double yawOffset = Math.cos(angle) * radius;
        double pitchOffset = Math.sin(angle) * radius;

        return new Vec2((float) pitchOffset, (float) yawOffset);
    }
    
    public static Vector applyPitchYawOffsetToVelocity(Vector velocity, float pitchOffsetDeg, float yawOffsetDeg) {
        double speed = velocity.length();

        // Step 1: Convert velocity vector to pitch and yaw
        double x = velocity.getX();
        double y = velocity.getY();
        double z = velocity.getZ();

        float originalPitch = (float) -Math.toDegrees(Math.asin(y / speed));
        float originalYaw = (float) -Math.toDegrees(Math.atan2(x, z));

        // Step 2: Apply pitch and yaw offsets
        float newPitch = originalPitch + pitchOffsetDeg;
        float newYaw = originalYaw + yawOffsetDeg;

        // Step 3: Convert back to direction vector
        Vector newDir = getDirectionVector(newPitch, newYaw);

        // Step 4: Scale to original speed
        return newDir.multiply(speed);
    }
    
	
	@EventHandler
	public void onShootArrow(EntityShootBowEvent event) {
		
		if (!(event.getProjectile() instanceof Arrow)) {
			return;
		}

		LivingEntity player = event.getEntity();
	    Arrow arrow = (Arrow) event.getProjectile();
	    
	    arrow.setCritical(false);
	    
	    ItemStack bow = event.getBow();
	    bow = ItemUtils.castOrCreateItem(bow).getBukkitItem();
	    
	    net.minecraft.world.item.ItemStack bowit = CraftItemStack.asNMSCopy(bow);
	    //System.out.println(bowit.getOrCreateTag());
	    CompoundTag bowtag = bowit.getOrCreateTag();
	    
	    long force = bowtag.getLong("force");
	    
	    double accuracy = bowtag.getDouble("accuracy");
	    //System.out.println(force + " " +  accuracy);
	     
	    
	    double dForce = (double) force;
	    
	    
	    arrow.setPickupStatus(PickupStatus.DISALLOWED);


	 // Get bow draw duration (in ticks)
	    int drawTime = event.getForce() >= 1.0f ? 20 : (int)(event.getForce() * 20);

	    // Recalculate velocity based on draw time
	    double velocity = drawTime / 20.0;
	    velocity = (velocity * velocity + velocity * 2.0) / 3.0;
	    if (velocity > 1.0) velocity = 1.0;

	    // Apply direction from pitch/yaw
	    float pitch = player.getLocation().getPitch();
	    float yaw = player.getLocation().getYaw();
	    Vector direction = getDirectionVector(pitch, yaw);
	    
	    arrow.getLocation().setPitch(pitch);
	    arrow.getLocation().setYaw(yaw);

	    // Multiply by vanilla bow power (max ≈ 3.0 blocks/tick)
	    
	    

	    ItemStack usedArrow = event.getConsumable(); // The actual arrow used!
	    
	    

	    if (usedArrow == null || usedArrow.getType() == Material.AIR) {
	        usedArrow = ItemUtils.createItem(RelizcNeoArrow.class, null).getBukkitItem();
	    }

	    net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(usedArrow);
	    CompoundTag tag = it.getOrCreateTag();
	    
	    //System.out.println(tag.toString());
	    
	    
	    ArrowPoint point = ArrowUtils.getPoint(tag.getInt("point"));
	    ArrowShaft shaft = ArrowUtils.getShaft(tag.getInt("shaft"));
	    ArrowFletching fletching = ArrowUtils.getFletching(tag.getInt("fletching"));
	    
	    double dWeight = point.seedWeight + shaft.seedWeight + fletching.seedWeight;
	    
	    double forceRatio = dForce / dWeight;
	    double actualSpeed = 4 * forceRatio;
	    actualSpeed *= (1- fletching.resistance);
	    
	    //System.out.println(actualSpeed);
	    
	    Vector newVelocity = direction.multiply(velocity * actualSpeed).add(player.getVelocity());
	    
	    double finalAccuracy = Math.max(accuracy + fletching.stability, 0d);
	    Vec2 offset = getRandomAimOffset(finalAccuracy);
	    
	    newVelocity = applyPitchYawOffsetToVelocity(newVelocity, offset.pitchOffset, offset.yawOffset);
	    arrow.setVelocity(newVelocity);
	    //arrow.setTicksLived(-5);
	    
	    //FragUtils.spawnArrowFragment(arrow.getLocation(), newVelocity.normalize(), velocity * actualSpeed);
	    
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "penetration"), PersistentDataType.LONG, point.penetration);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "damage"), PersistentDataType.LONG, point.damage);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "breakChance"), PersistentDataType.DOUBLE, shaft.flightBreak);
	    arrow.getPersistentDataContainer().set(new NamespacedKey(EventRegistery.main, "point"), PersistentDataType.LONG, point.id);
	    arrowLocationStorage.put(arrow, arrow.getLocation());
	    getLastTicksLived.put(arrow, Integer.MIN_VALUE);
	    
	}
	
	public static List<Player> getAllPlayerEntities() {
        List<Player> players = new ArrayList<>();

        // Add all real online players
        players.addAll(Bukkit.getOnlinePlayers());

        // Loop through all NPCs and check if they're player-type
        for (NPC npc : CitizensAPI.getNPCRegistry()) {
            if (npc.isSpawned() && npc.getEntity() instanceof Player) {
                players.add((Player) npc.getEntity());
            }
        }

        return players;
    }
	
	public static void startDetectingArrows() {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				Iterator<Map.Entry<Arrow, Location>> iterator = arrowLocationStorage.entrySet().iterator();

				while (iterator.hasNext()) {
				    Map.Entry<Arrow, Location> entry = iterator.next();
				    
				    
				    
				    Arrow arrow = entry.getKey();
				    ////(arrow.getTicksLived() + "");
				    Location previous = entry.getValue();
				    Location current = arrow.getLocation();
				    
				    long penetration = arrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "penetration"), PersistentDataType.LONG);
            	    long damage = arrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "damage"), PersistentDataType.LONG);
            	    long point = arrow.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "point"), PersistentDataType.LONG);

				    //AStarPathfinder.drawParticleLine(current.getWorld(), current, previous, 0.5);
            	    
            	    if (arrow.getLocation().getBlock().getType() == Material.END_PORTAL || arrow.getLocation().getBlock().getType() == Material.NETHER_PORTAL) {
            	    	// Patch 8.1.52-7: Fixed arrows causing damage in portals
            	    	
            	    	arrow.setTicksLived(999999); 
            	    	
            	    }
            	    
            	    
            	    
				    if (arrow.getTicksLived() > 0) {
				    	
				    	
				    	
				    	
				    	for (LivingEntity player : getAllPlayerEntities()) {
				    		
				    		if (!(player.getLocation().getWorld().getName().equals(current.getWorld().getName()))) continue;
				    		
				    		if (player.getLocation().distance(current) > arrow.getVelocity().length()) continue;
				    		
				    		if (arrow.getShooter() instanceof Player) {
				    			Player shooter = (Player) arrow.getShooter();
				    			if (player.getUniqueId().equals(shooter.getUniqueId())) continue;
				    		}
				    		
				    		Body body = Body.parts.get(player.getUniqueId().toString());
	                        if (body == null) {
	                        	continue;
	                        }
				    		
				    		
		                	
//		                    if (loc.distance(player.getLocation()) > 4) continue; // distance check optimization\
	                        Vector direction = current.toVector().subtract(previous.toVector()).normalize();
	                        Location extended = current.clone().add(direction); // 1 block further
		                    BodyPart hit = Collisions.getHitBodyPart(player, extended, previous);
		                    
		                    if (hit != BodyPart.NONE) {
		                        HitDirection side = Collisions.getHitSide(current, player);
		                        
		                        int result = -1;
		                        if (hit == BodyPart.HEAD) result = 0;
		                        else if (hit == BodyPart.CHEST) result = (side.cardinal == HitSide.LEFT) ? 4 : (side.cardinal == HitSide.RIGHT) ? 3 : 1;
		                        else if (hit == BodyPart.STOMACH) result = 2;
		                        else if (hit == BodyPart.FEET || hit == BodyPart.LEGS) result = (side.leftOrRightOnly == HitSide.LEFT) ? 5 : 6;
		                        
		                        ////(player + " was hit on " + result);
		                        
		                        
		                        
		                        //(result +  "" + player);
		                        
		                        int hitresult;
		                	    
		                	    if (arrow.getShooter() instanceof LivingEntity) {
		                	    	hitresult = body.damageWithPenetrationCheck(result, damage, "item.arrow_point.name." + point, null, (LivingEntity) arrow.getShooter(), penetration, hit);
		                	    } else {
		                	    	hitresult = body.damageWithPenetrationCheck(result, damage, "item.arrow_point.name." + point, null, null, penetration, hit);
		                	    }
		                	    
		                	    if (hitresult == 0) {
		                	    } else if (hitresult == 2) {
		                	    	
		                	    } else if (hitresult == 1) {
		                	    	ArrowUtils.shootArrowWithSameVelocityInDirection(arrow, getRandomDirection(), arrow.getShooter());
		                	    }
		                        
		                	    arrow.setTicksLived(999999);
		                        
		                        break;
		                    }
		                }
				    }

				    
				    
				    arrowLocationStorage.put(arrow, current);
				    
				    int currentTick = getLastTicksLived.get(arrow);
				    
				    if (arrow.isInBlock() || !current.getChunk().isLoaded() || arrow.getTicksLived() > 30 * 20 || currentTick == arrow.getTicksLived()) {
				        arrow.remove(); // Remove the arrow entity from the world
				        iterator.remove(); // Safely remove the entry from the map
				        arrowLocationStorage.remove(arrow);
				        getLastTicksLived.remove(arrow);
				        continue;
				    }
				    
				    getLastTicksLived.put(arrow, arrow.getTicksLived());
				}
			}
			
		}.runTaskTimer(EventRegistery.main, 0, 1l);
		
	}
	
	public static HashMap<Arrow, Location> arrowLocationStorage = new HashMap<Arrow, Location>();
	public static HashMap<Arrow, Integer> getLastTicksLived = new HashMap<Arrow, Integer>();
	
	public static Vector getRandomDirection() {
	    ThreadLocalRandom rand = ThreadLocalRandom.current();

	    // Generate spherical coordinates
	    double theta = rand.nextDouble(0, 2 * Math.PI); // azimuthal angle
	    double phi = Math.acos(2 * rand.nextDouble() - 1); // polar angle (uniform sphere)

	    // Convert to Cartesian coordinates
	    double x = Math.sin(phi) * Math.cos(theta);
	    double y = Math.sin(phi) * Math.sin(theta);
	    double z = Math.cos(phi);

	    return new Vector(x, y, z).normalize(); // Unit vector
	}
	
	public static Vector getDirectionVector(float pitch, float yaw) {
	    // Convert angles to radians
	    double pitchRad = Math.toRadians(-pitch);
	    double yawRad = Math.toRadians(-yaw);

	    double x = Math.sin(yawRad) * Math.cos(pitchRad);
	    double y = Math.sin(pitchRad);
	    double z = Math.cos(yawRad) * Math.cos(pitchRad);

	    return new Vector(x, y, z).normalize();
	}

	
	private void checkArmorBeforeDamage() {
		
	}
	
	private Vector rotateVectorAroundY(Vector vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.getX() * cos - vec.getZ() * sin;
        double z = vec.getX() * sin + vec.getZ() * cos;

        return new Vector(x, vec.getY(), z);
    }
}
