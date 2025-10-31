package net.itsrelizc.health2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.itsrelizc.gunmod.deathutils.DamageLogs;
import net.itsrelizc.gunmod.deathutils.DamageRecord;
import net.itsrelizc.players.locales.Locale;

public class Limb {
	
	
	
	public static class Effect {
		public Effect(String name) {
			
		}
	}
	
	private long maxhealth;
	private long health;
	private String name;
	private List<Effect> effects;
	
	private DamageLogs logs;
	protected LivingEntity owner;

	public Limb(LivingEntity entity, long health, long maxhealth, String name) {
		this.health = health;
		this.maxhealth = maxhealth;
		this.name = name;
		this.effects = new ArrayList<Effect>();
		
		this.owner = entity;
		
		this.logs = new DamageLogs();
	}
	
	public long getHealth() {
		return health;
	}
	
	public long getMaxHealth() {
		return maxhealth;
	}

	public boolean isAbnormal() {
		if (this.health != this.maxhealth || this.effects.size() > 0) return true;
		return false;
	}

	public String getCriticalColor(Player player) {
		
		String color = "§a";
		String icon = " ✔ ";
		String message = "§l" + Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + " §7(§a%d §c❤§7)".formatted(this.health, this.maxhealth);;
		
		if (this.health != this.maxhealth ) {
			
			color = "§e";
			icon = " !!  ";
			message = "§l" + Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + " §7(§e%d §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if ((this.health * 1.0 / this.maxhealth) < 0.66 ||
				this.effects.size() > 0) {
			
			color = "§6";
			icon = " !!  ";
			message = "§l" + Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + " §7(§6%d §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if ((this.health * 1.0 / this.maxhealth) < 0.33 ) {
			
			color = "§c";
			icon = " !!  ";
			message = "§l" + Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + " §7(§c%d §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if (this.health == 0) { //§8/§7%d
			color = "§c";
			icon = " ✖ ";
			message = "§l" + Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + " §7(§c%d §c❤§7)".formatted(this.health, this.maxhealth);
		}
		
		
		return color + icon + message;
	}
	
	/**
	 * Deals some amount of damage. Capped when the health of this limb reaches 0
	 * @param amount Amount of damage
	 * @return The overflow damage, if any, or else returns 0
	 */
	public long damage(long amount, String name) {
		
		if (amount == 0) return 0;
		
		if (Math.min(amount, this.health) != 0) {
			logs.add(new DamageRecord(name, Math.min(amount, this.health)));
		}
		
		
		////(this.name);
		//logs.debugBroadcastRecords(owner);
		
		this.health -= amount;
		if (this.health < 0) {
			long abs = Math.abs(amount);
			this.health = 0;
			return abs;
		}
		
		
		
		return 0;
		
	}
	
//	public long damage(long amount) {
//		return damage(amount, "damage.unknown");
//	}
	
	public static enum RelizcDamageCause {
		
		FRAGMENT;
		
	}
	
	public long damage(long amount, RelizcDamageCause cause) {
		long as = damage(amount, "damage." + cause.toString().toLowerCase());
		////(cause.toString());
		return as;
	}

	
	/**
	 * Heal this limb. If the healing amount exceeds the maximum, this function will
	 * return the overflow amount.
	 * @param amount The amount to heal
	 * @return Overflow amount, if none returns 0.
	 */
	public long heal(long amount) {
		
		//if (this.health <= 0) return amount;
		//if (amount == 0) return 0;
		
		long val = -Math.min(this.maxhealth - this.health, amount);
		
		if (val != 0) {
			logs.add(new DamageRecord("damage.heal.natural", val));
		}
		
		this.health += amount;
		long overflow = Math.max(this.health - this.maxhealth, 0);
		this.health = Math.min(this.health, this.maxhealth);
		return overflow;
		
	}

	public void reset() {
		this.logs.clear();
		this.health = this.maxhealth;
	}
	
	public String getLocaleId() {
		return "combat.limb." + this.getName() + ".name";
	}

	public long damage(long amount, String damageCause, Limb limb) {
		
		////(amount + " aaabb" + this.name); 
		
		if (amount == 0) return 0;
		if (limb == null) {
			amount = damage(amount, damageCause);
		}
		
		long actual = this.health;
		this.health -= amount;
		if (this.health < 0) {
			long abs = Math.abs(this.health);
			this.health = 0;
			long dmg = actual - this.health;
			logs.add(new DamageRecord(damageCause, dmg, limb));
			return abs;
		}
		
		logs.add(new DamageRecord(damageCause, amount, limb));
		
		////(this.name);
		//logs.debugBroadcastRecords(owner);
		
		return 0;
	}

	public DamageLogs getDamageLogs() {
		return logs;
	}

	public String getName() {
		return name;
	}

	public void setPlayer(LivingEntity livingEntity) {
		owner = livingEntity;
	}

	public void setLatestRecordAsLethal() {
		DamageRecord rec = logs.getLatest();
		rec.setLethal(true);
	}

	public void setHealth(int i) {
		this.health = i;
	}

}
