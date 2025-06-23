package net.itsrelizc.health2;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.gunmod.deathutils.DamageLogs;
import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.health2.Limb.RelizcDamageCause;
import net.itsrelizc.players.Profile;

public class Body {
	
	public static class PlayerBodyHealthStatusChangedEvent extends Event {
		
		private static final HandlerList HANDLERS = new HandlerList();
		private int limbId;
		private Limb limb;
		private Player player;
		private Body body;

		@Override
		public HandlerList getHandlers() {
			// TODO Auto-generated method stub
			return HANDLERS;
		}
		
		public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
		
		public PlayerBodyHealthStatusChangedEvent(Player player, int limbId, Limb limb, Body body) {
			this.limbId = limbId;
			this.player = player;
			this.limb = limb;
			this.body = body;
		}
		
		public Limb getLimb() {
			return this.limb;
		}
		
		public Player getPlayer() {
			return this.player;
		}
		
		public int getLimbId() {
			return this.limbId;
		}
		
		public Body getBody() {
			return body;
		}
	}
	
	public static Map<String, Body> parts = new HashMap<String, Body>();
	
	private Limb head;
	private Limb leftArm;
	private Limb rightArm;
	private Limb chest;
	private Limb abs;
	private Limb leftLegs;
	private Limb rightLegs;
	private long maxTotalHealth = 0;
	private LivingEntity owner;
	
	private boolean[] isDisplaying = {false, false, false, false, false, false, false};
	
	private boolean showing = false;
	
	public long getMaxHealth() {
		return maxTotalHealth;
	}
	
	public long getHealth() {
		long health = 0;
		for (int i = 0; i < 7; i ++) {
			health += convert(i).getHealth();
		}
		return health;
	}
	
	
	public Body ( LivingEntity entity ) {
		head = new Limb(entity, 30, 30, "head");
		chest = new Limb(entity, 60, 60, "chest");
		leftArm = new Limb(entity, 80, 80, "leftArm");
		rightArm = new Limb(entity, 80, 80, "rightArm");
		abs = new Limb(entity, 40, 40, "abs");
		leftLegs = new Limb(entity, 60, 60, "leftLegs");
		rightLegs = new Limb(entity, 60, 60, "rightLegs");
		
		this.owner = entity;
		
		for (int i = 0; i < 7; i ++) {
			maxTotalHealth += convert(i).getMaxHealth();
		}
	}
	
	public Limb convert(int partId) {
		if (partId == 0) return head;
		else if (partId == 1) return chest;
		else if (partId == 2) return abs;
		else if (partId == 3) return leftArm;
		else if (partId == 4) return rightArm;
		else if (partId == 5) return leftLegs;
		else if (partId == 6) return rightLegs;
		else return null;
		
	}
	
	
	private int countHealthyParts() {
		int c = 0;
		for (int i = 0; i < 7; i ++) {
			if (convert(i).getHealth() > 0) {
				c ++;
			}
		}
		return c;
	}
	
	public void damage(int partId, long amount, String damageCause) {
		damage(partId, amount, damageCause, null);
	}
	
	/**
	 * For partId, please refer:
	 * Head - 0
	 * Chest - 1
	 * Stomach - 2
	 * Left Arm - 3
	 * Right Arm - 4
	 * Left Legs - 5
	 * Right Legs - 6
	 * Average Distribution = -1
	 * 
	 * @param partId The part to damage
	 * @param amount The amount of damage
	 * @param damageCause 
	 */
	public void damage(int partId, long amount, String damageCause, Limb from) {
		
		
		
		if (owner instanceof Player) {
			if (DeathUtils.isDead((Player) owner)) {
				return;
			}
		} else {
			if (owner.isDead()) {
				return;
			}
		}
		
		owner.damage(0.1);

//		
		Limb limb = convert(partId);
		
		long remaining = limb.damage(amount, damageCause, from);
		
		if (owner instanceof Player) {
			PlayerBodyHealthStatusChangedEvent event = new PlayerBodyHealthStatusChangedEvent((Player) owner, partId, convert(partId), this);
			Bukkit.getPluginManager().callEvent(event);
		}
		
		
		
		while (remaining > 0) {
			int healthy = countHealthyParts();
			
			if (healthy == 0) {
				convert(partId).setLatestRecordAsLethal();
				death(damageCause);
				break;
			}
			
			long average = Math.max(remaining / healthy, 1);
			
			for (int i = 6; i >= 0; i --) {
				
				
				
				Limb current = convert(i);
				if (current.getHealth() <= 0) continue;
				
				remaining -= average;
				remaining += current.damage(average, damageCause, limb);
				if (owner instanceof Player) {
					PlayerBodyHealthStatusChangedEvent event2 = new PlayerBodyHealthStatusChangedEvent((Player) owner, i, convert(i), this);
					Bukkit.getPluginManager().callEvent(event2);
				}
				
				
				if (remaining <= 0) break;
				
			}
		}
//		
		
		
		
		this.updateLegStatus();
		refreshHealthDisplay();
		
		
		
		
		if (convert(0).getHealth() <= 0 || convert(1).getHealth() <= 0) {
			convert(partId).setLatestRecordAsLethal();
			death(damageCause);
		}
	}
	
