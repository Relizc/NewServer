package net.itsrelizc.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.itsrelizc.events.EventRegistery;
import net.minecraft.world.entity.Entity;

public class NPCActions {
	
	private NPC npc;
	private boolean cancelled = false;

	public NPCActions(NPC npc) {
		this.npc = npc;
	}
	
	public void cancel() {
		this.cancelled = true;
	}

	public void jumpAndPlaceBlock(Block block, Material dirt) {
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		
		new BukkitRunnable() {
        	
        	double prevabs = Double.MAX_VALUE;

			@Override
			public void run() {
				
				if (!npc.isSpawned() || cancelled) {
	                cancel();
	                return;
	            }
				
				double vabs = Math.abs(player.getVelocity().getY());
				if (vabs > prevabs) {
					////("descending now ass");
					
					
					new BukkitRunnable() {

						@Override
						public void run() {
							player.swingMainHand();
							placeBlockNaturally(block, dirt);
						}
						
					}.runTaskLater(EventRegistery.main, new Random().nextInt(2));
					
					
					cancel();
				}
				prevabs = vabs;
			}
        	
        }.runTaskTimer(EventRegistery.main, 0, 1l);
	}
	
	public void placeBlockNaturally(Block block, Material material) {
        block.setType(material);

        // Play sound
        block.getWorld().playSound(
            block.getLocation(),
            block.getBlockData().getSoundGroup().getPlaceSound(),
            1.0f,
            1.0f
        );


    }
	
	private static void sendBowDrawAnimation(Entity target) {

	    net.minecraft.network.protocol.game.ClientboundEntityEventPacket packet = new net.minecraft.network.protocol.game.ClientboundEntityEventPacket(target, (byte) 9);
	    
	    for (Player player : Bukkit.getOnlinePlayers()) {
	    	CraftPlayer cplayer = (CraftPlayer) player;
	    	cplayer.getHandle().connection.send(packet);
	    	
	    	//("sent packet");
	    }
	}
	
	private static void resetBowDrawAnimation(Entity target) {

	    net.minecraft.network.protocol.game.ClientboundEntityEventPacket packet = new net.minecraft.network.protocol.game.ClientboundEntityEventPacket(target, (byte) 8);
	    
	    for (Player player : Bukkit.getOnlinePlayers()) {
	    	CraftPlayer cplayer = (CraftPlayer) player;
	    	cplayer.getHandle().connection.send(packet);
	    }
	    
	    
	}
	
	public void useBow(ArrowShootCallback back) {
		this.setMainHandItem(new ItemStack(Material.BOW));
		
		if (!(npc.getEntity() instanceof Player)) return;
		
		new BukkitRunnable() {

			@Override
			public void run() {
				PlayerAnimation.START_USE_MAINHAND_ITEM.play((Player) npc.getEntity());
			}
			
		}.runTaskLater(EventRegistery.main, 2l);
		
		new BukkitRunnable() {

			@Override
			public void run() {
				PlayerAnimation.STOP_USE_ITEM.play((Player) npc.getEntity());
				back.onComplete(true);
			}
			
		}.runTaskLater(EventRegistery.main, 7l);

	}
	
	BukkitTask placeTask = null;

	public void temporarySetMainHandItem(ItemStack it) {
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		if (player.getItemInHand() != null && player.getItemInHand().getType() == it.getType()) return;
		
		setMainHandItem(it);
		
		
		
		if (placeTask != null) {
			placeTask.cancel();
		}
		
		placeTask = new BukkitRunnable() {
			
			BukkitTask current = placeTask;

			@Override
			public void run() {
				if (npc.getEntity() == null) {
					cancel();
					return;
				}
				setMainHandItem(null);

			}
			
		}.runTaskLater(EventRegistery.main, 40L);
	}
	
	public void setMainHandItem(ItemStack it) {
		////(it.toString());
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		Random rand = new Random();
		final int[] slot = {0};
		int i = 0;
		while (i <= 64) {
			i ++;
			slot[0] = rand.nextInt(1, 36);
			
			Material a = it == null ? Material.AIR : it.getType();
			Material b = player.getInventory().getItem(slot[0]) == null ? Material.AIR : player.getInventory().getItem(slot[0]).getType();
			
			
			if (a == b) break;
			
		}
		
		this.swapWithMainHand(slot[0]);
	}
	
	public void swapWithMainHand(int slot) {
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		ItemStack main = player.getItemInHand();
		ItemStack inv = player.getInventory().getItem(slot);
		////(main + " " + inv);
		
		player.setItemInHand(inv);
		player.getInventory().setItem(slot, main);
	}

	public void bridgeBeneath(Block block, Material dirt) {
		placeBlockNaturally(block, dirt);
	}
	
	public void holdWaterBucket() {
		if (placeTask != null) {
			placeTask.cancel();
		}
		setMainHandItem(new ItemStack(Material.WATER_BUCKET));
	}
	
	Location looker = null;;
	
	public void lookAt(Location loc) {
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		////(getShiftedLookTarget(player.getEyeLocation(), loc, xDelta, yDelta).toString());

		if (this.task == LookTask.SCOUT) {
			npc.faceLocation(looker); 
		} else {
			npc.faceLocation(loc);
		}
		
		
	}
	
