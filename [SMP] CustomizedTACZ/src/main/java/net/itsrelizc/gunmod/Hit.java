package net.itsrelizc.gunmod;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_20_R1.persistence.CraftPersistentDataContainer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.joml.Math;
import org.json.simple.JSONObject;

import com.mohistmc.bukkit.entity.MohistModsEntity;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.gunmod.blood.Container;
import net.itsrelizc.gunmod.blood.SwapHands;
import net.itsrelizc.gunmod.craft.CartridgeAssemblerListener;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.ChatUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.entity.Entity;

public class Hit implements Listener {
	
	static EntityType BULLET = EntityType.valueOf("TACZ_BULLET");
	public static double bulletSpeed = 20;
	public static double g = 64;
	public static double delta = 2;
	
	private static class BallisticInformation {
		
		private double speed;
		private long eject;
		private Player player;

		public BallisticInformation(double speed, Player player) {
			this.speed = speed;
			this.eject = System.currentTimeMillis();
			this.player = player;
			
		}
		
	}
	
	private static Map<MohistModsEntity, BallisticInformation> content = new HashMap<MohistModsEntity, BallisticInformation>();
	private static Map<MohistModsEntity, NBTTagCompound> bullet_info = new HashMap<MohistModsEntity, NBTTagCompound>();
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void damage(EntityDamageByEntityEvent event) {
//		ChatUtils.broadcastSystemMessage("ballistics", "Hit by " + event.getEntityType() + " " + event.getEntity().getClass() + " " + event.getDamager().getType().getClass() + " " + event.getDamager().getClass());
	
		if (!(event.getEntity() instanceof LivingEntity)) return;
		
		if (event.getDamager() instanceof MohistModsEntity) {
			
			MohistModsEntity mod = (MohistModsEntity) event.getDamager();
			
			
			if (mod.getType() == BULLET) {
				
				if (!content.containsKey(mod)) return;
				
				boolean delta = false;
				
				long a = System.currentTimeMillis();
				
				BallisticInformation info = content.get(mod);
				Player player = (Player) info.player;
				
				event.setDamage(0);
				event.setCancelled(false);
				
				int result = 0; // 1 = feet 2 = legs 3 = thorax 4 = head 0=miss
				
				if (player.getLocation().getY() > event.getEntity().getLocation().getY()) {
					
					if (!detectPart(player, event.getEntity(), mod, 1.41, 3, 0.28, info.speed, delta)) {
						if (!detectPart(player, event.getEntity(), mod, 0.9, 1.41, 0.3, info.speed, delta)) {
							if (!detectPart(player, event.getEntity(), mod, 0.4, 0.9, 0.28, info.speed, delta)) {
								if (!detectPart(player, event.getEntity(), mod, -1, 0.4, 0.28, info.speed, delta)) {
									result = 0;
								} else {
									result = 1;
								}
							} else {
								result = 2;
							}
						} else {
							result = 3;
						}
					} else {
						result = 4;
					}
					
				} else {
					
					

					if (!detectPart(player, event.getEntity(), mod, -1, 0.4, 0.28, info.speed, delta)) {
						if (!detectPart(player, event.getEntity(), mod, 0.4, 0.9, 0.28, info.speed, delta)) {
							if (!detectPart(player, event.getEntity(), mod, 0.9, 1.41, 0.3, info.speed, delta)) {
								if (!detectPart(player, event.getEntity(), mod, 1.41, 3, 0.28, info.speed, delta)) {
									result = 0;
								} else {
									result = 4;
								}
							} else {
								result = 3;
							}
						} else {
							result = 2;
						}
					} else {
						result = 1;
					}
					
				}
				
//				Bukkit.broadcastMessage(String.valueOf(result));
				
				NBTTagCompound tag = bullet_info.get(mod);
//				Bukkit.broadcastMessage(tag.toString());
				
//				Bukkit.broadcastMessage("bs: " + content.get(mod).speed);
//				Bukkit.broadcastMessage("g: " + Hit.g);
//				Bukkit.broadcastMessage("Hit Feet: " + detectPart(player, event.getEntity(), mod, 0, 0.4, 0.28));
//				Bukkit.broadcastMessage("Hit Legs: " + detectPart(player, event.getEntity(), mod, 0.4, 0.9, 0.28)); 
//				Bukkit.broadcastMessage("Hit Chest: " + detectPart(player, event.getEntity(), mod, 0.9, 1.41, 0.35));
//				Bukkit.broadcastMessage("Hit Head: " + detectPart(player, event.getEntity(), mod, 1.41, 3, 0.28));
				
				long b = System.currentTimeMillis();
				
//				Bukkit.broadcastMessage("algTime: " + (b-a) + "ms L:" + content.size());
				
				double finalDamage = processDamage(player, (LivingEntity) event.getEntity(), result, tag);
				if (finalDamage <= 0) {
					event.setCancelled(true);
					
				} else {
					if (event.getEntity() instanceof Player) {
						processPartDamage(player, (Player) event.getEntity(), result, finalDamage);
					} else {
						event.setDamage(finalDamage);
					}
				}
				
				
				player.sendMessage("damage " + finalDamage);
//				processPartDamage(player, result, finalDamage);
				
				content.remove(mod);
				bullet_info.remove(mod);


				
			}
			
			

			
		}
	}

	
	private void processPartDamage(Player player, Player damager, int result, double finalDamage) {
		Container.processDamage(player, damager, result, finalDamage);
	}


