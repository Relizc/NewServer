package net.itsrelizc.health2;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;

public class Limb {
	
	
	
	public static class Effect {
		public Effect(String name) {
			
		}
	}
	
	private double maxhealth;
	private double health;
	private String name;
	private List<Effect> effects;

	public Limb(double health, double maxhealth, String name) {
		this.health = health;
		this.maxhealth = maxhealth;
		this.name = name;
		this.effects = new ArrayList<Effect>();
	}
	
	public double getHealth() {
		return health;
	}

	public boolean isAbnormal() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCriticalColor(Player player) {
		
		String color = "";
		String message = Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + Locale.get(player, "combat.injured");
		
		if ((this.health / this.maxhealth) < 0.75 ) {
			
			color = "§e";
			message = Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + Locale.get(player, "combat.injured") + " §7(§e%.1f§8/§7%.1f §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if ((this.health / this.maxhealth) < 0.5 ||
				this.effects.size() > 0) {
			
			color = "§6";
			message = Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + Locale.get(player, "combat.injured") + " §7(§6%.1f§8/§7%.1f §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if ((this.health / this.maxhealth) < 0.25 ) {
			
			color = "§e";
			message = Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + Locale.get(player, "combat.injured") + " §7(§e%.1f§8/§7%.1f §c❤§7)".formatted(this.health, this.maxhealth);
			
		}
		
		if (this.health < 0) {
			color = "§4";
			message = Locale.get(player, "combat.limb." + this.name.toLowerCase() + ".name") + Locale.get(player, "combat.heavyinjured")  + " §7(§4%.1f§8/§7%.1f §c❤§7)".formatted(this.health, this.maxhealth);
		}
		
		
		
	}

}
