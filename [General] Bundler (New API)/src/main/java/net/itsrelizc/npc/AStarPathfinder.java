package net.itsrelizc.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.events.EventRegistery;

public class AStarPathfinder {
	
	public enum MovementAction {
	    WALK,       // Walking/jumping small gap
	    SHORT_FALL, // Fall ≤3 blocks
	    MLG_FALL,   // Big fall >3 blocks
	    STACK_UP,   // Pillaring up
	    DIG,        // Breaking blocks
	    BRIDGE,     // Bridging over air
	    INVALID     // Blocked or invalid move
, DIG_HORIZONTAL, DIG_VERTICAL_UP, DIG_VERTICAL_DOWN, JUMP
	}

	
	public class LocationNode {
	    public final Location location;
	    public double gCost;
	    public double hCost;
	    public LocationNode parent;
	    public LocationNode child;
	    public MovementAction action;  // <-- Add this

	    public LocationNode(Location location) {
	        // Snap to block center
	    	this.action = MovementAction.WALK; // default or null
	        this.location = new Location(
	            location.getWorld(),
	            location.getBlockX() + 0.5,
	            location.getBlockY(),
	            location.getBlockZ() + 0.5
	        );
	    }

	    public double getFCost() {
	        return gCost + hCost;
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (!(obj instanceof LocationNode)) return false;
	        LocationNode other = (LocationNode) obj;
	        return location.getBlockX() == other.location.getBlockX()
	            && location.getBlockY() == other.location.getBlockY()
	            && location.getBlockZ() == other.location.getBlockZ();
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	    }
	}


    private final World world;
    private final Set<Material> breakableMaterials;

    public AStarPathfinder(World world, Set<Material> breakableMaterials) {
        this.world = world;
        this.breakableMaterials = breakableMaterials;
    }
    
//    private void showDebugParticle(Location location) {
//        World world = location.getWorld();
//        if (world == null) return;
//
//        // Show particle at center of block
//        Location particleLoc = location.clone().add(0, 0.5, 0); // Slightly above ground
//
//        world.spawnParticle(
//            Particle.FLAME, // Or REDSTONE, FLAME, etc.
//            particleLoc,
//            1, // count
//            0, 0, 0, // spread
//            0, // speed
//            null,
//            true
//        );
//    }
//    
//    private void showDebugParticle(Location location, Color color) {
//        World world = location.getWorld();
//        if (world == null) return;
//
//        Location particleLoc = location.clone().add(0.5, 0.2, 0.5); // center of block
//
//        // Create REDSTONE dust particle with color
//        Particle.DustOptions dust = new Particle.DustOptions(color, 1.0F);
//
//        world.spawnParticle(
//            Particle.REDSTONE,
//            particleLoc,
//            1,  // count
//            0, 0, 0, // no spread
//            0,
//            dust,
//            true
//        );
//    }
    
    NPCActions actions;
    
    public void startAsyncPathSearch(RelizcNPC npc, Location start, Location goal, int failed) {
    	
    	
    	////("serarching");
    	
    	actions = new NPCActions(npc.npc);
    	npc.actionHandler = actions;
    	
        BukkitTask task = new BukkitRunnable() {

			@Override
			public void run() {
				LocationNode path = findPath(start, goal);
	            
	            

	            if (path != null) {
	                // Show final path particles on main thread

	                	
	                	while (path.parent != null) {
	                		
	                		
	                		path.parent.child = path;
	                		path = path.parent;
	                	}
	                	
	                	LocationNode prev = path;
	                	LocationNode node = path.child;

	                	while (node != null) {
	                		
	                		drawParticleLine(prev.location.getWorld(), prev.location, node.location, 0.1);
	                		
	                		
	                		prev = node;
	                		
	                		if (node.child == null) {
	                			
	                			
	                			List<Location> pathv = new ArrayList<>();
	                	        LocationNode current = node;
	                	        //(current + " DICK");
	                	        
	                	        while (current != null) {
	                	        	pathv.add(current.location.clone()); // center of block
	                	            current = current.parent;
	                	            
	                	        }
	                	        Collections.reverse(pathv);
	                	        
	                	        moveNpcWithVelocity(npc, npc.npc, pathv, success -> {
	                	            if (success) {
	                	                npc.event("astardone");
	                	                
	                	                
	                	            } else {

	                	                System.out.println("❌ Path failed or deviated. " + failed);
	                	                if (failed >= 5) {
	                	                	npc.despawn();
	                	                }
	                	                startAsyncPathSearch(npc, npc.npc.getEntity().getLocation(), goal, failed + 1);
	                	            }
	                	        });
	                	        
	                			
	                			break;
	                		}
	                		
	                		node = node.child;
	                		
	                		
	                		
	                	}

	            } else {

	                Bukkit.getLogger().info("[AStar] No path found.");

	                
	                npc.despawn();
	                
	            }
	        }
			
        	
        	
        }.runTaskLater(EventRegistery.main, 0l);

        
        Bukkit.getScheduler().runTaskLater(EventRegistery.main, () -> {
            if (task != null && !task.isCancelled() && (npc.actionHandler == null || npc.actionHandler.task != null)) {
                Bukkit.getLogger().info("Interrupting thread after 10 mins.");
                task.cancel();
            }
        }, 20L * 60 * 10); // 20 ticks * 60 seconds = 60 seconds
    }
    
