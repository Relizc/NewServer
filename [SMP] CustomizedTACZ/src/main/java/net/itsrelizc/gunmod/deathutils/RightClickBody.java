package net.itsrelizc.gunmod.deathutils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.gunmod.npcs.SleepingTrait;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.locales.Locale;

public class RightClickBody implements Listener {
	
	public static class InventoryMenu extends MenuTemplate2 {

		private Inventory inventory;

		public InventoryMenu(Player player, String name, Inventory operator) {
			super(Locale.a(player, "menu.deathinventory").formatted(name));
			this.inventory = operator;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void apply() {
			for (int i = 5; i <= 17; i ++) {
				if (i == 6)
				setItem(i, BLACK_GLASS());
			}
			
			
			setItem(0, get(39));
			setItem(1, get(38));
			setItem(2, get(37));
			setItem(3, get(36));
			setItem(4, get(35));
			
			setItem(8, get(44));
		}
		
		private ItemStack get(int orgSlot) {
			
			ItemStack is = inventory.getItem(orgSlot);
			if (is == null) {
				return ItemGenerator.generate(Material.GRAY_STAINED_GLASS_PANE, 1, "§7" + Locale.a(getPlayer(), "menu.deathinventory.noitem"));
			}
			return is;
			
		}
		
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onPlayerRightClick(PlayerInteractEvent event) {
	    Player player = event.getPlayer();
	    
	    if (event.getHand() != EquipmentSlot.HAND) return;
	    if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

	    Location eyeLoc = player.getEyeLocation();
	    Vector direction = eyeLoc.getDirection().normalize();
	    Location rayEnd = eyeLoc.add(direction.multiply(2)); // 2 blocks ahead

	    for (NPC npc : CitizensAPI.getNPCRegistry()) {
	        if (!npc.isSpawned()) continue;
	        if (!npc.hasTrait(SleepingTrait.class)) continue;

	        Location npcLoc = npc.getEntity().getLocation().add(0, 0.5, 0); // middle of the body

	        if (npcLoc.getWorld() == player.getWorld() && npcLoc.distanceSquared(rayEnd) < 3) {
	            // You "clicked" near the NPC
	            event.setCancelled(true);
	            //player.sendMessage("You interacted with a sleeping NPC!");
	            
	            Inventory inv = npc.data().get("inventory");
	            player.openInventory(inv);
	            
//	            Menu2 guy = new Menu2(player, 6, new InventoryMenu(player, npc.data().get("name"), inv));
//	            guy.open();
	            
	            // open inventory or do something here
	            return;
	        }
	    }
	}

}