	private void death(String damageCause) {
		
		//this.reset();
		if (owner instanceof Player) {
			DeathUtils.addPlayer((Player) owner, damageCause);
		} else {
			owner.setHealth(0);
		}

	}
	
	private void updateLegStatus() {
		if (owner instanceof Player) {
			int res = 0;
			if (convert(5).getHealth() <= 0) {
				res ++;
			}
			if (convert(6).getHealth() <= 0) {
				res ++;
			}
			Player player = (Player) owner;
			player.setWalkSpeed(0.2f - (0.05f * res));
		}
	}



	public void healWithPriority(long amount) {
		
		if (owner instanceof Player) {
			if (DeathUtils.isDead((Player) owner)) {
				return;
			}
		} else {
			if (owner.isDead()) {
				return;
			}
		}
		
		for (int i = 0; i < 7; i ++) {
			if (amount <= 0) break;
			Limb limb = convert(i);
			
			
			
			
			amount = limb.heal(amount);
			
			if (owner instanceof Player) {
				PlayerBodyHealthStatusChangedEvent event = new PlayerBodyHealthStatusChangedEvent((Player) owner, i, convert(i), this);
				Bukkit.getPluginManager().callEvent(event);
			}
		}
		
		if (amount >= 0) {
			
		}
		
		this.updateLegStatus();
		refreshHealthDisplay();
		
	}
	
	public void reset() {
		for (int i = 0; i < 7; i ++) {
			convert(i).reset();
			if (owner instanceof Player) {
				PlayerBodyHealthStatusChangedEvent event = new PlayerBodyHealthStatusChangedEvent((Player) owner, i, convert(i), this);
				Bukkit.getPluginManager().callEvent(event);
			}
			
		}
	}

	public void refreshHealthDisplay() {
		//Bukkit.broadcastMessage("refreshing ");
		long total = 0;
		for (int i = 0; i < 7; i ++) {
			total += convert(i).getHealth();
		}
		
		double ratio = (total * 1.0) / maxTotalHealth;
		//Bukkit.broadcastMessage(ratio + "");
		
		double health = Math.floor(Math.max(ratio * 20, 1));
		
		if (this.getHealth() != this.getMaxHealth()) {
			health = Math.min(18, health);
		} else {
			health = 20;
		}
		
		owner.setHealth(health);
	}

	public void damage(int result, int amount, RelizcDamageCause fragment) {
		damage(result, amount, "damage." + fragment.toString().toLowerCase());
	}

	public void setPlayer(LivingEntity livingEntity) {
		owner = livingEntity;
		
		for (int i = 0; i < 7; i ++) {
			convert(i).setPlayer(livingEntity);
		}
	}
	
	public long calculateCureFee(long currentHealth, long maxHealth) {
		
		if (owner instanceof Player) {
			Profile prof = Profile.findByOwner((Player) owner);
			
			if (((Long) prof.getMetadata("freeRevives")) > 0 || ((Long) prof.getMetadata("level")) <= 5) {
				return -1;
			}
		}
		
		
	    if (currentHealth >= maxHealth) return 0L;

	    double missingHealth = maxHealth - currentHealth;
	    double baseRate = 0.05;

	    // Logarithmic scaling
	    double scalingFactor = Math.log1p(missingHealth) / Math.log1p(maxHealth);
	    double costMultiplier = 1.0 + scalingFactor;

	    double totalCost = missingHealth * baseRate * costMultiplier;

	    return (long) Math.ceil(totalCost);
	}


}
