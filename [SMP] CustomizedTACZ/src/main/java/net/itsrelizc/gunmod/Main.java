package net.itsrelizc.gunmod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DeathListeners;
import net.itsrelizc.gunmod.deathutils.DeathSummaryScreen;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.gunmod.deathutils.RelizcOverridedPlayerHead;
import net.itsrelizc.gunmod.deathutils.RightClickBody;
import net.itsrelizc.gunmod.npcs.SleepingTrait;
import net.itsrelizc.health2.Body;
import net.itsrelizc.health2.ballistics.Collisions;
import net.itsrelizc.health2.ballistics.FragUtils;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.nbt.UUIDConverter;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new CommandDamage());
		CommandRegistery.register(new DeathSummaryScreen.CommandRevive());
		
		EventRegistery.register(new DeathListeners());
		EventRegistery.register(this);
		EventRegistery.register(new RightClickBody());
		EventRegistery.register(new DeathSummaryScreen.DeathSummaryListeners());
		
		DeathUtils.init();
		
		ItemUtils.register(RelizcOverridedPlayerHead.class);
		
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(SleepingTrait.class).withName("sleepingtrait"));
		
	}
	
	@Override
	public void onDisable() {
		
		Bukkit.getScoreboardManager().getMainScoreboard().getTeam("nocollision").unregister();
		
	}
	
	@EventHandler(ignoreCancelled=true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
		// DamageCause.PROJECTILE
		
		
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Arrow )) return;
        
        
        
        Player player = (Player) event.getEntity();
        
        if (DeathUtils.isDead(player)) {
        	event.setCancelled(true);
        	return;
        }
        
        Arrow arrow = (Arrow) event.getDamager();

        Location playerLoc = player.getLocation();
        Location arrowLoc = arrow.getLocation();

        // Player body yaw (0 is south, increases clockwise)
        float bodyYaw = playerLoc.getYaw();

        // Calculate vector from player feet location to arrow hit location
        Vector relativeHitVec = arrowLoc.toVector().subtract(playerLoc.toVector());

        // Rotate relativeHitVec by -bodyYaw degrees to get it in player's local coordinate system
        Vector localVec = rotateVectorAroundY(relativeHitVec, -bodyYaw);

        // Now localVec.x < 0 means left side, > 0 means right side
        // localVec.y = height difference (vertical)
        // localVec.z = forward/backward (we can ignore or use for depth)

        int hitPart = Collisions.determineHitPart(localVec);

        if (hitPart != -1) {
        	Bukkit.broadcastMessage(player.getUniqueId().toString());
        	Body body = Body.parts.get(player.getUniqueId().toString());
        	body.damage(hitPart, (long) (event.getFinalDamage() * 10), "damage." + event.getCause().toString().toLowerCase());
        	
        }
    }
	
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void damage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		event.setDamage(0);
		
		DamageCause cause = event.getCause();
		if (cause == DamageCause.PROJECTILE || cause == DamageCause.FALL) return;
		
		Player player = (Player) event.getEntity();
		
		
		
		
	}
	
	
	
	
	
	@EventHandler
	public void onTNTExplode(EntityExplodeEvent event) {
	    if (!(event.getEntity() instanceof TNTPrimed)) return;

	    Location center = event.getLocation();

	    
	    
	    
	    FragUtils.spawnStandardFragmentation(center, (int) (25 * Math.random() + 15), 8);
	    
	}
	
	

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
    private Vector rotateVectorAroundY(Vector vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        double x = vec.getX() * cos - vec.getZ() * sin;
        double z = vec.getX() * sin + vec.getZ() * cos;

        return new Vector(x, vec.getY(), z);
    }

    

    
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
    @EventHandler
    public void join(NPCSpawnEvent event) {
    	
    	//Bukkit.broadcastMessage((event.getNPC().getEntity() instanceof LivingEntity) + " " + event.getNPC().getUniqueId().toString());
    	if (!(event.getNPC().getEntity() instanceof LivingEntity)) return;

    	event.getNPC().data().set("uuidv2", UUIDConverter.forceUUIDv2(event.getNPC().getUniqueId()));
    	
    	if (!Body.parts.containsKey(event.getNPC().data().get("uuidv2"))) {
    		Body.parts.put(event.getNPC().data().get("uuidv2").toString(), new Body((LivingEntity) event.getNPC().getEntity()));
    	} else {
    		
    		Body body = Body.parts.get(event.getNPC().data().get("uuidv2").toString());
    		
    		body.setPlayer((LivingEntity)event.getNPC().getEntity());
    	}
    	
    	
    }
    
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
