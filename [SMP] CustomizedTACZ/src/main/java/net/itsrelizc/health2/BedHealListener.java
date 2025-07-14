package net.itsrelizc.health2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.locales.Locale;

public class BedHealListener implements Listener {

    private final Map<UUID, BukkitTask> repeatingTasks = new HashMap<>();

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return; // Main hand only

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || !isBed(clickedBlock.getType())) return;

        Player player = event.getPlayer();

        // Conditions
        if (!player.isSneaking()) {
        	Locale.a(player, "damage.heal.shift_click_bed_notice");
        	return;
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
        	Locale.a(player, "damage.heal.shift_click_bed_notice");
        	return;
        }
        
        event.setCancelled(true);

        UUID uuid = player.getUniqueId();

        // Start only if not already running
        if (!repeatingTasks.containsKey(uuid)) {
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    // Check again if player still meets the conditions
                    if (!player.isSneaking()
                            || player.getInventory().getItemInMainHand().getType() != Material.AIR
                            || !isBed(player.getTargetBlockExact(5) != null ? player.getTargetBlockExact(5).getType() : null)) {
                        this.cancel();
                        repeatingTasks.remove(uuid);
                        return;
                    }

                    Body body = Body.parts.get(player.getUniqueId().toString());
                    body.healWithPriority(10);
                    
                    player.playSound(player, Sound.BLOCK_LAVA_EXTINGUISH, 1f, 2f);
                    //player.playSound(player, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f, 2f);
                    player.getLocation().getWorld().spawnParticle(
                    	    Particle.REDSTONE,        // Particle type
                    	    player.getLocation(),     // Where the particle spawns
                    	    15,                      // Number of particles
                    	    0.5, 1.0, 0.5,            // Offsets (x, y, z spread)
                    	    0.1,                      // Speed
                    	    new DustOptions(
                    	        Color.fromRGB(0, 255, 0), // Green color
                    	        2.0f                        // Size (2.0 = large)
                    	    )
                    	);

                    if (body.isAllHealthy()) {
                    	Locale.a(player, "menu.death.cure.all");
                    	player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 2f);
                    } else {
                    	player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 2f);
                    }

                }
            }.runTaskTimer(EventRegistery.main, 0L, 10L); // 10 ticks = 0.5 sec

            repeatingTasks.put(uuid, task);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (repeatingTasks.containsKey(uuid)) {
            repeatingTasks.get(uuid).cancel();
            repeatingTasks.remove(uuid);
        }
    }

    private boolean isBed(Material material) {
        return material != null && material.name().endsWith("_BED");
    }
}

