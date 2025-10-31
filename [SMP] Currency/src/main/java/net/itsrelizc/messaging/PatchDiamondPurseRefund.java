package net.itsrelizc.messaging;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;

public class PatchDiamondPurseRefund implements Listener {
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		long amount = DiamondPurse.getPurse(event.getPlayer());
		
		if (amount != 0) {
			DiamondPurse.removePurse(event.getPlayer(), amount);
			DiamondPurse.savePurse();
			
			List<ItemStack> itms = new ArrayList<ItemStack>();
			long diamonds = amount / 100;
			for (int i = 0; i < 512; i ++) {
				if (diamonds >= 64) {
					
					itms.add(ItemUtils.castOrCreateItem(new ItemStack(Material.DIAMOND, 64)).getBukkitItem());
					
					diamonds -= 64;
				} else {
					
					itms.add(ItemUtils.castOrCreateItem(new ItemStack(Material.DIAMOND, (int) diamonds)).getBukkitItem());
					
					diamonds = 0;
					break;
				}
			}
			
			Messaging.Message msg = new Messaging.Message(
				    Messaging.generateId(),
				    "general",
				    "§§diamondrefund.corp",
				    "§§diamondrefund.corp.title",
				    "§§diamondrefund.corp.msg", System.currentTimeMillis(),
				    itms, false, false
				);
			
			new BukkitRunnable() {

				@Override
				public void run() {
					Messaging.addMessage(event.getPlayer(), msg);
				}
				
			}.runTaskLater(EventRegistery.main, 20l*5);
			
		}
	}

}
