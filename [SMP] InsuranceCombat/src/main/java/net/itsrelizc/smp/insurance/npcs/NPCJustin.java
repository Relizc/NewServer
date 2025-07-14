package net.itsrelizc.smp.insurance.npcs;

import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.netty.util.internal.ThreadLocalRandom;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.PlayerAnimation;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.gunmod.items.RelizcItemMFCU;
import net.itsrelizc.gunmod.items.RelizcItemSatellitePhone;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.Limb;
import net.itsrelizc.health2.fletching.ArrowUtils;
import net.itsrelizc.health2.fletching.RelizcNeoArrow;
import net.itsrelizc.health2.fletching.RelizcOverridenBow;
import net.itsrelizc.health2.npcs.BodiedNPC;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.npc.AStarPathfinder;
import net.itsrelizc.npc.AStarPathfinder.PathCallback;
import net.itsrelizc.npc.LongDistanceNavigator;
import net.itsrelizc.npc.LookTask;
import net.itsrelizc.npc.NPCActions;

import org.bukkit.inventory.Inventory;

public class NPCJustin extends BodiedNPC {
	
	private static final String skinValue = "ewogICJ0aW1lc3RhbXAiIDogMTc1MDcwMjY3NzM5NywKICAicHJvZmlsZUlkIiA6ICJiMTE1MjE1ZDNlODU0ZGMwYmYxMTY1NGI5ZDEzMDhiZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJSZWxpemMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE4ODAxNzc2NzdlYjkwYmIzNTIxMjhmNGVlOTQ3MmU5NzcxZGYyOGE5NjVmN2JjZWNmYmUwMTkyNjQwZTVmZiIKICAgIH0sCiAgICAiQ0FQRSIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjM0MGMwZTAzZGQyNGExMWIxNWE4YjMzYzJhN2U5ZTMyYWJiMjA1MWIyNDgxZDBiYTdkZWZkNjM1Y2E3YTkzMyIKICAgIH0KICB9Cn0=";
	private static final String skinSignature = "ge27cYnSWKuOeub7eQiKlDihQgL0rilESn/DFYGe+mwI4DdDLuWI20uXVfsafhfJKEvvINFYtyBaFu7/xwCQIyGrvniJ0/d5brFVWjl/1BtcpyNOi48sFC0pZ1PsZ1/U0HwlfAe/jha/FUmr+42h9+6aSri10BgLqQcfyOklMfXFmca3bTXTdSFEzyoTYaXNphD8e+URwRowkaBSe0T4Wkw0D2z2hFkj97ShL7/aM/zC/NQ1nbbGI3SaEgSJfGvk1GxT038Med7Ku5sffZqHpX82JaMn1SCkU6Jz0ussft2fE8SJyewm0YGu69gmIVssp/N248ahJXRKRCaNWiNaiRPp81kZj/e/U8zvbgV3EKNkneg3aEZ7NAU0DxzdEpwX96OWqD5vUNN2qyCM1S4fp9DJ8JWv08aMpnKIQnexR5rjGU5h8FrkMCYraA0zW2xnbZn2TA7PbXIrp6+aTZStJzb9VpVEoG/ker0JwHgvBvDWbilrSAp+K/Xs+GEjO3yPI9P80B6GXTTBx8/sWLp8KKlVXmzkRboaZkpySXsRjd0wLOHGiupv543mGgiaqkU8ZgJPHL7eU/bU613HHvtVLDaOJmGmKKJKQE7gOvglRZUWGVrREgI4Krv3E+7r8vR0CBLwLw3Gh9cEBP9i0ddbb7n42zbQxQ4llhuazxfLfKc=";
	
	private static final String[] dialogue_startMoving = {
			"why would anyone die so easily?",
			"this is my job... yeaah..",
			"umm..\\wait..\\where is he?",
			"let me..\\i think i have a map",
			"gotta tell my boss im quittin",
			"just sayin\\if anyones gonna get in my way\\ill get their balls fucked\\hahahaaaa",
			"havent had sex in 20 years...\\miserable"
	};
	
	private static final String[] dialogue_fightBack = {
			"you got BALLS??",
			"HAVENT DIED SINCE\\MY WIFE IS WAITING",
			"GO FUCK YOURSELF U FUCKING ASS",
			"GET THE FUCK OUT OF MY WAY",
			"MY JOB IS FULL OF MISERY\\AND NOW U HAVE COME TO WORSEN IT\\I WOULD FUCK YOU WITH MY DAMN HOT DICK",
			"GO TO HELL U FUCKING JERK",
			"SERIOUSLY???\\FUCK OFF"
	};
	
