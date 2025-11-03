package net.itsrelizc.gunmod.deathutils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.Profile.NewPlayerJoinedEvent;
import net.itsrelizc.players.locales.Locale;

public class DeathListeners implements Listener {
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void interact(PlayerInteractEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(BlockBreakEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(BlockPlaceEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void breaker(EntityDamageByEntityEvent event) {
		
		if (!(event.getDamager() instanceof Player)) return;
		
		Player player = (Player) event.getDamager();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(PlayerInteractEntityEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(PlayerFishEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(PlayerDropItemEvent event) {
		if (DeathUtils.isDead(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(EntityShootBowEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
			player.setSaturation(20);
			player.setFoodLevel(20);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(CraftItemEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
		
		Player player = (Player) event.getWhoClicked();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void pickup(PlayerPickupItemEvent event) {
		
		Player player = event.getPlayer();
		
		////("clearing");
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void breaker(EntityRegainHealthEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
			
			player.setHealth(20);
		}
	}
	
	@EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
		if (!(event.getAttacker() instanceof Player)) return;
		
		Player player = (Player) event.getAttacker();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
    }
	
	
	
	@EventHandler
    public void onVehicleDestroy(VehicleDamageEvent event) {
		if (!(event.getAttacker() instanceof Player)) return;
		
		Player player = (Player) event.getAttacker();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
    }
	
	@EventHandler
    public void onVehicleDestroy(VehicleEnterEvent event) {
		if (!(event.getEntered() instanceof Player)) return;
		
		Player player = (Player) event.getEntered();
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
		}
    }
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void newguy(NewPlayerJoinedEvent event) {
		event.getProfile().setMetadata("freeRevives", 15l);
		event.getProfile().save();
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void target(EntityTargetEvent event) {
		if (!(event.getTarget() instanceof Player)) return;
		
		Player player = (Player) event.getTarget();
		
		////(player.getName());
		
		if (DeathUtils.isDead(player)) {
			event.setCancelled(true);
			
			event.setTarget(null);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void join(PlayerJoinEvent event) {

		
		if (DeathUtils.isDead(event.getPlayer())) {
			event.getPlayer().setAllowFlight(true);
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.hidePlayer(event.getPlayer());
			}
			
		} else {
			event.getPlayer().setAllowFlight(false);
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.showPlayer(event.getPlayer());
			}
		}
		
		
		
		Player player = event.getPlayer();
		
		PotionEffect pot = player.getPotionEffect(PotionEffectType.INVISIBILITY);
		if (pot != null) {
			if (pot.getDuration() > (1000 * 24 * 60 * 60 * 20)) {
				if (!DeathUtils.getDeadSet().contains(player.getUniqueId().toString())) {
					player.getInventory().clear();
					Location bed = player.getBedSpawnLocation();
					if (bed == null) {
						bed = Bukkit.getWorld("world").getSpawnLocation();
						player.sendMessage(Locale.a(player, "death.nobed"));
					}
//					player.teleport(bed);
					CustomPlayerTeleportEvent.teleport(player, bed);
					player.setFallDistance(0);
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
					player.removePotionEffect(PotionEffectType.BLINDNESS);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					player.removePotionEffect(PotionEffectType.SPEED);
					
					DeathUtils.deadteam.removeEntry(player.getName());
					
				}
			}
		}
		
	}
}
