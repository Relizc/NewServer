package net.itsrelizc.gunmod.craft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class WeaponWorkstationBan implements Listener {
	
	private static final Material TACZ_GUN_SMITH_TABLE = Material.getMaterial("TACZ_GUN_SMITH_TABLE");

	@EventHandler
	public void interact(PlayerInteractEvent event) {
		
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		if (event.getClickedBlock() != null) {
			
			if (event.getClickedBlock().getType() == TACZ_GUN_SMITH_TABLE) {
				StringUtils.message(event.getPlayer(), "§e§l" + Locale.get(event.getPlayer(), "weapon.crafting_table"), "§c" + Locale.get(event.getPlayer(), "weapon.crafting_table.disabled"));
				event.setCancelled(true);
			}
			
//			Bukkit.broadcastMessage(event.getClickedBlock().getType().toString());
			
		}
	}
	
}