	private static final String[] dialogue_flee = {
			"nvm\\go fuck urself.",
			"go have fun with ur penis kid",
			"fucking runner",
			"L RUNNER",
			"fucking idiot.",
			"see u probably never",
			"yea\\keep running asshole",
			"son of a fucking bitch"
	};
	
	private static final String[] dialogue_killed = {
			"oops\\hehehhhh\\hahahahahahahha\\LOLLLLOL",
			"HAHHAH FUCK LOL.",
			"awww no wayyyy...\\LOL",
			"so u like pvp..",
			"oops",
			"hey..\\its fine\\try better",
	};
	
	private static final String[] dialogue_startMovingAway = {
			"alright\\time to go!",
			"this guy is way thiiccker than i expect",
			"full of JUICE\\hahahah"
	};
	
	private static final String[] dialogue_spotted = {
		"oh here u r",
		"there u r!\\like a portal turret lol",
		"oops\\must suffered a lot",
		"welp\\this is mines now!",
		"anyone around??",
		"ill never get these as a fucking loser",
		"guess whos insurance worked!"
	};
	
	private static final class JustinBody extends Body {

		public JustinBody(LivingEntity entity) {
			
			super(entity);

			head = new Limb(entity, 360, 360, "head");
			chest = new Limb(entity, 640, 640, "chest");
			leftArm = new Limb(entity, 415, 415, "leftArm");
			rightArm = new Limb(entity, 430, 430, "rightArm");
			abs = new Limb(entity, 120, 120, "abs");
			leftLegs = new Limb(entity, 480, 480, "leftLegs");
			rightLegs = new Limb(entity, 480, 480, "rightLegs");
			
			this.owner = entity;
			
			for (int i = 0; i < 7; i ++) {
				maxTotalHealth += convert(i).getMaxHealth();
			}
		}
		
	}
	
	public LongDistanceNavigator navigate;
	public NPC bodyTarget;
	public Location determined;
	public JustinNPCListeners listener;
	public AStarPathfinder pathfinder;
	private boolean attacking;
			
	public NPCJustin(NPC tar, Location loc) {
		this.bodyTarget = tar;
		
		determined = tar.getEntity().getLocation();
		new JustinNPCListeners(this);
		
		EventRegistery.register(listener);
		
	}
	
	private static final Random random = new Random();
	
	public static Location getRandomSpawnableLocation(Location center, int radius) {
		////("fuck");
        World world = center.getWorld();
        if (world == null) return null;

        int tries = 256; // Limit attempts to avoid infinite loop

        for (int i = 0; i < tries; i++) {
            int x = center.getBlockX() + random.nextInt(radius * 2 + 1) - radius;
            int z = center.getBlockZ() + random.nextInt(radius * 2 + 1) - radius;
            int y = world.getHighestBlockYAt(x, z); // one block below the highest

            Block base = world.getBlockAt(x, y, z);
            Block air1 = world.getBlockAt(x, y + 1, z);
            Block air2 = world.getBlockAt(x, y + 2, z);

            if (base.getType().isSolid()
                    && air1.getType() == Material.AIR
                    && air2.getType() == Material.AIR) {
                return new Location(world, x + 0.5, y + 1, z + 0.5); // spawn at air1
            }
            
            ////(base.getType() + " " + air1.getType() + " " + air2.getType());
        }

        return null; // No valid location found
    }
	
	@Override
	protected Body getNewBody(LivingEntity entity) {
		return new JustinBody(entity);
	}

	public NPCJustin(NPC tar) {
		this(tar, null);
	}
	
	int checkpointstatus = 0;
	
