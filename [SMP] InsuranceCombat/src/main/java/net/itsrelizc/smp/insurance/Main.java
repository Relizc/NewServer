package net.itsrelizc.smp.insurance;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.smp.insurance.listeners.ListenerCraftItem;
import net.itsrelizc.smp.insurance.listeners.ListenerWhenSomeoneDies;
import net.itsrelizc.smp.insurance.npcs.CommandSpawnJustin;

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		CommandRegistery.register(new InsuranceCommand());
		CommandRegistery.register(new CommandSpawnJustin());
		CommandRegistery.register(new PlayerStashManager.GiveMyStashBackCommand());
		
		EventRegistery.register(new ListenerCraftItem());
		EventRegistery.register(new ListenerWhenSomeoneDies());
		EventRegistery.register(new ListenerNewGuyInsurance());
		
	}
	
	@Override
	public void onDisable() {
		
		PlayerStashManager manager = new PlayerStashManager();
		
		for (NPC npc : CitizensAPI.getNPCRegistry()) {
			
			if (npc.data().has("player")) {
				Player deadguy = npc.data().get("craftplayer");
				
				Inventory contents = npc.data().get("inventory");
				////(contents.getSize() + " ");
				
				for (ItemStack content : contents.getContents()) {
					
					
					
					if (content == null) continue;
					////(content.toString());
					
					RelizcItemStack it = ItemUtils.castOrCreateItem(deadguy, content);
					////(it.toString());
					
					if (!it.getID().equals("RELIZC_PLAYER_HEAD")) {
						manager.addItem(deadguy, it.getBukkitItem());
					}
				}
				
				
			}
			
            
//			if (npc != null && npc.isSpawned()) {
//                npc.despawn(); // Despawn from the world
//                
//            }
//            
//            
//            CitizensAPI.getNPCRegistry().deregister(npc); // Unregister from registry
			
        }
		
		CitizensAPI.getNPCRegistry().deregisterAll();
		
	
		
        getLogger().info("All Citizens NPCs have been removed.");
	}

}