	private double processDamage(Player player, org.bukkit.entity.LivingEntity victim, int result, NBTTagCompound tag) {
		
		ItemStack protector;
		if (result == 0) {
			return 0d;
		} else if (result == 1) {
			protector = victim.getEquipment().getBoots();
		} else if (result == 2) {
			protector = victim.getEquipment().getLeggings();
		} else if (result == 3) {
			protector = victim.getEquipment().getChestplate();
		} else {
			protector = victim.getEquipment().getHelmet();
		}
		
		JSONObject armorInfo = (JSONObject) CartridgeAssemblerListener.database.get(protector.getType().toString());
		double defense;
		if (armorInfo == null) {
			defense = 0;
		} else {
			defense = (double) armorInfo.get("base");
		}
		
		String[] a = NBT.getString(tag, "id").split("_");
		
		String bullet = "RELIZC_BULLET_" + a[1] + "_" + a[3].toUpperCase(); // relizc:cartridge_55645_copper_m855
//		player.sendMessage(bullet);
		JSONObject data = (JSONObject) CartridgeAssemblerListener.database.get(bullet);
		int energy = NBT.getInteger(NBT.getCompound(tag, "tag"), "energy");
		
		double pen = (double) data.get("pen");
		
		if (pen < defense) {
			return 0;
		} else {
			return (double) data.get("base");
		}
		
		
	}


	private boolean detectPart(Player player, org.bukkit.entity.Entity entity, MohistModsEntity mod, double yi, double yf, double size, double speed, boolean delta) {
		

        Location rayOrigin = player.getEyeLocation(); // Ray starts from the player's eye
        Vector rayDirection = rayOrigin.getDirection().normalize(); // Ray direction is where the player is lookin
//        
        Location targetLocation = entity.getLocation();
        
//        drawCubeOutline(targetLocation.add(new Vector(-size, yi, -size)), targetLocation.add(new Vector(size, yf, size)), Particle.VILLAGER_HAPPY, 0.05);
        
        Vector boxMin = targetLocation.toVector().add(new Vector(-size, yi, -size)); // Adjust for width
        Vector boxMax = targetLocation.toVector().add(new Vector(size, yf, size)); // Adjust for height
        
//        if (delta) {
//        	Bukkit.broadcastMessage("spreadDelta");
//        	Vector offset = new Vector(Hit.delta * Math.random(), Hit.delta * Math.random(), Hit.delta * Math.random());
//        	boxMin.add(offset);
//        	boxMax.add(offset);
//        }
		
		return RayCast.rayIntersectsAABBWithGravity(rayOrigin.toVector(), rayDirection, boxMin, boxMax, speed);
	}
	
	

//	@EventHandler
//    public void onPlayerInteract(PlayerInteractEvent event) {
//		
//		long a = System.currentTimeMillis();
//		
//        // Check if the action is a left-click
//        if (!event.getAction().toString().contains("LEFT_CLICK")) {
//            return;
//        }
//
//        Player shooter = event.getPlayer();
//        Location rayOrigin = shooter.getEyeLocation(); // Ray starts from the player's eye
//        Vector rayDirection = rayOrigin.getDirection().normalize(); // Ray direction is where the player is looking
//
//        // Iterate through all online players to check for a hit
//        for (Player target : Bukkit.getOnlinePlayers()) {
//            if (target.equals(shooter)) {
//                continue; // Skip the shooter
//            }
//
//            // Get the target player's hitbox (AABB)
//            Location targetLocation = target.getLocation();
//            Vector boxMin = targetLocation.toVector().add(new Vector(-0.3, 0, -0.3)); // Adjust for width
//            Vector boxMax = targetLocation.toVector().add(new Vector(0.3, 1.8, 0.3)); // Adjust for height
//
//            // Check for ray intersection
//            if (RayCast.rayIntersectsAABBWithGravity(rayOrigin.toVector(), rayDirection, boxMin, boxMax)) {
//                shooter.sendMessage("§aYou hit " + target.getName() + "!");
//                target.sendMessage("You were hit by " + shooter.getName() + "!");
//                break;
//            }
//        }
//        
//        long b = System.currentTimeMillis();
//        
//        ChatUtils.broadcastSystemMessage("ballistics", "algorithmTime: " + (b - a) + "ms gravity: " + g + "b/s^2 bulletSpeed: " + bulletSpeed);
//    }
	
