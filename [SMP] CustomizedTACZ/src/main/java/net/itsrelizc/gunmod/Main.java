package net.itsrelizc.gunmod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathListeners;
import net.itsrelizc.gunmod.deathutils.DeathSummaryScreen;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.gunmod.deathutils.LegBrokenJumpListeners;
import net.itsrelizc.gunmod.deathutils.PlayerGhostOutsideBoundChecker;
import net.itsrelizc.gunmod.deathutils.RelizcOverridedPlayerHead;
import net.itsrelizc.gunmod.deathutils.RightClickBody;
import net.itsrelizc.gunmod.items.RelizcItemMFCU;
import net.itsrelizc.gunmod.items.RelizcItemSatellitePhone;
import net.itsrelizc.gunmod.items.armor.RelizcNeoArmor;
import net.itsrelizc.gunmod.items.armor.RelizcOverridenLeatherHelmet;
import net.itsrelizc.gunmod.npcs.SleepingTrait;
import net.itsrelizc.health2.BedHealListener;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.ballistics.FragUtils;
import net.itsrelizc.health2.fletching.ArrowUtils;
import net.itsrelizc.health2.fletching.RelizcNeoArrow;
import net.itsrelizc.health2.fletching.RelizcOverridenBow;
import net.itsrelizc.health2.fletching.RelizcOverridenCrossbow;
import net.itsrelizc.health2.fletching.RelizcSpectralArrow;
import net.itsrelizc.health2.fletching.RelizcTippedArrow;
import net.itsrelizc.health2.penetration.ArrowHitListeners;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.players.Grouping;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new CommandDamage());
		CommandRegistery.register(new DeathSummaryScreen.CommandRevive());
		
		EventRegistery.register(new DeathListeners());
		EventRegistery.register(this);
		EventRegistery.register(new RightClickBody());
		EventRegistery.register(new DeathSummaryScreen.DeathSummaryListeners());
		EventRegistery.register(new LegBrokenJumpListeners());
		EventRegistery.register(new ArrowHitListeners());
		EventRegistery.register(new BedHealListener());
		EventRegistery.register(new PlayerGhostOutsideBoundChecker());
		
		DeathUtils.init();
		
		ItemUtils.register(RelizcOverridedPlayerHead.class);
		ItemUtils.register(RelizcNeoArrow.class);
		ItemUtils.register(RelizcOverridenBow.class);
		ItemUtils.register(RelizcOverridenCrossbow.class);
		ItemUtils.register(RelizcItemMFCU.class);
		ItemUtils.register(RelizcItemSatellitePhone.class);
		ItemUtils.register(RelizcSpectralArrow.class);
		ItemUtils.register(RelizcTippedArrow.class);
		
		EventRegistery.register(new RelizcNeoArmor.RelizcNeoArmorDamageRepairListener());
		ItemUtils.register(RelizcOverridenLeatherHelmet.class);
		
		try {
			ArrowUtils.loadAllArrowPoints();
			ArrowUtils.loadAllArrowFletchings();
			ArrowUtils.loadAllArrowShafts();
		} catch (Exception e) {
			this.getLogger().severe("One or more arrow ballistic data cannot be loaded.");
		}
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SleepingTrait.class).withName("sleepingtrait"));
		
		ArrowHitListeners.startDetectingArrows();
		
	}
	
	@Override
	public void onDisable() {
		
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("nocollision").unregister();
		
		Grouping.removeAllTeams();
	}
	
	
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onPlayerDamagesPlayer(EntityDamageByEntityEvent event) {
        // Check if the entity being damaged is a player
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof LivingEntity)) return;

        // Check if the damager is a player (can also be a projectile, etc.)
        //if (!(event.getDamager() instanceof Player)) return;

        LivingEntity damager = (LivingEntity) event.getDamager();
        Player damaged = (Player) event.getEntity();
        
        long actual = (long) (event.getFinalDamage() * 5);
		
		event.setDamage(0);

        // Call your custom logic here
