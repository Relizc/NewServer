package net.itsrelizc.gunmod.deathutils;

import java.util.Iterator;

import org.bukkit.entity.Player;

import net.itsrelizc.health2.Limb;
import net.itsrelizc.players.locales.Locale;

public class DamageRecord {
	
	private String name;
	private long amount;
	private Limb limb;
	private boolean lethal;
	
	public DamageRecord(String name, long amount, Limb limb, boolean lethal) {
		this.name = name;
		this.amount = amount;
		this.limb = limb;
		this.lethal = lethal;
	}
	
	public DamageRecord(String name, long amount, Limb limb) {
		this(name, amount, limb, false);
	}
	
	public DamageRecord(String name, long amount) {
		this(name, amount, null);
	}
	
	private String color() {
		if (this.amount > 0) {
			return "§c";
		} else if (this.amount == 0) {
			return "§7";
		} else {
			return "§a";
		}
	}
	
	
	private String symbol() {
		if (!this.lethal) {
			return "";
		} else {
			return "§7[§c☠§7] ";
		}
	}
	
	public String toString(Player player) {
		String a = "§8║ §r" + "§7(%s%+d§7) %s§f%s ".formatted(color(), -amount, symbol(), Locale.get(player, name));
		
		if (limb != null) {
			a += Locale.a(player, "damage.frombroken").formatted(Locale.a(player, "combat.limb." + limb.getName().toLowerCase() + ".name"));
		}
		
		return a;
		
	}

	public void setLethal(boolean b) {
		this.lethal = b;
	}


	
}
