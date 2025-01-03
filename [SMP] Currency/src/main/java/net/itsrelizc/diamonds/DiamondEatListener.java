package net.itsrelizc.diamonds;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.itsrelizc.menus.ItemGenerator;

public class DiamondEatListener implements Listener {
	
	@EventHandler
	public void right(PlayerInteractEvent event) {
		
		if (event.getItem() == null) return;
		
		if (event.getHand() == EquipmentSlot.OFF_HAND) return;
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem().getType() == Material.DIAMOND) {
				event.getItem().setAmount(event.getItem().getAmount() - 1);
				
				event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EAT, 1f, 1f);
				event.getPlayer().getWorld().spawnParticle(Particle.ITEM_CRACK, event.getPlayer().getLocation().add(0, 1.5, 0), 10, 0.1, 0.1, 0.1, 0.05, ItemGenerator.generate(Material.DIAMOND, 1));
			
				DiamondPurse.addPurse(event.getPlayer(), 100);
			}
		}
		
	}
	
	@EventHandler
	public void food(PlayerItemConsumeEvent event) {
		
		Material mat = event.getItem().getType();
		long sugar = 1;

		if (mat == Material.APPLE || mat == Material.GOLDEN_APPLE || mat == Material.MELON_SLICE) {
		    sugar = 5;
		} else if (mat == Material.ENCHANTED_GOLDEN_APPLE) {
		    sugar = 6;
		} else if (mat == Material.POTATO || mat == Material.BAKED_POTATO) {
		    sugar = 7;
		} else if (mat == Material.BEETROOT || mat == Material.CARROT) {
		    sugar = 3;
		} else if (mat == Material.MUSHROOM_STEW || mat == Material.SWEET_BERRIES || mat == Material.GLOW_BERRIES) {
		    sugar = 2;
		} else if (mat == Material.RABBIT_STEW) {
		    sugar = 11;
		} else if (mat == Material.SUSPICIOUS_STEW) {
		    sugar = 0;
		} else if (mat == Material.CHORUS_FRUIT) {
		    sugar = -1;
		} else if (mat == Material.POISONOUS_POTATO) {
		    sugar = -2;
		} else if (mat == Material.PUMPKIN_PIE) {
		    sugar = 10;
		} else if (mat == Material.HONEY_BOTTLE) {
		    sugar = 20;
		} else if (mat == Material.SPIDER_EYE) {
		    sugar = -5;
		} else {
		    sugar = 1;
		}
		
		
		DiamondPurse.addSugar(event.getPlayer(), sugar);
	}

}
