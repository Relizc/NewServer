package net.itsrelizc.gunmod.blood;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.itsrelizc.events.EventRegistery;
import net.minecraft.world.level.block.MultifaceSpreader.e;

public class Container {
	
	private static Map<Player, Container> bodyPart = new HashMap<Player, Container>();
	
	private static class ContainerListener implements Listener {
		
		@EventHandler
		public void join(PlayerJoinEvent event) {
			bodyPart.put(event.getPlayer(), new Container(event.getPlayer()));
			
			event.getPlayer().setMaxHealth(40);
			event.getPlayer().setHealth(40);
		}
		
		@EventHandler
		public void leave(PlayerQuitEvent event) {
			if (bodyPart.containsKey(event.getPlayer())) bodyPart.remove(event.getPlayer());
		}
		
		@EventHandler
		public void leave(PlayerRespawnEvent event) {
			Container c = bodyPart.get(event.getPlayer());
			c.refill();
			
			event.getPlayer().setHealth(40);
			event.getPlayer().setMaxHealth(40);
			
		}
		
		@EventHandler(priority=EventPriority.HIGH)
		public void entityDamage(EntityDamageEvent event) {
			
			
			
			if (!(event.getEntity() instanceof Player)) return;
			
			
			
//			event.setCancelled(true);
			
			
			Player player = (Player) event.getEntity();
			if (event.getFinalDamage() >= player.getHealth()) {
				event.setDamage(Double.MIN_NORMAL);
			}
			
			player.setMaxHealth(40);
			
			double damage = event.getFinalDamage();
			
			Container c = Container.get(player);
			
			if (event.getCause() == DamageCause.FALL) {

				c.damageLegs(damage, null);
			} else {
				double per = damage / 8;
				
				
				c.damageHead(per, null);
				c.damageChest(per * 3, null);
				c.damageLegs(per * 4, null);
			}
			
			c.updateHealth();
		}
		
	}
	
	private double head = 3.5d; // ratio 1
	private double thorax = 16d; // ratio 3
	private double legs = 20.5d; // ratio 4
	private Player player;
	
	public Container(Player owner) {
		this.player = owner;
	}
	
	public void damageHead(double value, Player who) {
		
		this.head -= value;
		updateHealth();
		if (this.head <= 0) {
			this.player.damage(999999d, player); // dead
		}
	}
	
	public static Container get(Player player) {
		return bodyPart.get(player);
	}
	
	public void damageChest(double value, Player who) {
		this.thorax -= value;
		updateHealth();
		if (this.thorax <= 0) {
			this.player.damage(999999d, player); // dead
		}
	}
	
	public void damageLegs(double value, Player who) {
		this.legs -= value;
		if (this.legs < 0) {
			double rem = -this.legs;
			rem = rem / 8;
			this.legs = 0;
			
			this.damageHead(rem * 1, who);
			this.damageChest(rem * 3, who);
			
			this.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 1, true), true); 
		}
		
	}
	
	public double getHead() {
		return head;
	}
	
	public double getChest() {
		return thorax;
	}
	
	public double getLegs() {
		return legs;
	}
	
	public void refill() {
		head = 3.5d;
		thorax = 16d;
		legs = 20.5d;
	}
	
	private void updateHealth() {
		this.player.setHealth(this.head + this.thorax + this.legs); // damageeffect in Hit.java
	}
	
	public static void init() {
		EventRegistery.register(new ContainerListener());
	}
	
	
	
	public static void processDamage(Player player, Player damager, int part, double damage) {
		// 1 = feet 2 = legs 3 = thorax 4 = head 0=miss
		Container c = bodyPart.get(player);
		if (part == 4) {
			c.damageHead(damage, damager);
		} else if (part == 3) {
			c.damageChest(damage, damager);
		} else {
			c.damageLegs(damage, damager);
		}
		
	}

}