//        damager.sendMessage("You hit " + damaged.getName());
//        damaged.sendMessage("You were hit by " + damager.getName());
        Body body = Body.parts.get(damaged.getUniqueId().toString());
        
        ItemStack it = damager.getEquipment().getItemInMainHand();
        String name;
        if (it == null || it.getType() == Material.AIR) {
        	name = "damage.empty_hand";
        } else {
        	name = it.getItemMeta().getDisplayName();
        }
        
        body.damageAverage(actual, name, damager);
    }
	
	@EventHandler
	public void onFireworkExplode(FireworkExplodeEvent event) {
	    Firework firework = event.getEntity();
	    
	    // You can get the shooter (if set earlier)
//	    if (firework.getShooter() instanceof Player) {
//	        Player shooter = (Player) firework.getShooter();
//	        //shooter.sendMessage("Your firework has exploded!");
//	    }
	    FragUtils.spawnFragmentsRandomly(event.getEntity().getLocation(), 10, 8);
	    TNTPrimed tnt = event.getEntity().getLocation().getWorld().spawn(event.getEntity().getLocation(), TNTPrimed.class);
	    tnt.setFuseTicks(0); // Explode immediately

	    // Debug
	    //System.out.println("Firework exploded at " + firework.getLocation());
	}
	
	@EventHandler
	public void onShoot(EntityShootBowEvent event) {
	    // Check if the shooter is a player or something else
	    if (!(event.getEntity() instanceof Player)) return;

	    // Check if the bow is a crossbow
	    ItemStack weapon = event.getBow();
	    if (weapon == null || weapon.getType() != Material.CROSSBOW) return;

	    // Check if the projectile is a firework
	    if (event.getProjectile() instanceof Firework) {
	        Firework firework = (Firework) event.getProjectile();
	        firework.setShooter(event.getEntity()); // Usually a Player or Mob
	    }
	}
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void damage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		if (event.getCause() == DamageCause.ENTITY_ATTACK || event.getCause() == DamageCause.ENTITY_SWEEP_ATTACK) return;
		DamageCause cause = event.getCause();
		if (cause == DamageCause.PROJECTILE || cause == DamageCause.FALL) return;
		
		long actual = (long) (event.getFinalDamage() * 5);
		
		event.setDamage(0);
		
		
		
		Player player = (Player) event.getEntity();
		
		
		Body body = Body.parts.get(player.getUniqueId().toString());
		
		body.damageAverage(actual, "damage." + event.getCause().toString(), null);
		
	}
	
	
	
	
	
//	@EventHandler
//	public void onTNTExplode(EntityExplodeEvent event) {
//	    if (!(event.getEntity() instanceof TNTPrimed)) return;
//
//	    Location center = event.getLocation();
//
//	    
//	    
//	    
//	    FragUtils.spawnStandardFragmentation(center, (int) (25 * Math.random() + 15), 8);
//	    
//	}
	
	

	// Utility to generate a random unit vector
	

	
	@EventHandler(ignoreCancelled=true)
	public void damageFall(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		Player player = (Player) event.getEntity();
		
		if (!(event.getCause() == DamageCause.FALL)) return;
		
		//System.out.println("Falling " + event.getDamage());
		
		long amt = (long) (event.getDamage() * 10);
		long avg = amt / 2;
		long other = amt - avg;
		
		event.setDamage(0);
		
		Body body = Body.parts.get(player.getUniqueId().toString());
		body.damage(5, avg, "damage." + event.getCause().toString().toLowerCase());
		body.damage(6, other, "damage." + event.getCause().toString().toLowerCase());
	}
	