	@Override
	public void event(String eventName) {
		
		//(eventName);
		
		if (eventName.equals("pathfindingdone")) {
			
			ThreadLocalRandom rand = ThreadLocalRandom.current();
			
			if (checkpointstatus == 0) {
				this.sayDialogue(dialogue_startMoving[rand.nextInt(dialogue_startMoving.length)]);
				this.npc.setProtected(false);
			} else {
				this.sayDialogue(dialogue_startMovingAway[rand.nextInt(dialogue_startMovingAway.length)]);
			}
		} else if (eventName.equals("arrived")) {
			
			
			checkpointstatus++;
			if (checkpointstatus == 2) {
				this.despawn();
			} else if (checkpointstatus == 1) {
				this.sayDialogue(dialogue_spotted[ThreadLocalRandom.current().nextInt(dialogue_spotted.length)]);
				this.npc.faceLocation(bodyTarget.getEntity().getLocation());
				
				BukkitTask lookTask = new BukkitRunnable() {

					@Override
					public void run() {
						if (ThreadLocalRandom.current().nextInt(4) == 0) PlayerAnimation.ARM_SWING.play((Player) npc.getEntity());;
						
						Inventory me = ((Player) npc).getInventory();
						Inventory body = bodyTarget.data().get("inventory");
						
						for (int slot = 0; slot < body.getSize(); slot ++) {
							ItemStack it = body.getItem(slot);
							if (it != null) {
								if (it.getType().getMaxStackSize() == 1) {
									int slot2 = ThreadLocalRandom.current().nextInt(me.getSize());
									ItemStack mines = me.getItem(slot2);
									
									while (true) {
										if (mines == null) break;
										if (mines.getType() == Material.DIRT) break;
										slot2 = ThreadLocalRandom.current().nextInt(me.getSize());
										mines = me.getItem(slot2);
									}
									
									NPCActions.swapInventoryItems(me, slot2, body, slot);
								}
							}
						}
					}
					
				}.runTaskTimer(EventRegistery.main, 0l, 5l);
				
				new BukkitRunnable() {

					@Override
					public void run() {
						lookTask.cancel();
						determined = getRandomSpawnableLocation(npc.getEntity().getLocation(), 128);
						moveToBody();
					}
					
				}.runTaskLater(EventRegistery.main, 20 * (ThreadLocalRandom.current().nextInt(15) + 5));
			}
			
		}
	}
	
	public static Vector getGravityAdjustedVector(Location from, Location to) {
        Vector direction = to.toVector().subtract(from.toVector());
        double distance = direction.length();
        direction.normalize();

        // Gravity compensation factor — tune for desired arc
        double upwardCompensation = 0.00001 * distance; // you can tweak 0.03 as needed

        // Add upward adjustment
        direction.add(new Vector(0, upwardCompensation, 0));

        return direction.normalize();
    }