	public void shootArrow() {
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
	}

	public void placeWaterNaturallyBriefly(Block block) {
		placeBlockNaturally(block, Material.WATER);
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		
		player.setItemInHand(new ItemStack(Material.BUCKET));
		
		
		new BukkitRunnable() {


			@Override
			public void run() {
				placeBlockNaturally(block, Material.AIR);
				
				npc.faceLocation(block.getLocation());
				////(player.getLocation() + " ");
				
				player.setItemInHand(new ItemStack(Material.WATER_BUCKET));
				
				if (placeTask != null) {
					placeTask.cancel();
				}
				
				placeTask = new BukkitRunnable() {

					@Override
					public void run() {
						setMainHandItem(null);
						
						

					}
					
				}.runTaskLater(EventRegistery.main, 60L);

			}
			
		}.runTaskLater(EventRegistery.main, 8L);
	}
	
	public LookTask task = null;
	
	float xDelta = 0f;
	float yDelta = 0f;
	float zDelta = 0f;
	
	@FunctionalInterface
    public interface ArrowShootCallback {
        void onComplete(boolean success);
    }

	public void setLookTask(LookTask default1) {

		////(default1.toString() + " " + task);
		
		if (task == LookTask.BREAKING) return;
		if (task == LookTask.ATTACK) return;
		if (task == LookTask.ARRIVAL) return;
		
		
		Player player = npc.getEntity() instanceof Player ? (Player) npc.getEntity() : null;
		if (default1 == LookTask.SCOUT) {
			looker = getRandomBlockInViewCone(player, 30);
			int i = 0;
			while (looker == null && i <= 64) {
				looker = getRandomBlockInViewCone(player, 30);
				i ++;
				
			}
			////(looker.toString() + "ass");
		} else if (default1 == LookTask.DEFAULT || default1 == LookTask.MLG) {
			looker = null;
		}
		
		
		
		
		task = default1;
		
		
	}
	
	
	
	private static final Random random = new Random();

	public static Location getRandomBlockInViewCone(Player player, int maxDistance) {
        Location eyeLoc = player.getEyeLocation();

        // Random yaw and pitch offset
        float yawOffset = -30 + random.nextFloat() * 60;    // -30° to +30°
        float pitchOffset = -30 + random.nextFloat() * 60;  // -30° to +30°

        float yaw = eyeLoc.getYaw() + yawOffset;
        float pitch = eyeLoc.getPitch() + pitchOffset;

        // Convert yaw and pitch to direction vector
        Vector dir = getDirection(yaw, pitch);

        // Ray trace in that direction
        for (int i = 1; i <= maxDistance; i++) {
            Vector step = dir.clone().multiply(i);
            Location checkLoc = eyeLoc.clone().add(step);
            Block block = checkLoc.getBlock();

            if (!block.isPassable() && block.getType() != Material.AIR) {
                return block.getLocation();
            }
        }
        

        return null;
    }
	
	public static void swapInventoryItems(Inventory invA, int slotA, Inventory invB, int slotB) {
	    ItemStack itemA = invA.getItem(slotA);
	    ItemStack itemB = invB.getItem(slotB);

	    // Perform swap
	    invA.setItem(slotA, itemB);
	    invB.setItem(slotB, itemA);
	}


    /**
     * Converts yaw and pitch to a normalized direction vector.
     */
    private static Vector getDirection(float yaw, float pitch) {
        double rotX = Math.toRadians(yaw);
        double rotY = Math.toRadians(pitch);

        double xz = Math.cos(rotY);
        double y = -Math.sin(rotY);
        double x = -xz * Math.sin(rotX);
        double z = xz * Math.cos(rotX);

        return new Vector(x, y, z).normalize();
    }
    
    /**
     * Checks whether a location is within a 30-degree FOV cone of the player's current view.
     *
     * @param player   The player whose view to check.
     * @param target   The location to test.
     * @return True if the location is within ±30 degrees horizontally and vertically from the player's view direction.
     */
    public static boolean isLocationInPlayerFOV(Player player, Location target) {
        Location eyeLoc = player.getEyeLocation();

        Vector toTarget = target.toVector().subtract(eyeLoc.toVector()).normalize();
        Vector playerDirection = eyeLoc.getDirection().normalize();

        // Compute the angle between the view direction and the target vector
        double angle = Math.toDegrees(playerDirection.angle(toTarget));

        // Check if angle is within 30 degrees
        return angle <= 80.0;
    }


