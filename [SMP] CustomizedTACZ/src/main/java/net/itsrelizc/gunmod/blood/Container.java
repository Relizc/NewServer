package net.itsrelizc.gunmod.blood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.minecraft.world.level.block.MultifaceSpreader.e;

public class Container {
	
	private static Map<Player, Container> bodyPart = new HashMap<Player, Container>();
	
	public static List<Player> blacklisted = new ArrayList<Player>();
	public static List<Player> blacklisted2 = new ArrayList<Player>();
	
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
			
			TaskDelay.delayTask(new Runnable() {

				@Override
				public void run() {
					event.getPlayer().setMaxHealth(40);
					event.getPlayer().setHealth(40);
				}
				
			}, 2L);
			
		}
		
		@EventHandler
		public void regen(EntityRegainHealthEvent event) {
			
			if (event.getEntityType() != EntityType.PLAYER) return;
			
			Player player = (Player) event.getEntity();
			Container c = Container.get(player);
			
			double sep = event.getAmount() / 8;
			c.head += sep;
			c.thorax += sep * 3;
			c.legs += sep * 4;
			
		}
		
		@EventHandler(priority=EventPriority.HIGHEST)
		public void entityDamage(EntityDamageEvent event) {
			
			
			
			
			if (!(event.getEntity() instanceof Player)) return;
			
			
			
//			event.setCancelled(true);
			
			
			Player player = (Player) event.getEntity();
//			System.out.println(blacklisted.size());
//			System.out.println(blacklisted2.size());
			
			if (Math.abs(event.getDamage() - 1.3000000715255737) <= 0.0001) {
				event.setDamage(0);
				return;
			}
			
//			if (blacklisted2.contains(player)) {
//				blacklisted2.remove(player);
//				return;
//			}
//			if (blacklisted.contains(player)) {
//				blacklisted.remove(player);
//				blacklisted2.add(player);
//				return;
//			}
			
//			System.out.println("Check second");
			
//			player.setMaxHealth(40);
			
//			if (event.getFinalDamage() >= player.getHealth()) {
//				
//				Container c = bodyPart.get(player);
//				
//				if (c.head > 0 && c.thorax > 0) {
//					c.updateHealth();
//					event.setCancelled(true);
//				}
//				
//			}
			
			double damage = event.getFinalDamage();
			
			Container c = Container.get(player);
			
			
//			System.out.println(event.getCause());
			
			if (event.getCause() == DamageCause.FALL) {

				c.damageLegs(damage, null, event);
			} else {
				double per = damage / 8;
				
				
				c.damageHead(per, null, event);
				c.damageChest(per * 3, null, event);
				c.damageLegs(per * 4, null, event);
			}
			
		}
		
	}
	
	private double head = 3.5d; // ratio 1
	private double thorax = 16d; // ratio 3
	private double legs = 20.5d; // ratio 4
	private Player player;
	
	public Container(Player owner) {
		this.player = owner;
	}
	
	public void damageHead(double value, Player player, EntityDamageEvent who) {
		
		this.head -= value;
		if (this.head <= 0) {
			this.player.setHealth(0);
		}
	}
	
	public static Container get(Player player) {
		return bodyPart.get(player);
	}
	
	public void damageChest(double value, Player player, EntityDamageEvent who) {
		this.thorax -= value;
		if (this.thorax <= 0) {
			this.player.setHealth(0);
		}
	}
	
	public void damageLegs(double value, Player player, EntityDamageEvent who) {
		this.legs -= value;
		if (this.legs < 0) {
			double rem = -this.legs;
			rem = rem / 8;
			this.legs = 0;
			
			this.head -= rem * 3;
			if (this.head <= 0) {
				this.player.setHealth(0);
			}
			
			
			this.thorax -= rem * 5;
			if (this.thorax <= 0) {
				this.player.setHealth(0);
			}
			
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
	
	public void updateHealth() {
		this.player.setHealth(this.head + this.thorax + this.legs); // damageeffect in Hit.java
	}
	
	public static void init() {
		EventRegistery.register(new ContainerListener());
	}
	
	
	
	public static void processDamage(Player player, Player victim, int part, double damage) {
		// 1 = feet 2 = legs 3 = thorax 4 = head 0=miss
		Container c = bodyPart.get(victim);
		if (part == 4) {
			c.damageHead(damage, player, null);
		} else if (part == 3) {
			c.damageChest(damage, player, null);
		} else {
			c.damageLegs(damage, player, null);
		}
		
	}

}
