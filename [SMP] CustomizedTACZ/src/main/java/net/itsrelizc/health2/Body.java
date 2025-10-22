package net.itsrelizc.health2;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.gunmod.deathutils.DeathUtils;
import net.itsrelizc.health2.Limb.RelizcDamageCause;
import net.itsrelizc.health2.ballistics.Collisions.BodyPart;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;

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
	
	protected Limb head;
	protected Limb leftArm;
	protected Limb rightArm;
	protected Limb chest;
	protected Limb abs;
	protected Limb leftLegs;
	protected Limb rightLegs;
	protected long maxTotalHealth = 0;
	protected LivingEntity owner;
	
	private boolean[] isDisplaying = {false, false, false, false, false, false, false};
	
	private boolean showing = false;

	public String status;
	
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
	
	public void printStatus() {
		//(owner.getName() + "'s Body (" + getHealth() +"/" + getMaxHealth() + ")");
		for (int i = 0; i < 7; i ++) {
			Limb limb = convert(i);
			//(limb.getName() + " " + limb.getHealth() + "/" + limb.getMaxHealth());
		}
	}
	
	
	public Body ( LivingEntity entity ) {
		head = new Limb(entity, 30, 30, "head"); 
		chest = new Limb(entity, 60, 60, "chest");
		leftArm = new BreakableLimb(entity, 80, 80, "leftArm");
		rightArm = new BreakableLimb(entity, 80, 80, "rightArm");
		abs = new Limb(entity, 40, 40, "abs");
		leftLegs = new BreakableLimb(entity, 60, 60, "leftLegs");
		rightLegs = new BreakableLimb(entity, 60, 60, "rightLegs");
		
		this.owner = entity;
		
		for (int i = 0; i < 7; i ++) {
			maxTotalHealth += convert(i).getMaxHealth();
		}
		
		status = "normal";
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
	
	private int countFullHealthyParts() {
		int c = 0;
		for (int i = 0; i < 7; i ++) {
			if (convert(i).getHealth() == convert(i).getMaxHealth()) {
				c ++;
			}
		}
		return c;
	}
	
	public boolean isAllHealthy() {
		return countFullHealthyParts() == 7;
	}
	
	public void damage(int partId, long amount, String damageCause) {
		damage(partId, amount, damageCause, null, null);
	}
	
	public void damage(int partId, long amount, String damageCause, Limb from) {
		damage(partId, amount, damageCause, from, null);
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
	public void damage(int partId, long amount, String damageCause, Limb from, LivingEntity killer) {
		
		////(" " + amount);
		
		
		if (owner instanceof Player) {
			if (DeathUtils.isDead((Player) owner)) {
				return;
			}
		} else {
			if (owner.isDead()) {
				return;
			}
		}
		
		owner.playHurtAnimation(0f);
		owner.getWorld().playSound(owner.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
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
				death(damageCause, killer);
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
			death(damageCause, killer);
		}
		
		//this.printStatus();
	}
	
	private void death(String damageCause, LivingEntity killer) {
		
		//this.reset();
		if (owner instanceof Player) {
			DeathUtils.addPlayer((Player) owner, damageCause, killer);
		} else {
			owner.setHealth(0);
		}

	}
	
	public void updateLegStatus() {
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
		this.updateLegStatus();
		}

	public void refreshHealthDisplay() {
		////("refreshing ");
		long total = 0;
		for (int i = 0; i < 7; i ++) {
			total += convert(i).getHealth();
		}
		
		double ratio = (total * 1.0) / maxTotalHealth;
		////(ratio + "");
		
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

	public int damageWithPenetrationCheck(int result, long damage, String string, Limb collateral,
			LivingEntity shooter, long penetration, BodyPart hit) {
		
		EquipmentSlot slot;
		
		////(" " + hit + " " + result);
		
		if (hit == BodyPart.HEAD) slot = EquipmentSlot.HEAD;
		else if (hit == BodyPart.CHEST) {
			if (result == 3 || result == 4) slot = EquipmentSlot.HAND;
			else slot = EquipmentSlot.CHEST;
		} else if (hit == BodyPart.LEGS) slot = EquipmentSlot.LEGS;
		else if (hit == BodyPart.FEET) slot = EquipmentSlot.FEET;
		else if (hit == BodyPart.STOMACH) slot = EquipmentSlot.HAND;
		else slot = null;
		
		if (slot == null) return -1;
		
		if (slot == EquipmentSlot.HAND) {
			damage(result, damage, string, collateral, shooter);
			return 0;
		}
		
		ItemStack stack = owner.getEquipment().getItem(slot);
		
		
		
		if (stack == null || stack.getType() == Material.AIR) {
			
			damage(result, damage, string, collateral, shooter);
			return 0;
		} else {
			long prot = 0;
			if (stack.getType().toString().startsWith("LEATHER")) 			prot = 1;
			else if (stack.getType().toString().startsWith("GOLDEN")) 		prot = 6;
			else if (stack.getType().toString().startsWith("CHAINMAIL")) 	prot = 2;
			else if (stack.getType().toString().startsWith("IRON")) 		prot = 3;
			else if (stack.getType().toString().startsWith("DIAMOND")) 		prot = 4;
			else if (stack.getType().toString().startsWith("NETHERITE")) 	prot = 5;
			
			double chance = 0;
			if (stack.getType().toString().startsWith("LEATHER_HELMET")) 			chance = 0.5;
			else if (stack.getType().toString().startsWith("LEATHER")) 				chance = 0.08;
			else if (stack.getType().toString().startsWith("GOLDEN")) 				chance = 0.8;
			else if (stack.getType().toString().startsWith("CHAINMAIL")) 			chance = 0.9;
			else if (stack.getType().toString().startsWith("IRON")) 				chance = 0.3;
			else if (stack.getType().toString().startsWith("DIAMOND")) 				chance = 0.35;
			else if (stack.getType().toString().startsWith("NETHERITE")) 			chance = 0.3;
			
			if (Math.random() < chance) { // ricochet
				
				applyDamage(stack, 16);
				damage(result, 5, Locale.a(null, string) + " " + "§7§o" + Locale.a(null, "damage.ricochet"), collateral, shooter);
				return 1;
			}
			
			if (penetration >= prot * 10) {
				
				applyDamage(stack, 10);
				damage(result, damage, string, collateral, shooter);
				return 0;
				
				
			} else {
				double remaining = prot * 10 - penetration;
				remaining /= 10d;
				if (Math.random() < (1 - remaining)) {
					
					applyDamage(stack, 10);
					damage(result, damage, string, collateral, shooter);
					return 0;
					
				} else {
					
					applyDamage(stack, 26);
					damage(result, 5, Locale.a(null, string) + " " + "§7§o" + Locale.a(null, "damage.blunt"), collateral, shooter);
					return 2;
					
				}
			}
		}
		
		
		
		
	}
	
	private static void applyDamage(ItemStack item, int damageAmount) {
	    if (item == null || !(item.getType().getMaxDurability() > 0)) return;

	    ItemMeta meta = item.getItemMeta();
	    if (meta instanceof Damageable) {
	        Damageable damageable = (Damageable) meta;
	        int newDamage = damageable.getDamage() + damageAmount;

	        if (newDamage >= item.getType().getMaxDurability()) {
	            // The item is "broken"
	            item.setAmount(0); // or remove it from inventory
	        } else {
	            damageable.setDamage(newDamage);
	            item.setItemMeta((ItemMeta) damageable);
	        }
	    }
	}

	public void damageAverage(long actual, String damageCause, LivingEntity damager) {
		
		int healthy = this.countHealthyParts();
		long avg = actual / healthy;
		
		for (int i = 0; i < 7; i ++) {
			Limb limb = convert(i);
			
			//limb.damage(avg, damageCause);
			damage(i, avg, damageCause, null, damager);
			actual -= avg;
		}
		
		for (int i = 6; i >= 0; i --) {
			damage(i, 1, damageCause, null, damager);
			actual -= 1;
			
			if (actual <= 0) break;
		}
		
	}

}