    public static void drawParticleLine(World world, Location a, Location b, double gap) {
	    Vector dir = b.toVector().subtract(a.toVector());
	    double length = dir.length();
	    Vector step = dir.normalize().multiply(gap);
	    int steps = (int) (length / gap);

	    Location particleLoc = a.clone();

	    for (int i = 0; i <= steps; i++) {
	        world.spawnParticle(Particle.VILLAGER_HAPPY, particleLoc, 1, 0, 0, 0, 0, null, true);
	        particleLoc.add(step);
	    }
	}


    public LocationNode findPath(Location start, Location goal) {
    	start = new Location(world, start.getBlockX() + 0.5, start.getBlockY(), start.getBlockZ() + 0.5);
    	goal  = new Location(world, goal.getBlockX() + 0.5, goal.getBlockY(), goal.getBlockZ() + 0.5);
        LocationNode startNode = new LocationNode(start);
        LocationNode endNode = new LocationNode(goal);

        PriorityQueue<LocationNode> openSet = new PriorityQueue<>(Comparator.comparingDouble(LocationNode::getFCost));
        Set<LocationNode> closedSet = new HashSet<>();

        startNode.gCost = 0;
        startNode.hCost = heuristic(start, goal);
        openSet.add(startNode);
        

        while (!openSet.isEmpty() && !Thread.currentThread().isInterrupted()) {
        	
        	//(openSet.size() + " ");
        	
            LocationNode current = openSet.poll();
            //world.spawnParticle(Particle.FLAME, current.location, 1, 0, 0, 0, 0);
            
            if (isSameBlock(current.location, goal)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (LocationNode neighbor : getNeighbors(current)) {
            	
                if (closedSet.contains(neighbor)) continue;

                double movementCost = getMovementCost(current, neighbor, goal);
                double tentativeG = current.gCost + movementCost;

                boolean inOpenSet = openSet.contains(neighbor);
                if (!inOpenSet || tentativeG < neighbor.gCost) {
                    neighbor.gCost = tentativeG;
                    neighbor.hCost = heuristic(neighbor.location, goal);
                    neighbor.parent = current;

                    if (!inOpenSet) openSet.add(neighbor);
                }
            }
        }

        return null; // No path found
    }

    private boolean isSameBlock(Location a, Location b) {
        return a.getBlockX() == b.getBlockX()
            && a.getBlockY() == b.getBlockY()
            && a.getBlockZ() == b.getBlockZ();
    }

    private LocationNode reconstructPath(LocationNode node) {

        while (node != null) {

            if (node.parent == null) return node;
            node.parent.child = node;
            node = node.parent;
        }
        //Collections.reverse(path);
        return null;
    }

    private double heuristic(Location a, Location b) {
        return Math.abs(a.getBlockX() - b.getBlockX())
             + Math.abs(a.getBlockY() - b.getBlockY())
             + Math.abs(a.getBlockZ() - b.getBlockZ());
    }

    private List<LocationNode> getNeighbors(LocationNode node) {
        List<LocationNode> neighbors = new ArrayList<>();
        Location loc = node.location;

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        int[][] deltas = {
            {1, 0, 0}, {-1, 0, 0},
            {0, 0, 1}, {0, 0, -1},
            {0, 1, 0}, {0, -1, 0}
        };

        for (int[] delta : deltas) {
            int dx = x + delta[0];
            int dy = y + delta[1];
            int dz = z + delta[2];

            Location neighborLoc = new Location(loc.getWorld(), dx + 0.5, dy, dz + 0.5);

            if (isTraversable(neighborLoc)) {
                neighbors.add(new LocationNode(neighborLoc));
            }
        }

        return neighbors;
    }
    
    private boolean isValidMove(LocationNode from, LocationNode to) {
        Location fromLoc = from.location;
        Location toLoc = to.location;

        int dy = toLoc.getBlockY() - fromLoc.getBlockY();

        // Basic checks: target block and surroundings
        Block targetBlock = world.getBlockAt(toLoc);
        Block belowTarget = targetBlock.getRelative(0, -1, 0);

        // Passable or breakable etc. (depends on your other rules)...

        // Special jump validity check:
        if (dy == 1) {
            // Must have solid block to jump onto
            if (!belowTarget.getType().isSolid()) return false;

            // Headroom above target must be free
            Block headroom = targetBlock.getRelative(0, 1, 0);
            if (!headroom.isPassable()) return false;
        }

        // Additional movement validations (falling, bridging, etc.) here...

        return true; // Valid move
    }


    private boolean isTraversable(Location loc) {
        Block block = world.getBlockAt(loc);
        if (block.isPassable()) return true;
        return breakableMaterials.contains(block.getType());
    }

    private double getMovementCost(LocationNode from, LocationNode to, Location goal) {
        Location fromLoc = from.location;
        Location toLoc = to.location;

        Block targetBlock = world.getBlockAt(toLoc);
        Block headBlock = world.getBlockAt(toLoc.clone().add(0, 1, 0));
        boolean passable = targetBlock.isPassable() && headBlock.isPassable();
        boolean breakable = breakableMaterials.contains(targetBlock.getType());

        int dy = toLoc.getBlockY() - fromLoc.getBlockY();
        int dx = toLoc.getBlockX() - fromLoc.getBlockX();
        int dz = toLoc.getBlockZ() - fromLoc.getBlockZ();

        double horizontalOffset = Math.hypot(dx, dz);
        int deltaYToGoal = goal.getBlockY() - fromLoc.getBlockY();
        double absVerticalDistance = Math.abs(deltaYToGoal);
        double horizontalDistance = Math.hypot(goal.getX() - fromLoc.getX(), goal.getZ() - fromLoc.getZ());

        double horizontalBias = 1.0 + Math.pow(horizontalDistance / 10.0, 3);
        double upwardBias     = deltaYToGoal > 0 ? 1.0 + Math.pow(absVerticalDistance / 10.0, 3) : 1.0;
        double downwardBias   = deltaYToGoal < 0 ? 1.0 + Math.pow(absVerticalDistance / 10.0, 3) : 1.0;

        boolean inWater = fromLoc.getBlock().isLiquid();
        boolean movingUp = dy > 0;

        Block feetBlock = toLoc.getBlock();
        Block belowBlock = toLoc.clone().subtract(0, 1, 0).getBlock();

        boolean walkingIntoLiquid = feetBlock.isLiquid();
        boolean bridgingOnLiquidSurface = belowBlock.isLiquid() && feetBlock.getType().isAir();
        boolean solidBelow = isSolidGround(toLoc);

        MovementAction action;
        double cost;

        // 🚫 Completely block if space is too tight or block is not passable
        if (!passable && !breakable) {
            to.action = MovementAction.INVALID;
            return 1000.0;
        }

        // 🚫 Ban walking into any liquid (only allow bridging over it)
        if (walkingIntoLiquid) {
            to.action = MovementAction.INVALID;
            return 1000.0;
        }

        // 🟢 Walk / Short Fall / 1 Block Jump
        if (horizontalOffset <= 2 && dy >= -3 && dy <= 1 && (solidBelow || bridgingOnLiquidSurface)) {
            if (dy == 1 && isSolidGround(toLoc) && headBlock.isPassable()) {
                cost = 0.23 * horizontalBias;
                action = MovementAction.JUMP;
            } else if (dy < 0) {
                cost = 0.25 * horizontalBias;
                action = MovementAction.SHORT_FALL;
            } else {
                cost = 0.25 * horizontalBias;
                action = MovementAction.WALK;
            }

            // Discourage horizontal movement while submerged
            if (inWater && dy == 0) {
                cost += 8.0;
            }

            if (bridgingOnLiquidSurface && !solidBelow) {
                cost += 1.3;
                action = MovementAction.BRIDGE;
            }

            to.action = action;
            return cost;
        }

        // 🟠 Digging
        ////(breakableMaterials.size() + "  ");
        if (!passable && breakable) {
            boolean verticalDig = dy != 0 && dx == 0 && dz == 0;

            if (verticalDig) {
                if (dy > 0) {
                    cost = 5000.0 * horizontalBias * 20 * upwardBias;
                    action = MovementAction.DIG_VERTICAL_UP;
                } else {
                    cost = 5000.0 * horizontalBias * 2 * downwardBias;
                    action = MovementAction.DIG_VERTICAL_DOWN;
                }
            } else {
                cost = 5000.0 * horizontalBias + 5.0;
                action = MovementAction.DIG_HORIZONTAL;
            }

            to.action = action;
            return cost;
        }

        // 🟢 MLG fall (≥4 blocks)
        if (dy < -3) {
            Block landingBlock = toLoc.clone().subtract(0, 1, 0).getBlock();
            boolean solidLanding = landingBlock.getType().isSolid();
            int fallDistance = fromLoc.getBlockY() - toLoc.getBlockY();

            double baseCost = 2.5 * Math.exp(-fallDistance / 5.0);
            if (!solidLanding) baseCost += 1000.0;

            if (deltaYToGoal > 0) {
                // Strongly discourage falling when the goal is above you
                double wrongWayPenalty = 1.0 + Math.pow(absVerticalDistance / 5.0, 2.5);
                cost = baseCost * wrongWayPenalty * 2.0; // amplified penalty
            } else {
                cost = baseCost * downwardBias;
            }

            to.action = MovementAction.MLG_FALL;
            return cost;
        }

        // 🟡 Stack up
        if (dy > 0) {
            cost = (2.0 + dy * 1.5) * upwardBias * horizontalBias * 1.5;
            if (inWater) cost *= 0.3;

            action = MovementAction.STACK_UP;
            to.action = action;
            return cost;
        }

        // 🔴 Bridging horizontally (air or liquid below)
        if (dy == 0 && !solidBelow) {
            cost = 5.0 * horizontalBias;

            if (bridgingOnLiquidSurface) cost += 2.1;
            if (Math.abs(dx) == 1 && Math.abs(dz) == 1) cost += 0.9;

            action = MovementAction.BRIDGE;
            to.action = action;
            return cost;
        }

        // Fallback
        to.action = MovementAction.INVALID;
        return 6.0;
    }
    
    @FunctionalInterface
    public interface PathCallback {
        void onComplete(boolean success);
    }
    
    private int offboundTicks = 0;
    
    public static enum CancelReason {
    	
    }

    public void moveNpcWithVelocity(RelizcNPC npc2, NPC npc, List<Location> path, PathCallback callback) {
    	offboundTicks = 0;
        if (path == null || path.size() < 2 || !npc.isSpawned()) return;
        
        npc2.event("pathfindingdone");
        npc2.triggerTerminateMove = false;

        Entity entity = npc.getEntity();
        Player player = (Player) entity; // must be a player-type NPC
        float walkSpeed = player.getWalkSpeed(); // typically ~0.2f
        Random random =  new Random();
        
        final boolean[] prepareFall = {false};
        final Location[] fallFromLocation = {null};
        final double[] landingDistance = {0.0};

        final int[] index = {0};
        
        actions.setLookTask(LookTask.DEFAULT);

	        new BukkitRunnable() {
	        	
	
				@Override
				public void run() {
					
					if (random.nextInt(60) == 5) {
						
						if (actions.task != LookTask.MLG) {
							actions.setLookTask(LookTask.SCOUT);
							
							
							
							actions.setLookTaskAfter(LookTask.DEFAULT, 20l);
						}
						
						
						
						
					}
					
					
					
					if (!npc.isSpawned() || index[0] >= path.size() - 1 || npc2.triggerTerminateMove) {
						
						
						if (index[0] >= path.size() - 1) { 
							npc2.actionHandler.setLookTask(LookTask.ARRIVAL);
	    	                npc2.event("arrived");
						}
						
						npc2.triggerTerminateMove = false;
			            cancel();
			            actions.cancel();
			            callback.onComplete(true);  // ✅ Success
			            
			            
			            
			            return;
			        }

					Location current = entity.getLocation();
					Location indice = path.get(index[0]);
					Location target = path.get(index[0] + 1);
					Location afterNext = index[0] + 2 < path.size() ? path.get(index[0] + 2) : null;
			        //drawParticleLine(current.getWorld(), current,target,0.2);
			        
			        
			        actions.lookAt(target);
			        
			        //(target.getBlock().getType() + " ");

			        double distance = current.distance(target);
			        
			        
			        if (prepareFall[0]) {
			        	target = target.clone().add(0, 2, 0);
			        }
			        
			        if (distance < 0.3) {
			        	
			        	if (prepareFall[0]) {
			        		prepareFall[0] = false;
			        		fallFromLocation[0] = null;
			        		landingDistance[0] = 0;
			        		index[0]++; // extra acsend
			        	}
			        	
			        	if (afterNext != null) {
			        		
			        		double tdy = Math.abs(afterNext.getY() - indice.getY());
			        		double tdx = Math.abs(afterNext.getX() - indice.getX());
			        		double tdz = Math.abs(afterNext.getZ() - indice.getZ());
			        		
			        		if (tdy < 0.001 && Math.abs(tdx - tdz) < 0.001) {
			        		    // now optionally check how large tdx is (e.g. 1 or 2)
			        		    //("Clean diagonal of %.2f blocks".formatted(tdx));
			        		    index[0] += (int) tdx;
			        		}
			        	}
			        	
			            index[0]++;
			            return;
			        }
			        
			        
			        if (distance > 1) {
			        	offboundTicks += 1;
			            
			        	
			        	
			        	int limit = prepareFall[0] ? 20 * 5 : 20 * 8;
			        	if (offboundTicks > limit) {
			        		cancel();
			        		actions.cancel();
			        		callback.onComplete(false);
			        	}
			        	
			        } else {
			        	offboundTicks = 0;
			        }

		            
		            
	
		            double dx = target.getX() - current.getX();
		            double dy = target.getY() - current.getY();
		            double dz = target.getZ() - current.getZ();
	
		            // Base movement vector
		            
		            Location tar = target;
		            if (fallFromLocation[0] != null) {
		            	tar = fallFromLocation[0];
		            }
		            //world.spawnParticle(Particle.VILLAGER_ANGRY, tar, 1, 0, 0, 0);
		            
		            Vector direction = tar.toVector().subtract(current.toVector()).normalize();
		            Vector velocity = direction.multiply(walkSpeed);
	
		            // Get existing vertical velocity
		            double verticalVelocity = player.getVelocity().getY();
	
		            // Check for jump
		            Block belowTarget = target.clone().subtract(0, 1, 0).getBlock();
		            boolean isStackingUp = dy > 0.1 && dy <= 1.1 && !belowTarget.getType().isSolid();
		            boolean canFitNext = !target.getBlock().getType().isSolid() && !target.clone().add(0, 1, 0).getBlock().getType().isSolid();
		            Block[] rand = {target.getBlock(), target.clone().add(0, 1, 0).getBlock()};
		            Random random = new Random();
		            
		            if (!canFitNext) {
		            	
		            	if (actions.task != LookTask.BREAKING) {
		            		
		            		Block targetBlockToBreak = rand[random.nextInt(2)];
		            		while (!targetBlockToBreak.getType().isSolid()) {
		            			if (!rand[0].getType().isSolid() && !rand[1].getType().isSolid()) {
		            				canFitNext = true;
		            				actions.setLookTask(LookTask.DEFAULT);
		            				break;
		            			}
		            			targetBlockToBreak = rand[random.nextInt(2)];
		            		}
		            		
		            		if (!canFitNext) {
		            			actions.startBreaking(targetBlockToBreak);
		            		} 
		            		
		            	}
		            	
		            	////("pausing s");
		            	
		            	return;
		            } 
		            
		            //(!prepareFall[0] + " ");

		            if (player.isOnGround() && !prepareFall[0]) {
		            	//(target.clone().add(0, 1, 0).equals(afterNext) + " ");
		            	//("A");
		            	
		            	boolean canDirectHop = target.clone().add(0, 1, 0).equals(afterNext);
		            	boolean isHop = afterNext == null ? target.getY() > current.getY() : afterNext.getY() > current.getY() && target.getY() > current.getY();
		            	
		            	boolean canHopStand =  afterNext == null ? false : afterNext.clone().subtract(0, 1, 0).getBlock().getType().isSolid();
		            	
		            	//(afterNext.getY() + " " +  current.getY() + " " + target.getY());
		            	
		                if (!canDirectHop && isHop && canHopStand) {
		                    // Natural 1-block jump
		                    verticalVelocity = 0.6;
		                    //("Jumping up natural step");
		                } else if (isStackingUp) {
		                    // Stacking upward
		                    verticalVelocity = 0.6;
		                    actions.temporarySetMainHandItem(new ItemStack(Material.DIRT, 1));
		                    actions.jumpAndPlaceBlock(belowTarget, Material.DIRT);
		                    //("Jumping and stacking up");
		                } else {
		                	
		                	
		                	
		                	boolean voidNextBelow = !belowTarget.getType().isSolid();
		                	boolean goingDown = afterNext == null ? false : afterNext.getY() < target.getY();
			                //(voidNextBelow + " ");
			                
			                if (voidNextBelow ) {
			                	
			                	if (goingDown) {
			                		
			                		int start = index[0] + 1;
			                		
			                		int dist = 0;
			                		
			                		
			                		for (int i = index[0] + 1; i < path.size(); i ++) {
			                			dist ++;
			                			////(dist + " ");
				                		Location downwards = path.get(i);
				                		
				                		boolean landed = downwards.subtract(0, 1, 0).getBlock().getType().isSolid();
				                		if (landed) {
				                			
				                			double landdist = downwards.distance(target);
				                			
				                			//(downwards.distance(target) + " ");
				                			
				                			landingDistance[0] = landdist;
				                			prepareFall[0] = true;
				                			fallFromLocation[0] = path.get(start);
				                			index[0] = i - 2;
				                			
				                			if (dist > 3) {
				                				actions.holdWaterBucket();
					                			actions.setLookTask(LookTask.MLG);
				                			}
				                			
				                			
				                			
				                			
				                			break;
				                			
				                			
				                		}
				                	
				                	}


			                		
			                	} else {
			                		//("place " + prepareFall[0]);
			                		
			                		Block belowCurrent = indice.clone().subtract(0, 1, 0).getBlock();
			                		if (!belowCurrent.getType().isSolid()) {
			                			actions.temporarySetMainHandItem(new ItemStack(Material.DIRT, 1));
				                		actions.placeBlockNaturally(belowCurrent, Material.DIRT);
			                		}
			                		
			                		actions.temporarySetMainHandItem(new ItemStack(Material.DIRT, 1));
			                		actions.placeBlockNaturally(belowTarget, Material.DIRT);
			                	}

			                }
			                
		                }
		                
		                
		                
		                
		                
		            } else {
		            	//("B");
		            	
		            	
		            	
		            	
		                


		                // Apply gravity
		                verticalVelocity -= 0.08;
		            }
	
		            velocity.setY(verticalVelocity);
		            
		            if (prepareFall[0] && landingDistance[0] > 3.4 && !player.isOnGround()) {
		            	Location predictedNextTickLocation = current.clone().add(velocity);
			            double nxtdist = predictedNextTickLocation.distance(target);
			            
			            if (nxtdist > distance) {
			            	//("gonnahit! fuckaa " + nxtdist + " " + distance);
			            	
			            	actions.placeWaterNaturallyBriefly(target.clone().subtract(0, 2, 0).getBlock());
			            }
			            
			            
		            }
	
		            //("next %.2f %.2f %.2f ass".formatted(velocity.getX(), velocity.getY(), velocity.getZ()));
		            entity.setVelocity(velocity);
		            
		            
	
	
	
	
		        }
			}.runTaskTimer(EventRegistery.main, 0L, 1L); // Run every tick
    }


    



    
    private List<LocationNode> flattenPath(LocationNode start) {
        List<LocationNode> path = new ArrayList<>();
        LocationNode current = start;

        while (current != null) {
            path.add(current);
            current = current.child; // use single child instead of list
        }

        return path;
    }




    private boolean isSolidGround(Location loc) {
        Block below = world.getBlockAt(loc).getRelative(0, -1, 0);
        return below.getType().isSolid();
    }

    private boolean isFreeFallSafe(Location from, Location to) {
        World world = to.getWorld();
        if (world == null) return false;

        int startY = to.getBlockY();
        int minY = world.getMinHeight();

        // Allow up to 100-block fall max (or limit if you want)
        for (int y = startY - 1; y >= minY; y--) {
            Block block = world.getBlockAt(to.getBlockX(), y, to.getBlockZ());
            if (!block.getType().isAir() && block.getType().isSolid()) {
                return true; // Valid fall target
            }
        }

        return false; // No solid landing found
    }


}



