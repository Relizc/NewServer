package net.itsrelizc.health2;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.locales.Locale;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BreakableLimb extends Limb {
	
	private static final Map<Player, BukkitTask> tasks = new HashMap<>();
	public static class BreakableLimbRepairListener implements Listener {
		
		// Map to track active tasks per player
	    
		
	    @EventHandler
	    public void onHotbarSwitch(PlayerItemHeldEvent event) {
	        Player player = event.getPlayer();
	        int newSlot = event.getNewSlot();  // direct new slot index :contentReference[oaicite:1]{index=1}
	        ItemStack newItem = player.getInventory().getItem(newSlot);

	        boolean holdingBone = newItem != null && newItem.getType() == Material.BONE;

	        if (holdingBone && !tasks.containsKey(player)) {
	        	
	        	Body body = Body.parts.get(player.getUniqueId().toString());
	        	
	            BukkitTask task = new BukkitRunnable() {
	            	
	            	@Override
	            	public void cancel() {
	            		super.cancel();
	            		
	            		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "));
	            	}
	            	
	            	
	                @Override
	                public void run() {
	                	
	                	Limb target = null;
	                	
	                	for (int i = 0; i < 7; i ++) {
	                		Limb limb = body.convert(i);
	                		if (limb.getHealth() == 0 && (limb instanceof BreakableLimb)) {
	                			target = limb;
	                			break;
	                		}
	                	}
	                	
	                	String name = target == null ? Locale.a(player, "damage.heal.none") : target.getCriticalColor(player).substring(0, 2) +Locale.a(player, "combat.limb." + target.getName() + ".name");
	                	
	                    String finalmsg = Locale.a(player, "damage.break_bone.cure_hold_shift").formatted(name);
	                    
	                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(finalmsg));
	                }
	            }.runTaskTimer(EventRegistery.main, 0L, 40L);
	            tasks.put(player, task);
	        } else if (!holdingBone && tasks.containsKey(player)) {
	            tasks.remove(player).cancel();
	        }
	    }
	    
	    @EventHandler
	    public void interact(PlayerInteractEvent event) {
	    	if (event.getHand() != EquipmentSlot.HAND) return;
	    	
	    	if (!(event.getItem().getType() == Material.BONE)) return;
	    	
	    	if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
	    		
	    		Body body = Body.parts.get(event.getPlayer().getUniqueId().toString());
	    		BreakableLimb target = null;
            	
            	for (int i = 0; i < 7; i ++) {
            		Limb limb = body.convert(i);
            		if (limb.getHealth() == 0 && (limb instanceof BreakableLimb)) {
            			target = (BreakableLimb) limb;
            			break;
            		}
            	}
            	
            	if (target == null) {
            		//event.getPlayer().sendMessage(Locale.a(event.getPlayer(), "damage.no_limb_heal"));
            		return;
            	}
            	
            	target.startRepairing(20 * 8);
	    		
	    	} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	    		
	    	}
	    }

	    @EventHandler
	    public void onQuit(PlayerQuitEvent e) {
	        // Clean up on disconnect
	        BukkitTask task = tasks.remove(e.getPlayer());
	        if (task != null) task.cancel();
	    }
	}
	
	public void startRepairing(long time) {
		
		Body body = Body.parts.get(owner.getUniqueId().toString());
		LivingEntity player = this.owner;
		
		
		
		AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
	    if (attr != null) {
	        attr.setBaseValue(0.1f);
	    }
	    
	    final String[] color = {""};
	    if (player instanceof Player) {
	    	
	    	if (tasks.containsKey(player)) tasks.remove(player).cancel();
	    	
	    	color[0] = this.getCriticalColor((Player) player).substring(0, 2);
	    }
	    
	    new BukkitRunnable() {
	    	
	    	long timer = time;

			@Override
			public void run() {
				
				double length = (double) timer / 20d;
				
				if (player instanceof Player) {
					double bozo = (double) timer / time;
					String action = Locale.a((Player) player, "damage.heal.repair").formatted(color[0] + Locale.a((Player) player, getLocaleId()), getVerticalProgressBar(1 - bozo), length);
					((Player) player).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(action));
				}
				
				timer -= 2;
			}
	    	
	    }.runTaskTimer(EventRegistery.main, 0l, 2l);
		
	}
	
	/**
	 * Returns a vertical progress bar using Minecraft-style colored lines.
	 *
	 * @param progress A double between 0.0 and 1.0 indicating the fill percentage.
	 * @return A String with 20 vertical bars, colored based on the progress.
	 */
	public String getVerticalProgressBar(double progress) {
	    // Clamp the value to [0, 1]
	    progress = Math.max(0, Math.min(1, progress));

	    int totalBars = 20;
	    int filledBars = (int) Math.round(progress * totalBars);
	    StringBuilder bar = new StringBuilder();

	    for (int i = 0; i < totalBars; i++) {
	        // Dark Gray for unfilled, Green for filled
	        if (i < filledBars) {
	            bar.append("§a|");
	        } else {
	            bar.append("§8|");
	        }
	    }

	    return bar.toString();
	}

	

	public BreakableLimb(LivingEntity entity, long health, long maxhealth, String name) {
		super(entity, health, maxhealth, name);
		// TODO Auto-generated constructor stub
	}
	
	public void repair() {
		
		this.owner.getLocation().getWorld().playSound(this.owner.getLocation(), Sound.ENTITY_SKELETON_HURT, 1f, 2f);
		
		Body body = Body.parts.get(owner.getUniqueId().toString());
		
		this.setHealth(1);
		
		body.updateLegStatus();
		
	}
	
	@Override
	public long damage(long amount, String name, Limb collateral) {
		long before = this.getHealth();
		long current = super.damage(amount, name, collateral);
		long delta = before - this.getHealth();
		
		if (this.getHealth() == 0 && (this.getHealth() != before)) {
			playBoneBreakAnimation(delta);
		}
		
		return current;
	}
	
	@Override
	public long damage(long amount, String name) {
		long before = this.getHealth();
		long current = super.damage(amount, name);
		long delta = before - this.getHealth();
		
		if (this.getHealth() == 0 && (this.getHealth() != before)) {
			playBoneBreakAnimation(delta);
		}
		
		return current;
	}
	
	public void playBoneBreakAnimation(long force) {
		
		this.owner.getWorld().playSound(this.owner.getLocation(), Sound.ENTITY_SKELETON_DEATH, 2f, 0f);
		this.owner.getWorld().playSound(this.owner.getLocation(), Sound.ENTITY_SKELETON_DEATH, 2f, 2f);
		
		this.owner.getLocation().getWorld().spawnParticle(
                Particle.ITEM_CRACK,
                this.owner.getLocation(),
                20,            // count
                0.05, 0.05, 0.05, // offset (spread)
                0.05 + 0.25 * ((double)force / this.getMaxHealth()),          // extra (speed)
                new ItemStack(Material.BONE, 1)           // item data
            );
		
	}
 
}