	public void spawn() {
		super.spawn(getRandomSpawnableLocation(bodyTarget.getEntity().getLocation(), 128));
		
		this.npc.setProtected(true);
		
		this.setActualName("贾斯丁");
		
		Player bukkitPlayer = (Player) npc.getEntity();
		net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer) bukkitPlayer).getHandle();

        // Get the GameProfile
        GameProfile profile = nmsPlayer.getGameProfile();

        // Remove old textures property
        profile.getProperties().removeAll("textures");

        // Add new textures property
        profile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));
        
        Random random = new Random();
        
        int maxGiven = random.nextInt(128, 960 - 64);
        while (maxGiven > 0) {
        	int thisone = random.nextInt(8, 65);
        	
        	this.setItemInInventoryRandomLocation(new ItemStack(Material.DIRT, thisone), true);
        	maxGiven -= thisone;
        	// no need to check max min. its ok to give some more dirt than usual. btw its also accounted on the previous line -64
        	
        	
        }
        
        
        this.setItemInInventoryRandomLocation(new ItemStack(Material.WATER_BUCKET, 1), true);
        
        this.setItemInInventoryRandomLocation(ItemUtils.createItem(RelizcOverridenBow.class, null).getBukkitItem(), true);
        
        if (Math.random() < 0.1) {
        	double randomRare = Math.random();
        	
        	if (randomRare <= 0.2) {
        		ItemStack amt = ItemUtils.createItem(RelizcItemMFCU.class, null).getBukkitItem();
        		amt.setAmount(10 + ThreadLocalRandom.current().nextInt(15));
        		this.setItemInInventoryRandomLocation(amt, true);
        	} else if (randomRare <= 0.4) {
        		this.setItemInInventoryRandomLocation(ItemUtils.createItem(RelizcItemSatellitePhone.class, null).getBukkitItem(), true);
        	}
        }
        
        Player player = (Player) this.npc.getEntity();
        
        ItemStack it = new ItemStack(Material.NETHERITE_HELMET, 1);
        Random rand = ThreadLocalRandom.current();
        
        int randA = rand.nextInt(3);
        if (randA == 0) {
        	it.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
        } else if (randA == 1) {
        	it.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        } else if (randA == 2) {
        	it.addEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 4);
        }
        
        if (rand.nextInt(20) == 0) {
        	it.addEnchantment(Enchantment.DURABILITY, 3);
        }
        
        if (rand.nextInt(5) == 0) {
        	it.addEnchantment(Enchantment.VANISHING_CURSE, 1);
        }
        
        Damageable im = (Damageable) it.getItemMeta();
        im.setDamage(it.getType().getMaxDurability() - (5 + rand.nextInt(50)));
        im.setDisplayName("§oMy wife Jasbin");
        it.setItemMeta(im);
        
        player.getInventory().setHelmet(it);
        
        ItemStack it2 = ItemUtils.createItem(RelizcNeoArrow.class, null, new MetadataPair("point", 1642375599), new MetadataPair("shaft", 2053745532), new MetadataPair("fletching", -1786176692)).getBukkitItem();
        it2.setAmount(random.nextInt(40) + 12);
        this.setItemInInventoryRandomLocation(it2, true);
	}
	
	@Override
	public void despawn() {
		super.despawn();
		
		this.npc.getEntity().getLocation().getWorld().spawnParticle(Particle.CLOUD, this.npc.getEntity().getLocation(), 10, 0, 1, 0, 0.03);
	}
	
	public boolean moveToBody() {
		
		if (this.actionHandler != null && this.actionHandler.task == LookTask.ATTACK) return false;
		
		Location loc = getRandomSpawnableLocation(determined, 2);
		if (loc == null) return false;
		
		////(loc.toString());

//		NavigatorParameters params = npc.getNavigator().getLocalParameters();
//
//		params.baseSpeed(1.5f);
//		params.range(64); // max range for pathfinding
//		params.avoidWater(true);
//		params.stationaryTicks(40); // time before considering the NPC stuck
//		this.npc.getNavigator().setTarget(body);
		
		Set<Material> breakable = Set.of();

		AStarPathfinder pathfinder = new AStarPathfinder(determined.getWorld(), breakable);
		this.pathfinder = pathfinder;
		pathfinder.startAsyncPathSearch(this, this.npc.getEntity().getLocation(), determined, 0);
		
		
		
		return true;
	}
	
	public static Vector getUnitVector(Location from, Location to) {
        Vector direction = to.toVector().subtract(from.toVector());
        return direction.normalize();
    }
	
	
	public void startAttacking(Entity entity, PathCallback whenEnd) {
		
		
		if (entity == npc.getEntity()) return;
		
		this.triggerTerminateMove = true;
		
		
		if (this.actionHandler.task == LookTask.ATTACK) return;
		if (this.attacking) return;
		
		this.attacking = true;
		
		
		this.sayDialogue(dialogue_fightBack[ThreadLocalRandom.current().nextInt(dialogue_startMoving.length)]);
		this.actionHandler.task = LookTask.ATTACK;
		final NPCJustin me = this;
		
		int[] lookEventTarget = {0};
		int[] shootCycleTarget = {0};
		int[] lockOnTarget = {0};
		
		new BukkitRunnable() {
			
			
			
			private void done() {
				cancel();
				whenEnd.onComplete(true);
				npc.getNavigator().cancelNavigation();
			}

			@Override
			public void run() {
				
				
				lookEventTarget[0] ++;
				shootCycleTarget[0] ++;
				lockOnTarget[0] ++;
				
				if (lockOnTarget[0] > 60 * 20) {
					done();
					
					me.sayDialogue(dialogue_flee[ThreadLocalRandom.current().nextInt(dialogue_flee.length)]);
					if (me.actionHandler.task != LookTask.ARRIVAL) {
						AStarPathfinder pathfinder = new AStarPathfinder(me.bodyTarget.getEntity().getLocation().getWorld(), Set.of());
						me.pathfinder = pathfinder;
	            		pathfinder.startAsyncPathSearch(me, me.npc.getEntity().getLocation(), me.bodyTarget.getEntity().getLocation(), 0);
					}
					
					return;
				}
				
				if (!npc.isSpawned()) {
					done();
		            return;
		        }
				
				if (entity instanceof Player) {
					Player ent = (Player) entity;
					if (DeathUtils.isDead(ent)) {
						done();
			            return;
					}
				} else if (entity.isDead()) {
					done();
		            return;
				}
				
				if (lookEventTarget[0] == 5) {
					lookEventTarget[0] = 0;
					
					npc.faceLocation(entity.getLocation());
					
					npc.getNavigator().setTarget(entity.getLocation());
					
					if (entity.getLocation().distance(npc.getEntity().getLocation()) > 60) {
						
						me.sayDialogue(dialogue_flee[ThreadLocalRandom.current().nextInt(dialogue_flee.length)]);
						if (me.actionHandler.task != LookTask.ARRIVAL) {
							AStarPathfinder pathfinder = new AStarPathfinder(me.bodyTarget.getEntity().getLocation().getWorld(), Set.of());
							me.pathfinder = pathfinder;
		            		pathfinder.startAsyncPathSearch(me, me.npc.getEntity().getLocation(), me.bodyTarget.getEntity().getLocation(), 0);
						}
						
						
						done();
						return;
					}
				}
				
				
		        if (shootCycleTarget[0] == 40) {
		        	shootCycleTarget[0] = 0;
		        	
		        	if (actionHandler.hasDirectEyeSight(entity)) {
						actionHandler.useBow((complete) -> {
							
							Location target;
							////(complete + "");
							
							if (entity instanceof Player) {
								target = ((Player) entity).getEyeLocation();
							} else {
								target = entity.getLocation().clone().add(0, 1, 0);
							}
							
							Location me = ((Player) npc.getEntity()).getEyeLocation().clone().add(0, 0.13, 0);
							
							ArrowUtils.shootArrowNaturally((LivingEntity) npc.getEntity(), 
									ItemUtils.createItem(RelizcNeoArrow.class, null, new MetadataPair("point", 1642375599)).getBukkitItem(), 
									ItemUtils.createItem(RelizcOverridenBow.class, null).getBukkitItem(), 
									me, getGravityAdjustedVector(me, target), 8);
							
						});;
					}
		        }
				
			}
			
		}.runTaskTimer(EventRegistery.main, 0, 1l);
	}
	
	public static class JustinNPCListeners implements Listener {
		
		private NPCJustin justin;

		public JustinNPCListeners(NPCJustin justin) {
			this.justin = justin;
			justin.listener = this;
		}
		
		@EventHandler(ignoreCancelled=true)
		public void dmg(EntityDamageByEntityEvent event) {
			
			Entity entity = event.getEntity();

	        if (CitizensAPI.getNPCRegistry().isNPC(entity)) {
	            NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);
	            
	            
	            boolean isMe = (npc == this.justin.npc);
	            
	            if (!isMe) return;
	            
	           
	            
	            if (event.getDamager() instanceof Projectile) {
	            	Projectile proj = (Projectile) event.getDamager();
	            	if (proj.getShooter() instanceof LivingEntity) {
	            		LivingEntity shooter = (LivingEntity) proj.getShooter();
	            		
	            		this.justin.startAttacking(shooter, complete -> {
	            			
	            			
	            			
	    	            	if (true) {
	    	            		justin.actionHandler.task = LookTask.DEFAULT;
	    	            		
	    	            		justin.attacking = false;
	    	            		justin.sayDialogue(dialogue_killed[ThreadLocalRandom.current().nextInt(dialogue_killed.length)]);
		            			
		            			NPCJustin me = justin;

								AStarPathfinder pathfinder = new AStarPathfinder(me.bodyTarget.getEntity().getLocation().getWorld(), Set.of());
								me.pathfinder = pathfinder;
			            		pathfinder.startAsyncPathSearch(me, me.npc.getEntity().getLocation(), me.bodyTarget.getEntity().getLocation(), 0);


	    	            	}
	    	            });
	            	}
	            } else {
	            	this.justin.startAttacking(event.getDamager(), complete -> {
	            		
	            		
		            	if (true) {
		            		justin.actionHandler.task = LookTask.DEFAULT;
		            		
		            		justin.sayDialogue(dialogue_killed[ThreadLocalRandom.current().nextInt(dialogue_killed.length)]);
	            			justin.attacking = false;

	            			AStarPathfinder pathfinder = new AStarPathfinder(justin.bodyTarget.getEntity().getLocation().getWorld(), Set.of());
		            		justin.pathfinder = pathfinder;
		            		pathfinder.startAsyncPathSearch(justin, justin.npc.getEntity().getLocation(), justin.bodyTarget.getEntity().getLocation(), 0);

		            	}
		            });
	            }
	            
	            

	            // Optional: cancel damage
	            // event.setCancelled(true);
	        }
			
		}
		

	}
	
}