	@EventHandler
	public void drop(PlayerDropItemEvent event) {
		
		if (event.getItemDrop().getItemStack() == event.getPlayer().getItemInHand() && event.getItemDrop().getItemStack().getType() == SwapHands.TACZ_MODERN_KINETIC_GUN) {
			
			event.setCancelled(false);
			ChatUtils.systemMessage(event.getPlayer(), Locale.get(event.getPlayer(), "tacz.weapon"), Locale.get(event.getPlayer(), "tacz.weapon.undroppable"));
			
		}
		
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {

			
		if (event.getEntity().getType() == BULLET) {
			
			MohistModsEntity mod = (MohistModsEntity) event.getEntity();
			
			CraftEntity entity = (CraftEntity) mod;
			Entity nmsEntity = entity.getHandle();
			
			NBTTagCompound nbt = new NBTTagCompound();
			nmsEntity.f(nbt); // 1775:1847:net.minecraft.nbt.CompoundTag saveWithoutId(net.minecraft.nbt.CompoundTag) -> f

			String uuid0 = Integer.toHexString(nbt.n("Owner")[0]);
			String uuid1 = Integer.toHexString(nbt.n("Owner")[1]);
			String uuid2 = Integer.toHexString(nbt.n("Owner")[2]); 
			String uuid3 = Integer.toHexString(nbt.n("Owner")[3]);
			
			String hypen = java.util.UUID.fromString(
					(uuid0 + uuid1 + uuid2 + uuid3)
				    .replaceFirst( 
				        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5" 
				    )
				).toString();
			
			Player player = Bukkit.getPlayer(UUID.fromString(hypen));
			
			
			ItemStack i = player.getItemInHand();
			NBTTagCompound data = NBT.getNBT(i);
			NBTTagList ammo = NBT.getNBTArray(data, "AmmoContent", NBTTagType.TAG_Compound);
			
			NBT.setInteger(data, "GunCurrentAmmoCount", 0);
			
			int index = NBT.getInteger(data, "AmmoIndex");
			if (ammo.size() != 0) {
				
				if (index == 1) {
					NBT.setBoolean(data, "HasBulletInBarrel", false);
					
					NBTTagList emptyMag = new NBTTagList();
					NBT.setCompound(data, "AmmoContent", emptyMag);
					
				} else {
					NBT.setBoolean(data, "HasBulletInBarrel", true);
				}
				
				NBTTagCompound current = NBT.getCompound(ammo, index - 1);
				

				bullet_info.put(mod, current);
				
//				Bukkit.broadcastMessage(entityNBT.toString());
				
				NBT.setInteger(data, "AmmoIndex", index - 1);
				
				
				
				
			} else {
//				event.setCancelled(true);
			}
			
			
			i = NBT.setCompound(i, data);
			player.setItemInHand(i);

			
			Vector vec = mod.getVelocity();
			double speed = vec.length() * 20;
			
			content.put(mod, new BallisticInformation(speed, player));
			TaskDelay.delayTask(new Runnable() {

				@Override
				public void run() {
					content.remove(mod);
					bullet_info.remove(mod);
				}
				
			}, 20L);
			
			
			
		}

	}


}
