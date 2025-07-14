package net.itsrelizc.health2.ballistics;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.Limb.RelizcDamageCause;
import net.itsrelizc.health2.ballistics.Collisions.BodyPart;
import net.itsrelizc.health2.ballistics.Collisions.HitDirection;
import net.itsrelizc.health2.ballistics.Collisions.HitSide;

public class Bullet {
	
	public static int rays = 24;
	public static double speed = 7;
	public static int maxSteps = 256;
	public static double maxDistance = 212;
	public static double gravity = 0.07;
	public static double airdrag = 0.02;
	
	private BukkitRunnable task;
	
	private int bounces = 0;
	private int maxBounces = 8;
	
	
	
	/**W
	 * Shoots a bullet-like ray
	 * 
	 * 
	 * @param center The origin of the ray.
	 */
	public Bullet(Location center, Vector direction, Double speed, List<LivingEntity> players) {

        
        
        World world = center.getWorld();

        task = new BukkitRunnable() {
            int currentStep = 0;
            Location loc = center.clone();
            Location current = center.clone();
            Vector velocity = direction.normalize().multiply(speed);
            
            Random rand = new Random();
            
            int correct = 0;
            int incorrect = 0;
            int op = 0;
            
            @Override
            public void cancel() {
            	super.cancel();
            	current.getWorld().playSound(current, Sound.ENTITY_PLAYER_BIG_FALL, 1.2f, 1.5f);
            	////("cancelled ray with " + op + " resourceful calcs suposed " + correct);
            }

            @Override
            public void run() {
                if (currentStep++ > maxSteps || current.distance(center) > (maxDistance + 5)) {
                    cancel();
                    
                    return;
                }

                // ricochet
                double stepSize = 0.25;
                int substeps = (int) Math.ceil(velocity.length() / stepSize);
                Vector step = velocity.clone().normalize().multiply(stepSize);
                
                

                for (int i = 0; i < substeps; i++) {
                	
                	Location keep = loc.clone();
                	
                    loc.add(step);
                    
                    
                    //world.spawnParticle(Particle.DRIP_LAVA, loc, 1, 0, 0, 0, 0, null, true);

                    // Block ricochet check
                    if (!loc.getChunk().isLoaded()) break;
                    if (loc.getBlock().getType().isSolid()) {
                    	
                    	Location test = loc.clone();
                    	Location inside = loc.clone();
                    	// marching backwards
                    	Vector reverseStep = velocity.clone().normalize().multiply(-0.05); // negative = backwards
                    	while (Collisions.isInsideBlock(test) && test.distance(loc) < 1.0) {
                    		test.add(reverseStep);
                    		
                    	}
                    	loc = test.clone();

                    	//current.getWorld().playSound(current, Sound.BLOCK_CHAIN_STEP, 2f, 1f);
                    	
               
                    	
                        if (++bounces > maxBounces) {
                            cancel();
                            
                            return;
                        } else {
                        	
                        }
                        
                        //world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0, 0, 0, 0, null, true);
                        BlockFace face = Collisions.getHitBlockFace(current, velocity, inside.getBlock());
                        if (face == null) {
                        	cancel();
                        	return;
                        }

                     // After computing final hitFace
                        Vector normal = getFaceNormal(face);
                        Vector rayDir = velocity.clone().normalize();

                        // Angle between ray direction and face normal
                        double dot = rayDir.dot(normal);
                        double angle = Math.acos(dot) * (180 / Math.PI) - 90; // in degrees

//                        if (dot > 0.342) {
//                            cancel(); // too steep, no ricochet
//                            return;
//                        }
                        
                        boolean bounce = false;
                        
                        Material hit = inside.getBlock().getType();
                        
                        
                        
                        if (BounceSurfaces.GLASSES.contains(hit)) {
                        	 bounce = true;
                        	 // no reflect here
                        	 inside.getBlock().breakNaturally();
                        	 velocity.multiply(0.8);
                        } else {
                        	if (velocity.length() <= 0.05) {
                            	cancel();
                            	
                            	
                            } else if (velocity.length() <= 0.8) {
                            	
                            	bounce = true;
                            	velocity = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                            	
                            	double bb = BounceSurfaces.getBounceCoefficient(hit);
                            	velocity.multiply(bb);
                            	
                            } else if (angle <= 15) {
                            	velocity = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                            	bounce = true;
                            } else if (angle <= 25) {
                            	if (Math.random() <= 0.9) {
                            		bounce = true;
                            		velocity = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                            	}
                            } else {
                            	if (Math.random() <= 0.08) {
                            		bounce = true;
                            		velocity = velocity.subtract(normal.multiply(2 * velocity.dot(normal)));
                            	}
                            } 
                            
                            
                        }
                        
                        if (!bounce) {
                        	
                        	inside.getWorld().spawnParticle(Particle.BLOCK_CRACK, inside, 15, 0, 0, 0, 0, inside.getBlock().getType().createBlockData());
                        	
                        	cancel();
                        } else {
                        	current.getWorld().playSound(current, Sound.BLOCK_COPPER_STEP, 5.5f, 2f);
                        	velocity.multiply(0.9);
                        }

                        
                        //loc = loc.clone().add(normal.multiply(0.05));
                        
                        ////("bounce " + face + " angle " + angle);
                        
                        break;
                    }

                    
                }

             // Player collision check
                for (LivingEntity player : players) {

                	
                    if (loc.distance(player.getLocation()) > 4) continue; // distance check optimization
                    BodyPart hit = Collisions.getHitBodyPart(player, current, loc);
                    if (hit != BodyPart.NONE) {
                        HitDirection side = Collisions.getHitSide(current, player);
                        
                        int result = -1;
                        if (hit == BodyPart.HEAD) result = 0;
                        else if (hit == BodyPart.CHEST) result = (side.cardinal == HitSide.LEFT) ? 3 : (side.cardinal == HitSide.RIGHT) ? 4 : 1;
                        else if (hit == BodyPart.STOMACH) result = 2;
                        else if (hit == BodyPart.FEET || hit == BodyPart.LEGS) result = (side.leftOrRightOnly == HitSide.LEFT) ? 5 : 6;
                        
                        ////(player + " was hit on " + result);
                        
                        Body body = Body.parts.get(player.getUniqueId().toString());
                        
                        if (body == null) {
                        	player.damage((int) (8 * velocity.length()));
                        } else {
                        	player.playEffect(EntityEffect.HURT);
                            body.damage(result, (int) (8 * velocity.length()), RelizcDamageCause.FRAGMENT);
                        }
                        
                        
                        cancel(); // penetration check
                        return;
                    }
                }

                world.spawnParticle(Particle.FLAME, loc, 1, 0, 0, 0, 0, null, true);
                drawParticleLine(world, current, loc, 0.3);
                current = loc.clone();

                // Apply gravity
                velocity.setY(velocity.getY() - gravity);
          
                
                velocity.multiply(1 - airdrag);
                
            }
        };
	}
	
	public void execute() {
		task.runTaskTimer(EventRegistery.main, 0l, 1l);
	}
	
	
	public static void drawParticleLine(World world, Location a, Location b, double gap) {
	    Vector dir = b.toVector().subtract(a.toVector());
	    double length = dir.length();
	    Vector step = dir.normalize().multiply(gap);
	    int steps = (int) (length / gap);

	    Location particleLoc = a.clone();

	    for (int i = 0; i <= steps; i++) {
	        world.spawnParticle(Particle.CRIT, particleLoc, 1, 0, 0, 0, 0, null, true);
	        particleLoc.add(step);
	    }
	}
	
	

	public static Vector getFaceNormal(BlockFace face) {
		
		
	    switch (face) {
	        case UP: return new Vector(0, 1, 0);
	        case DOWN: return new Vector(0, -1, 0);
	        case NORTH: return new Vector(0, 0, -1); // -Z
	        case SOUTH: return new Vector(0, 0, 1);  // +Z
	        case EAST: return new Vector(1, 0, 0);   // +X
	        case WEST: return new Vector(-1, 0, 0);  // -X
	        default: return new Vector(0, 0, 0);
	    }
	}
	
	
	

}