//	@EventHandler
//	public void respawn(PlayerRespawnEvent event) {
//		Body body = Body.parts.get(event.getPlayer().getUniqueId().toString());
//		body.reset();
//	}
	
	
    // Helper: rotate vector around Y axis by degrees
    

    

    
    @EventHandler
    public void join(PlayerJoinEvent event) {
    	
    	if (!Body.parts.containsKey(event.getPlayer().getUniqueId().toString())) {
    		Body.parts.put(event.getPlayer().getUniqueId().toString(), new Body(event.getPlayer()));
    	} else {
    		Body body = Body.parts.get(event.getPlayer().getUniqueId().toString());
    		
    		body.setPlayer(event.getPlayer());
    	}
    	
    }
    
    // for realistic npcs
//    @EventHandler
//    public void join(NPCSpawnEvent event) {
//    	
//    	////((event.getNPC().getEntity() instanceof LivingEntity) + " " + event.getNPC().getUniqueId().toString());
//    	if (!(event.getNPC().getEntity() instanceof LivingEntity)) return;
//
//    	event.getNPC().data().set("uuidv2", UUIDConverter.forceUUIDv2(event.getNPC().getUniqueId()));
//    	
//    	if (!Body.parts.containsKey(event.getNPC().data().get("uuidv2"))) {
//    		Body.parts.put(event.getNPC().data().get("uuidv2").toString(), new Body((LivingEntity) event.getNPC().getEntity()));
//    	} else {
//    		
//    		Body body = Body.parts.get(event.getNPC().data().get("uuidv2").toString());
//    		
//    		body.setPlayer((LivingEntity)event.getNPC().getEntity());
//    	}
//    	
//    	
//    }
    
    @EventHandler
    public void regen(EntityRegainHealthEvent event) {
    	if (!(event.getEntity() instanceof Player)) return;
    	Player player = (Player) event.getEntity();
    	Body body = Body.parts.get(player.getUniqueId().toString());
    	
    	
    	
    	long amount = (long) (event.getAmount() * 10);
    	
    	body.healWithPriority(amount);
    	
    	event.setCancelled(true);
    }
    
    
    
    
    public static boolean isPlayerBetweenPoints(Player player, Location a, Location b) {
        Location playerLoc = player.getLocation();
        
        Vector boxMin = playerLoc.clone().add(-0.3, 0, -0.3).toVector();
        Vector boxMax = playerLoc.clone().add(0.3, 1.8, 0.3).toVector();

        Vector origin = a.toVector();
        Vector end = b.toVector();
        Vector dir = end.clone().subtract(origin);
        
        Vector invDir = new Vector(
            1.0 / (dir.getX() == 0 ? 1e-10 : dir.getX()),
            1.0 / (dir.getY() == 0 ? 1e-10 : dir.getY()),
            1.0 / (dir.getZ() == 0 ? 1e-10 : dir.getZ())
        );

        double tmin = (boxMin.getX() - origin.getX()) * invDir.getX();
        double tmax = (boxMax.getX() - origin.getX()) * invDir.getX();
        if (tmin > tmax) { double temp = tmin; tmin = tmax; tmax = temp; }

        double tymin = (boxMin.getY() - origin.getY()) * invDir.getY();
        double tymax = (boxMax.getY() - origin.getY()) * invDir.getY();
        if (tymin > tymax) { double temp = tymin; tymin = tymax; tymax = temp; }

        if (tmin > tymax || tymin > tmax) return false;
        if (tymin > tmin) tmin = tymin;
        if (tymax < tmax) tmax = tymax;

        double tzmin = (boxMin.getZ() - origin.getZ()) * invDir.getZ();
        double tzmax = (boxMax.getZ() - origin.getZ()) * invDir.getZ();
        if (tzmin > tzmax) { double temp = tzmin; tzmin = tzmax; tzmax = temp; }

        if (tmin > tzmax || tzmin > tmax) return false;
        if (tzmin > tmin) tmin = tzmin;
        if (tzmax < tmax) tmax = tzmax;

        // Optional: Clamp tmin/tmax to [0,1] if you want segment only, not infinite ray
        return tmax >= 0 && tmin <= 1;
    }

    
    public static void main(String[] args) {
    	
    }
    
    
}