	public Location getShiftedLookTarget(Location headLocation, Location lookTarget, float deltaYaw, float deltaPitch) {
	    // Step 1: Get direction vector and distance
	    Vector dir = lookTarget.clone().subtract(headLocation).toVector();
	    double distance = dir.length();
	    dir.normalize();

	    // Step 2: Convert current direction to yaw and pitch
	    float currentYaw = (float) Math.toDegrees(Math.atan2(-dir.getX(), dir.getZ()));
	    float currentPitch = (float) Math.toDegrees(-Math.asin(dir.getY()));

	    // Step 3: Apply delta yaw and pitch
	    float newYaw = currentYaw + deltaYaw;
	    float newPitch = currentPitch + deltaPitch;
	    newPitch = Math.max(-90, Math.min(90, newPitch)); // clamp pitch

	    // Step 4: Convert new yaw and pitch to direction vector
	    double yawRad = Math.toRadians(newYaw);
	    double pitchRad = Math.toRadians(newPitch);
	    double x = -Math.cos(pitchRad) * Math.sin(yawRad);
	    double y = -Math.sin(pitchRad);
	    double z = Math.cos(pitchRad) * Math.cos(yawRad);
	    Vector newDir = new Vector(x, y, z).normalize().multiply(distance);

	    // Step 5: Return new look target
	    return headLocation.clone().add(newDir);
	}

	public void setLookTaskAfter(LookTask default1, long l) {
		
		new BukkitRunnable() {

			@Override
			public void run() {
				
				setLookTask(default1);
			}
			
		}.runTaskLater(EventRegistery.main, l);
		
	}
	
	private int breakingStep = 0;
	
	private void simulateBreakingBlock(Location loc) {
		

		
		if (task == LookTask.BREAKING) return;
		this.setLookTask(LookTask.BREAKING);

		
		if (loc.getBlock().getType() == Material.AIR) {
			return;
		}
		

		
        int entityId = loc.hashCode(); // Unique ID per block
        int delayPerStage = 4; // Ticks between each crack stage (4 = 0.2s)
        final int[] index = {0};
        final int[] sound = {0};


        new BukkitRunnable() {
            int stage = 0;

            @Override
            public void run() {
            	
            	////("running");
            	
            	if (loc.getBlock().getType() == Material.AIR) {
            		cancel();
            		return;
            	}
            	
            	sound[0]++;
            	if (sound[0] == 4) {
            		sound[0] = 0;
            		 Sound breakSound = loc.getBlock().getType().createBlockData().getSoundGroup().getHitSound();
            		    loc.getWorld().playSound(loc, breakSound, SoundCategory.BLOCKS, 0.5f, 1.0f);
            	}
                if (index[0] > delayPerStage) {
                	if (stage > 9) {
                        // Remove animation at the end
                        sendCrackPacketToAll(loc, -1, entityId);
                        
                        breakBlockNaturally(loc.getBlock());
                        ////("broke");
                        task = LookTask.DEFAULT;
                        
                        cancel();
                        return;
                    }
                	
                	

                    sendCrackPacketToAll(loc, stage, entityId);
                    stage++;
                    ////(stage + " ");
                    index[0] =0;
                }
                
                index[0] ++;
            }
        }.runTaskTimer(EventRegistery.main, 0L, 0l);
        ////("started digging ");
    }
	
	private void sendCrackPacketToAll(Location loc, int crackStage, int entityId) {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
        packet.getIntegers().write(0, entityId);
        packet.getBlockPositionModifier().write(0, new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
        packet.getIntegers().write(1, crackStage);

        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(p, packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

	public void startBreaking(Block targetBlockToBreak) {

		
		
		simulateBreakingBlock(targetBlockToBreak.getLocation());
	}
	
	public static boolean isPathClear(Location from, Location to) {
        if (!from.getWorld().equals(to.getWorld())) {
            throw new IllegalArgumentException("Locations must be in the same world.");
        }

        World world = from.getWorld();
        Vector direction = to.toVector().subtract(from.toVector());
        double distance = direction.length();
        direction.normalize();

        double step = 0.3; // Step size in blocks (adjust for precision/performance)
        Vector current = from.toVector().clone();

        for (double traveled = 0; traveled <= distance; traveled += step) {
            Block block = world.getBlockAt(current.getBlockX(), current.getBlockY(), current.getBlockZ());
            if (block.getType().isSolid()) {
                return false; // Block is obstructing the path
            }
            current.add(direction.clone().multiply(step));
        }

        return true; // No solid block in the way
    }
	
	public ItemStack breakBlockNaturally(Block block) {
	    if (block == null || block.getType() == Material.AIR) return null;

	    Material type = block.getType();
	    Location loc = block.getLocation();

	    // Play block breaking sound
	    Sound breakSound = type.createBlockData().getSoundGroup().getBreakSound();
	    loc.getWorld().playSound(loc, breakSound, SoundCategory.BLOCKS, 1.0f, 1.0f);

	    // Spawn block breaking particles
	    loc.getWorld().spawnParticle(
	        Particle.BLOCK_CRACK,
	        loc.add(0.5, 0.5, 0.5), // Center of the block
	        20,
	        0.3, 0.3, 0.3,          // Spread
	        block.getBlockData()
	    );

	    // Remove the block
	    block.setType(Material.AIR);

	    // Get one item from the block's drops
	    ItemStack drop = block.getDrops().stream().findFirst().orElse(new ItemStack(type));
	    drop.setAmount(1);

	    return drop;
	}

	public boolean hasDirectEyeSight(org.bukkit.entity.Entity entity) {
		return isPathClear(entity.getLocation().clone().add(0, 1.6, 0), npc.getEntity().getLocation().clone().add(0, 1.6, 0));
	}

}
